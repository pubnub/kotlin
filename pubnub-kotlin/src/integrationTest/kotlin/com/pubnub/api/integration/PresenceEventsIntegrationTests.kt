package com.pubnub.api.integration

import com.pubnub.api.CommonUtils
import com.pubnub.api.CommonUtils.generatePayload
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.subscribeToBlocking
import com.pubnub.api.unsubscribeFromBlocking
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class PresenceEventsIntegrationTests : BaseIntegrationTest() {

    lateinit var guest: PubNub

    override fun onBefore() {
        guest = createPubNub()
    }

    @Test
    fun testJoinChannel() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun presence(pubnub: PubNub, event: PNPresenceEventResult) {
                assertEquals("join", event.event)
                assertEquals(expectedChannel, event.channel)
                success.set(true)
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        success.listen()
    }

    @Test
    fun testJoinChannelUsingOnPresenceField() {
        val successPresenceEventCont = AtomicInteger()
        val expectedChannel = randomChannel()

        pubnub.onPresence = { pnPresenceEventResult: PNPresenceEventResult ->
            assertEquals("join", pnPresenceEventResult.event)
            assertEquals(expectedChannel, pnPresenceEventResult.channel)
            successPresenceEventCont.incrementAndGet()
        }

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.onPresence = null
        guest.subscribeToBlocking(expectedChannel)

        assertEquals(1, successPresenceEventCont.get())
    }

    @Test
    fun testJoinChannelUsingSubscription() {
        val successPresenceEventCont = AtomicInteger()
        val expectedChannel = randomChannel()
        val subscription = pubnub.channel(expectedChannel).subscription(SubscriptionOptions.receivePresenceEvents())

        subscription.onPresence = { pnPresenceEventResult: PNPresenceEventResult ->
            assertEquals("join", pnPresenceEventResult.event)
            assertEquals(expectedChannel, pnPresenceEventResult.channel)
            successPresenceEventCont.incrementAndGet()
        }

        subscription.subscribe()

        Thread.sleep(2000)
        assertEquals(1, successPresenceEventCont.get())
    }

    @Test
    fun testJoinChannelUsingSubscriptionSet() {
        val successPresenceEventCont = AtomicInteger()
        val expectedChannel01 = randomChannel()
        val expectedChannel02 = randomChannel()
        val subscriptionSetOf = pubnub.subscriptionSetOf(
            channels = setOf(expectedChannel01, expectedChannel02),
            options = SubscriptionOptions.receivePresenceEvents()
        )

        subscriptionSetOf.onPresence = { pnPresenceEventResult: PNPresenceEventResult ->
            assertEquals("join", pnPresenceEventResult.event)
            assertTrue(pnPresenceEventResult.channel == expectedChannel01 || pnPresenceEventResult.channel == expectedChannel02)
            successPresenceEventCont.incrementAndGet()
        }

        subscriptionSetOf.subscribe()

        Thread.sleep(2000)
        assertEquals(2, successPresenceEventCont.get())
    }

    @Test
    fun testLeaveChannel() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)
        guest.subscribeToBlocking(expectedChannel)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun presence(pubnub: PubNub, event: PNPresenceEventResult) {
                if (event.event == "leave") {
                    assertEquals(expectedChannel, event.channel)
                    success.set(true)
                }
            }
        })

        guest.unsubscribeFromBlocking(expectedChannel)

        Awaitility.await()
            .atMost(CommonUtils.DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .with()
            .pollInterval(Durations.TWO_SECONDS)
            .until {
                success.get()
            }
    }

    @Test
    fun testTimeoutFromChannel() {
        val expectedChannel = randomChannel()
        val expectedTimeout = 20

        //
        val subscribed = AtomicBoolean()
        val timeoutReceived = AtomicBoolean()
        //

        pubnub.configuration.presenceTimeout = expectedTimeout
        pubnub.configuration.heartbeatInterval = 0

        pubnub.addListener(object : SubscribeCallback() {

            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus is PNStatus.Connected) {
                    subscribed.set(true)
                }
            }

            override fun presence(pubnub: PubNub, event: PNPresenceEventResult) {
                if (event.event == "timeout") {
                    assertEquals(expectedChannel, event.channel)
                    timeoutReceived.set(true)
                }
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        Awaitility.await()
            .atMost(10, TimeUnit.SECONDS)
            .pollInterval(Durations.ONE_SECOND)
            .untilTrue(subscribed)

        Awaitility.await()
            .atMost(expectedTimeout + 3L, TimeUnit.SECONDS)
            .pollInterval(Durations.ONE_SECOND)
            .untilAtomic(timeoutReceived, IsEqual.equalTo(true))
    }

    @Test
    fun testStateChangeEvent() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.addListener(object : SubscribeCallback() {

            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun presence(pubnub: PubNub, event: PNPresenceEventResult) {
                if (event.event == "state-change") {
                    assertEquals(pubnub.configuration.userId.value, event.uuid)
                    success.set(true)
                }
            }
        })

        pubnub.setPresenceState(
            channels = listOf(expectedChannel),
            state = generatePayload()
        ).sync()

        success.listen()
    }
}
