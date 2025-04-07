package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.test.CommonUtils
import com.pubnub.test.CommonUtils.DEFAULT_LISTEN_DURATION
import com.pubnub.test.CommonUtils.generatePayload
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.listen
import com.pubnub.test.subscribeToBlocking
import com.pubnub.test.unsubscribeFromBlocking
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class PresenceEventsIntegrationTests : BaseIntegrationTest() {
    lateinit var guest: PubNub

    override fun onBefore() {
        guest = createPubNub {}
    }

    @Test
    fun testJoinChannel() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {}

                override fun presence(
                    pubnub: PubNub,
                    event: PNPresenceEventResult,
                ) {
                    assertEquals("join", event.event)
                    assertEquals(expectedChannel, event.channel)
                    success.set(true)
                }
            },
        )

        pubnub.subscribeToBlocking(expectedChannel)

        success.listen()
    }

    @Test
    fun testMultipleSubscribeShouldCauseLeaveEventToAppear() {
        // “Generate Leave on TCP FIN or RST” should be disabled
        val countDownLatchForJoinChannel01 = CountDownLatch(1)
        val countDownLatchForJoinChannel02 = CountDownLatch(1)
        val countDownLatchForJoinChannel03 = CountDownLatch(1)
        val channel01Name = randomChannel() + "chan01"
        val channel02Name = randomChannel() + "chan02"
        val channel03Name = randomChannel() + "chan03"

        pubnub.addListener(object : EventListener {
            override fun presence(pubnub: PubNub, result: PNPresenceEventResult) {
                if (result.event == "join") {
                    when (result.channel) {
                        channel01Name -> countDownLatchForJoinChannel01.countDown()
                        channel02Name -> countDownLatchForJoinChannel02.countDown()
                        channel03Name -> countDownLatchForJoinChannel03.countDown()
                    }
                }
                println("-= global onPresence channel: ${result.channel} event: ${result.event} occupancy: ${result.occupancy}")
            }
        })

        pubnub.subscribe(channels = listOf(channel01Name), withPresence = true)
        Thread.sleep(2000)
        pubnub.subscribe(channels = listOf(channel01Name, channel02Name), withPresence = true)
        Thread.sleep(2000)
        pubnub.subscribe(channels = listOf(channel03Name), withPresence = true)

        assertTrue(countDownLatchForJoinChannel01.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
        assertTrue(countDownLatchForJoinChannel02.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
        assertTrue(countDownLatchForJoinChannel03.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @Test
    fun testMultipleSubscribeOnChannelEntitiesShouldCauseLeaveEventToAppear() {
        // “Generate Leave on TCP FIN or RST” should be disabled
        val countDownLatchForJoinPubNubUser = CountDownLatch(1)
        val countDownLatchForJoinPubQuestUser = CountDownLatch(1)
        val channel01Name = randomChannel() + "chan01"

        val subscription01 = pubnub.channel(channel01Name).subscription(SubscriptionOptions.receivePresenceEvents())
        val subscription02 = guest.channel(channel01Name).subscription(SubscriptionOptions.receivePresenceEvents())

        subscription01.onPresence = { pnPresenceEventResult: PNPresenceEventResult ->
            println("-= global1 onPresence channel: ${pnPresenceEventResult.channel} event: ${pnPresenceEventResult.event} occupancy: ${pnPresenceEventResult.occupancy}")
            println("-= ${pubnub.configuration.userId.value == pnPresenceEventResult.uuid}")
            if (pubnub.configuration.userId.value == pnPresenceEventResult.uuid && pnPresenceEventResult.event == "join") {
                countDownLatchForJoinPubNubUser.countDown()
            }
            if (guest.configuration.userId.value == pnPresenceEventResult.uuid && pnPresenceEventResult.event == "join") {
                countDownLatchForJoinPubQuestUser.countDown()
            }
        }

        subscription01.subscribe()
        Thread.sleep(2000)
        subscription02.subscribe()
        Thread.sleep(2000)

        assertTrue(countDownLatchForJoinPubNubUser.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
        assertTrue(countDownLatchForJoinPubQuestUser.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
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
        val expectedChannel = randomChannel()
        val subscription = pubnub.channel(expectedChannel).subscription(SubscriptionOptions.receivePresenceEvents())
        val capturePresenceEvent = mutableListOf<PNPresenceEventResult>()
        val countDownLatch = CountDownLatch(1)

        subscription.onPresence = { pnPresenceEventResult: PNPresenceEventResult ->
            capturePresenceEvent.add(pnPresenceEventResult)
            countDownLatch.countDown()
        }

        subscription.subscribe()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
        assertEquals(1, capturePresenceEvent.size)
        assertEquals("join", capturePresenceEvent.first().event)
        assertEquals(expectedChannel, capturePresenceEvent.first().channel)
    }

    @Test
    fun testJoinChannelUsingSubscriptionSet() {
        val countDownLatch = CountDownLatch(2)
        val capturePresenceEvent = mutableListOf<PNPresenceEventResult>()
        val expectedChannel01 = randomChannel()
        val expectedChannel02 = randomChannel()
        val subscriptionSetOf =
            pubnub.subscriptionSetOf(
                channels = setOf(expectedChannel01, expectedChannel02),
                options = SubscriptionOptions.receivePresenceEvents(),
            )

        subscriptionSetOf.onPresence = { pnPresenceEventResult: PNPresenceEventResult ->
            capturePresenceEvent.add(pnPresenceEventResult)
            countDownLatch.countDown()
        }

        subscriptionSetOf.subscribe()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
        assertEquals(2, capturePresenceEvent.size)
        assertEquals("join", capturePresenceEvent.first().event)
        assertEquals("join", capturePresenceEvent[1].event)
        val subscribedChannels = capturePresenceEvent.map { pnPresenceEvent -> pnPresenceEvent.channel }.toSet()
        assertTrue(subscribedChannels.contains(expectedChannel01))
        assertTrue(subscribedChannels.contains(expectedChannel02))
    }

    @Test
    fun testLeaveChannel() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)
        guest.subscribeToBlocking(expectedChannel)

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {}

                override fun presence(
                    pubnub: PubNub,
                    event: PNPresenceEventResult,
                ) {
                    if (event.event == "leave") {
                        assertEquals(expectedChannel, event.channel)
                        success.set(true)
                    }
                }
            },
        )

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

        clientConfig = {
            presenceTimeout = expectedTimeout
            heartbeatInterval = 0
        }

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                    if (pnStatus.category == PNStatusCategory.PNConnectedCategory) {
                        subscribed.set(true)
                    }
                }

                override fun presence(
                    pubnub: PubNub,
                    event: PNPresenceEventResult,
                ) {
                    if (event.event == "timeout") {
                        assertEquals(expectedChannel, event.channel)
                        timeoutReceived.set(true)
                    }
                }
            },
        )

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

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {}

                override fun presence(
                    pubnub: PubNub,
                    event: PNPresenceEventResult,
                ) {
                    if (event.event == "state-change") {
                        assertEquals(pubnub.configuration.userId.value, event.uuid)
                        success.set(true)
                    }
                }
            },
        )

        pubnub.setPresenceState(
            channels = listOf(expectedChannel),
            state = generatePayload(),
        ).sync()

        success.listen()
    }
}
