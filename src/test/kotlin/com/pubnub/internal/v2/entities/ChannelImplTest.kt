package com.pubnub.internal.v2.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.subscriptions.ReceivePresenceEvents
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChannelImplTest {

    private lateinit var pn: PubNub
    private val CHANNEL_NAME = "myChannel"

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

        Assertions.assertEquals(setOf(ChannelName(CHANNEL_NAME)), subscription.channels)
        Assertions.assertTrue(subscription.channelGroups.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val channel = ChannelImpl(pn, ChannelName(CHANNEL_NAME))

        val subscription = channel.subscription(ReceivePresenceEvents())

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
