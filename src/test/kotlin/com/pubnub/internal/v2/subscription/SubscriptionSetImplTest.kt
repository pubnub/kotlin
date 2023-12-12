package com.pubnub.internal.v2.subscription

import com.google.gson.JsonNull
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionSetImplTest {
    private lateinit var pubnub: PubNub
    private lateinit var subscriptionSet: SubscriptionSet
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = PubNub(PNConfiguration(UserId("uuid")))
        subscriptionSet = pubnub.subscriptionSetOf(channels = setOf(channelName))
    }

    @AfterEach
    fun teardown() {
        subscriptionSet.close()
        pubnub.destroy()
    }

    @Test
    fun add() {
        // given
        val anotherSubscription = pubnub.channel("anotherChannel").subscription()

        // when
        subscriptionSet.add(anotherSubscription)
        subscriptionSet.subscribe()

        // then
        assertTrue(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName, "anotherChannel"), pubnub.getSubscribedChannels().toSet())
    }

    @Test
    fun remove() {
        // given
        val anotherSubscription = pubnub.channel("anotherChannel").subscription()
        subscriptionSet.add(anotherSubscription)

        // when
        subscriptionSet.remove(anotherSubscription)
        subscriptionSet.subscribe()

        // then
        assertFalse(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName), pubnub.getSubscribedChannels().toSet())
    }

    @Test
    fun plus() {
        // given
        val anotherSubscription = pubnub.channel("anotherChannel").subscription()

        // when
        val originalSet = subscriptionSet
        subscriptionSet += anotherSubscription
        subscriptionSet.subscribe()

        // then
        assertEquals(originalSet, subscriptionSet)
        assertTrue(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName, "anotherChannel"), pubnub.getSubscribedChannels().toSet())
    }

    @Test
    fun subscribe() {
        // when
        subscriptionSet.subscribe()

        // then
        assertEquals(setOf(channelName), pubnub.getSubscribedChannels().toSet())
    }

    @Test
    fun unsubscribe() {
        // given
        subscriptionSet.subscribe()

        // when
        subscriptionSet.unsubscribe()

        // then
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannels())
    }

    @Test
    fun close() {
        // given
        subscriptionSet.subscribe()
        subscriptionSet.addListener(object : EventListener() {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                throw IllegalStateException("We should not get a message after close!")
            }
        })

        // when
        subscriptionSet.close()

        (subscriptionSet as SubscriptionSetImpl).eventEmitter.subscribeCallback.message(
            pubnub,
            PNMessageResult(
                BasePubSubResult(channelName, null, null, null, null),
                JsonNull.INSTANCE
            )
        )

        // then
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannelGroups())
    }
}
