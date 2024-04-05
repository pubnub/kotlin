package com.pubnub.api

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride

interface Endpoint<OUTPUT> : ExtendedRemoteAction<OUTPUT> {
    fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit)

    fun overrideConfiguration(configuration: PNConfiguration)
}
