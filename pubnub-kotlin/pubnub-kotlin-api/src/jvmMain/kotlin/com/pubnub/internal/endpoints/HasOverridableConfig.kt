package com.pubnub.internal.endpoints

import com.pubnub.api.v2.PNConfiguration

interface HasOverridableConfig {
    fun overrideConfigurationInternal(configuration: PNConfiguration)

    val configuration: PNConfiguration
}
