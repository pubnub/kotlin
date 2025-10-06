package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.test.CommonUtils.generatePayload
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.asyncRetry
import com.pubnub.test.await
import com.pubnub.test.listen
import com.pubnub.test.subscribeNonBlocking
import com.pubnub.test.subscribeToBlocking
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
import org.junit.jupiter.api.Assertions.assertNotNull
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

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(expectedClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeToBlocking(*expectedChannels.toTypedArray())
        }

        pubnub.hereNow(
            includeUUIDs = true,
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
                            value.occupants.map { it.uuid }.toList(),
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

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(expectedClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeToBlocking(*expectedChannels.toTypedArray())
        }

        assertEquals(expectedChannelsCount, expectedChannels.size)
        assertEquals(expectedClientsCount, clients.size)

        pubnub.hereNow(
            channels = expectedChannels,
            includeUUIDs = true,
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
                state = expectedStatePayload,
            ).sync()

            val pnPresenceEventResult = nextEvent<PNPresenceEventResult>()
            assertEquals("state-change", pnPresenceEventResult.event)
            assertEquals(expectedChannel, pnPresenceEventResult.channel)
            assertEquals(pubnub.configuration.userId.value, pnPresenceEventResult.uuid)
            assertEquals(expectedStatePayload, pnPresenceEventResult.state)

            val result =
                pubnub.getPresenceState(
                    channels = listOf(expectedChannel),
                ).sync()
            assertEquals(expectedStatePayload, result!!.stateByUUID[expectedChannel])
        }
    }

    @org.junit.jupiter.api.Test
    @Timeout(25, unit = TimeUnit.SECONDS)
    fun testHeartbeatsDisabled2() {
        val expectedChannel = randomChannel()

        clientConfig = {
            heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
            presenceTimeout = 20
            heartbeatInterval = 0
        }

        assertEquals(PNHeartbeatNotificationOptions.ALL, pubnub.configuration.heartbeatNotificationOptions)
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

        val pubnub =
            createPubNub {
                heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
                presenceTimeout = 20
            }
        assertEquals(PNHeartbeatNotificationOptions.ALL, pubnub.configuration.heartbeatNotificationOptions)
        assertEquals(20, pubnub.configuration.presenceTimeout)
        assertEquals(9, pubnub.configuration.heartbeatInterval)

        pubnub.test(withPresence = true) {
            subscribe(expectedChannel)
            assertEquals(PNStatusCategory.PNHeartbeatSuccess, nextStatus().category)
            skip(1)
        }
    }

    @Test
    fun testHeartbeatsEnabled() {
        val heartbeatCallsCount = AtomicInteger()
        val subscribeSuccess = AtomicBoolean()
        val expectedChannel: String = randomValue()

        val pubnub =
            createPubNub {
                heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
                presenceTimeout = 20
            }
        assertEquals(PNHeartbeatNotificationOptions.ALL, pubnub.configuration.heartbeatNotificationOptions)
        assertEquals(20, pubnub.configuration.presenceTimeout)
        assertEquals(9, pubnub.configuration.heartbeatInterval)

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                    if (pnStatus.category == PNStatusCategory.PNConnectedCategory && pnStatus.affectedChannels.contains(expectedChannel)) {
                        subscribeSuccess.set(true)
                    } else {
                        if (pnStatus.category == PNStatusCategory.PNHeartbeatSuccess) {
                            heartbeatCallsCount.incrementAndGet()
                        }
                    }
                }
            },
        )

        pubnub.subscribe(
            channels = listOf(expectedChannel),
            withPresence = true,
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
        var interceptedUrl: HttpUrl? = null
        clientConfig = {
            heartbeatInterval = 1
            httpLoggingInterceptor =
                HttpLoggingInterceptor {
                    if (it.startsWith("--> GET https://")) {
                        interceptedUrl = it.substringAfter("--> GET ").toHttpUrlOrNull()
                        success.set(true)
                    }
                }.apply { level = HttpLoggingInterceptor.Level.BASIC }
        }
        // when
        try {
            pubnub.presence(
                channels = listOf("a"),
                connected = true,
            )

            success.listen()
        } finally {
            pubnub.forceDestroy()
        }

        // then
        Assert.assertNotNull(interceptedUrl)
        assertTrue(interceptedUrl!!.queryParameterNames.contains("ee"))
    }

    @Test
    fun testHereNowWithLimit() {
        val testLimit = 3
        val totalClientsCount = 6
        val expectedChannel = randomChannel()

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(expectedChannel)
        }
        Thread.sleep(2000)

        pubnub.hereNow(
            channels = listOf(expectedChannel),
            includeUUIDs = true,
            limit = testLimit,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(1, it.totalChannels)
                assertEquals(1, it.channels.size)
                assertTrue(it.channels.containsKey(expectedChannel))

                val channelData = it.channels[expectedChannel]!!
                assertEquals(totalClientsCount, channelData.occupancy)

                // With limit=3, we should get only 3 occupants even though 6 are present
                assertEquals(testLimit, channelData.occupants.size)

                // nextOffset should be present since we limited results
                assertEquals(3, it.nextOffset)
            }
        }
    }

    @Test
    fun testHereNowWithStartFrom() {
        val startFromValue = 2
        val totalClientsCount = 5
        val expectedChannel = randomChannel()

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(expectedChannel)
        }
        Thread.sleep(2000)

        pubnub.hereNow(
            channels = listOf(expectedChannel),
            includeUUIDs = true,
            startFrom = startFromValue,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(1, it.totalChannels)
                assertEquals(1, it.channels.size)
                assertTrue(it.channels.containsKey(expectedChannel))

                val channelData = it.channels[expectedChannel]!!
                assertEquals(totalClientsCount, channelData.occupancy)

                // With startFrom=2, we should get remaining occupants (5 total - 2 skipped = 3 remaining)
                assertEquals(totalClientsCount - startFromValue, channelData.occupants.size)

                // nextOffset should be null since we got all remaining results
                assertNull(it.nextOffset)
            }
        }
    }

    @Test
    fun testHereNowPaginationFlow() {
        // 8 users in expectedChannel
        // 3 users in expectedChannel02
        val pageSize = 3
        val totalClientsCount = 11
        val channel01TotalCount = 8
        val channel02TotalCount = 3
        val channel01 = randomChannel()
        val channel02 = randomChannel()

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(channel01TotalCount - 1).toList())
            }

        println("-=channel: $channel01")
        clients.forEach {
            println("-= ${it.configuration.userId.value}")
            it.subscribeNonBlocking(channel01)
        }

        println("-=channel02: $channel01")
        clients.take(3).forEach {
            println("-= ${it.configuration.userId.value}")
            it.subscribeNonBlocking(channel02)
        }

        Thread.sleep(2000)


        val allOccupantsInChannel01 = mutableSetOf<String>()

        // First page
        val firstPage = pubnub.hereNow(
            channels = listOf(channel01, channel02),
            includeUUIDs = true,
            limit = pageSize,
        ).sync()!!

        firstPage.channels.forEach { it: Map.Entry<String, PNHereNowChannelData> ->
            println("-=Channel=${it.key} or ${it.value.channelName}")
            val pnHereNowChannelData: PNHereNowChannelData = it.value
            pnHereNowChannelData.occupants.forEach { occupant ->
                println("-=uuid firstPage ${occupant.uuid}")
            }
        }
        assertEquals(2, firstPage.totalChannels)
        val channel01DataPage01 = firstPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01DataPage01.occupancy)
        assertEquals(totalClientsCount, firstPage.totalOccupancy) // this is totalOccupancy in all pages
        assertEquals(pageSize, channel01DataPage01.occupants.size)
        assertEquals(3, firstPage.nextOffset)
        val channel02Data = firstPage.channels[channel02]!!
        assertEquals(channel02TotalCount, channel02Data.occupancy)
        assertEquals(pageSize, channel02Data.occupants.size)

        // Collect UUIDs from first page
        channel01DataPage01.occupants.forEach { allOccupantsInChannel01.add(it.uuid) }

        // Second page using nextOffset
        val secondPage = pubnub.hereNow(
            channels = listOf(channel01),
            includeUUIDs = true,
            limit = pageSize,
            startFrom = firstPage.nextOffset!!,
        ).sync()!!

        secondPage.channels.forEach { it: Map.Entry<String, PNHereNowChannelData> ->
            val pnHereNowChannelData: PNHereNowChannelData = it.value
            pnHereNowChannelData.occupants.forEach { occupant ->
                println("-=uuid secondPage ${occupant.uuid}")
            }
        }
        val channel01DataPage02 = secondPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01DataPage02.occupancy)
        assertEquals(
            channel01TotalCount,
            secondPage.totalOccupancy
        ) // we get result only from channel01 because there is no more result for channel02
        assertEquals(pageSize, channel01DataPage02.occupants.size)
        assertEquals(6, secondPage.nextOffset)

        assertFalse(secondPage.channels.containsKey(channel02))

        // Collect UUIDs from second page (should not overlap with first page)
        channel01DataPage02.occupants.forEach {
            assertFalse("UUID ${it.uuid} already found in first page", allOccupantsInChannel01.contains(it.uuid))
            allOccupantsInChannel01.add(it.uuid)
        }

        // Third page using nextOffset from second page
        val thirdPage = pubnub.hereNow(
            channels = listOf(channel01),
            includeUUIDs = true,
            limit = pageSize,
            startFrom = secondPage.nextOffset!!,
        ).sync()!!

        thirdPage.channels.forEach { it: Map.Entry<String, PNHereNowChannelData> ->
            val pnHereNowChannelData: PNHereNowChannelData = it.value
            pnHereNowChannelData.occupants.forEach { occupant ->
                println("-=uuid thirdPage ${occupant.uuid}")
            }
        }

        val channel01DataPage03 = thirdPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01DataPage03.occupancy)

        // Should have remaining clients (8 - 3 - 3 = 2)
        val expectedRemainingCount = channel01TotalCount - (pageSize * 2)
        assertEquals(expectedRemainingCount, channel01DataPage03.occupants.size)

        // Should be null since no more pages
        assertNull(thirdPage.nextOffset)

        // Collect UUIDs from third page
        channel01DataPage03.occupants.forEach {
            assertFalse("UUID ${it.uuid} already found", allOccupantsInChannel01.contains(it.uuid))
            allOccupantsInChannel01.add(it.uuid)
        }

        // Verify we got all unique clients
        assertEquals(channel01TotalCount, allOccupantsInChannel01.size)
    }


    @Test
    fun testHereNowNextStartFromWhenMoreResults() {
        val limitValue = 4
        val totalClientsCount = 10
        val expectedChannel = randomChannel()

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(expectedChannel)
        }
        Thread.sleep(2000)

        pubnub.hereNow(
            channels = listOf(expectedChannel),
            includeUUIDs = true,
            limit = limitValue,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(1, it.totalChannels)
                val channelData = it.channels[expectedChannel]!!
                assertEquals(totalClientsCount, channelData.occupancy)
                assertEquals(limitValue, channelData.occupants.size)

                // Since returned count equals limit and there are more clients,
                // nextOffset should be present
                assertNotNull(it.nextOffset)
                assertEquals(limitValue, it.nextOffset)
            }
        }
    }
}
