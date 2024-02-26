package com.pubnub.internal.endpoints.access

import com.pubnub.api.endpoints.access.RevokeToken

class RevokeTokenImpl internal constructor(token: IRevokeToken) : IRevokeToken by token, RevokeToken
