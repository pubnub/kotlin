package com.pubnub.contract.state

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.contract.CONTRACT_TEST_CONFIG
import java.util.*

interface WorldState {
    val configuration: PNConfiguration
    var pnException: PubNubException?
    var tokenString: String?
    var responseStatus: Int?
}

class World : WorldState {
    override val configuration: PNConfiguration by lazy { PNConfiguration(UserId("pn-" + UUID.randomUUID())).apply {
        origin = CONTRACT_TEST_CONFIG.serverHostPort()
        isSecure = false
        logVerbosity = PNLogVerbosity.BODY
    } }
    val pubnub: PubNub by lazy { PubNub(configuration) }
    override var pnException: PubNubException? = null
    override var tokenString: String? = null
    override var responseStatus: Int? = null //we are storing this member here so that we can have one common verification step "I receive a successful response"
}
