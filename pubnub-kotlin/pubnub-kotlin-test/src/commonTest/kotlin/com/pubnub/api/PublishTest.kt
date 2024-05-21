package com.pubnub.api

import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.kmp.createEventListener
import com.pubnub.kmp.createStatusListener
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class PublishTest : BaseIntegrationTest() {
    private val channel = "myChannel"

    @Test
    fun can_publish_message() =
        runTest(timeout = 10.seconds) {
            val result = pubnub.publish(channel, "some message").await()
            // never getting to here
            assertTrue { result.timetoken > 0 }
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
        pubnub.subscribeAndWait(pubnub, channel)

        pubnub.publish(channel, "some message").await()
        val result = queue.receive()
        assertEquals("some message", result.message.asString())
    }
}

private suspend fun PubNub.subscribeAndWait(pubNub: PubNub, channel: String) = suspendCancellableCoroutine { cont ->
    val statusListener = createStatusListener(pubNub) { pubNub, pnStatus ->
        if (pnStatus.category == PNStatusCategory.PNConnectedCategory || pnStatus.category == PNStatusCategory.PNSubscriptionChanged
            && pnStatus.affectedChannels.contains(channel)) {
            cont.resume(Unit)
        }
        if (pnStatus.category == PNStatusCategory.PNUnexpectedDisconnectCategory || pnStatus.category == PNStatusCategory.PNConnectionError) {
            cont.resumeWithException(pnStatus.exception ?: RuntimeException(pnStatus.category.toString()))
        }
    }
    pubNub.addListener(statusListener)
    cont.invokeOnCancellation {
        pubNub.removeListener(statusListener)
    }
    subscribe(listOf(channel))
}
