package com.pubnub.internal.endpoints.presence

import com.pubnub.api.legacy.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HereNowEndpointTest : BaseTest() {
    @Test
    fun testNextStartFromCalculation_hasMoreResults() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 10,
            offset = 20
        )

        // When actual result size equals limit, there might be more results
        val nextOffset = endpoint.calculateNextOffset(10)
        assertEquals(30, nextOffset) // offset + limit
    }

    @Test
    fun testNextStartFromCalculation_noMoreResults() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 10,
            offset = 20
        )

        // When actual result size is less than limit, no more results
        val nextOffset = endpoint.calculateNextOffset(5)
        assertNull(nextOffset)
    }

    @Test
    fun testNextStartFromCalculation_withExplicitLimit() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 50,
            offset = 20
        )

        // When result equals limit, return next page
        val nextOffset = endpoint.calculateNextOffset(50)
        assertEquals(70, nextOffset) // 20 + 50
    }

    @Test
    fun testNextStartFromCalculation_offsetDefaultsToZero() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 10,
            offset = null // Should default to 0 in calculation
        )

        val nextOffset = endpoint.calculateNextOffset(10)
        assertEquals(10, nextOffset) // 0 + 10
    }

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

        // But effectiveLimit should be capped at 1000, verified by calculateNextStartFrom
        // If we get 1000 results, nextOffset should be 1000 (not 1500)
        val nextOffset = endpoint.calculateNextOffset(1000)
        assertEquals(1000, nextOffset) // 0 + 1000 (capped limit)
    }

    @Test
    fun testHereNowLimitBelowMinimumUsesDefault() {
        // Test that limit=0 uses default of 1000
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            channels = listOf("test-channel"),
            includeUUIDs = true,
            limit = 0
        )
        assertNotNull(endpoint)
        // When limit is out of range, the original value is stored
        assertEquals(0, endpoint.limit)

        // But effectiveLimit should default to 1000, verified by calculateNextStartFrom
        val nextOffset = endpoint.calculateNextOffset(1000)
        assertEquals(1000, nextOffset) // 0 + 1000 (default limit)
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

        // But effectiveLimit should default to 1000, verified by calculateNextStartFrom
        val nextOffset = endpoint.calculateNextOffset(1000)
        assertEquals(1000, nextOffset) // 0 + 1000 (default limit)
    }

    @Test
    fun testHereNowStartFromZeroBoundary() {
        // Test minimum valid offset value
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            offset = 0
        )
        assertNotNull(hereNow)
        assertEquals(0, (hereNow as HereNowEndpoint).offset)
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
    fun testHereNowStartFromNullIsValid() {
        // Test that null offset is valid (defaults to 0)
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            offset = null
        )
        assertNotNull(hereNow)
        assertNull((hereNow as HereNowEndpoint).offset)
    }

    @Test
    fun testHereNowStartFromNegativeAccepted() {
        // Test that offset=-1 is accepted at creation time
        // (validation happens during execution in doWork())
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            offset = -1
        )
        assertNotNull(hereNow)
        assertEquals(-1, (hereNow as HereNowEndpoint).offset)
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
