package com.pubnub.internal.kotlin.endpoints.access

import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.internal.endpoints.access.IRevokeToken

class RevokeTokenImpl internal constructor(token: IRevokeToken) : IRevokeToken by token, RevokeToken
