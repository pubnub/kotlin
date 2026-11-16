package com.pubnub.internal.presence

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.retry.RetryConfiguration
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

/**
 * Presence event engine against WireMock: the heartbeat is cancelled before `leave`, and `reconnect()` restarts the
 * heartbeat in `Heartbeating` and `HeartbeatCooldown`.
 *
 * `heartbeatInterval` is long, so no timer-driven heartbeat is sent while a test runs. Nothing is subscribed, so every
 * request comes from the presence event engine.
 */
class PresenceHeartbeatCancelTest : BaseTest() {
    private val statuses = CopyOnWriteArrayList<PNStatusCategory>()

    override fun onBefore() {
        super.onBefore()
        config.heartbeatInterval = HEARTBEAT_INTERVAL_SEC
        config.heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
        stubFor(get(urlMatching(LEAVE_URL)).willReturn(aResponse().withBody(OK_BODY)))
    }

    /**
     * With presence retries enabled by the app, a failed heartbeat schedules a retry. `Left` must cancel it, or the
     * retry reaches the server after `leave` and joins the user again until the presence timeout.
     */
    @Test
    fun pendingHeartbeatRetryIsNotSentAfterLeave() {
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Linear.createForTest(
                delayInSec = RETRY_DELAY_MS.milliseconds,
                maxRetryNumber = 3,
                // PRESENCE not excluded: heartbeats are retried
                excludedOperations = emptyList(),
                // without isInternal the public 2 s minimum delay is applied and RETRY_WINDOW_MS is too short
                isInternal = true,
            )
        config.retryConfiguration = retryConfiguration
        stubFor(get(urlMatching(HEARTBEAT_URL)).willReturn(aResponse().withStatus(500)))

        pubnub.presence(channels = listOf(CHANNEL), connected = true)
        awaitRequests(HEARTBEAT_URL, 1)
        // let the 500 arrive, so the retry is scheduled when the leave goes out
        Thread.sleep(SETTLE_MS)
        pubnub.presence(channels = listOf(CHANNEL), connected = false)
        awaitRequests(LEAVE_URL, 1)
        Thread.sleep(RETRY_WINDOW_MS)

        assertEquals("heartbeat retried after leave", 1, requestCount(HEARTBEAT_URL))
    }

    /** The common case: `reconnect()` lands in cooldown and must send a heartbeat now, not after the interval. */
    @Test
    fun reconnectInHeartbeatCooldownSendsHeartbeatAtOnce() {
        stubFor(get(urlMatching(HEARTBEAT_URL)).willReturn(aResponse().withBody(OK_BODY)))
        pubnub.addListener(statusRecorder())

        pubnub.presence(channels = listOf(CHANNEL), connected = true)
        awaitHeartbeatSuccesses(1)
        Thread.sleep(SETTLE_MS)
        pubnub.reconnect()

        awaitRequests(HEARTBEAT_URL, 2)
        awaitHeartbeatSuccesses(2)
        Thread.sleep(SETTLE_MS)
        assertEquals(2, requestCount(HEARTBEAT_URL))
    }

    /**
     * `reconnect()` while a heartbeat is in flight cancels it and sends a new one at once. The cancelled heartbeat
     * reports nothing: only the new heartbeat's success is announced.
     */
    @Test
    fun reconnectWhileHeartbeatInFlightRestartsHeartbeat() {
        stubFor(
            get(urlMatching(HEARTBEAT_URL))
                .willReturn(aResponse().withFixedDelay(SLOW_RESPONSE_MS.toInt()).withBody(OK_BODY)),
        )
        pubnub.addListener(statusRecorder())

        pubnub.presence(channels = listOf(CHANNEL), connected = true)
        awaitRequests(HEARTBEAT_URL, 1)
        Thread.sleep(SETTLE_MS)
        pubnub.reconnect()

        // well before the first heartbeat's response would arrive
        Awaitility.await()
            .atMost(SLOW_RESPONSE_MS - SETTLE_MS - 500, TimeUnit.MILLISECONDS)
            .until { requestCount(HEARTBEAT_URL) >= 2 }
        // past both responses
        Thread.sleep(SLOW_RESPONSE_MS + SETTLE_MS)
        assertEquals(2, requestCount(HEARTBEAT_URL))
        assertEquals(listOf(PNStatusCategory.PNHeartbeatSuccess), statuses.toList())
    }

    private fun statusRecorder() =
        object : SubscribeCallback() {
            override fun status(
                pubnub: PubNub,
                pnStatus: PNStatus,
            ) {
                statuses.add(pnStatus.category)
            }
        }

    private fun awaitHeartbeatSuccesses(count: Int) {
        Awaitility.await().atMost(Durations.FIVE_SECONDS).until {
            statuses.count { it == PNStatusCategory.PNHeartbeatSuccess } >= count
        }
    }

    private fun awaitRequests(
        url: String,
        count: Int,
    ) {
        Awaitility.await().atMost(Durations.FIVE_SECONDS).until { requestCount(url) >= count }
    }

    private fun requestCount(url: String) = findAll(getRequestedFor(urlMatching(url))).size

    private companion object {
        const val CHANNEL = "ch1"
        const val HEARTBEAT_URL = "/v2/presence/sub-key/mySubscribeKey/channel/$CHANNEL/heartbeat.*"
        const val LEAVE_URL = "/v2/presence/sub-key/mySubscribeKey/channel/$CHANNEL/leave.*"
        const val OK_BODY = """{"message":"OK","service":"Presence","status":200}"""
        const val HEARTBEAT_INTERVAL_SEC = 60
        const val SETTLE_MS = 300L
        const val RETRY_DELAY_MS = 1_000L

        // retry delay plus the up-to-1 s random jitter, plus margin
        const val RETRY_WINDOW_MS = 3_000L
        const val SLOW_RESPONSE_MS = 4_000L
    }
}
