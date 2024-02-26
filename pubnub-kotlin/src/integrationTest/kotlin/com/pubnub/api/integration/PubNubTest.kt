package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import org.junit.Assert
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.time.Duration

class PubNubTest(private val pubNub: PubNub, private val withPresenceOverride: Boolean) : AutoCloseable {
    private val messageQueue = ArrayBlockingQueue<PNEvent>(10)
    private val statusQueue = ArrayBlockingQueue<PNStatus>(10)

    private val verificationListener =
        object : SubscribeCallback() {
            override fun status(
                pubnub: PubNub,
                pnStatus: PNStatus,
            ) {
                statusQueue.put(pnStatus)
            }

            override fun message(
                pubnub: PubNub,
                event: PNMessageResult,
            ) {
                messageQueue.put(event)
            }

            override fun signal(
                pubnub: PubNub,
                event: PNSignalResult,
            ) {
                messageQueue.put(event)
            }

            override fun file(
                pubnub: PubNub,
                event: PNFileEventResult,
            ) {
                messageQueue.put(event)
            }

            override fun messageAction(
                pubnub: PubNub,
                event: PNMessageActionResult,
            ) {
                messageQueue.put(event)
            }

            override fun objects(
                pubnub: PubNub,
                event: PNObjectEventResult,
            ) {
                messageQueue.put(event)
            }

            override fun presence(
                pubnub: PubNub,
                event: PNPresenceEventResult,
            ) {
                messageQueue.put(event)
            }
        }

    init {
        pubNub.addListener(verificationListener)
    }

    fun subscribe(
        vararg channels: String,
        withPresence: Boolean = false,
    ) {
        subscribe(channels.toSet())
    }

    fun subscribe(
        channels: Collection<String> = emptyList(),
        channelGroups: Collection<String> = emptyList(),
        withPresence: Boolean = false,
    ) {
        pubNub.subscribe(channels.toList(), channelGroups.toList(), withPresence = withPresence || withPresenceOverride)
        val status = statusQueue.take()
        Assert.assertTrue(status.category == PNStatusCategory.Connected || status.category == PNStatusCategory.SubscriptionChanged)
        if (status.category == PNStatusCategory.Connected) {
            Assert.assertTrue(status.channels.containsAll(channels))
            Assert.assertTrue(status.channelGroups.containsAll(channelGroups))
        } else if (status.category == PNStatusCategory.SubscriptionChanged) {
            Assert.assertTrue(status.channels.containsAll(channels))
            Assert.assertTrue(status.channelGroups.containsAll(channelGroups))
        }
    }

    fun unsubscribe(
        channels: Collection<String> = emptyList(),
        channelGroups: Collection<String> = emptyList(),
    ) {
        pubNub.unsubscribe(channels.toList(), channelGroups.toList())
        val status = statusQueue.take()
        Assert.assertTrue(status.category == PNStatusCategory.Disconnected || status.category == PNStatusCategory.SubscriptionChanged)
        if (status.category == PNStatusCategory.SubscriptionChanged) {
            Assert.assertTrue(
                "Unsubscribe list: ${status.channels} doesn't contain all requested channels: $channels",
                status.channels.containsAll(channels),
            )
            Assert.assertTrue(
                "Unsubscribe list: ${status.channelGroups} doesn't contain all requested channelGroups: $channelGroups",
                status.channelGroups.containsAll(channelGroups),
            )
        }
    }

    fun unsubscribeAll() {
        pubNub.unsubscribeAll()
        val status = statusQueue.take()
        Assert.assertTrue(status.category == PNStatusCategory.Disconnected)
    }

    fun nextStatus(): PNStatus = statusQueue.take()

    fun nextStatus(timeout: Duration): PNStatus? {
        return try {
            FutureTask {
                statusQueue.take()
            }.get(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        } catch (e: TimeoutException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : PNEvent> nextEvent(): T {
        return messageQueue.take() as T
    }

    fun nextMessage() = nextEvent<PNMessageResult>()

    fun skip(n: Int = 1) {
        for (i in 0 until n) {
            nextEvent<PNEvent>()
        }
    }

    override fun close() {
        if (pubNub.getSubscribedChannels().isNotEmpty() || pubNub.getSubscribedChannelGroups().isNotEmpty()) {
            Thread.sleep(1000)
        }
        pubNub.removeListener(verificationListener)
        Assert.assertTrue(
            "There were ${messageQueue.size} unverified events in the test: ${messageQueue.joinToString(", ")}",
            messageQueue.isEmpty(),
        )
    }
}

fun PubNub.test(
    withPresence: Boolean = false,
    action: PubNubTest.() -> Unit,
) = PubNubTest(this, withPresence).use(action)
