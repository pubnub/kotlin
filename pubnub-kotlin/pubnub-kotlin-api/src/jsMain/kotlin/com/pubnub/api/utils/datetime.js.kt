package com.pubnub.api.utils

import kotlin.js.Date

actual interface Clock {
    actual fun now(): Instant

    actual object System : Clock {
        actual override fun now(): Instant {
            return Instant.fromEpochMilliseconds(Date.now().toLong())
        }
    }
}