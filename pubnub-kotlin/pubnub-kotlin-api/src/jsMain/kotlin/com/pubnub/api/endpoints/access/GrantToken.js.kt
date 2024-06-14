package com.pubnub.api.endpoints.access

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult

actual interface GrantToken : PNFuture<PNGrantTokenResult>