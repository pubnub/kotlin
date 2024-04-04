package com.pubnub.api.endpoints

import com.pubnub.api.v2.BasePNConfiguration

interface HasOverridableConfig {
    fun overrideConfiguration(configuration: BasePNConfiguration)
}
