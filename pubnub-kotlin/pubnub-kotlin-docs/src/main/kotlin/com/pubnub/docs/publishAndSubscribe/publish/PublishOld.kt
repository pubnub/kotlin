package com.pubnub.docs.publishAndSubscribe.publish

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pubnub.docs.SnippetBase
import org.json.JSONArray
import org.json.JSONObject

class PublishOld : SnippetBase() {
    private fun publishMessageToChannel() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#basic-usage-10

        val pubnub = createPubNub()

        // snippet.publishMessageToChannel
        pubnub.publish(
            message = JsonObject().apply {
                addProperty("lat", 32L)
                addProperty("lng", 32L)
            },
            channel = "my_channel"
        ).async { result ->
            result.onSuccess { value ->
                println("Publish timetoken ${value.timetoken}")
            }
        }
        // snippet.end
    }

    private fun publishWithMetadata() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publish-with-metadata-1

        val pubnub = createPubNub()

        // snippet.publishWithMetadata
        pubnub.publish(
            message = mapOf("hello" to "there"),
            channel = "my_channel",
            shouldStore = true,
            meta = mapOf("lang" to "en"),
            usePost = true
        ).async { result ->
            // check result
        }
        // snippet.end
    }

    private fun publishJsonObjectGson() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonobject-google-gson

        val pubnub = createPubNub()

        // snippet.publishJsonObjectGson
        pubnub.publish(
            message = JsonObject().apply {
                addProperty("lat", 32L)
                addProperty("lng", 32L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end
    }

    private fun publishJsonArrayGson() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonarray-google-gson-1

        val pubnub = createPubNub()

        // snippet.publishJsonArrayGson
        pubnub.publish(
            message = JsonArray().apply {
                add(32L)
                add(35L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end
    }

    private fun publishJsonObjectJsonOrg() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonobject-orgjson-1

        val pubnub = createPubNub()

        // snippet.publishJsonObjectJsonOrg
        pubnub.publish(
            message = JSONObject().apply {
                put("lat", 32L)
                put("lng", 32L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end
    }

    private fun publishJsonArrayJsonOrg() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#publishing-jsonarray-orgjson-1

        val pubnub = createPubNub()

        // snippet.publishJsonArrayJsonOrg
        pubnub.publish(
            message = JSONArray().apply {
                put(32L)
                put(33L)
            },
            channel = "my_channel"
        ).async { result ->
            // check result
        }
        // snippet.end
    }

    private fun publishAndStoreMessagesFor10Hours() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/publish-and-subscribe#store-the-published-message-for-10-hours-1

        val pubnub = createPubNub()

        // snippet.publishAndStoreMessagesFor10Hours
        pubnub.publish(
            message = "test",
            channel = "my_channel",
            shouldStore = true,
            ttl = 10
        ).async { result -> // check result }
            // snippet.end
        }
    }
}
