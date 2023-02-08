package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import java.util.Locale
import java.util.UUID
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

fun PubNub.subscribeAndWaitForConnected(channel: String, listener: SubscribeCallback): SubscribeCallback {
    addListener(listener)
    val connectedLatch = CountDownLatch(1)
    val statusListener = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            if (pnStatus.category == PNStatusCategory.PNConnectedCategory) {
                connectedLatch.countDown()
            }
        }
    }
    addListener(statusListener)
    subscribe(channels = listOf(channel))

    if (!connectedLatch.await(3_000, TimeUnit.MILLISECONDS)) {
        throw RuntimeException("Connected not received after 3000ms")
    }

    removeListener(statusListener)
    return listener
}

data class SubscribeContext(
    val channel: String,
    val receivedSignals: BlockingQueue<PNSignalResult> = LinkedBlockingDeque(),
    val receivedMessages: BlockingQueue<PNMessageResult> = LinkedBlockingDeque(),
    val receivedFiles: BlockingQueue<PNFileEventResult> = LinkedBlockingDeque(),
    val messageFuture: CompletableFuture<PNMessageResult> = CompletableFuture(),
    val signalFuture: CompletableFuture<PNSignalResult> = CompletableFuture(),
)

fun randomValue(length: Int = 10): String {
    return UUID.randomUUID().toString().replace("-", "").take(length).uppercase(Locale.getDefault())
}

fun randomChannel(): String {
    return "ch_${System.currentTimeMillis()}_${randomValue()}"
}

fun PubNub.doWhenSubscribedAndConnected(
    channel: String = randomChannel(),
    block: (SubscribeContext) -> Unit
) {
    val subscribeContext = SubscribeContext(channel = channel)

    val listener = subscribeAndWaitForConnected(
        subscribeContext.channel,
        object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                subscribeContext.receivedMessages.put(pnMessageResult)
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                subscribeContext.receivedSignals.put(pnSignalResult)
            }

            override fun file(pubnub: PubNub, pnFileEventResult: PNFileEventResult) {
                subscribeContext.receivedFiles.put(pnFileEventResult)
            }
        }
    )

    try {
        block(subscribeContext)
    } finally {
        removeListener(listener)
    }
}

fun <T> BlockingQueue<T>.pollOrThrow(timeout: Long, unit: TimeUnit): T {
    val result = poll(timeout, unit)
    return checkNotNull(result) { "No element to poll in required time: $timeout $unit" }
}
