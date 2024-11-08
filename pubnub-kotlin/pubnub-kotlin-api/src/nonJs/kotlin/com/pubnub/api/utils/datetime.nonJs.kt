package com.pubnub.api.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

actual typealias Instant = Instant

actual interface Clock {
    actual fun now(): com.pubnub.api.utils.Instant

    actual object System : com.pubnub.api.utils.Clock {
        actual override fun now(): com.pubnub.api.utils.Instant {
            return Clock.System.now()
        }
    }
}
