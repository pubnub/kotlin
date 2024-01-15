package com.pubnub.internal.endpoints.access

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult

interface IGrantToken : ExtendedRemoteAction<PNGrantTokenResult> {
    val ttl: Int
}
