package com.pubnub.api.legacy

import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.Test

class TestTest {

    @Test
    fun aaa() {
        val config = PNConfiguration(PubNub.generateUUID(), enableSubscribeBeta = true).apply {
            subscribeKey = Keys.subscribeKey
            publishKey = Keys.publishKey
            logVerbosity = PNLogVerbosity.BODY
        }

        val pn = PubNub(config)

        pn.subscribe(channels = listOf("ch1"))

        Thread.sleep(3000)
    }
}
