package com.pubnub.internal.v2.subscriptions

import com.google.gson.JsonNull
import com.pubnub.api.BasePubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.TestEventListener
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.BasePNConfigurationImpl
import com.pubnub.internal.v2.callbacks.EventListenerCore
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseSubscriptionImplTest {
    private lateinit var pubnub: TestPubNub
    private lateinit var subscription: BaseSubscriptionImpl<TestEventListener>
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pubnub = TestPubNub(BasePNConfigurationImpl(UserId("uuid")))
        subscription =
            object : BaseSubscriptionImpl<TestEventListener>(pubnub.pubNubCore, setOf(ChannelName(channelName))) {
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
        assertEquals(listOf(channelName), pubnub.pubNubCore.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.pubNubCore.getSubscribedChannelGroups())
    }

    @Test
    fun `unsubscribe removes channel from mix`() {
        // given
        subscription.subscribe()

        // when
        subscription.unsubscribe()

        // then
        assertEquals(emptyList<String>(), pubnub.pubNubCore.getSubscribedChannels())
        assertEquals(emptyList<String>(), pubnub.pubNubCore.getSubscribedChannelGroups())
    }

    @Test
    fun `close stops the subscription`() {
        // given
        subscription.subscribe()
        subscription.addListener(TestEventListener { throw IllegalStateException("We should not get a message after close!") })

        // when
        subscription.close()

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

    @Test
    fun `subscriptions with filter doesn't deliver filtered messages`() {
        // given
        val subWithFilter =
            object : BaseSubscriptionImpl<TestEventListener>(
                pubnub.pubNubCore,
                setOf(
                    ChannelName(channelName),
                ),
                options =
                    SubscriptionOptions.filter {
                        it !is PNMessageResult
                    },
            ) {
                override fun addListener(listener: TestEventListener) {
                    TODO("Not yet implemented")
                }
            }

        // when
        subWithFilter.subscribe()
        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, 0L, null, "nonNull"),
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
            TestEventListener { throw IllegalStateException("Message should not be received without subscribe call!") },
        )

        // when
        pubnub.listenerManager.announce(
            PNMessageResult(
                BasePubSubResult(channelName, null, 1000L, null, "nonNull"),
                JsonNull.INSTANCE,
            ),
        )

        // then
        // no exception
    }
}
