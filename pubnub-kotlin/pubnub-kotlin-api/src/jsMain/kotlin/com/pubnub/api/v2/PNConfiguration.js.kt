package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration

actual interface PNConfiguration {
    actual val userId: UserId
    actual val subscribeKey: String
    actual val publishKey: String
}

actual fun createPNConfiguration(
    userId: UserId,
    subscribeKey: String
): PNConfiguration {
    return object : PNConfiguration {
        override val userId: UserId
            get() = userId
        override val subscribeKey: String
            get() = subscribeKey
        override val publishKey: String
            get() = ""
    }
}