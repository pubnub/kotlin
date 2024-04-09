package com.pubnub.internal

import com.pubnub.api.endpoints.HasOverridableConfig
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction

interface EndpointInterface<Output> : ExtendedRemoteAction<Output>, HasOverridableConfig
