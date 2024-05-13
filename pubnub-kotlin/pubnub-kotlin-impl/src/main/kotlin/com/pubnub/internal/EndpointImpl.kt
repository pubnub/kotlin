package com.pubnub.internal

import com.pubnub.api.Endpoint
import com.pubnub.api.endpoints.HasOverridableConfig
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.internal.v2.PNConfigurationImpl

abstract class EndpointImpl<OUTPUT>(private val endpoint: HasOverridableConfig) : Endpoint<OUTPUT> {
    override fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit): Endpoint<OUTPUT> {
        endpoint.overrideConfiguration(PNConfigurationImpl.Builder(endpoint.configuration).apply(action).build())
        return this
    }

    override fun overrideConfiguration(configuration: PNConfiguration): Endpoint<OUTPUT> {
        endpoint.overrideConfiguration(configuration)
        return this
    }
}
