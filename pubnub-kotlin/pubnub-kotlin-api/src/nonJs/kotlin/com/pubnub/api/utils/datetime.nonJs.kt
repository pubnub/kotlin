package com.pubnub.api.utils

actual interface Clock {
    actual fun now(): Instant

    actual object System : Clock {
        @OptIn(kotlin.time.ExperimentalTime::class)
        actual override fun now(): Instant {
            val ktInstant = kotlin.time.Clock.System.now()
            return Instant(ktInstant.epochSeconds, ktInstant.nanosecondsOfSecond)
        }
    }
}
