package com.pubnub.internal.v2.entities

import com.pubnub.api.UserId
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChannelImplTest {

    private lateinit var pn: TestPubNub
    private val CHANNEL_NAME = "myChannel"

    @BeforeEach
    fun setUp() {
        pn = TestPubNub(PNConfiguration(UserId("uuid")))
    }

    @AfterEach
    fun teardown() {
        pn.destroy()
    }

    @Test
    fun `create subscription`() {
        val channel = BaseChannelImpl(pn.pubNubImpl, ChannelName(CHANNEL_NAME), ::BaseSubscriptionImpl)

        val subscription = channel.subscription()

        Assertions.assertEquals(setOf(ChannelName(CHANNEL_NAME)), subscription.channels)
        Assertions.assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val channel = BaseChannelImpl(pn.pubNubImpl, ChannelName(CHANNEL_NAME), ::BaseSubscriptionImpl)

        val subscription = channel.subscription(SubscriptionOptions.receivePresenceEvents())

        Assertions.assertEquals(
            setOf(ChannelName(CHANNEL_NAME), ChannelName(CHANNEL_NAME).withPresence),
            subscription.channels
        )
        Assertions.assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create ChannelName-pnpres from ChannelName`() {
        val name = ChannelName(CHANNEL_NAME)

        val nameWithPresence = name.withPresence

        Assertions.assertEquals(ChannelName("$CHANNEL_NAME-pnpres"), nameWithPresence)
    }
}
