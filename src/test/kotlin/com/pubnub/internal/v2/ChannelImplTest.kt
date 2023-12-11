package com.pubnub.internal.v2

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.ChannelOptions
import com.pubnub.api.v2.SubscriptionOptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val CHANNEL_NAME = "myChannel"

class ChannelImplTest {

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
        val channel = ChannelImpl(pn, ChannelName(CHANNEL_NAME))

        val subscription = channel.subscription()

        assertEquals(setOf(ChannelName(CHANNEL_NAME)), subscription.channels)
        assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val channel = ChannelImpl(pn, ChannelName(CHANNEL_NAME))

        val subscription = channel.subscription(ChannelOptions.receivePresenceEvents())

        assertEquals(setOf(ChannelName(CHANNEL_NAME), ChannelName(CHANNEL_NAME).withPresence), subscription.channels)
        assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create ChannelName-pnpres from ChannelName`() {
        val name = ChannelName(CHANNEL_NAME)

        val nameWithPresence = name.withPresence

        assertEquals(ChannelName("$CHANNEL_NAME-pnpres"), nameWithPresence)
    }
}
