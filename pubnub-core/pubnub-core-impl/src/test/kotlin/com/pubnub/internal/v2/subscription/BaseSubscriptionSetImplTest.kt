package com.pubnub.internal.v2.subscriptions

import com.google.gson.JsonNull
import com.pubnub.api.BasePubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.internal.TestEventListener
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.BasePNConfigurationImpl
import com.pubnub.internal.v2.callbacks.EventListenerCore
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseSubscriptionSetImplTest {
    private lateinit var pubnub: TestPubNub
    private lateinit var subscriptionSet: BaseSubscriptionSetImpl<TestEventListener, BaseSubscriptionImpl<TestEventListener>>
    private lateinit var anotherSubscription: BaseSubscriptionImpl<TestEventListener>
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = TestPubNub(BasePNConfigurationImpl(UserId("uuid")))
        subscriptionSet =
            object :
                BaseSubscriptionSetImpl<TestEventListener, BaseSubscriptionImpl<TestEventListener>>(pubnub.pubNubCore) {
                override fun addListener(listener: TestEventListener) {
                    addListener(
                        object : EventListenerCore {
                            override fun message(
                                pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                                event: PNMessageResult,
                            ) {
                                listener.message(event)
                            }
                        },
                    )
                }
            }

        subscriptionSet.add(
            object : BaseSubscriptionImpl<TestEventListener>(pubnub.pubNubCore, setOf(ChannelName(channelName))) {
                override fun addListener(listener: TestEventListener) {
                    TODO("Not yet implemented")
                }
            },
        )

        anotherSubscription =
            object : BaseSubscriptionImpl<TestEventListener>(pubnub.pubNubCore, setOf(ChannelName("anotherChannel"))) {
                override fun addListener(listener: TestEventListener) {
                    TODO("Not yet implemented")
                }
            }
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
        assertEquals(setOf(channelName, "anotherChannel"), pubnub.pubNubCore.getSubscribedChannels().toSet())
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
        assertEquals(setOf(channelName), pubnub.pubNubCore.getSubscribedChannels().toSet())
    }

    @Test
    fun subscribe() {
        // when
        subscriptionSet.subscribe()

        // then
        assertEquals(setOf(channelName), pubnub.pubNubCore.getSubscribedChannels().toSet())
    }

    @Test
    fun unsubscribe() {
        // given
        subscriptionSet.subscribe()

        // when
        subscriptionSet.unsubscribe()

        // then
        assertEquals(emptyList<String>(), pubnub.pubNubCore.getSubscribedChannels())
    }

    @Test
    fun close() {
        // given
        subscriptionSet.subscribe()
        subscriptionSet.addListener(
            object : TestEventListener {
                override fun message(message: PNMessageResult) {
                    throw IllegalStateException("We should not get a message after close!")
                }
            },
        )

        // when
        subscriptionSet.close()

        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, 0L, null, "nonNull"),
                JsonNull.INSTANCE,
            ),
        )

        // then
        // no exception from listener
        assertEquals(emptyList<String>(), pubnub.pubNubCore.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.pubNubCore.getSubscribedChannelGroups())
    }
}
