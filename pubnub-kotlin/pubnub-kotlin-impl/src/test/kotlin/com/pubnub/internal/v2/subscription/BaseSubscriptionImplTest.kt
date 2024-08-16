package com.pubnub.internal.v2.subscriptions

import com.google.gson.JsonNull
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.PNConfigurationImpl
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseSubscriptionImplTest {
    private lateinit var pubnub: PubNubImpl
    private lateinit var subscription: SubscriptionImpl
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = PubNubImpl(PNConfigurationImpl(UserId("uuid")))
        subscription = SubscriptionImpl(pubnub, setOf(ChannelName(channelName)))
    }

    @AfterEach
    fun teardown() {
        subscription.close()
        pubnub.destroy()
    }

    @Test
    fun `subscribe adds channel to mix`() {
        // when
        subscription.subscribe()

        // then
        assertEquals(listOf(channelName), pubnub.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannelGroups())
    }

    @Test
    fun `unsubscribe removes channel from mix`() {
        // given
        subscription.subscribe()

        // when
        subscription.unsubscribe()

        // then
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannelGroups())
    }

    @Test
    fun `close stops the subscription`() {
        // given
        subscription.subscribe()
        subscription.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                throw IllegalStateException("We should not get a message after close!")
            }
        })

        // when
        subscription.close()

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

    @Test
    fun `subscriptions with filter doesn't deliver filtered messages`() {
        // given
        val subWithFilter =
            SubscriptionImpl(
                pubnub,
                setOf(
                    ChannelName(channelName),
                ),
                options =
                    SubscriptionOptions.filter {
                        it !is PNMessageResult
                    },
            )

        // when
        subWithFilter.subscribe()
        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, null, null, null),
                JsonNull.INSTANCE,
            ),
        )

        // then
        // no exception
    }

    @Test
    fun `subscription doesn't get events until subscribe is called`() {
        // given
        subscription.addListener(
            object : EventListener {
                override fun message(pubnub: PubNub, result: PNMessageResult) {
                    throw IllegalStateException("Message should not be received without subscribe call!")
                }
            }
        )

        // when
        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, 1000L, null, null),
                JsonNull.INSTANCE,
            ),
        )

        // then
        // no exception
    }
}
