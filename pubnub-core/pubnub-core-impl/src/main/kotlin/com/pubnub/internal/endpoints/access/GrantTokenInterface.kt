package com.pubnub.internal.endpoints.access

import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.internal.EndpointInterface

interface GrantTokenInterface : EndpointInterface<PNGrantTokenResult> {
    val ttl: Int
}
