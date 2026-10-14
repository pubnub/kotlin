package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.test.CommonUtils.randomChannel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * A receive long-poll cancelled on purpose (disconnect / unsubscribeAll) must not be retried.
 * Otherwise the retry clone is untracked by the event engine, nothing cancels it, and every
 * disconnect/reconnect cycle leaks one more concurrent long-poll.
 */
class SubscribeCancelRetryIntegrationTest : BaseIntegrationTest() {
    private val tracker = SubscribeRequestTracker()

    override fun onBefore() {
        clientConfig = {
            customLoggers = listOf(tracker)
            // BaseIntegrationTest disables retries; SUBSCRIBE retries are on by default in the SDK, which is where the leak happens.
            retryConfiguration = RetryConfiguration.Exponential()
        }
    }

    @Test
    fun disconnectWhileReceiveIsInFlightShouldNotRetryCancelledRequest() {
        assertCancelledReceiveIsNotRetried { pubnub.disconnect() }
    }

    @Test
    fun unsubscribeAllWhileReceiveIsInFlightShouldNotRetryCancelledRequest() {
        assertCancelledReceiveIsNotRetried { pubnub.unsubscribeAll() }
    }

    @Test
    fun disconnectReconnectCyclesShouldKeepSingleReceiveRequestOpen() {
        pubnub.subscribe(channels = listOf(randomChannel()))
        awaitReceiveInFlight(previousReceiveCount = 0)

        repeat(CYCLES) {
            pubnub.disconnect()
            Thread.sleep(RETRY_WINDOW_MS)
            val receiveCount = tracker.receiveRequestCount.get()
            pubnub.reconnect()
            awaitReceiveInFlight(receiveCount)
        }

        assertEquals(
            "Open subscribe requests after $CYCLES disconnect/reconnect cycles",
            1,
            tracker.inFlightCount(),
        )
    }

    /**
     * A leaked receive clone still long-polls the old channel. Its result lands in the new `Receiving` state and its
     * messages are emitted, although the client is no longer subscribed to that channel.
     */
    @Test
    fun staleReceiveCloneShouldNotDeliverMessagesForUnsubscribedChannel() {
        val oldChannel = randomChannel()
        val newChannel = randomChannel()
        // global listener: a subscription-scoped one could filter the stale message out by channel
        val listener = RecordingListener()
        pubnub.addListener(listener)

        pubnub.subscribe(channels = listOf(oldChannel))
        awaitReceiveInFlight(previousReceiveCount = 0)
        pubnub.unsubscribeAll()
        // without the fix the retry clone (long-polling oldChannel) is in flight by now
        Thread.sleep(RETRY_WINDOW_MS)

        val receiveCount = tracker.receiveRequestCount.get()
        val connectedCount = listener.connectedCount.get()
        pubnub.subscribe(channels = listOf(newChannel))
        awaitConnected(listener, connectedCount)
        awaitReceiveInFlight(receiveCount)

        pubnub.publish(channel = oldChannel, message = "stale").sync()
        Thread.sleep(DELIVERY_WINDOW_MS)

        assertEquals("Messages delivered for an unsubscribed channel", 0, listener.messageCount(oldChannel))
    }

    /**
     * Regression guard, not proof: without the fix a leaked receive clone may deliver the message a second time
     * (racy, 1 or 2 deliveries). Relies on the default `dedupOnSubscribe = false`, which would mask a duplicate.
     */
    @Test
    fun messageShouldBeDeliveredOnceAfterDisconnectReconnect() {
        val channel = randomChannel()
        val listener = RecordingListener()
        pubnub.addListener(listener)

        pubnub.subscribe(channels = listOf(channel))
        awaitReceiveInFlight(previousReceiveCount = 0)
        pubnub.disconnect()
        Thread.sleep(RETRY_WINDOW_MS)

        val receiveCount = tracker.receiveRequestCount.get()
        val connectedCount = listener.connectedCount.get()
        pubnub.reconnect()
        awaitConnected(listener, connectedCount)
        awaitReceiveInFlight(receiveCount)

        pubnub.publish(channel = channel, message = "once").sync()
        Thread.sleep(DELIVERY_WINDOW_MS)

        assertEquals("Deliveries of a single published message", 1, listener.messageCount(channel))
    }

    private fun awaitConnected(listener: RecordingListener, previousConnectedCount: Int) {
        val deadline = System.currentTimeMillis() + RECEIVE_START_TIMEOUT_MS
        while (listener.connectedCount.get() <= previousConnectedCount) {
            assertTrue("Timed out waiting for PNConnectedCategory", System.currentTimeMillis() < deadline)
            Thread.sleep(100)
        }
    }

    private fun assertCancelledReceiveIsNotRetried(stop: () -> Unit) {
        pubnub.subscribe(channels = listOf(randomChannel()))
        awaitReceiveInFlight(previousReceiveCount = 0)

        stop()
        val requestsAtStop = tracker.subscribeRequestCount.get()
        Thread.sleep(RETRY_WINDOW_MS)

        assertEquals(
            "Subscribe requests sent after the subscribe loop was stopped",
            requestsAtStop,
            tracker.subscribeRequestCount.get(),
        )
        assertEquals("Open subscribe requests after the subscribe loop was stopped", 0, tracker.inFlightCount())
    }

    private fun awaitReceiveInFlight(previousReceiveCount: Int) {
        val deadline = System.currentTimeMillis() + RECEIVE_START_TIMEOUT_MS
        while (tracker.receiveRequestCount.get() <= previousReceiveCount) {
            assertTrue("Timed out waiting for a receive request", System.currentTimeMillis() < deadline)
            Thread.sleep(100)
        }
        // let the long-poll reach the server so the cancel hits an in-flight request
        Thread.sleep(500)
    }

    private companion object {
        const val CYCLES = 4

        // first Exponential retry fires after minDelay (2s) + random jitter (< 1s)
        const val RETRY_WINDOW_MS = 5_000L
        const val RECEIVE_START_TIMEOUT_MS = 10_000L
        const val DELIVERY_WINDOW_MS = 5_000L
    }
}

private class RecordingListener : SubscribeCallback() {
    val connectedCount = AtomicInteger()
    private val messages = ConcurrentLinkedQueue<PNMessageResult>()

    fun messageCount(channel: String): Int = messages.count { it.channel == channel }

    override fun status(
        pubnub: PubNub,
        pnStatus: PNStatus,
    ) {
        if (pnStatus.category == PNStatusCategory.PNConnectedCategory) {
            connectedCount.incrementAndGet()
        }
    }

    override fun message(
        pubnub: PubNub,
        event: PNMessageResult,
    ) {
        messages.add(event)
    }
}

private class SubscribeRequestTracker : CustomLogger {
    val subscribeRequestCount = AtomicInteger()
    val receiveRequestCount = AtomicInteger()

    // CustomPnHttpLoggingInterceptor logs a request and its response / failure on the same OkHttp thread
    private val inFlight = ConcurrentHashMap.newKeySet<Thread>()

    fun inFlightCount(): Int = inFlight.size

    override fun debug(logMessage: LogMessage) = onLog(logMessage)

    override fun error(logMessage: LogMessage) = onLog(logMessage)

    private fun onLog(logMessage: LogMessage) {
        val content = logMessage.message
        when {
            content is LogMessageContent.NetworkRequest && content.path.contains("/v2/subscribe/") -> {
                subscribeRequestCount.incrementAndGet()
                // handshake is sent with tt=0, receive with the cursor timetoken
                val tt = content.query?.get("tt")
                if (tt != null && tt != "0") {
                    receiveRequestCount.incrementAndGet()
                }
                inFlight.add(Thread.currentThread())
            }

            content is LogMessageContent.NetworkResponse ||
                (content is LogMessageContent.Error && logMessage.details?.startsWith("HTTP Request failed") == true) -> {
                inFlight.remove(Thread.currentThread())
            }
        }
    }
}
