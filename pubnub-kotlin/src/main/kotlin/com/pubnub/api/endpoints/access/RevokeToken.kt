package com.pubnub.api.endpoints.access

import com.pubnub.api.Endpoint
import com.pubnub.internal.endpoints.access.IRevokeToken

class RevokeToken internal constructor(token: IRevokeToken) : Endpoint<Unit>(), IRevokeToken by token
