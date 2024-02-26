// package com.pubnub.internal.v2.subscriptions
//
// import com.google.gson.JsonNull
// import com.pubnub.api.UserId
// import com.pubnub.api.callbacks.Listener
// import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
// import com.pubnub.api.models.consumer.pubsub.PNMessageResult
// import com.pubnub.api.v2.subscriptions.BaseSubscription
// import com.pubnub.api.v2.subscriptions.SubscriptionOptions
// import com.pubnub.internal.BasePubNubImpl
// import com.pubnub.internal.PNConfiguration
// import com.pubnub.internal.TestPubNub
// import com.pubnub.internal.v2.callbacks.InternalEventListener
// import org.junit.jupiter.api.AfterEach
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
//
// class SubscriptionImplTest {
//
//    private lateinit var pubnub: TestPubNub
//    private lateinit var subscriptions: BaseSubscription
//    private val channelName = "myChannel"
//
//    @BeforeEach
//    fun setUp() {
//        pubnub = TestPubNub(PNConfiguration(UserId("uuid")))
//        subscriptions = pubnub.internalPubNubClient.channel(channelName).subscriptions()
//    }
//
//    @AfterEach
//    fun teardown() {
//        subscriptions.close()
//        pubnub.destroy()
//    }
//
//    @Test
//    fun `subscriptions + subscriptions should return subscriptionSet`() {
//        // given
//        val anotherSubscription = pubnub.internalPubNubClient.channel("anotherChannel").subscriptions()
//
//        // when
//        val subscriptionSet = subscriptions + anotherSubscription
//
//        // then
//        assertEquals(setOf(subscriptions, anotherSubscription), subscriptionSet.subscriptions)
//    }
//
//    @Test
//    fun subscribe() {
//        // when
//        subscriptions.subscribe()
//
//        // then
//        assertEquals(listOf(channelName), pubnub.internalPubNubClient.getSubscribedChannels())
//        assertEquals(emptyList<String>(), pubnub.internalPubNubClient.getSubscribedChannelGroups())
//    }
//
//    @Test
//    fun unsubscribe() {
//        // given
//        subscriptions.subscribe()
//
//        // when
//        subscriptions.unsubscribe()
//
//        // then
//        assertEquals(emptyList<String>(), pubnub.internalPubNubClient.getSubscribedChannels())
//        assertEquals(emptyList<String>(), pubnub.internalPubNubClient.getSubscribedChannelGroups())
//    }
//
//    @Test
//    fun close() {
//        // given
//        subscriptions.subscribe()
//        subscriptions.addListener(object : InternalEventListener {
//            override fun message(pubnub: BasePubNubImpl<Listener>, result: PNMessageResult) {
//                throw IllegalStateException("We should not get a message after close!")
//            }
//        })
//
//        // when
//        subscriptions.close()
//
//        pubnub.listenerManager.announce(
//            PNMessageResult(
//                BasePubSubResult(channelName, null, null, null, null),
//                JsonNull.INSTANCE
//            )
//        )
//
//        // then
//        // no exception from listener
//        assertEquals(emptyList<String>(), pubnub.internalPubNubClient.getSubscribedChannels())
//        assertEquals(emptyList<String>(), pubnub.internalPubNubClient.getSubscribedChannelGroups())
//    }
//
//    @Test
//    fun `subscriptions with filter`() {
//        // given
//        val subWithFilter =
//            pubnub.internalPubNubClient.channel(channelName).subscriptions(SubscriptionOptions.filter { it !is PNMessageResult })
//        subWithFilter.addListener(object : InternalEventListener {
//            override fun message(pubnub: BasePubNubImpl<Listener>, result: PNMessageResult) {
//                throw IllegalStateException("Message should have been filtered out!")
//            }
//        })
//
//        // when
//        subWithFilter.subscribe()
//        pubnub.listenerManager.announce(
//            PNMessageResult(
//                BasePubSubResult(channelName, null, null, null, null),
//                JsonNull.INSTANCE
//            )
//        )
//
//        // then
//        // no exception
//    }
//
//    @Test
//    fun `subscriptions shouldn't get events until subscribe is called`() {
//        // given
//        subscriptions.addListener(object : InternalEventListener {
//            override fun message(pubnub: BasePubNubImpl<Listener>, result: PNMessageResult) {
//                throw IllegalStateException("Message should not be received without subscribe call!")
//            }
//        })
//
//        // when
//        pubnub.listenerManager.announce(
//            PNMessageResult(
//                BasePubSubResult(channelName, null, 1000L, null, null),
//                JsonNull.INSTANCE
//            )
//        )
//
//        // then
//        // no exception
//    }
// }
