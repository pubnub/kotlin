package com.pubnub.internal.v2.entities

import com.pubnub.api.UserId
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.PNConfigurationImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BaseChannelImplTest {
    private lateinit var pn: PubNubImpl
    private lateinit var channel: ChannelImpl
    private val channelName = "myChannel"

    @BeforeEach
    fun setUp() {
        pn = PubNubImpl(PNConfigurationImpl(UserId("uuid")))
        channel = ChannelImpl(pn, ChannelName(channelName))
    }

    @AfterEach
    fun teardown() {
        pn.destroy()
    }

    @Test
    fun `create subscription`() {
        val subscription = channel.subscription()

        Assertions.assertEquals(setOf(ChannelName(channelName)), subscription.channels)
        Assertions.assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val subscriptions = channel.subscription(SubscriptionOptions.receivePresenceEvents())

        Assertions.assertEquals(
            setOf(
                ChannelName(channelName),
                ChannelName(channelName).withPresence,
            ),
            subscriptions.channels,
        )
        Assertions.assertTrue(subscriptions.channelGroups.isEmpty())
    }

    @Test
    fun `create ChannelName-pnpres from ChannelName`() {
        val name = ChannelName(channelName)

        val nameWithPresence = name.withPresence

        Assertions.assertEquals(ChannelName("$channelName-pnpres"), nameWithPresence)
    }
}
