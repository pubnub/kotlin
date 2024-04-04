package com.pubnub.internal

import com.pubnub.api.endpoints.HasOverridableConfig
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.BasePNConfiguration

interface EndpointInterface<Output> : ExtendedRemoteAction<Output>, HasOverridableConfig {
    val configuration: BasePNConfiguration
}
