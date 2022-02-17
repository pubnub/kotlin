package com.pubnub.api.subscribe.internal

import java.time.Duration
import kotlin.math.pow

interface RetryPolicy {
    val maxRetries: Int
    fun shouldRetry(count: Int): Boolean = count <= maxRetries
    fun computeDelay(count: Int): Duration
}

object NoPolicy : RetryPolicy {
    override val maxRetries: Int = 0
    override fun computeDelay(count: Int): Duration = Duration.ZERO
}

class LinearPolicy(
    override val maxRetries: Int = 5,
    private val fixedDelay: Duration = Duration.ofSeconds(3)
) : RetryPolicy {
    override fun computeDelay(count: Int): Duration = fixedDelay
}

class ExponentialPolicy(override val maxRetries: Int = 5) : RetryPolicy {
    override fun computeDelay(count: Int): Duration = Duration.ofSeconds((2.0.pow(count - 1)).toLong())
}