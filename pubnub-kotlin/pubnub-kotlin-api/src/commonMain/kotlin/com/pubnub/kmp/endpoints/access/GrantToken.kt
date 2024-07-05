package com.pubnub.kmp.endpoints.access

import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.kmp.PNFuture

interface GrantToken : PNFuture<PNGrantTokenResult> {
}