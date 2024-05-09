package com.pubnub.api.v2

import com.pubnub.api.UserId
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration

actual interface PNConfiguration {
    actual val userId: UserId

}