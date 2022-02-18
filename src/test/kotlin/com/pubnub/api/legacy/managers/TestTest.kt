package com.pubnub.api.legacy.managers

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import org.junit.Test

class TestTest {

    @Test
    fun test() {
        val pubnub = PubNub(PNConfiguration("uuid_i_tyle", enableSubscribeBeta = true).apply {
            subscribeKey = "demo-36"
            publishKey = "demo-36"
            logVerbosity = PNLogVerbosity.BODY
        })

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                println(pnStatus)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println(pnMessageResult)
            }
        })


        pubnub.subscribe(channels = listOf("ch1"))

        Thread.sleep(500)

        pubnub.publish(channel = "ch1", message = "It's alive").sync()

        Thread.sleep(5_000)
    }
}