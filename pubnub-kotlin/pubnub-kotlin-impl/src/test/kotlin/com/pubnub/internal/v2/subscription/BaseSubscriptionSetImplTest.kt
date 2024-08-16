package com.pubnub.internal.v2.subscriptions

import com.google.gson.JsonNull
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.PNConfigurationImpl
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseSubscriptionSetImplTest {
    private lateinit var pubnub: PubNubImpl
    private lateinit var subscriptionSet: SubscriptionSetImpl
    private lateinit var anotherSubscription: SubscriptionImpl
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = PubNubImpl(PNConfigurationImpl(UserId("uuid")))
        subscriptionSet = SubscriptionSetImpl(pubnub, setOf())

        subscriptionSet.add(
            SubscriptionImpl(pubnub, setOf(ChannelName(channelName)))
        )

        anotherSubscription = SubscriptionImpl(pubnub, setOf(ChannelName("anotherChannel")))
    }

    @AfterEach
    fun teardown() {
        subscriptionSet.close()
        anotherSubscription.close()
        pubnub.destroy()
    }

    @Test
    fun add() {
        // given

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
        subscriptionSet.add(anotherSubscription)

        // when
        subscriptionSet.remove(anotherSubscription)
        subscriptionSet.subscribe()

        // then
        assertFalse(subscriptionSet.subscriptions.contains(anotherSubscription))
        assertEquals(setOf(channelName), pubnub.getSubscribedChannels().toSet())
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
        subscriptionSet.addListener(
            object : EventListener {
                override fun message(pubnub: PubNub, result: PNMessageResult) {
                    throw IllegalStateException("We should not get a message after close!")
                }
            },
        )

        // when
        subscriptionSet.close()

        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, null, null, null),
                JsonNull.INSTANCE,
            ),
        )

        // then
        // no exception from listener
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannelGroups())
    }
}
