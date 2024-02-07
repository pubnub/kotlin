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
import com.pubnub.api.retry.RetryConfiguration
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
import org.junit.jupiter.api.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
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
    fun testSubscribeWithFilterExpression() {
        val success = AtomicBoolean()

        val expectedMessage = randomValue()

        val metaParameter = "color"
        pubnub.configuration.filterExpression = "$metaParameter LIKE 'blue*'"
        pubnub.subscribeToBlocking("my.*")

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                assertEquals(expectedMessage, pnMessageResult.message.asString)
                success.set(true)
            }
        })
        val meta: MutableMap<String, Any> = HashMap<String, Any>().apply { put("$metaParameter", "blue12367") }
        guestClient.publish(
            channel = "my.test",
            message = expectedMessage,
            meta = meta
        ).sync()!!

        success.listen()
    }

    @Test
    fun testTwoAndMoreConsecutiveSubscribeCallFirstWithTimeTokenShouldReceiveMessages() {
        val countDownLatch = CountDownLatch(3)

        // make two pubnub instances
        val pubnub1 = PubNub(getBasicPnConfiguration())
        val pubnub2 = PubNub(getBasicPnConfiguration())

        // create a channel
        val channel01 = randomChannel()

        // create messages
        val expectedMessage01 = randomValue()
        val expectedMessage02 = randomValue()
        val expectedMessage03 = randomValue()

        // send a message from user 1
        pubnub1.publish(channel01, expectedMessage01).sync()

        // send a message from user 2
        pubnub2.publish(channel01, expectedMessage02).sync()

        // get timestamp for 10 minutes ago
        val timeToken10minAgo = (System.currentTimeMillis() - 600_000L) * 10_000L

        pubnub1.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                // nothing here
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println("-=message ${pnMessageResult.message}") // <-- here I am able to get msgFromUser1ToChannel01 and msgFromUser2ToChannel01
                assertTrue(listOf(expectedMessage01, expectedMessage02, expectedMessage03).contains(pnMessageResult.message.asString))
                countDownLatch.countDown()
            }
        })

        pubnub1.subscribe(channels = listOf(channel01), withTimetoken = timeToken10minAgo)
        pubnub1.publish(channel01, expectedMessage03).sync()
        // we are subscribing for the second time immediately after first subscribe
        pubnub1.subscribe(channels = listOf("ch03"))
        pubnub1.subscribe(channels = listOf("ch04"))
        pubnub1.subscribe(channels = listOf("ch05"))
        pubnub1.subscribe(channels = listOf("ch06"))
        pubnub1.subscribe(channels = listOf("ch07"))

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
    }

    @Test
    fun testUnsubscribeFromChannel() {
        val success = AtomicBoolean()

        val expectedChannel = randomChannel()

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                // because we have one channel subscribed unsubscribing from it will cause UnsubscribeAll
                if (pnStatus.affectedChannels.contains(expectedChannel) && pnStatus.operation == PNOperationType.PNUnsubscribeOperation) {
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
                if (pnStatus.affectedChannels.contains(randomChannel) &&
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

    @Test
    fun `when retryConfiguration is defined should get proper status`() {
        val success = AtomicBoolean()

        guestClient = createPubNub(
            getBasicPnConfiguration().apply {
                val notExistingUri = "ps.pndsn_notExisting_URI.com" // we want to trigger UnknownHostException to initiate retry
                origin = notExistingUri
                retryConfiguration = RetryConfiguration.Linear(delayInSec = 1, maxRetryNumber = 2)
                enableEventEngine = true
                heartbeatInterval = 1
            }
        )

        guestClient.subscribeToBlocking("my.*")

        guestClient.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                assertEquals(PNStatusCategory.PNConnectionError, pnStatus.category)
                success.set(true)
            }
        })

        success.listen(10)
    }

    @org.junit.jupiter.api.Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun `second consecutive subscribe works with new test helper`() = pubnub.test {
        subscribe(listOf("abc"))
        val tt = pubnub.publish("abc", "myMessage").sync()!!.timetoken
        assertEquals(tt, nextMessage().timetoken!!)
        subscribe(listOf("def"))
        val tt2 = pubnub.publish("def", "myMessage").sync()!!.timetoken
        assertEquals(tt2, nextMessage().timetoken!!)
    }
}
