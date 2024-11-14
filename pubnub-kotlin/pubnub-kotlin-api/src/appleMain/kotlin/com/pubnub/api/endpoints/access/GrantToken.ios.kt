package com.pubnub.api.endpoints.access

import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.kmp.PNFuture

actual interface GrantToken : PNFuture<PNGrantTokenResult>
