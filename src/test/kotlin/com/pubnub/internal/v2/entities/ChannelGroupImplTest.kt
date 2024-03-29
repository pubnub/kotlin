package com.pubnub.internal.v2.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChannelGroupImplTest {

    private lateinit var pn: PubNub
    private val CHANNEL_GROUP_NAME = "myChannelGroup"

    @BeforeEach
    fun setUp() {
        pn = PubNub(PNConfiguration(UserId("uuid")))
    }

    @AfterEach
    fun teardown() {
        pn.destroy()
    }

    @Test
    fun `create subscription`() {
        val channelGrp = ChannelGroupImpl(pn, ChannelGroupName(CHANNEL_GROUP_NAME))

        val subscription = channelGrp.subscription()

        Assertions.assertEquals(setOf(ChannelGroupName(CHANNEL_GROUP_NAME)), subscription.channelGroups)
        Assertions.assertTrue(subscription.channels.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val channelGrp = ChannelGroupImpl(pn, ChannelGroupName(CHANNEL_GROUP_NAME))

        val subscription = channelGrp.subscription(SubscriptionOptions.receivePresenceEvents())

        Assertions.assertEquals(
            setOf(
                ChannelGroupName(CHANNEL_GROUP_NAME),
                ChannelGroupName(CHANNEL_GROUP_NAME).withPresence
            ),
            subscription.channelGroups
        )
        Assertions.assertTrue(subscription.channels.isEmpty())
    }

    @Test
    fun `create ChannelGroupName-pnpres from ChannelGroupName`() {
        val name = ChannelGroupName(CHANNEL_GROUP_NAME)

        val nameWithPresence = name.withPresence

        Assertions.assertEquals(ChannelGroupName("$CHANNEL_GROUP_NAME-pnpres"), nameWithPresence)
    }
}
