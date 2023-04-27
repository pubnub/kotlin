package com.pubnub.api.subscribe

import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import org.junit.jupiter.api.Test

class SubscribeTest {

    @Test
    fun simpleTest() {
        val pubnub = PubNub(
            PNConfiguration(UserId("This is userId")).also {
                it.subscribeKey = Keys.subscribeKey
                it.publishKey = Keys.publishKey
                // it.logVerbosity = PNLogVerbosity.BODY
            }
        )

        val subscribe = pubnub.createSubscribeModule()

        subscribe.subscribe(
            channels = listOf("ch1", "ch2"),
            channelGroups = listOf("cg1", "cg2"),
            withPresence = false,
            withTimetoken = 0
        )

        Thread {
            repeat(5) {
                pubnub.publish("ch1", message = "aaaa").sync()
                Thread.sleep(500)
            }
        }.start()

        Thread.sleep(5_000)
    }
}
