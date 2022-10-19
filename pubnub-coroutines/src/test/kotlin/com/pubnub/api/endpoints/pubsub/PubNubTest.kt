package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PubNubTest {

    val pubNub = com.pubnub.api.coroutine.PubNub(PNConfiguration(UserId(PubNub.generateUUID())).apply {
        publishKey = "pub-c-18fe6ef7-cd67-4d47-91fc-0a0e216bd751"
        subscribeKey = "sub-c-a2207fc4-daac-4e91-8cc7-64bc2bf96216"
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
