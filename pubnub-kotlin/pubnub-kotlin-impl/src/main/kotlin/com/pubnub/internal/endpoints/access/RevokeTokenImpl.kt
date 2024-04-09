package com.pubnub.internal.endpoints.access

import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.internal.EndpointImpl

class RevokeTokenImpl internal constructor(token: RevokeTokenInterface) :
    RevokeTokenInterface by token,
    RevokeToken,
    EndpointImpl<Unit>(token)
