package com.pubnub.internal.endpoints.presence

import com.pubnub.api.legacy.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HereNowPaginationTest : BaseTest() {
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
}
