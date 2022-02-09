package com.pubnub.api

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.PNStatus
import org.junit.Test

class TestTest {

    @Test
    fun aa() {
        val config = PNConfiguration("test_uuid").apply {
            publishKey = "demo-36"
            subscribeKey = "demo-36"
            origin = "127.0.0.1:26379"
            logVerbosity = PNLogVerbosity.BODY
            secure = false
        }

        val pubnub = PubNub(config)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                println(pnStatus)
            }
        })

        pubnub.subscribe(channels = listOf("ch1"))

        Thread.sleep(5_000)
    }
}