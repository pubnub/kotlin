package com.pubnub.internal.endpoints.presence

import com.pubnub.api.legacy.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HereNowEndpointTest : BaseTest() {
    @Test
    fun testPubNubHereNowWithPaginationParameters() {
        // Test the public API with new pagination parameters
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            channelGroups = emptyList(),
            includeState = false,
            includeUUIDs = true,
            limit = 50,
            offset = 100
        )

        assertNotNull(hereNow)
        assertEquals(50, (hereNow as HereNowEndpoint).limit)
        assertEquals(100, hereNow.offset)
    }

    @Test
    fun testPubNubHereNowWithDefaultParameters() {
        // Test that default parameters still work (backward compatibility)
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            channelGroups = emptyList(),
            includeState = false,
            includeUUIDs = true
        )

        assertNotNull(hereNow)
        assertEquals(1000, (hereNow as HereNowEndpoint).limit) // Default limit is 1000
        assertNull(hereNow.offset)
    }

    @Test
    fun testHereNowAcceptsDefaultLimitValue() {
        // Test maximum valid limit value
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            limit = 1000
        )
        assertNotNull(hereNow)
        assertEquals(1000, (hereNow as HereNowEndpoint).limit)
    }

    @Test
    fun testHereNowAcceptsLimitBeyondAdvisedMaximum() {
        val hereNow = pubnub.hereNow(channels = listOf("test"), limit = 5000)
        assertEquals(5000, (hereNow as HereNowEndpoint).limit) // Passes to server for validation
    }

    @Test
    fun testHereNowStartFromLargeValue() {
        // Test large valid offset value
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            offset = 1000000
        )
        assertNotNull(hereNow)
        assertEquals(1000000, (hereNow as HereNowEndpoint).offset)
    }
}
