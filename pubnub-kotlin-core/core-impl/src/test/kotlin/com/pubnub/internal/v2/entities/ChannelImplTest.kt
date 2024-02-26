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
// class ChannelImplTest {
//
//    private lateinit var pn: TestPubNub
//    private val CHANNEL_NAME = "myChannel"
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
//        val channel = BaseChannelImpl(pn.internalPubNubClient, ChannelName(CHANNEL_NAME), ::BaseSubscriptionImpl)
//
//        val subscriptions = channel.subscriptions()
//
//        Assertions.assertEquals(setOf(ChannelName(CHANNEL_NAME)), subscriptions.channels)
//        Assertions.assertTrue(subscriptions.channelGroups.isEmpty())
//    }
//
//    @Test
//    fun `create subscriptions with presence`() {
//        val channel = BaseChannelImpl(pn.internalPubNubClient, ChannelName(CHANNEL_NAME), ::BaseSubscriptionImpl)
//
//        val subscriptions = channel.subscriptions(SubscriptionOptions.receivePresenceEvents())
//
//        Assertions.assertEquals(
//            setOf(ChannelName(CHANNEL_NAME), ChannelName(CHANNEL_NAME).withPresence),
//            subscriptions.channels
//        )
//        Assertions.assertTrue(subscriptions.channelGroups.isEmpty())
//    }
//
//    @Test
//    fun `create ChannelName-pnpres from ChannelName`() {
//        val name = ChannelName(CHANNEL_NAME)
//
//        val nameWithPresence = name.withPresence
//
//        Assertions.assertEquals(ChannelName("$CHANNEL_NAME-pnpres"), nameWithPresence)
//    }
// }
