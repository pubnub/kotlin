 package com.pubnub.internal.v2.entities

 import com.pubnub.api.UserId
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PNConfigurationCore
import com.pubnub.internal.TestPubNub
 import com.pubnub.internal.v2.callbacks.EventListenerCore
 import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl
 import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

 class BaseChannelGroupImplTest {

    private lateinit var pn: TestPubNub
    private lateinit var channelGrp: BaseChannelGroupImpl<EventListenerCore, BaseSubscriptionImpl<EventListenerCore>>
    private val CHANNEL_GROUP_NAME = "myChannelGroup"

    @BeforeEach
    fun setUp() {
        pn = TestPubNub(PNConfigurationCore(UserId("uuid")))
        channelGrp = BaseChannelGroupImpl(pn.pubNubCore, ChannelGroupName(CHANNEL_GROUP_NAME)) { channels, channelGroups, options ->
            object : BaseSubscriptionImpl<EventListenerCore>(pn.pubNubCore, channels, channelGroups, options) {
                override fun addListener(listener: EventListenerCore) { }
            }
        }
    }

    @AfterEach
    fun teardown() {
        pn.destroy()
    }

    @Test
    fun `create subscription`() {
        val subscription = channelGrp.subscription()

        Assertions.assertEquals(setOf(ChannelGroupName(CHANNEL_GROUP_NAME)), subscription.channelGroups)
        Assertions.assertTrue(subscription.channels.isEmpty())
    }

    @Test
    fun `create subscription with presence`() {
        val subscriptions = channelGrp.subscription(SubscriptionOptions.receivePresenceEvents())

        Assertions.assertEquals(
            setOf(
                ChannelGroupName(CHANNEL_GROUP_NAME),
                ChannelGroupName(CHANNEL_GROUP_NAME).withPresence
            ),
            subscriptions.channelGroups
        )
        Assertions.assertTrue(subscriptions.channels.isEmpty())
    }

    @Test
    fun `create ChannelGroupName-pnpres from ChannelGroupName`() {
        val name = ChannelGroupName(CHANNEL_GROUP_NAME)

        val nameWithPresence = name.withPresence

        Assertions.assertEquals(ChannelGroupName("$CHANNEL_GROUP_NAME-pnpres"), nameWithPresence)
    }
 }
