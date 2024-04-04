package com.pubnub.api

import com.pubnub.api.endpoints.HasOverridableConfig
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.PNConfigurationOverride

interface Endpoint<OUTPUT> : ExtendedRemoteAction<OUTPUT>, HasOverridableConfig {
    fun overrideConfiguration(action: PNConfigurationOverride.Builder.() -> Unit)
}
