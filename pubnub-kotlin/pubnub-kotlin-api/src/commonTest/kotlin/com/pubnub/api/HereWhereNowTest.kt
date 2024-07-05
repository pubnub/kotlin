package com.pubnub.api

import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import com.pubnub.test.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class HereWhereNowTest : BaseIntegrationTest() {

    @Test
    fun hereNow() = runTest(timeout = defaultTimeout) {
        val channel = randomString()
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            withContext(Dispatchers.Default) {
                delay(3200) // herenow has a 3 second cache
            }
            val result = pubnub.hereNow(listOf(channel)).await()
            assertEquals(config.userId.value, result.channels[channel]?.occupants?.single()?.uuid)
            assertEquals(1, result.channels[channel]?.occupancy)
            assertEquals(1, result.totalChannels)
            assertEquals(1, result.totalOccupancy)
            assertEquals(channel, result.channels[channel]?.channelName)

        }
    }

    @Test
    fun whereNow() = runTest(timeout = defaultTimeout) {
        val channel = randomString()
        pubnub.test(backgroundScope) {
            pubnub.awaitSubscribe(listOf(channel))
            withContext(Dispatchers.Default) {
                delay(3200)
            }

            val result = pubnub.whereNow().await()
            assertEquals(listOf(channel), result.channels)
        }
    }


}