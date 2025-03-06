package com.pubnub.matchmaking.server

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.server.testCommon.Keys
import java.util.*
import kotlin.test.Test

class MatchmakingImplTest {


    @Test
    fun test(){
        val pnConfiguration: PNConfiguration = PNConfiguration.builder(UserId("client-${UUID.randomUUID()}"), Keys.subKey) {
            publishKey = Keys.pubKey
        }.build()
        val pubNub = PubNub.create(pnConfiguration)
        val matchmakingRestService = MatchmakingRestService(pubNub)
        val matchmaking: Matchmaking = MatchmakingImpl(matchmakingRestService)

    }
}