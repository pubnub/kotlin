package com.pubnub.contract.state

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.contract.ContractTestConfig
import java.util.Collections

class World {
    val configuration: PNConfiguration by lazy {
        PNConfiguration().apply {
            origin = ContractTestConfig.serverHostPort
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }
    }

    val clientConfiguration: PNConfiguration by lazy {
        PNConfiguration().apply {
            origin = ContractTestConfig.serverHostPort
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }
    }

    val pubnub: PubNub by lazy { PubNub(configuration) }
    val clientPubnub: PubNub by lazy { PubNub(clientConfiguration) }
    var pnException: PubNubException? = null
    var tokenString: String? = null
    val statuses: MutableList<PNStatus> = Collections.synchronizedList(mutableListOf())
    val messages: MutableList<PNMessageResult> = Collections.synchronizedList(mutableListOf())
}
