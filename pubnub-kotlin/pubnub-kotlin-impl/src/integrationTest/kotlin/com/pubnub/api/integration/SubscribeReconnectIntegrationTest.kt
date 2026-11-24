package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.test.CommonUtils.randomChannel
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * Regression tests for https://github.com/pubnub/kotlin/issues/437: `reconnect()` used to be a silent no-op while the
 * subscribe event engine was in `SubscribeState.Receiving`.
 *
 * The client talks to the origin through [BlackholeConnectProxy], an HTTP CONNECT proxy on loopback. TLS and ALPN stay
 * end-to-end, so the SDK negotiates the origin's real protocol. [Tunnel.blackhole] turns the open tunnels into a
 * silently dead path (network switch, dropped NAT mapping, captive portal): the client's writes succeed and nothing
 * comes back. Without `reconnect()` the SDK would only notice at `subscribeTimeout` (310 s by default).
 *
 * - [HTTP_1_1_ORIGIN] tests: the state-machine fix. `reconnect()` while connected restarts the receive request
 *   without emitting a status. Cancelling a request closes its HTTP/1.1 socket, so the new one opens a new connection.
 * - [HTTP_2_ORIGIN] tests: the connection reset. On HTTP/2 the cancel only resets the stream; without
 *   `ResetSubscribeConnection` the new request is multiplexed onto the dead connection (bytes swallowed by the dead
 *   tunnel, no new tunnel) and nothing arrives within [RECOVERY_TIMEOUT_MS].
 *
 * Difference from a real dead path: the local proxy ACKs at the TCP level, so the client's kernel never retransmits.
 * On a real dead path the write also succeeds (bytes go into the send buffer); TCP gives up only after minutes,
 * well beyond [RECOVERY_TIMEOUT_MS].
 */
class SubscribeReconnectIntegrationTest : BaseIntegrationTest() {
    private val blackhole = BlackholeConnectProxy()

    override fun onAfter() {
        // close the dead tunnels first so client teardown doesn't wait on them
        blackhole.close()
    }

    /**
     * Status contract: `reconnect()` while connected emits nothing, and the session still counts as connected, so a
     * later `disconnect()` emits exactly one `PNDisconnectedCategory`. Nothing is blackholed here.
     */
    @Test
    fun reconnectWhileReceivingEmitsNoStatusAndDisconnectStillEmitsDisconnected() {
        val session = subscribeAndAwaitReceiving(HTTP_1_1_ORIGIN)
        val statusesBefore = session.listener.statuses()

        session.reconnectAndAwaitNewReceiveRequest()
        Thread.sleep(RECONNECT_SETTLE_MS)
        assertEquals("Statuses emitted by reconnect() while receiving", statusesBefore, session.listener.statuses())

        session.client.disconnect()
        assertTrue(
            "Timed out waiting for PNDisconnectedCategory",
            awaitCondition(START_TIMEOUT_MS) { session.listener.count(PNStatusCategory.PNDisconnectedCategory) > 0 },
        )
        Thread.sleep(RECONNECT_SETTLE_MS)
        assertEquals(
            "Statuses after reconnect() + disconnect()",
            statusesBefore + PNStatusCategory.PNDisconnectedCategory,
            session.listener.statuses(),
        )
    }

    /**
     * Healthy connection: `reconnect()` starts a new receive request, a published message arrives, and the status list
     * doesn't change. A new `PNConnectedCategory` here would mean `reconnect()` went through a handshake.
     */
    @Test
    fun reconnectWhileReceivingOnHealthyConnectionDeliversMessageWithoutStatus() {
        val session = subscribeAndAwaitReceiving(HTTP_1_1_ORIGIN)
        val statusesBefore = session.listener.statuses()

        session.reconnectAndAwaitNewReceiveRequest()
        Thread.sleep(RECONNECT_SETTLE_MS)

        assertTrue("Message not delivered after reconnect()", session.publishAndAwaitDelivery())
        assertEquals("Statuses emitted by reconnect() while receiving", statusesBefore, session.listener.statuses())
    }

    /**
     * The scenario from the issue: the network drops silently while the receive long-poll is in flight, the app calls
     * `reconnect()`, and another client publishes. The message arrives (before the fix only after the stale long-poll
     * timed out, 310 s) and no status is emitted (no Connected, no Disconnected, no UnexpectedDisconnect).
     */
    @Test
    fun reconnectAfterSilentNetworkDropWhileReceivingResumesDeliveryWithoutStatus() {
        val session = subscribeAndAwaitReceiving(HTTP_1_1_ORIGIN)
        val statusesBefore = session.listener.statuses()
        blackholeOpenTunnels()

        session.client.reconnect()
        Thread.sleep(RECONNECT_SETTLE_MS)

        assertTrue("Message not delivered after reconnect()", session.publishAndAwaitDelivery())
        assertEquals("Statuses emitted by reconnect() after a silent drop", statusesBefore, session.listener.statuses())
    }

    /**
     * Plain `reconnect()` while receiving must move the receive request to a new HTTP/2 connection: the SDK closes the
     * old connection (`ResetSubscribeConnection`) instead of only resetting the cancelled stream.
     */
    @Test
    fun reconnectWhileReceivingOverSilentlyDeadHttp2ConnectionRecovers() {
        assertProtocol(HTTP_2_ORIGIN, Protocol.HTTP_2)
        val session = subscribeAndAwaitReceiving(HTTP_2_ORIGIN)
        blackholeOpenTunnels()
        val tunnelsBeforeReconnect = blackhole.tunnelCount()
        val statusesBefore = session.listener.statuses()

        session.client.reconnect()
        val newTunnelOpened = awaitCondition(RECOVERY_TIMEOUT_MS) { blackhole.tunnelCount() > tunnelsBeforeReconnect }
        // let the new receive long-poll reach the server before publishing
        Thread.sleep(SETTLE_MS)
        val delivered = newTunnelOpened && session.publishAndAwaitDelivery()
        val newTunnels = blackhole.tunnelCount() - tunnelsBeforeReconnect

        val summary =
            "after reconnect() while receiving: new tunnels=$newTunnels, message delivered=$delivered, " +
                "statuses=${session.listener.statuses()}"
        println(summary)

        assertTrue("New receive request rode the dead connection. $summary", newTunnels > 0)
        assertTrue("Message not delivered after reconnect(). $summary", delivered)
        assertEquals("reconnect() while receiving must not emit a status. $summary", statusesBefore, session.listener.statuses())
    }

    /**
     * The `disconnect()` + `reconnect()` workaround must run the handshake on a new HTTP/2 connection. Nothing is in
     * flight after `disconnect()`, but the dead connection is still pooled, so `reconnect()` must close it too.
     */
    @Test
    fun disconnectThenReconnectOverSilentlyDeadHttp2ConnectionRecovers() {
        assertProtocol(HTTP_2_ORIGIN, Protocol.HTTP_2)
        val session = subscribeAndAwaitReceiving(HTTP_2_ORIGIN)
        val deadTunnels = blackholeOpenTunnels()
        val tunnelsBeforeReconnect = blackhole.tunnelCount()

        session.client.disconnect()
        Thread.sleep(SETTLE_MS)
        // measured after disconnect, so the cancel's own bytes (RST_STREAM on h2) are not counted
        val swallowedBeforeReconnect = deadTunnels.sumOf { it.bytesSwallowedFromClient.get() }
        val connectedBeforeReconnect = session.listener.count(PNStatusCategory.PNConnectedCategory)

        session.client.reconnect()
        val reconnected =
            awaitCondition(RECOVERY_TIMEOUT_MS) {
                session.listener.count(PNStatusCategory.PNConnectedCategory) > connectedBeforeReconnect
            }
        val newTunnels = blackhole.tunnelCount() - tunnelsBeforeReconnect
        val writtenIntoDeadTunnels = deadTunnels.sumOf { it.bytesSwallowedFromClient.get() } - swallowedBeforeReconnect
        val delivered = reconnected && session.publishAndAwaitDelivery()

        val summary =
            "after disconnect()+reconnect(): Connected within ${RECOVERY_TIMEOUT_MS}ms=$reconnected, " +
                "new tunnels=$newTunnels, bytes written into dead tunnels without error=$writtenIntoDeadTunnels, " +
                "message delivered=$delivered, statuses=${session.listener.statuses()}"
        println(summary)

        assertTrue("New subscribe request rode the dead connection. $summary", newTunnels > 0)
        assertTrue("No PNConnectedCategory after reconnect(). $summary", reconnected)
        assertTrue("Message not delivered after reconnect(). $summary", delivered)
    }

    private inner class Session(
        val client: PubNub,
        val listener: StatusAndMessageRecorder,
        val tracker: ReceiveRequestTracker,
        val channel: String,
    ) {
        // publishes directly, so the dead tunnels can't affect it
        private val publisher: PubNub by lazy { createPubNub {} }

        fun reconnectAndAwaitNewReceiveRequest() {
            val receiveCountBefore = tracker.receiveRequestCount.get()
            client.reconnect()
            assertTrue(
                "Timed out waiting for a new receive request",
                awaitCondition(START_TIMEOUT_MS) { tracker.receiveRequestCount.get() > receiveCountBefore },
            )
        }

        fun publishAndAwaitDelivery(): Boolean {
            publisher.publish(channel = channel, message = "after-reconnect").sync()
            return awaitCondition(DELIVERY_TIMEOUT_MS) { listener.messageCount.get() > 0 }
        }
    }

    /** Subscribes through the proxy and returns once the receive long-poll has reached the server. */
    private fun subscribeAndAwaitReceiving(host: String): Session {
        val tracker = ReceiveRequestTracker()
        val listener = StatusAndMessageRecorder()
        val client =
            createPubNub {
                origin = host
                customLoggers = listOf(tracker)
                proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(InetAddress.getLoopbackAddress(), blackhole.port))
            }
        client.addListener(listener)
        val channel = randomChannel()

        client.subscribe(channels = listOf(channel))
        assertTrue(
            "Timed out waiting for the first PNConnectedCategory",
            awaitCondition(START_TIMEOUT_MS) { listener.count(PNStatusCategory.PNConnectedCategory) > 0 },
        )
        assertTrue(
            "Timed out waiting for a receive request",
            awaitCondition(START_TIMEOUT_MS) { tracker.receiveRequestCount.get() > 0 },
        )
        // let the long-poll reach the server, so it is the pending request on the wire
        Thread.sleep(SETTLE_MS)
        return Session(client, listener, tracker, channel)
    }

    private fun blackholeOpenTunnels(): List<Tunnel> {
        val tunnels = blackhole.openTunnels()
        assertTrue("No open tunnel to blackhole; is the SDK using the proxy?", tunnels.isNotEmpty())
        tunnels.forEach { it.blackhole() }
        return tunnels
    }

    /**
     * Plain OkHttp call through the same proxy; tells which protocol the origin negotiates via ALPN. Assert, not
     * `Assume`: a skipped test would hide that the origin no longer models this protocol.
     */
    private fun assertProtocol(
        host: String,
        expected: Protocol,
    ) {
        val okHttp =
            OkHttpClient
                .Builder()
                .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(InetAddress.getLoopbackAddress(), blackhole.port)))
                .build()
        try {
            okHttp.newCall(Request.Builder().url("https://$host/time/0").build()).execute().use {
                assertEquals("Protocol negotiated with $host through the proxy", expected, it.protocol)
            }
        } finally {
            // closes the canary's tunnel so only the SDK's tunnels are open when the test blackholes them
            okHttp.connectionPool.evictAll()
            okHttp.dispatcher.executorService.shutdown()
        }
    }

    private fun awaitCondition(
        timeoutMs: Long,
        condition: () -> Boolean,
    ): Boolean {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (condition()) {
                return true
            }
            Thread.sleep(POLL_MS)
        }
        return condition()
    }

    private companion object {
        const val HTTP_1_1_ORIGIN = "ps.pndsn.com"
        const val HTTP_2_ORIGIN = "h2.pubnubapi.com"
        const val START_TIMEOUT_MS = 10_000L

        // far below the 310 s subscribe read timeout, far above a normal handshake round trip
        const val RECOVERY_TIMEOUT_MS = 15_000L
        const val DELIVERY_TIMEOUT_MS = 10_000L
        const val RECONNECT_SETTLE_MS = 2_000L
        const val SETTLE_MS = 1_000L
        const val POLL_MS = 100L
    }
}

private class StatusAndMessageRecorder : SubscribeCallback() {
    val messageCount = AtomicInteger()
    private val statuses = ConcurrentLinkedQueue<PNStatusCategory>()

    fun statuses(): List<PNStatusCategory> = statuses.toList()

    fun count(category: PNStatusCategory): Int = statuses.count { it == category }

    override fun status(
        pubnub: PubNub,
        pnStatus: PNStatus,
    ) {
        statuses.add(pnStatus.category)
    }

    override fun message(
        pubnub: PubNub,
        event: PNMessageResult,
    ) {
        messageCount.incrementAndGet()
    }
}

/** Counts receive requests (handshakes are sent with `tt=0`, receives with the cursor timetoken). */
private class ReceiveRequestTracker : CustomLogger {
    val receiveRequestCount = AtomicInteger()

    override fun debug(logMessage: LogMessage) {
        val content = logMessage.message
        if (content is LogMessageContent.NetworkRequest && content.path.contains("/v2/subscribe/")) {
            val tt = content.query?.get("tt")
            if (tt != null && tt != "0") {
                receiveRequestCount.incrementAndGet()
            }
        }
    }
}
