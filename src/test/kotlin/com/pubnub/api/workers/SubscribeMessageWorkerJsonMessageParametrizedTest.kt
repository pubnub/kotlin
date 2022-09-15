package com.pubnub.api.workers

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.server.SubscribeMessage
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import org.hamcrest.core.Is.`is` as iz

@RunWith(Parameterized::class)
class SubscribeMessageWorkerJsonMessageParametrizedTest(
    private val messageJson: JsonElement
) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Any> {
            return listOf(
                JsonPrimitive("thisIsMessage"),
                JsonObject().apply { add("test", JsonPrimitive("value")) },
                JsonArray().apply {
                    add(JsonPrimitive("array"))
                    add(JsonPrimitive("of"))
                    add(JsonPrimitive("elements"))
                },
                JsonPrimitive(true),
                JsonPrimitive(1337),
                JsonNull.INSTANCE,
                JsonObject().apply {
                    add(
                        "with",
                        JsonObject().apply {
                            add(
                                "array",
                                JsonArray().apply {
                                    add(JsonPrimitive("array"))
                                    add(JsonPrimitive("of"))
                                    add(JsonPrimitive("elements"))
                                }
                            )
                        }
                    )
                },
            )
        }
    }

    @Test
    fun testDifferentJsonMessages() {
        val gson = Gson()
        val queue = LinkedBlockingQueue<SubscribeMessage>()
        val subscribeMessage =
            gson.fromJson(fileMessage(messageJson.toString()), SubscribeMessage::class.java)

        val maybeResult = queue.getFirstFileEventResult()

        queue.add(subscribeMessage)

        val result = maybeResult.get(1_000, TimeUnit.MILLISECONDS)
        assertThat(result.jsonMessage, iz(messageJson))
    }

    private fun LinkedBlockingQueue<SubscribeMessage>.getFirstFileEventResult(): CompletableFuture<PNFileEventResult> {
        val completableFuture = CompletableFuture<PNFileEventResult>()

        val configuration = PNConfiguration(userId = UserId("test"))
        val pubnub = PubNub(configuration)
        val listenerManager = ListenerManager(pubnub)

        val subscribeMessageWorker = SubscribeMessageWorker(
            pubnub = pubnub,
            listenerManager = listenerManager,
            queue = this,
            duplicationManager = DuplicationManager(configuration)
        )
        val thread = Thread {
            subscribeMessageWorker.run()
        }.apply { start() }

        listenerManager.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            }

            override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
                completableFuture.complete(pnFileEventResult)
            }
        })

        return completableFuture.whenCompleteAsync { t, u ->
            thread.interrupt()
        }
    }

    private fun fileMessage(messageJson: String) =
        """{"a":"0","f":0,"e":4,"i":"client-52774e6f-2f4e-4915-aefd-e8bb75cd2e7d","p":{"t":"16632349939765880","r":43},"k":"sub-c-4b1dbfef-2fa9-495f-a316-2b634063083d","c":"ch_1663234993171_F4FC4F460F","u":"This is meta","d":{"message":$messageJson,"file":{"id":"30ce0095-3c50-4cdc-a626-bf402d233731","name":"fileNamech_1663234993171_F4FC4F460F.txt"}}}"""
}
