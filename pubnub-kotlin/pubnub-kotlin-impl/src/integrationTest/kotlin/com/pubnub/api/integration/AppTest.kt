package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.test.Keys
import com.pubnub.test.listen
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class AppTest {
    lateinit var pubnub: PubNub

    @Before
    fun initPubnub() {
        pubnub =
            PubNub.create(
                PNConfiguration.builder(userId = UserId(PubNub.generateUUID()), subscribeKey = Keys.subKey) {
                    publishKey = Keys.pubKey
                }.build(),
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
            message = UUID.randomUUID().toString(),
        ).sync().let {
            assertNotNull(it)
        }
    }

    @Test
    fun testPublishAsync() {
        val success = AtomicBoolean()

        pubnub.publish(
            channel = UUID.randomUUID().toString(),
            message = UUID.randomUUID().toString(),
        ).async { result ->
            result.onSuccess {
                success.set(true)
            }
        }

        success.listen()

        success.set(false)

        Thread.sleep(2000)

        pubnub.publish(
            channel = UUID.randomUUID().toString(),
            message = UUID.randomUUID().toString(),
        ).async { result ->
            result.onSuccess {
                success.set(true)
            }
        }

        success.listen()
    }

    @Test
    fun testSubscribe() {
        val expectedChannel = UUID.randomUUID().toString()
        pubnub.test {
            subscribe(expectedChannel, withPresence = true)
        }
    }

    @Test
    fun testHereNow() {
        val expectedChannels = listOf(UUID.randomUUID().toString())

        pubnub.subscribe(
            channels = expectedChannels,
            withPresence = true,
        )

        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollDelay(Durations.ONE_SECOND)
            .pollInterval(Durations.ONE_SECOND)
            .with()
            .until {
                pubnub.whereNow(
                    uuid = pubnub.configuration.userId.value,
                ).sync()
                    .channels
                    .containsAll(expectedChannels)
            }

        pubnub.hereNow(
            channels = expectedChannels,
            includeUUIDs = false,
            includeState = false,
        ).sync()
    }
}
