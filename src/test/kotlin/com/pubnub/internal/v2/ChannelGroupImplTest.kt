package com.pubnub.internal.v2

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.SubscriptionOptions
import com.pubnub.api.v2.receivePresenceEvents
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val CHANNEL_GROUP_NAME = "myChannelGroup"

class ChannelGroupImplTest {

    private lateinit var pn: PubNub

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

        assertEquals(setOf(ChannelGroupName(CHANNEL_GROUP_NAME)), subscription.channelGroups)
        assertTrue(subscription.channels.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val channelGrp = ChannelGroupImpl(pn, ChannelGroupName(CHANNEL_GROUP_NAME))

        val subscription = channelGrp.subscription(SubscriptionOptions.Channel.receivePresenceEvents())

        assertEquals(setOf(ChannelGroupName(CHANNEL_GROUP_NAME), ChannelGroupName(CHANNEL_GROUP_NAME).withPresence), subscription.channelGroups)
        assertTrue(subscription.channels.isEmpty())
    }

    @Test
    fun `create ChannelGroupName-pnpres from ChannelGroupName`() {
        val name = ChannelGroupName(CHANNEL_GROUP_NAME)

        val nameWithPresence = name.withPresence

        assertEquals(ChannelGroupName("$CHANNEL_GROUP_NAME-pnpres"), nameWithPresence)
    }
}
