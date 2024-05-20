package com.pubnub.api

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.kmp.createEventListener
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class PublishTest : BaseIntegrationTest() {
    private val channel = "myChannel"

    @Test
    fun can_publish_message() {
        runTest(timeout = 10.seconds) {
            println("A" + Clock.System.now())
            val result = pubnub.publish(channel, "some message").await()
            // never getting to here
            assertTrue { result.timetoken > 0 }
            println("B" + Clock.System.now())
        }
        println("C" + Clock.System.now())
    }

    @Test
    @Ignore
    fun can_receive_message() = runTest(timeout = 10.seconds) {
        val queue = Channel<PNMessageResult>()
        pubnub.addListener(createEventListener(pubnub, onMessage = { pubNub, pnMessageResult ->
            backgroundScope.launch {
                queue.send(pnMessageResult)
            }
        }))
        pubnub.subscribeWithDelay(channel)

        pubnub.publish(channel, "some message").await()
        val result = queue.receive()
        assertEquals("some message", result.message.asString())
    }
}

private suspend fun PubNub.subscribeWithDelay(channel: String) {
    subscribe(listOf(channel))
    withContext(Dispatchers.Default) {
        delay(1.seconds)
    }
}
