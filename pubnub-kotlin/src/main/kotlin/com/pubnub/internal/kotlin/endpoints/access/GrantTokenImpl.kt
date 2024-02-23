package com.pubnub.internal.kotlin.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.internal.endpoints.access.IGrantToken

class GrantTokenImpl internal constructor(grantToken: IGrantToken) :
    Endpoint<PNGrantTokenResult>,
    IGrantToken by grantToken,
    GrantToken
