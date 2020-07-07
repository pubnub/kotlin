package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.randomChannel
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

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

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                assertEquals("join", pnPresenceEventResult.event)
                assertEquals(expectedChannel, pnPresenceEventResult.channel)
                success.set(true)
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        success.listen()
    }

    @Test
    fun testLeaveChannel() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)
        guest.subscribeToBlocking(expectedChannel)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                assertEquals(expectedChannel, pnPresenceEventResult.channel)
                assertEquals("leave", pnPresenceEventResult.event)
                success.set(true)
            }
        })

        guest.unsubscribeFromBlocking(expectedChannel)

        success.listen()
    }

    @Test
    fun testTimeoutFromChannel() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.configuration.presenceTimeout = 20
        pubnub.configuration.heartbeatInterval = 0

        pubnub.addListener(object : SubscribeCallback() {

            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "timeout") {
                    assertEquals(expectedChannel, pnPresenceEventResult.channel)
                    success.set(true)
                }
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        Awaitility.await()
            .atMost(21, TimeUnit.SECONDS)
            .untilTrue(success)
    }

    @Test
    fun testStateChangeEvent() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.addListener(object : SubscribeCallback() {

            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                assertEquals("state-change", pnPresenceEventResult.event)
                assertEquals(pubnub.configuration.uuid, pnPresenceEventResult.uuid)
                success.set(true)
            }
        })

        pubnub.setPresenceState().apply {
            channels = listOf(expectedChannel)
            state = generatePayload()
        }.sync()!!

        success.listen()
    }
}
