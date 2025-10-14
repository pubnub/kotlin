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
    fun testHereNowLimitMinimumBoundary() {
        // Test minimum valid limit value
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            limit = 1
        )
        assertNotNull(hereNow)
        assertEquals(1, (hereNow as HereNowEndpoint).limit)
    }

    @Test
    fun testHereNowLimitMaximumBoundary() {
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
    fun testHereNowLimitAboveMaximumShrinks() {
        // Test that limit > 1000 automatically shrinks to 1000
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            channels = listOf("test-channel"),
            includeUUIDs = true,
            limit = 1500
        )
        assertNotNull(endpoint)
        // When limit is out of range, endpoint stores the original value
        assertEquals(1500, endpoint.limit)
        assertEquals(1000, endpoint.effectiveLimit)
    }

    @Test
    fun testHereNowLimitNegativeUsesDefault() {
        // Test that negative limit uses default of 1000
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            channels = listOf("test-channel"),
            includeUUIDs = true,
            limit = -5
        )
        assertNotNull(endpoint)
        // When limit is out of range, the original value is stored
        assertEquals(-5, endpoint.limit)
        assertEquals(1000, endpoint.effectiveLimit)
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

    @Test
    fun testHereNowStartFromLargeNegativeAccepted() {
        // Test that large negative offset is accepted at creation time
        // (validation happens during execution in doWork())
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            offset = -100
        )
        assertNotNull(hereNow)
        assertEquals(-100, (hereNow as HereNowEndpoint).offset)
    }
}
