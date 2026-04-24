package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribeToBlocking
import com.pubnub.api.unsubscribeFromBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class SubscribeIntegrationTests : BaseIntegrationTest() {

    lateinit var guestClient: PubNub

    override fun onBefore() {
        guestClient = createPubNub()
    }

    @Test
    fun testSubscribeToMultipleChannels() {
        val expectedChannelList = generateSequence { randomValue() }.take(3).toList()

        pubnub.subscribe(
            channels = expectedChannelList,
            withPresence = true
        )

        wait()

        assertEquals(3, pubnub.getSubscribedChannels().size)
        assertTrue(pubnub.getSubscribedChannels().contains(expectedChannelList[0]))
        assertTrue(pubnub.getSubscribedChannels().contains(expectedChannelList[1]))
        assertTrue(pubnub.getSubscribedChannels().contains(expectedChannelList[2]))
    }

    @Test
    fun testSubscribeToChannel() {
        val expectedChannel = randomChannel()

        pubnub.subscribe(
            channels = listOf(expectedChannel),
            withPresence = true
        )

        wait()

        assertEquals(1, pubnub.getSubscribedChannels().size)
        assertTrue(pubnub.getSubscribedChannels().contains(expectedChannel))
    }

    @Test
    fun testWildcardSubscribe() {
        val success = AtomicBoolean()

        val expectedMessage = randomValue()

        pubnub.subscribeToBlocking("my.*")

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                assertEquals(expectedMessage, pnMessageResult.message.asString)
                success.set(true)
            }
        })

        guestClient.publish(
            channel = "my.test",
            message = expectedMessage
        ).sync()!!

        success.listen()
    }

    @Test
    fun testUnsubscribeFromChannel() {
        val success = AtomicBoolean()

        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNUnsubscribeOperation &&
                    pnStatus.category == PNStatusCategory.PNAcknowledgmentCategory
                ) {
                    success.set(pubnub.getSubscribedChannels().none { it == expectedChannel })
                }
            }
        })

        pubnub.unsubscribeFromBlocking(expectedChannel)

        success.listen()
    }

    @Test
    fun testUnsubscribeFromAllChannels() {
        val success = AtomicBoolean()
        val randomChannel = randomChannel()

        pubnub.subscribeToBlocking(randomChannel)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.category == PNStatusCategory.PNAcknowledgmentCategory &&
                    pnStatus.affectedChannels.contains(randomChannel) &&
                    pnStatus.operation == PNOperationType.PNUnsubscribeOperation
                ) {
                    success.set(pubnub.getSubscribedChannels().isEmpty())
                }
            }
        })

        pubnub.unsubscribeAll()

        success.listen()
    }

    @Test
    fun `when eventEngine enabled then subscribe REST call contains "ee" query parameter`() {
        // given
        val success = AtomicBoolean()
        val config = getBasicPnConfiguration()
        config.enableEventEngine = true
        config.heartbeatInterval = 1
        var interceptedUrl: HttpUrl? = null
        config.httpLoggingInterceptor = HttpLoggingInterceptor {
            if (it.startsWith("--> GET https://")) {
                interceptedUrl = it.substringAfter("--> GET ").toHttpUrlOrNull()
                success.set(true)
            }
        }.apply { level = HttpLoggingInterceptor.Level.BASIC }

        val pubnub = PubNub(config)

        // when
        try {
            pubnub.subscribe(
                channels = listOf("a")
            )

            success.listen()
        } finally {
            pubnub.forceDestroy()
        }

        // then
        assertNotNull(interceptedUrl)
        assertTrue(interceptedUrl!!.queryParameterNames.contains("ee"))
    }

    @Test
    fun `when eventEngine disabled then subscribe REST call doesn't contain "ee" query parameter`() {
        // given
        val success = AtomicBoolean()
        val config = getBasicPnConfiguration()
        config.enableEventEngine = false
        var interceptedUrl: HttpUrl? = null
        config.httpLoggingInterceptor = HttpLoggingInterceptor {
            if (it.startsWith("--> GET https://")) {
                interceptedUrl = it.substringAfter("--> GET ").toHttpUrlOrNull()
                success.set(true)
            }
        }.apply { level = HttpLoggingInterceptor.Level.BASIC }

        val pubnub = PubNub(config)

        // when
        try {
            pubnub.subscribe(
                channels = listOf("a")
            )

            success.listen()
        } finally {
            pubnub.forceDestroy()
        }

        // then
        assertNotNull(interceptedUrl)
        assertFalse(interceptedUrl!!.queryParameterNames.contains("ee"))
    }
}
