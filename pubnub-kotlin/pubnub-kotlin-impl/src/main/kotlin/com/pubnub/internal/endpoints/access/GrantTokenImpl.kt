package com.pubnub.internal.endpoints.access

import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.internal.EndpointImpl

class GrantTokenImpl internal constructor(grantToken: GrantTokenInterface) :
    EndpointImpl<PNGrantTokenResult>(grantToken),
    GrantTokenInterface by grantToken,
    GrantToken
