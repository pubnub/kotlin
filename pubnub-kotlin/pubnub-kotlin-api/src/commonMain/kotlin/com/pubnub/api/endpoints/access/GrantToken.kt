package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult

expect interface GrantToken : Endpoint<PNGrantTokenResult> {
}