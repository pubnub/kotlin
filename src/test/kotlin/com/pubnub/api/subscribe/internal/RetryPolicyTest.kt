package com.pubnub.api.subscribe.internal

import org.junit.Assert.*
import org.junit.Test
import java.time.Duration

class RetryPolicyTest {

    @Test
    fun linearPolicyReturnsAlwaysTheSame() {
        val retryPolicy = LinearPolicy()
        assertEquals(retryPolicy.computeDelay(3), retryPolicy.computeDelay(7))
    }

    @Test
    fun exponentialPolicy() {
        val policy = ExponentialPolicy()
        assertEquals(Duration.ofSeconds(0), policy.computeDelay(0))
    }

    @Test
    fun exponentialPolicy1() {
        val policy = ExponentialPolicy()
        assertEquals(Duration.ofSeconds(1), policy.computeDelay(1))
    }

    @Test
    fun exponentialPolicy2() {
        val policy = ExponentialPolicy()
        assertEquals(Duration.ofSeconds(2), policy.computeDelay(2))
    }

    @Test
    fun exponentialPolicy3() {
        val policy = ExponentialPolicy()
        assertEquals(Duration.ofSeconds(4), policy.computeDelay(3))
    }

    @Test
    fun exponentialPolicy4() {
        val policy = ExponentialPolicy()
        assertEquals(Duration.ofSeconds(8), policy.computeDelay(4))
    }
}