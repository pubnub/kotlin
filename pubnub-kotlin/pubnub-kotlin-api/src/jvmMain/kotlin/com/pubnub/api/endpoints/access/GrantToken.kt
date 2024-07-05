package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.kmp.endpoints.access.GrantToken

interface GrantToken : GrantToken, Endpoint<PNGrantTokenResult> {
    val ttl: Int
}
