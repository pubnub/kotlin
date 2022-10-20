package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PubNubTest {

    val pubNub = com.pubnub.api.coroutine.PubNub(PNConfiguration(UserId(PubNub.generateUUID())).apply {
        publishKey = "demo"
        subscribeKey = "demo"
        logVerbosity = PNLogVerbosity.BODY
    })

    @Test
    fun test() {
        val result = runBlocking {
            pubNub.publish(
                channel = "hackathon",
                message = "Hello world!",
            )
        }

        println("Publish result: $result")
    }
}
