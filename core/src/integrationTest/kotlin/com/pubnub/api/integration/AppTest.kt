package com.pubnub.api.integration

import com.pubnub.api.Keys
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.PubNub
import com.pubnub.internal.callbacks.SubscribeCallback
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class AppTest {

    lateinit var pubnub: PubNub

    @Before
    fun initPubnub() {
        pubnub = PubNub(
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                subscribeKey = Keys.subKey
                publishKey = Keys.pubKey
                logVerbosity = PNLogVerbosity.BODY
            }
        )
    }

    @After
    fun cleanUp() {
        pubnub.forceDestroy()
    }

    @Test
    fun testPublishSync() {
        pubnub.publish(
            channel = UUID.randomUUID().toString(),
            message = UUID.randomUUID().toString()
        ).sync().let {
            assertNotNull(it)
        }
    }

    @Test
    fun testPublishAsync() {
        val success = AtomicBoolean()

        pubnub.publish(
            channel = UUID.randomUUID().toString(),
            message = UUID.randomUUID().toString()
        ).async { result, status ->
            assertFalse(status.error)
            result!!.timetoken
            success.set(true)
        }

        success.listen()

        success.set(false)

        Thread.sleep(2000)

        pubnub.publish(
            channel = UUID.randomUUID().toString(),
            message = UUID.randomUUID().toString()
        ).async { result, status ->
            assertFalse(status.error)
            result!!.timetoken
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testSubscribe() {
        val success = AtomicBoolean()
        val expectedChannel = UUID.randomUUID().toString()

        pubnub.addListener(object : SubscribeCallback<PubNub>() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                assertTrue(pnStatus.operation == PNOperationType.PNSubscribeOperation)
                assertTrue(pnStatus.category == PNStatusCategory.PNConnectedCategory)
                assertTrue(pnStatus.affectedChannels.contains(expectedChannel))
                success.set(true)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}
            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {}
            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}
        })

        pubnub.subscribe(
            channels = listOf(expectedChannel),
            withPresence = true
        )

        success.listen()
    }

    @Test
    fun testHereNow() {
        val expectedChannels = listOf(UUID.randomUUID().toString())

        pubnub.subscribe(
            channels = expectedChannels,
            withPresence = true
        )

        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollDelay(Durations.ONE_SECOND)
            .pollInterval(Durations.ONE_SECOND)
            .with()
            .until {
                pubnub.whereNow(
                    uuid = pubnub.configuration.userId.value
                ).sync()!!
                    .channels
                    .containsAll(expectedChannels)
            }

        pubnub.hereNow(
            channels = expectedChannels,
            includeUUIDs = false,
            includeState = false
        ).sync()
    }
}
