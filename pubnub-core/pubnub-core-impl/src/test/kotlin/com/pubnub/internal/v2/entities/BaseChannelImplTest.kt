package com.pubnub.internal.v2.entities

import com.pubnub.api.UserId
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PNConfigurationCore
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseChannelImplTest {

    private lateinit var pn: TestPubNub
    private lateinit var channel: BaseChannelImpl<BaseEventListener, BaseSubscriptionImpl<BaseEventListener>>
    private val CHANNEL_NAME = "myChannel"

    @BeforeEach
    fun setUp() {
        pn = TestPubNub(PNConfigurationCore(UserId("uuid")))
        channel = BaseChannelImpl(pn.pubNubCore, ChannelName(CHANNEL_NAME)) { channels, channelGroups, options ->
            object : BaseSubscriptionImpl<BaseEventListener>(pn.pubNubCore, channels, channelGroups, options) {
                override fun addListener(listener: BaseEventListener) { }
            }
        }
    }

    @AfterEach
    fun teardown() {
        pn.destroy()
    }

    @Test
    fun `create subscription`() {
        val subscription = channel.subscription()

        Assertions.assertEquals(setOf(ChannelName(CHANNEL_NAME)), subscription.channels)
        Assertions.assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val subscriptions = channel.subscription(SubscriptionOptions.receivePresenceEvents())

        Assertions.assertEquals(
            setOf(
                ChannelName(CHANNEL_NAME),
                ChannelName(CHANNEL_NAME).withPresence
            ),
            subscriptions.channels
        )
        Assertions.assertTrue(subscriptions.channelGroups.isEmpty())
    }

    @Test
    fun `create ChannelName-pnpres from ChannelName`() {
        val name = ChannelName(CHANNEL_NAME)

        val nameWithPresence = name.withPresence

        Assertions.assertEquals(ChannelName("$CHANNEL_NAME-pnpres"), nameWithPresence)
    }
}
