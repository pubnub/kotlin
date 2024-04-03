package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.HasOverridableConfig

interface WhereNowInterface : HasOverridableConfig {
    val uuid: String
}
