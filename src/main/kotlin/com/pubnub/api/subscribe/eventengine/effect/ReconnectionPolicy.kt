package com.pubnub.api.subscribe.eventengine.effect

import java.time.Duration
import kotlin.math.pow

abstract class RetryPolicy {
    protected abstract val maxRetries: Int
    protected abstract fun computeDelay(count: Int): Duration
    fun nextDelay(attempt: Int): Duration? {
        if (attempt > maxRetries) {
            null
        }
        return computeDelay(attempt)
    }
}

object NoRetriesPolicy : RetryPolicy() {
    override val maxRetries: Int = 0
    override fun computeDelay(count: Int): Duration = Duration.ZERO
}

class LinearPolicy(
    override val maxRetries: Int = 5,
    private val fixedDelay: Duration = Duration.ofSeconds(3)
) : RetryPolicy() {
    override fun computeDelay(count: Int): Duration = fixedDelay
}

class ExponentialPolicy(override val maxRetries: Int = 5) : RetryPolicy() {
    override fun computeDelay(count: Int): Duration = Duration.ofSeconds((2.0.pow(count - 1)).toLong())
}
