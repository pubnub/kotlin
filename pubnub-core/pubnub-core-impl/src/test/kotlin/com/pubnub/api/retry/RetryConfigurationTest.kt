package com.pubnub.api.retry

import org.junit.Assert.assertEquals
import org.junit.Test

class RetryConfigurationTest {
    @Test
    fun `should set delay to 3 in RetryConfiguration Linear when user set it lower than 3`() {
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 1, maxRetryNumber = 10)

        assertEquals(2, retryConfiguration.delayInSec.inWholeSeconds)
    }

    @Test
    fun `should set maxRetry to 10 in RetryConfiguration Linear when user set it above 10`() {
        val retryConfiguration = RetryConfiguration.Linear(delayInSec = 3, maxRetryNumber = 11)

        assertEquals(10, retryConfiguration.maxRetryNumber)
    }

    @Test
    fun `should set minDelayInSec to 2 in RetryConfiguration Exponential when user set it lower than 2`() {
        val retryConfiguration =
            RetryConfiguration.Exponential(minDelayInSec = 1, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(2, retryConfiguration.minDelayInSec.inWholeSeconds)
    }

    @Test
    fun `should set maxRetry to 6 in RetryConfiguration Exponential when user set it above 6`() {
        val retryConfiguration =
            RetryConfiguration.Exponential(minDelayInSec = 5, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(6, retryConfiguration.maxRetryNumber)
    }

    @Test
    fun `should set maxDelayInSec to 150 in RetryConfiguration Exponential when user set it above 150`() {
        val retryConfiguration =
            RetryConfiguration.Exponential(minDelayInSec = 5, maxDelayInSec = 10, maxRetryNumber = 10)

        assertEquals(6, retryConfiguration.maxRetryNumber)
    }
}
