// package com.pubnub.internal.v2.entities
//
// import com.pubnub.api.UserId
// import com.pubnub.api.v2.subscriptions.SubscriptionOptions
// import com.pubnub.internal.PNConfiguration
// import com.pubnub.internal.TestPubNub
// import com.pubnub.internal.v2.subscriptions.BaseSubscriptionImpl
// import org.junit.jupiter.api.AfterEach
// import org.junit.jupiter.api.Assertions
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
//
// class BaseChannelGroupImplTest {
//
//    private lateinit var pn: TestPubNub
//    private val CHANNEL_GROUP_NAME = "myChannelGroup"
//
//    @BeforeEach
//    fun setUp() {
//        pn = TestPubNub(PNConfiguration(UserId("uuid")))
//    }
//
//    @AfterEach
//    fun teardown() {
//        pn.destroy()
//    }
//
//    @Test
//    fun `create subscriptions`() {
//        val channelGrp =
//            BaseChannelGroupImpl(pn.internalPubNubClient, ChannelGroupName(CHANNEL_GROUP_NAME), ::BaseSubscriptionImpl)
//
//        val subscriptions = channelGrp.subscriptions()
//
//        Assertions.assertEquals(setOf(ChannelGroupName(CHANNEL_GROUP_NAME)), subscriptions.channelGroups)
//        Assertions.assertTrue(subscriptions.channels.isEmpty())
//    }
//
//    @Test
//    fun `create subscriptions with presence`() {
//        val channelGrp =
//            BaseChannelGroupImpl(pn.internalPubNubClient, ChannelGroupName(CHANNEL_GROUP_NAME), ::BaseSubscriptionImpl)
//
//        val subscriptions = channelGrp.subscriptions(SubscriptionOptions.receivePresenceEvents())
//
//        Assertions.assertEquals(
//            setOf(
//                ChannelGroupName(CHANNEL_GROUP_NAME),
//                ChannelGroupName(CHANNEL_GROUP_NAME).withPresence
//            ),
//            subscriptions.channelGroups
//        )
//        Assertions.assertTrue(subscriptions.channels.isEmpty())
//    }
//
//    @Test
//    fun `create ChannelGroupName-pnpres from ChannelGroupName`() {
//        val name = ChannelGroupName(CHANNEL_GROUP_NAME)
//
//        val nameWithPresence = name.withPresence
//
//        Assertions.assertEquals(ChannelGroupName("$CHANNEL_GROUP_NAME-pnpres"), nameWithPresence)
//    }
// }
