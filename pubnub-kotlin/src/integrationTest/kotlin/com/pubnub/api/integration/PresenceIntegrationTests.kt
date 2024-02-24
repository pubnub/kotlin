package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.generatePayload
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.PubNub
import com.pubnub.api.asyncRetry
import com.pubnub.api.await
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.subscribeToBlocking

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.awaitility.Awaitility
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds

class PresenceIntegrationTests : BaseIntegrationTest() {

    @Test
    fun testWhereNow() {
        val expectedChannelsCount = 4
        val expectedChannels = generateSequence { randomValue() }.take(expectedChannelsCount).toList()

        pubnub.subscribeToBlocking(*expectedChannels.toTypedArray())

        pubnub.whereNow().await { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(expectedChannelsCount, it.channels.size)
                assertEquals(expectedChannels.sorted(), it.channels.sorted())
            }
        }
    }

    @Test
    fun testGlobalHereNow() {
        val expectedChannelsCount = 2
        val expectedClientsCount = 3

        val expectedChannels = generateSequence { randomValue() }.take(expectedChannelsCount).toList()

        val clients = mutableListOf(pubnub).apply {
            addAll(generateSequence { createPubNub() }.take(expectedClientsCount - 1).toList())
        }

        clients.forEach {
            it.subscribeToBlocking(*expectedChannels.toTypedArray())
        }

        pubnub.hereNow(
            includeUUIDs = true
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertTrue(it.totalOccupancy >= expectedClientsCount)
                assertTrue(it.totalChannels >= expectedChannelsCount)
                assertTrue(it.channels.size >= expectedChannelsCount)

                assertTrue(it.channels.keys.containsAll(expectedChannels))

                it.channels.forEach { (key, value) ->
                    if (expectedChannels.contains(key)) {
                        assertTrue(value.occupancy >= expectedClientsCount)
                        assertTrue(value.occupants.size >= expectedClientsCount)

                        assertEquals(
                            clients.map { it.configuration.userId.value }.toList(),
                            value.occupants.map { it.uuid }.toList()
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testHereNow() {
        val expectedChannelsCount = 2
        val expectedClientsCount = 3

        val expectedChannels = generateSequence { randomValue() }.take(expectedChannelsCount).toList()

        val clients = mutableListOf(pubnub).apply {
            addAll(generateSequence { createPubNub() }.take(expectedClientsCount - 1).toList())
        }

        clients.forEach {
            it.subscribeToBlocking(*expectedChannels.toTypedArray())
        }

        assertEquals(expectedChannelsCount, expectedChannels.size)
        assertEquals(expectedClientsCount, clients.size)

        pubnub.hereNow(
            channels = expectedChannels,
            includeUUIDs = true
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(expectedChannelsCount, it.totalChannels)
                assertEquals(expectedChannelsCount, it.channels.size)
                assertEquals(expectedChannelsCount * expectedClientsCount, it.totalOccupancy)
                it.channels.forEach { (key, value) ->
                    assertTrue(expectedChannels.contains(key))
                    assertTrue(expectedChannels.contains(value.channelName))
                    assertEquals(expectedClientsCount, value.occupancy)
                    assertEquals(expectedClientsCount, value.occupants.size)
                    value.occupants.forEach { occupant ->
                        val uuid = occupant.uuid
                        var contains = false
                        for (client in clients) {
                            if (client.configuration.userId.value == uuid) {
                                contains = true
                                break
                            }
                        }
                        assertTrue(contains)
                    }
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun testPresenceState() {
        val expectedStatePayload = generatePayload()
        val expectedChannel = randomChannel()

        pubnub.test(withPresence = true) {
            subscribe(expectedChannel)
            assertEquals("join", nextEvent<PNPresenceEventResult>().event)

            pubnub.setPresenceState(
                channels = listOf(expectedChannel),
                state = expectedStatePayload
            ).sync()

            val pnPresenceEventResult = nextEvent<PNPresenceEventResult>()
            assertEquals("state-change", pnPresenceEventResult.event)
            assertEquals(expectedChannel, pnPresenceEventResult.channel)
            assertEquals(pubnub.configuration.userId.value, pnPresenceEventResult.uuid)
            assertEquals(expectedStatePayload, pnPresenceEventResult.state)

            val result = pubnub.getPresenceState(
                channels = listOf(expectedChannel)
            ).sync()
            assertEquals(expectedStatePayload, result!!.stateByUUID[expectedChannel])
        }
    }

    @org.junit.jupiter.api.Test
    @Timeout(25, unit = TimeUnit.SECONDS)
    fun testHeartbeatsDisabled2() {
        val expectedChannel = randomChannel()

        pubnub.configuration.heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
        assertEquals(PNHeartbeatNotificationOptions.ALL, pubnub.configuration.heartbeatNotificationOptions)

        pubnub.configuration.presenceTimeout = 20
        pubnub.configuration.heartbeatInterval = 0

        assertEquals(20, pubnub.configuration.presenceTimeout)
        assertEquals(0, pubnub.configuration.heartbeatInterval)

        pubnub.test(withPresence = true) {
            subscribe(expectedChannel)
            assertNull(nextStatus(20.seconds)) // no heartbeats in the next 20 seconds
            skip(2)
        }
    }

    @org.junit.jupiter.api.Test
    @Timeout(25, unit = TimeUnit.SECONDS)
    fun testHeartbeatsEnabled2() {
        val expectedChannel: String = randomValue()

        val pubnub = createPubNub(
            getBasicPnConfiguration().apply {
                heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
                presenceTimeout = 20
            }
        )
        assertEquals(PNHeartbeatNotificationOptions.ALL, pubnub.configuration.heartbeatNotificationOptions)
        assertEquals(20, pubnub.configuration.presenceTimeout)
        assertEquals(9, pubnub.configuration.heartbeatInterval)

        pubnub.test(withPresence = true) {
            subscribe(expectedChannel)
            assertEquals(PNStatusCategory.HeartbeatSuccess, nextStatus())
            skip(1)
        }
    }

    @Test
    fun testHeartbeatsEnabled() {
        val heartbeatCallsCount = AtomicInteger()
        val subscribeSuccess = AtomicBoolean()
        val expectedChannel: String = randomValue()

        val pubnub = createPubNub(
            getBasicPnConfiguration().apply {
                heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
                presenceTimeout = 20
            }
        )
        assertEquals(PNHeartbeatNotificationOptions.ALL, pubnub.configuration.heartbeatNotificationOptions)
        assertEquals(20, pubnub.configuration.presenceTimeout)
        assertEquals(9, pubnub.configuration.heartbeatInterval)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.category == PNStatusCategory.Connected && pnStatus.channels.contains(expectedChannel)) {
                    subscribeSuccess.set(true)
                } else if (pnStatus.category == PNStatusCategory.HeartbeatSuccess) {
                    heartbeatCallsCount.incrementAndGet()
                }
            }
        })

        pubnub.subscribe(
            channels = listOf(expectedChannel),
            withPresence = true
        )

        Awaitility.await()
            .atMost(20, TimeUnit.SECONDS)
            .until {
                subscribeSuccess.get() && heartbeatCallsCount.get() > 2
            }
    }

    @Test
    fun `when hearbeatInterval greater than 0 and eventEngine enabled then REST call contains "ee" query parameter`() {
        // given
        val success = AtomicBoolean()
        val config = getBasicPnConfiguration()
        config.heartbeatInterval = 1
        var interceptedUrl: HttpUrl? = null
        config.httpLoggingInterceptor = HttpLoggingInterceptor {
            if (it.startsWith("--> GET https://")) {
                interceptedUrl = it.substringAfter("--> GET ").toHttpUrlOrNull()
                success.set(true)
            }
        }.apply { level = HttpLoggingInterceptor.Level.BASIC }

        val pubnub = PubNub.create(config)

        // when
        try {
            pubnub.presence(
                channels = listOf("a"),
                connected = true
            )

            success.listen()
        } finally {
            pubnub.forceDestroy()
        }

        // then
        Assert.assertNotNull(interceptedUrl)
        assertTrue(interceptedUrl!!.queryParameterNames.contains("ee"))
    }
}
