package com.pubnub.api.hackathon

import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import java.time.Instant
import java.util.concurrent.*

typealias PublishCallback = (result: PNPublishResult?, status: PNStatus) -> Unit

data class Batch(
    val channel: String,
    val messages: MutableList<Any> = mutableListOf(),
    val scheduledRunTimestamp: Long,
    val callbacks: MutableList<PublishCallback> = mutableListOf()
)

data class NewPublish(val publish: Publish, val callback: PublishCallback)

class Batcher(
    private val pubnub: IPubNub,
    executorService: ExecutorService = Executors.newSingleThreadExecutor(),
    private val pendingBatches: MutableMap<String, Batch> = mutableMapOf(),
    private val batchQueue: LinkedBlockingQueue<NewPublish> = LinkedBlockingQueue(500)

) {
    private val batchingJob: CompletableFuture<Void>

    init {
        batchingJob = CompletableFuture.runAsync(Runnable {
            while (!Thread.interrupted()) {
                try {
                    sendAll()

                    val maybeNew = batchQueue.poll(howManyMsTillNextBatch() ?: 60_000, TimeUnit.MILLISECONDS)
                    if (maybeNew != null) {
                        val batch = pendingBatches.getOrPut(maybeNew.publish.channel) {
                            Batch(
                                channel = maybeNew.publish.channel,
                                scheduledRunTimestamp = Instant.now().plusMillis(pubnub.configuration.batchingMaxWindow)
                                    .toEpochMilli()
                            )
                        }
                        batch.messages.add(maybeNew.publish.message)
                        batch.callbacks.add(maybeNew.callback)
                        sendAll()
                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }, executorService)
    }

    private fun sendAll() {
        val nowTimestamp = Instant.now().toEpochMilli()

        val toSend =
            pendingBatches.values.filter { it.scheduledRunTimestamp <= nowTimestamp } + pendingBatches.values.filter { it.messages.size >= pubnub.configuration.batchingNumberMessages }
        toSend
            .forEach {
                pubnub.publish(
                    channel = it.channel,
                    message = it.messages,
                    meta = if (it.messages.size > 1) "batched" else null
                ).async { r, s ->
                    it.callbacks.forEach { callback -> callback(r, s) }
                }
            }
        toSend.forEach { pendingBatches.remove(it.channel) }

    }

    private fun howManyMsTillNextBatch(): Long {
        val nowTimestamp = Instant.now().toEpochMilli()
        return pendingBatches.values.minBy { it.scheduledRunTimestamp }?.scheduledRunTimestamp?.let { it - nowTimestamp }
            ?: 60_000
    }

    fun batch(publish: Publish, callback: PublishCallback) {
        batchQueue.put(NewPublish(publish = publish, callback = callback))
    }
}

class BatchablePublish(private val pubnub: IPubNub, private val batcher: Batcher, private val publish: Publish) :
    RemoteAction<PNPublishResult> by publish {

    override fun async(callback: PublishCallback) {
        if (pubnub.configuration.batchingMaxWindow > 0 || pubnub.configuration.batchingNumberMessages > 0) {
            batcher.batch(publish, callback)
        } else {
            publish.async(callback)
        }
    }
}
