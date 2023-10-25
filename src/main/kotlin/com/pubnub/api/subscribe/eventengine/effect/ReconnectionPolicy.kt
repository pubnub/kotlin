package com.pubnub.api.subscribe.eventengine.effect

import java.time.Duration
import kotlin.math.pow

internal abstract class RetryPolicy {
    protected abstract val maxRetries: Int
    protected abstract fun computeDelay(count: Int): Duration
    fun nextDelay(attempt: Int): Duration? {
        if (attempt >= maxRetries) {
            return null
        }
        return computeDelay(attempt)
    }
}

internal object NoRetriesPolicy : RetryPolicy() {
    override val maxRetries: Int = 0
    override fun computeDelay(count: Int): Duration = Duration.ZERO
}

internal class LinearPolicy(
    override val maxRetries: Int = 5, // LinearPolicy is created in PNConfiguration default maxRetries is -1 which is unlimited
    private val fixedDelay: Duration = Duration.ofSeconds(3)
) : RetryPolicy() {
    override fun computeDelay(count: Int): Duration = fixedDelay
}

internal class ExponentialPolicy(override val maxRetries: Int = 5) : RetryPolicy() { // LinearPolicy is created in PNConfiguration default maxRetries is -1 which is unlimited
    override fun computeDelay(count: Int): Duration = Duration.ofSeconds((2.0.pow(count - 1)).toLong())
}
