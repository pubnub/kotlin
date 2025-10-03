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
            startFrom = 20
        )

        // When actual result size equals limit, there might be more results
        val nextStartFrom = endpoint.calculateNextStartFrom(10)
        assertEquals(30, nextStartFrom) // startFrom + limit
    }

    @Test
    fun testNextStartFromCalculation_noMoreResults() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 10,
            startFrom = 20
        )

        // When actual result size is less than limit, no more results
        val nextStartFrom = endpoint.calculateNextStartFrom(5)
        assertNull(nextStartFrom)
    }

    @Test
    fun testNextStartFromCalculation_withExplicitLimit() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 50,
            startFrom = 20
        )

        // When result equals limit, return next page
        val nextStartFrom = endpoint.calculateNextStartFrom(50)
        assertEquals(70, nextStartFrom) // 20 + 50
    }

    @Test
    fun testNextStartFromCalculation_startFromDefaultsToZero() {
        val endpoint = HereNowEndpoint(
            pubnub = pubnub,
            limit = 10,
            startFrom = null // Should default to 0 in calculation
        )

        val nextStartFrom = endpoint.calculateNextStartFrom(10)
        assertEquals(10, nextStartFrom) // 0 + 10
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
            startFrom = 100
        )

        assertNotNull(hereNow)
        assertEquals(50, (hereNow as HereNowEndpoint).limit)
        assertEquals(100, hereNow.startFrom)
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
        assertNull(hereNow.startFrom)
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
        // If we get 1000 results, nextStartFrom should be 1000 (not 1500)
        val nextStartFrom = endpoint.calculateNextStartFrom(1000)
        assertEquals(1000, nextStartFrom) // 0 + 1000 (capped limit)
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
        val nextStartFrom = endpoint.calculateNextStartFrom(1000)
        assertEquals(1000, nextStartFrom) // 0 + 1000 (default limit)
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
        val nextStartFrom = endpoint.calculateNextStartFrom(1000)
        assertEquals(1000, nextStartFrom) // 0 + 1000 (default limit)
    }

    @Test
    fun testHereNowStartFromZeroBoundary() {
        // Test minimum valid startFrom value
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            startFrom = 0
        )
        assertNotNull(hereNow)
        assertEquals(0, (hereNow as HereNowEndpoint).startFrom)
    }

    @Test
    fun testHereNowStartFromLargeValue() {
        // Test large valid startFrom value
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            startFrom = 1000000
        )
        assertNotNull(hereNow)
        assertEquals(1000000, (hereNow as HereNowEndpoint).startFrom)
    }

    @Test
    fun testHereNowStartFromNullIsValid() {
        // Test that null startFrom is valid (defaults to 0)
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            startFrom = null
        )
        assertNotNull(hereNow)
        assertNull((hereNow as HereNowEndpoint).startFrom)
    }

    @Test
    fun testHereNowStartFromNegativeAccepted() {
        // Test that startFrom=-1 is accepted at creation time
        // (validation happens during execution in doWork())
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            startFrom = -1
        )
        assertNotNull(hereNow)
        assertEquals(-1, (hereNow as HereNowEndpoint).startFrom)
    }

    @Test
    fun testHereNowStartFromLargeNegativeAccepted() {
        // Test that large negative startFrom is accepted at creation time
        // (validation happens during execution in doWork())
        val hereNow = pubnub.hereNow(
            channels = listOf("test-channel"),
            includeUUIDs = true,
            startFrom = -100
        )
        assertNotNull(hereNow)
        assertEquals(-100, (hereNow as HereNowEndpoint).startFrom)
    }
}
