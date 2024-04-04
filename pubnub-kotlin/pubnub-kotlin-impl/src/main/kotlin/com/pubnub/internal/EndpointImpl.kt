package com.pubnub.internal

import com.pubnub.api.Endpoint
import com.pubnub.api.v2.BasePNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.internal.v2.PNConfigurationImpl

abstract class EndpointImpl<OUTPUT>(private val configuration: BasePNConfiguration) : Endpoint<OUTPUT> {
    override fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit) {
        overrideConfiguration(PNConfigurationImpl.Builder(configuration).apply(action).build())
    }
}
