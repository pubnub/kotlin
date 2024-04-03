package com.pubnub.api.endpoints

import com.pubnub.api.v2.BasePNConfigurationOverride

interface HasOverridableConfig {
    fun overrideConfiguration(configuration: BasePNConfigurationOverride)
}
