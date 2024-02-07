package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribeToBlocking
import com.pubnub.api.unsubscribeFromBlocking
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

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

        assertEquals(listOf(expectedChannel), pubnub.getSubscribedChannels())
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
    fun testSubscriptionSet() {
        val success = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        sub01.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("01: $result")
                success.incrementAndGet()
            }
        })

        sub02.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("02: $result")
                success.incrementAndGet()
            }
        })

        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            setOf(sub02, sub01)
        )

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("set: $result")
                success.incrementAndGet()
            }
        })

        subscriptionSetOf.subscribe()
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        Thread.sleep(5000)

        assertEquals(6, success.get())
    }

    @Test
    fun testSubscriptionSetStartWithOlderTimetoken() {
        val success = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        sub01.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("01: $result")
                success.incrementAndGet()
            }
        })

        sub02.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("02: $result")
                success.incrementAndGet()
            }
        })

        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            setOf(sub02, sub01)
        )

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("set: $result")
                success.incrementAndGet()
            }
        })

        subscriptionSetOf.subscribe(SubscriptionCursor((System.currentTimeMillis() - 10_000) * 10_000))
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        Thread.sleep(5000)

        assertEquals(12, success.get())
    }

    @Test
    fun testSubscriptionSetResubscribeWithOlderTimetoken() {
        val success = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        pubnub.publish(chan01.name, expectedMessage + "01").sync()
        pubnub.publish(chan02.name, expectedMessage + "02").sync()
        pubnub.publish(chan01.name, expectedMessage + "03").sync()

        sub01.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=01: ${result.message}")
                success.incrementAndGet()
            }
        })

        sub02.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=02: ${result.message}")
                success.incrementAndGet()
            }
        })

        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            setOf(sub02, sub01)
        )

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=set: ${result.message}")
                success.incrementAndGet()
            }
        })

        subscriptionSetOf.subscribe()
        Thread.sleep(3000)

        pubnub.publish(chan01.name, expectedMessage + "04").sync()
        pubnub.publish(chan02.name, expectedMessage + "05").sync()
        pubnub.publish(chan01.name, expectedMessage + "06").sync()

        Thread.sleep(2000)

        subscriptionSetOf.subscribe(SubscriptionCursor((System.currentTimeMillis() - 30_000) * 10_000))
        Thread.sleep(3000)

        assertEquals(18, success.get())
    }

    @Test
    fun `testSubscriptionSet resubscribe unrelated Subscription with older timetoken`() {
        val success = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        val unrelatedSubscription = chan02.subscription()

        pubnub.publish(chan01.name, expectedMessage + "01").sync()
        pubnub.publish(chan02.name, expectedMessage + "02").sync()
        pubnub.publish(chan01.name, expectedMessage + "03").sync()

        sub01.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=01: ${result.message}")
                success.incrementAndGet()
            }
        })

        sub02.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=02: ${result.message}")
                success.incrementAndGet()
            }
        })

        unrelatedSubscription.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=03: ${result.message}")
            }
        })

        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            setOf(sub02, sub01)
        )

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=set: ${result.message}")
                success.incrementAndGet()
            }
        })

        subscriptionSetOf.subscribe()
        Thread.sleep(3000)

        pubnub.publish(chan01.name, expectedMessage + "04").sync()
        pubnub.publish(chan02.name, expectedMessage + "05").sync()
        pubnub.publish(chan01.name, expectedMessage + "06").sync()

        Thread.sleep(2000)

        unrelatedSubscription.subscribe(SubscriptionCursor((System.currentTimeMillis() - 30_000) * 10_000))
        Thread.sleep(3000)

        assertEquals(6, success.get())
    }

    @Test
    fun `testSubscriptionSet resubscribe one of the subscriptions with older timetoken`() {
        val success = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        pubnub.publish(chan01.name, expectedMessage + "01").sync()
        pubnub.publish(chan02.name, expectedMessage + "02").sync()
        pubnub.publish(chan01.name, expectedMessage + "03").sync()

        sub01.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=01: ${result.message}")
                success.incrementAndGet()
            }
        })

        sub02.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=02: ${result.message}")
                success.incrementAndGet()
            }
        })

        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            setOf(sub02, sub01)
        )

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                println("-=set: ${result.message}")
                success.incrementAndGet()
            }
        })

        subscriptionSetOf.subscribe()
        Thread.sleep(3000)

        pubnub.publish(chan01.name, expectedMessage + "04").sync()
        pubnub.publish(chan02.name, expectedMessage + "05").sync()
        pubnub.publish(chan01.name, expectedMessage + "06").sync()

        Thread.sleep(2000)

        sub02.subscribe(SubscriptionCursor((System.currentTimeMillis() - 30_000) * 10_000))
        Thread.sleep(3000)

        assertEquals(10, success.get())
    }
}
