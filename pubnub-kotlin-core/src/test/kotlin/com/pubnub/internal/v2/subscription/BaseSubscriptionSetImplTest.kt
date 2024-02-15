package com.pubnub.internal.v2.subscription

import com.google.gson.JsonNull
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.callbacks.EventListener
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseSubscriptionSetImplTest {
    private lateinit var pubnub: TestPubNub
    private lateinit var subscriptionSet: BaseSubscriptionSet
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = TestPubNub(PNConfiguration(UserId("uuid")))
        subscriptionSet = pubnub.pubNubImpl.subscriptionSetOf(channels = setOf(channelName))
    }

    @AfterEach
    fun teardown() {
        subscriptionSet.close()
        pubnub.destroy()
    }

    @Test
    fun add() {
        // given
        val anotherSubscription = pubnub.pubNubImpl.channel("anotherChannel").subscription()

        // when
        subscriptionSet.add(anotherSubscription)
        subscriptionSet.subscribe()

        // then
        assertTrue(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName, "anotherChannel"), pubnub.pubNubImpl.getSubscribedChannels().toSet())
    }

    @Test
    fun remove() {
        // given
        val anotherSubscription = pubnub.pubNubImpl.channel("anotherChannel").subscription()
        subscriptionSet.add(anotherSubscription)

        // when
        subscriptionSet.remove(anotherSubscription)
        subscriptionSet.subscribe()

        // then
        assertFalse(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName), pubnub.pubNubImpl.getSubscribedChannels().toSet())
    }

    @Test
    fun plus() {
        // given
        val anotherSubscription = pubnub.pubNubImpl.channel("anotherChannel").subscription()

        // when
        val originalSet = subscriptionSet
        subscriptionSet += anotherSubscription
        subscriptionSet.subscribe()

        // then
        assertEquals(originalSet, subscriptionSet)
        assertTrue(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName, "anotherChannel"), pubnub.pubNubImpl.getSubscribedChannels().toSet())
    }

    @Test
    fun subscribe() {
        // when
        subscriptionSet.subscribe()

        // then
        assertEquals(setOf(channelName), pubnub.pubNubImpl.getSubscribedChannels().toSet())
    }

    @Test
    fun unsubscribe() {
        // given
        subscriptionSet.subscribe()

        // when
        subscriptionSet.unsubscribe()

        // then
        assertEquals(emptyList<String>(), pubnub.pubNubImpl.getSubscribedChannels())
    }

    @Test
    fun close() {
        // given
        subscriptionSet.subscribe()
        subscriptionSet.addListener(object : EventListener {
            override fun message(pubnub: BasePubNub, result: PNMessageResult) {
                throw IllegalStateException("We should not get a message after close!")
            }
        })

        // when
        subscriptionSet.close()

        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, null, null, null),
                JsonNull.INSTANCE
            )
        )

        // then
        // no exception from listener
        assertEquals(emptyList<String>(), pubnub.pubNubImpl.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.pubNubImpl.getSubscribedChannelGroups())
    }
}
