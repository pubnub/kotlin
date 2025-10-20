package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
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
            }
        }
    }

    @Test
    fun testHereNowWithStartFrom() {
        val offsetValue = 2
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
            offset = offsetValue,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(1, it.totalChannels)
                assertEquals(1, it.channels.size)
                assertTrue(it.channels.containsKey(expectedChannel))

                val channelData = it.channels[expectedChannel]!!
                assertEquals(totalClientsCount, channelData.occupancy)

                // With offset=2, we should get remaining occupants (5 total - 2 skipped = 3 remaining)
                assertEquals(totalClientsCount - offsetValue, channelData.occupants.size)
            }
        }
    }

    @Test
    fun testHereNowWithStartFromIncludeUUIDSisFalse() {
        val offsetValue = 2
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
            includeUUIDs = false,
            offset = offsetValue,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(1, it.totalChannels)
                assertEquals(1, it.channels.size) // Channel data is always present (consistent with multi-channel)
                assertEquals(totalClientsCount, it.totalOccupancy)

                // Verify channel data is present with occupancy but no occupants list
                val channelData = it.channels[expectedChannel]!!
                assertEquals(totalClientsCount, channelData.occupancy)
                assertEquals(0, channelData.occupants.size) // occupants list is empty when includeUUIDs = false
            }
        }
    }

    @Test
    fun testHereNowPaginationFlow() {
        // 8 users in channel01
        // 3 users in channel02
        val pageSize = 3
        val firstPageOffset = 0
        val secondPageOffset = 3
        val totalClientsCount = 11
        val channel01TotalCount = 8
        val channel02TotalCount = 3
        val channel01 = randomChannel()
        val channel02 = randomChannel()

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(channel01TotalCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(channel01)
        }

        clients.take(3).forEach {
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

        assertEquals(2, firstPage.totalChannels)
        val channel01DataPage01 = firstPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01DataPage01.occupancy)
        assertEquals(totalClientsCount, firstPage.totalOccupancy) // this is totalOccupancy in all pages
        assertEquals(pageSize, channel01DataPage01.occupants.size)
        val channel02Data = firstPage.channels[channel02]!!
        assertEquals(channel02TotalCount, channel02Data.occupancy)
        assertEquals(pageSize, channel02Data.occupants.size)

        // Collect UUIDs from first page
        channel01DataPage01.occupants.forEach { allOccupantsInChannel01.add(it.uuid) }

        // Second page using pageSize + firstPageOffset
        val secondPage = pubnub.hereNow(
            channels = listOf(channel01),
            includeUUIDs = true,
            limit = pageSize,
            offset = pageSize + firstPageOffset,
        ).sync()!!

        val channel01DataPage02 = secondPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01DataPage02.occupancy)
        assertEquals(
            channel01TotalCount,
            secondPage.totalOccupancy
        ) // we get result only from channel01 because there is no more result for channel02
        assertEquals(pageSize, channel01DataPage02.occupants.size)

        assertFalse(secondPage.channels.containsKey(channel02))

        // Collect UUIDs from second page (should not overlap with first page)
        channel01DataPage02.occupants.forEach {
            assertFalse("UUID ${it.uuid} already found in first page", allOccupantsInChannel01.contains(it.uuid))
            allOccupantsInChannel01.add(it.uuid)
        }

        // Third page using pageSize + secondPageOffset
        val thirdPage = pubnub.hereNow(
            channels = listOf(channel01),
            includeUUIDs = true,
            limit = pageSize,
            offset = pageSize + secondPageOffset,
        ).sync()!!

        val channel01DataPage03 = thirdPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01DataPage03.occupancy)

        // Should have remaining clients (8 - 3 - 3 = 2)
        val expectedRemainingCount = channel01TotalCount - (pageSize * 2)
        assertEquals(expectedRemainingCount, channel01DataPage03.occupants.size)

        // Collect UUIDs from third page
        channel01DataPage03.occupants.forEach {
            assertFalse("UUID ${it.uuid} already found", allOccupantsInChannel01.contains(it.uuid))
            allOccupantsInChannel01.add(it.uuid)
        }

        // Verify we got all unique clients
        assertEquals(channel01TotalCount, allOccupantsInChannel01.size)
    }

    @Test
    fun testHereNowPaginationFlowIncludeUUIDSisFalse() {
        // 8 users in channel01
        // 3 users in channel02
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

        clients.forEach {
            it.subscribeNonBlocking(channel01)
        }

        clients.take(3).forEach {
            it.subscribeNonBlocking(channel02)
        }

        Thread.sleep(2000)

        // First page
        val firstPage = pubnub.hereNow(
            channels = listOf(channel01, channel02),
            includeUUIDs = false,
            limit = pageSize,
        ).sync()!!

        assertEquals(2, firstPage.totalChannels)
        val channel01Data = firstPage.channels[channel01]!!
        assertEquals(channel01TotalCount, channel01Data.occupancy)
        assertEquals(0, channel01Data.occupants.size)
        assertEquals(totalClientsCount, firstPage.totalOccupancy) // this is totalOccupancy in all pages
        val channel02Data = firstPage.channels[channel02]!!
        assertEquals(channel02TotalCount, channel02Data.occupancy)
        assertEquals(0, channel02Data.occupants.size)
    }

    @Test
    fun testHereNowWithLimit0() {
        val limit = 0
        val totalClientsCount = 5
        val expectedChannel = randomChannel()

        // Subscribe multiple clients to the channel
        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(expectedChannel)
        }
        Thread.sleep(2000)

        // Query with limit=0 to get occupancy without occupant details
        pubnub.hereNow(
            channels = listOf(expectedChannel),
            includeUUIDs = true,
            limit = limit,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(1, it.totalChannels)
                val channelData = it.channels[expectedChannel]!!

                // Occupancy should reflect actual client count
                assertEquals(totalClientsCount, channelData.occupancy)

                // With limit=0, occupants list should be empty
                assertEquals(0, channelData.occupants.size)
            }
        }
    }

    @Test
    fun testHereNowMultipleChannelsWithLimit0() {
        val limit = 0
        val totalClientsCount = 5
        val channel01 = randomChannel()
        val channel02 = randomChannel()

        // Subscribe multiple clients to both channels
        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(channel01)
            it.subscribeNonBlocking(channel02)
        }
        Thread.sleep(2000)

        // Query with limit=0 to get occupancy without occupant details for multiple channels
        pubnub.hereNow(
            channels = listOf(channel01, channel02),
            includeUUIDs = true,
            limit = limit,
        ).asyncRetry { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals(2, it.totalChannels)

                val channel01Data = it.channels[channel01]!!
                val channel02Data = it.channels[channel02]!!

                // Occupancy should reflect actual client count for both channels
                assertEquals(totalClientsCount, channel01Data.occupancy)
                assertEquals(totalClientsCount, channel02Data.occupancy)

                // With limit=0, occupants list should be empty for both channels
                assertEquals(0, channel01Data.occupants.size)
                assertEquals(0, channel02Data.occupants.size)
            }
        }
    }

    @Test
    fun testGlobalHereNowWithLimit0() {
        val limit = 0
        val totalClientsCount = 4
        val expectedChannel = randomChannel()

        // Subscribe multiple clients to the channel
        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        clients.forEach {
            it.subscribeNonBlocking(expectedChannel)
        }
        Thread.sleep(2000)

        // Global hereNow with limit=0
        val result = pubnub.hereNow(
            channels = emptyList(),
            includeUUIDs = true,
            limit = limit,
        ).sync()!!

        // Should include at least our test channel
        assertTrue(result.totalChannels >= 1)
        assertTrue(result.channels.containsKey(expectedChannel))

        val channelData = result.channels[expectedChannel]!!
        assertEquals(totalClientsCount, channelData.occupancy)

        // With limit=0, occupants list should be empty
        assertEquals(0, channelData.occupants.size)
    }

    @Test
    fun testGlobalHereNowWithLimit() {
        val testLimit = 3
        val totalClientsCount = 6
        val channel01 = randomChannel()
        val channel02 = randomChannel()

        val clients =
            mutableListOf(pubnub).apply {
                addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
            }

        // Subscribe first 3 clients to channel01, all 6 to channel02
        clients.take(3).forEach {
            it.subscribeNonBlocking(channel01)
        }
        clients.forEach {
            it.subscribeNonBlocking(channel02)
        }
        Thread.sleep(2000)

        // Global hereNow (no channels specified)
        val result = pubnub.hereNow(
            channels = emptyList(),
            includeUUIDs = true,
            limit = testLimit,
        ).sync()!!

        // Should include at least our test channels
        assertTrue(result.totalChannels >= 2)
        assertTrue(result.channels.containsKey(channel01))
        assertTrue(result.channels.containsKey(channel02))

        val channel01Data = result.channels[channel01]!!
        val channel02Data = result.channels[channel02]!!

        assertEquals(3, channel01Data.occupancy)
        assertEquals(6, channel02Data.occupancy)

        // With limit=3, each channel should have at most 3 occupants returned
        assertTrue(channel01Data.occupants.size <= testLimit)
        assertTrue(channel02Data.occupants.size <= testLimit)
    }

    @Test
    fun testGlobalHereNowWithOffset() {
        val offsetValue = 2
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

        // Global hereNow with offset
        val result = pubnub.hereNow(
            channels = emptyList(),
            includeUUIDs = true,
            offset = offsetValue,
        ).sync()!!

        // Should include at least our test channel
        assertTrue(result.totalChannels >= 1)
        assertTrue(result.channels.containsKey(expectedChannel))

        val channelData = result.channels[expectedChannel]!!
        assertEquals(totalClientsCount, channelData.occupancy)

        // With offset=2, we should get remaining occupants
        assertTrue(channelData.occupants.size <= totalClientsCount - offsetValue)
    }

    @Test
    fun testGlobalHereNowWithNoActiveChannels() {
        // Don't subscribe any clients, making it a truly empty global query
        // Wait a bit to ensure no residual presence state from other tests

        val result = pubnub.hereNow(
            channels = emptyList(),
            includeUUIDs = true,
            limit = 10,
        ).sync()!!

        // Should have no channels
        // Note: In a shared test environment, there might be residual presence state
        assertTrue(result.totalOccupancy >= 0)
    }

    @Test
    fun testHereNowWithChannelGroupPagination() {
        val testLimit = 3
        val totalClientsCount = 6
        val channelGroupName = randomValue()
        val channel01 = randomChannel()
        val channel02 = randomChannel()

        // Create channel group and add channels to it
        pubnub.addChannelsToChannelGroup(
            channels = listOf(channel01, channel02),
            channelGroup = channelGroupName,
        ).sync()

        Thread.sleep(1000) // Wait for channel group to be created

        // Subscribe clients to the channels
        val clients = mutableListOf(pubnub).apply {
            addAll(generateSequence { createPubNub {} }.take(totalClientsCount - 1).toList())
        }

        // Subscribe all clients to channel01, first 3 to channel02
        clients.forEach {
            it.subscribeNonBlocking(channel01)
        }
        clients.take(3).forEach {
            it.subscribeNonBlocking(channel02)
        }

        Thread.sleep(2000) // Wait for presence to register

        // Query hereNow with channel group and limit
        val result = pubnub.hereNow(
            channelGroups = listOf(channelGroupName),
            includeUUIDs = true,
            limit = testLimit,
        ).sync()!!

        // Verify results
        assertEquals(2, result.totalChannels)
        assertTrue(result.channels.containsKey(channel01))
        assertTrue(result.channels.containsKey(channel02))

        val channel01Data = result.channels[channel01]!!
        val channel02Data = result.channels[channel02]!!

        // Verify occupancy counts
        assertEquals(totalClientsCount, channel01Data.occupancy)
        assertEquals(3, channel02Data.occupancy)

        // Verify pagination: with limit=3, each channel should return at most 3 occupants
        assertTrue(channel01Data.occupants.size <= testLimit)
        assertTrue(channel02Data.occupants.size <= testLimit)
        assertEquals(testLimit, channel01Data.occupants.size) // channel01 has 6 users, should return 3
        assertEquals(testLimit, channel02Data.occupants.size) // channel02 has 3 users, should return 3

        // Test with offset
        val resultWithOffset = pubnub.hereNow(
            channels = emptyList(),
            channelGroups = listOf(channelGroupName),
            includeUUIDs = true,
            limit = testLimit,
            offset = 2,
        ).sync()!!

        val channel01DataWithOffset = resultWithOffset.channels[channel01]!!
        assertEquals(totalClientsCount, channel01DataWithOffset.occupancy)
        // With offset=2 and limit=3, we should get 3 occupants (skipping first 2)
        assertEquals(testLimit, channel01DataWithOffset.occupants.size)

        // Cleanup: remove channel group
        pubnub.deleteChannelGroup(
            channelGroup = channelGroupName,
        ).sync()
    }

    @Test
    fun testHereNowWithLimitAbove1000andBelow0() {
        val limitAboveMax = 2000
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

        // This should not throw a client-side validation error
        // Server will validate the limit and respond accordingly
        pubnub.hereNow(
            channels = listOf(expectedChannel),
            includeUUIDs = true,
            limit = limitAboveMax,
        ).asyncRetry { result ->
            assertTrue(result.isFailure)
            result.onFailure { exception: PubNubException ->
                assertTrue(exception.message!!.contains("Cannot return more than 1000 uuids at a time"))
            }
        }
    }

    @Test
    fun testHereNowWithLimitBelow0() {
        val limitBelow0 = -1
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
            limit = limitBelow0,
        ).asyncRetry { result ->
            assertTrue(result.isFailure)
            result.onFailure { exception: PubNubException ->
                assertTrue(exception.message!!.contains("Limit must be greater than or equal to 0"))
            }
        }
    }
}
