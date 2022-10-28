package com.pubnub.contract.state

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.contract.ContractTestConfig

class World {
    val configuration: PNConfiguration by lazy {
        PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
            origin = ContractTestConfig.serverHostPort
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }
    }

    val pubnub: PubNub by lazy { PubNub(configuration) }
    var pnException: PubNubException? = null
    var tokenString: String? = null
    var responseStatus: Int? = null
}
