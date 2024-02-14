package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.internal.endpoints.access.IGrantToken

class GrantToken internal constructor(grantToken: IGrantToken) :
    Endpoint<PNGrantTokenResult>(),
    IGrantToken by grantToken
