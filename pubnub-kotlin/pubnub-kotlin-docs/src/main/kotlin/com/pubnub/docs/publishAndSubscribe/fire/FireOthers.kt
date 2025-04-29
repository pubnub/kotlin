package com.pubnub.docs.publishAndSubscribe.fire

import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.PNPublishResult

class FireOthers {

    private fun fireBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-1

        // snippet.fireBasic
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }

        val pubnub = PubNub.create(configBuilder.build())
        val channel = pubnub.channel("myChannel")
        val myMessage = JsonObject().apply {
            addProperty("text", "Hello, world")
        }

        channel.fire(myMessage).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { fireResult ->
                println("Message sent, timetoken: ${fireResult.timetoken}")
            }
        }

        // snippet.end
    }

}