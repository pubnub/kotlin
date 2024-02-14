package com.pubnub.contract.state

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.contract.ContractTestConfig
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub

interface WorldState {
    val configuration: PNConfiguration
    var pnException: PubNubException?
    var tokenString: String?
    var responseStatus: Int?
}

class World : WorldState {
    override val configuration: PNConfiguration by lazy {
        PNConfiguration(userId = UserId(BasePubNub.generateUUID())).apply {
            origin = ContractTestConfig.serverHostPort
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }
    }

    val pubnub: BasePubNub by lazy { TestPubNub(configuration) }
    override var pnException: PubNubException? = null
    override var tokenString: String? = null
    override var responseStatus: Int? = null
}
