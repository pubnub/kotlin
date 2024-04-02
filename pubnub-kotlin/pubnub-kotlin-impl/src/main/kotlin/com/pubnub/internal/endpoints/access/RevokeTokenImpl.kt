package com.pubnub.internal.endpoints.access

import com.pubnub.api.endpoints.access.RevokeToken

class RevokeTokenImpl internal constructor(token: RevokeTokenInterface) : RevokeTokenInterface by token, RevokeToken
