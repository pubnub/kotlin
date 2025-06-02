package com.pubnub.docs.publishAndSubscribe.publish

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import org.json.JSONArray
import org.json.JSONObject

class PublishOthers {
    private fun publishRequestExecution(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe

        // snippet.publishRequestExecution
        val channel = pubnub.channel("channelName")

        channel.publish("This SDK rules!").async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun publishWithMetadata() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publish-with-metadata

        // snippet.publishWithMetadata
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }
        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JsonObject().apply {
            addProperty("text", "Hello, world")
        }

        channel.publish(
            message = myMessage,
            meta = mapOf("lang" to "en"),
            usePost = true,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { publishResult ->
                println("Message sent, timetoken: ${publishResult.timetoken}")
            }
        }
        // snippet.end
    }

    private fun publishJsonArrayGSON() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonarray-google-gson

        // snippet.publishJsonArrayGSON
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }
        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JsonArray().apply {
            add(32L)
            add(35L)
        }

        channel.publish(
            message = myMessage,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
    }

    private fun publishJsonObjectOrgJson() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonobject-orgjson

        // snippet.publishJsonObjectOrgJson
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }
        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JSONObject().apply {
            put("lat", 32L)
            put("lng", 32L)
        }

        channel.publish(
            message = myMessage,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
    }

    private fun publishJsonArrayOrgJson() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonarray-orgjson

        // snippet.publishJsonArrayOrgJson
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }

        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JSONArray().apply {
            put(32L)
            put(33L)
        }

        channel.publish(
            message = myMessage,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
    }

    private fun publishAndStoreMessage10Hours() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#store-the-published-message-for-10-hours

        // snippet.publishAndStoreMessage10Hours
        val configBuilder = com.pubnub.api.v2.PNConfiguration.builder(UserId("myUserId"), "demo").apply {
            publishKey = "demo"
        }

        val pubnub = PubNub.create(configBuilder.build())

        val channel = pubnub.channel("myChannel")

        val myMessage = JsonObject().apply {
            addProperty("text", "Hello, world")
        }

        channel.publish(
            message = myMessage,
            shouldStore = true,
            ttl = 10,
            customMessageType = "text-message"
        ).async { result ->
            result.onFailure { exception ->
                println("Error while publishing")
                exception.printStackTrace()
            }.onSuccess { value ->
                println("Message sent, timetoken: ${value.timetoken}")
            }
        }
        // snippet.end
    }
}
