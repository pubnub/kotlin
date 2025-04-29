package com.pubnub.docs

import com.pubnub.api.PubNub
import com.pubnub.api.UserId

abstract class SnippetBase {


    protected fun createPubNub(): PubNub {
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }
        val pubnub = PubNub.create(configBuilder.build())
        return pubnub
    }
}