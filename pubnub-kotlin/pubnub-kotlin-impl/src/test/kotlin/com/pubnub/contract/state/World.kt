package com.pubnub.contract.state

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.contract.ContractTestConfig
import com.pubnub.internal.PubNubImpl

interface WorldState {
    val configuration: PNConfiguration.Builder
    var pnException: PubNubException?
    var tokenString: String?
    var responseStatus: Int?
}

class World : WorldState {
    override val configuration: PNConfiguration.Builder =
        PNConfiguration.builder(userId = UserId(PubNubImpl.generateUUID()), "").apply {
            origin = ContractTestConfig.serverHostPort
            secure = false
        }

    val pubnub: PubNubImpl by lazy { PubNubImpl(configuration.build()) }
    override var pnException: PubNubException? = null
    override var tokenString: String? = null
    override var responseStatus: Int? = null
}
