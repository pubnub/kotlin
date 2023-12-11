package com.pubnub.api.policies

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RequestRetryPolicyTest {

    @Test
    fun `when delayInSec less than 3 should set it to 3 or higher`() {
        val linear = RequestRetryPolicy.Linear(delayInSec = 0, maxRetryNumber = 8)
        assertTrue(linear.delay >= 3.0)
    }

    @Test
    fun `when max retries over 10 should reset to 10`() {
        val linear = RequestRetryPolicy.Linear(delayInSec = 5, maxRetryNumber = 20)
        assertEquals(10, linear.maxRetryNumber)
    }

    @Test
    fun `when max retries under 10 should stay same`() {
        val linear = RequestRetryPolicy.Linear(delayInSec = 5, maxRetryNumber = 8)
        assertEquals(8, linear.maxRetryNumber)
    }

    @Test
    fun `should incremented delay value with random double`() {
        val linear = RequestRetryPolicy.Linear(delayInSec = 4, maxRetryNumber = 8)
        assertTrue(linear.delay > 4.0)
    }
}
