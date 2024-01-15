package com.pubnub.contract.state

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.contract.CONTRACT_TEST_CONFIG
import java.util.*

class World {
    val configuration: PNConfiguration by lazy { PNConfiguration(UserId("pn-" + UUID.randomUUID())).apply {
        origin = CONTRACT_TEST_CONFIG.serverHostPort()
        isSecure = false
        logVerbosity = PNLogVerbosity.BODY
    } }
    val pubnub: PubNub by lazy { PubNub(configuration) }
    var pnException: PubNubException? = null
    var tokenString: String? = null
    var responseStatus: Int? = null //we are storing this member here so that we can have one common verification step "I receive a successful response"
}
