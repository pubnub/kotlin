package com.pubnub.internal.v2.subscription

import com.google.gson.JsonNull
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.callbacks.EventListener
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SubscriptionImplTest {

    private lateinit var pubnub: TestPubNub
    private lateinit var subscription: BaseSubscription
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = TestPubNub(PNConfiguration(UserId("uuid")))
        subscription = pubnub.pubNubImpl.channel(channelName).subscription()
    }

    @AfterEach
    fun teardown() {
        subscription.close()
        pubnub.destroy()
    }

    @Test
    fun `subscription + subscription should return subscriptionSet`() {
        // given
        val anotherSubscription = pubnub.pubNubImpl.channel("anotherChannel").subscription()

        // when
        val subscriptionSet = subscription + anotherSubscription

        // then
        assertEquals(setOf(subscription, anotherSubscription), subscriptionSet.subscriptions)
    }

    @Test
    fun subscribe() {
        // when
        subscription.subscribe()

        // then
        assertEquals(listOf(channelName), pubnub.pubNubImpl.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.pubNubImpl.getSubscribedChannelGroups())
    }

    @Test
    fun unsubscribe() {
        // given
        subscription.subscribe()

        // when
        subscription.unsubscribe()

        // then
        assertEquals(emptyList<String>(), pubnub.pubNubImpl.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.pubNubImpl.getSubscribedChannelGroups())
    }

    @Test
    fun close() {
        // given
        subscription.subscribe()
        subscription.addListener(object : EventListener {
            override fun message(pubnub: BasePubNub, result: PNMessageResult) {
                throw IllegalStateException("We should not get a message after close!")
            }
        })

        // when
        subscription.close()

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

    @Test
    fun `subscription with filter`() {
        // given
        val subWithFilter =
            pubnub.pubNubImpl.channel(channelName).subscription(SubscriptionOptions.filter { it !is PNMessageResult })
        subWithFilter.addListener(object : EventListener {
            override fun message(pubnub: BasePubNub, result: PNMessageResult) {
                throw IllegalStateException("Message should have been filtered out!")
            }
        })

        // when
        subWithFilter.subscribe()
        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, null, null, null),
                JsonNull.INSTANCE
            )
        )

        // then
        // no exception
    }

    @Test
    fun `subscription shouldn't get events until subscribe is called`() {
        // given
        subscription.addListener(object : EventListener {
            override fun message(pubnub: BasePubNub, result: PNMessageResult) {
                throw IllegalStateException("Message should not be received without subscribe call!")
            }
        })

        // when
        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, 1000L, null, null),
                JsonNull.INSTANCE
            )
        )

        // then
        // no exception
    }
}
