package com.pubnub.api.integration

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.logging.CustomLogger
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.test.CommonUtils.DEFAULT_LISTEN_DURATION
import com.pubnub.test.CommonUtils.randomChannel
import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.awaitZero
import com.pubnub.test.listen
import com.pubnub.test.subscribeToBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class SubscribeIntegrationTests : BaseIntegrationTest() {
    lateinit var guestClient: PubNub

    override fun onBefore() {
        guestClient = createPubNub {}
    }

    @Test
    fun testSubscribeToMultipleChannels() {
        val expectedChannelList = generateSequence { randomValue() }.take(3).toList()

        pubnub.subscribe(
            channels = expectedChannelList,
            withPresence = true,
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
            withPresence = true,
        )

        wait()

        assertEquals(listOf(expectedChannel), pubnub.getSubscribedChannels())
    }

    @Test
    fun testWildcardSubscribe() {
        val success = AtomicBoolean()

        val expectedMessage = randomValue()

        pubnub.subscribeToBlocking("my.*")

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                }

                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    assertEquals(expectedMessage, event.message.asString)
                    success.set(true)
                }
            },
        )

        guestClient.publish(
            channel = "my.test",
            message = expectedMessage,
        ).sync()

        success.listen()
    }

    @Test
    fun testSubscribeWithFilterExpression() {
        val success = AtomicBoolean()

        val expectedMessage = randomValue()

        val metaParameter = "color"
        clientConfig = { filterExpression = "$metaParameter LIKE 'blue*'" }
        pubnub.subscribeToBlocking("my.*")

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                }

                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    assertEquals(expectedMessage, event.message.asString)
                    success.set(true)
                }
            },
        )
        val meta: MutableMap<String, Any> = HashMap<String, Any>().apply { put("$metaParameter", "blue12367") }
        guestClient.publish(
            channel = "my.test",
            message = expectedMessage,
            meta = meta,
        ).sync()

        success.listen()
    }

    @Test
    fun testSubscribeWithFilterExpressionOnMessageElements() {
        val filterExpression = "data.name == \"Kate\" || data.name == 'Joe' || data.age == 20"

        val messageCountdown = AtomicInteger(3)
        val configWithFilterExpression = getBasicPnConfiguration().apply {
            this.filterExpression = filterExpression
        }.build()

        val pubnub = PubNub.create(configWithFilterExpression)
        registerGuestClient(pubnub)

        val expectedChannelName = randomChannel()
        val channel = pubnub.channel(expectedChannelName)
        val subscription = channel.subscription()

        subscription.onMessage = { message: PNMessageResult ->
            messageCountdown.decrementAndGet()
        }

        subscription.subscribe()
        Thread.sleep(150)

        val expectedPayload01 =
            JsonObject().apply {
                addProperty("name", "Joe")
                addProperty("age", 48)
                add(
                    "colors",
                    JsonArray().apply {
                        add("red")
                        add("blue")
                        add("green")
                    }
                )
            }
        val expectedPayload02 =
            JsonObject().apply {
                addProperty("name", "Kate")
                addProperty("age", 30)
            }
        val expectedPayload03 =
            JsonObject().apply {
                addProperty("name", "Tom")
                addProperty("age", 20)
            }

        channel.publish(message = expectedPayload01).sync()
        channel.publish(message = expectedPayload02).sync()
        channel.publish(message = expectedPayload03).sync()

        messageCountdown.awaitZero()
    }

    @Test
    fun testSubscribeWithFilterExpressionOnMetadata() {
        val filterExpression = "meta.category == 'urgent'"

        val receivesMessageCount = AtomicInteger(0)
        val configWithFilterExpression = getBasicPnConfiguration().apply {
            this.filterExpression = filterExpression
        }.build()

        val pubnub = PubNub.create(configWithFilterExpression)
        registerGuestClient(pubnub)

        val expectedPayload = JsonObject().apply {
            addProperty("content", "This is an urgent message")
        }

        val matchingMeta = JsonObject().apply {
            addProperty("category", "urgent")
        }

        val nonMatchingMeta = JsonObject().apply {
            addProperty("category", "normal")
        }

        val expectedChannelName = randomChannel()
        val channel = pubnub.channel(expectedChannelName)
        val subscription = channel.subscription()

        subscription.onMessage = { message: PNMessageResult ->
            receivesMessageCount.incrementAndGet()
        }

        subscription.subscribe()
        Thread.sleep(150)

        // Publish message with non-matching metadata - should be filtered out
        channel.publish(message = expectedPayload, meta = nonMatchingMeta).sync()

        // Publish message with matching metadata - should be received
        channel.publish(message = expectedPayload, meta = matchingMeta).sync()

        Thread.sleep(2000)

        assertEquals(1, receivesMessageCount.get())
    }

    @Test
    fun testSubscribeWithFilterExpressionOnMetadataWithArraysAndObject() {
        val filterExpression = "data.colors[0] == 'blue' || data.config['enabled'] == \"true\""

        val receivesMessageCount = AtomicInteger(0)
        val configWithFilterExpression = getBasicPnConfiguration().apply {
            this.filterExpression = filterExpression
        }.build()

        val pubnub = PubNub.create(configWithFilterExpression)
        registerGuestClient(pubnub)

        val expectedChannelName = randomChannel()
        val channel = pubnub.channel(expectedChannelName)
        val subscription = channel.subscription()

        subscription.onMessage = { message: PNMessageResult ->
            receivesMessageCount.incrementAndGet()
        }

        subscription.subscribe()
        Thread.sleep(150)

        val expectedPayload01 =
            JsonObject().apply {
                addProperty("name", "Joe")
                addProperty("age", 48)
                add(
                    "colors",
                    JsonArray().apply {
                        add("blue")
                        add("red")
                        add("green")
                    }
                )
            }
        val expectedPayload02 =
            JsonObject().apply {
                addProperty("name", "Kate")
                addProperty("age", 30)
                add(
                    "config",
                    JsonObject().apply {
                        addProperty("enabled", "true")
                        addProperty("version", 2)
                    }
                )
            }
        val expectedPayload03 =
            JsonObject().apply {
                addProperty("name", "Tom")
                addProperty("age", 20)
            }

        channel.publish(message = expectedPayload01).sync()
        channel.publish(message = expectedPayload02).sync()
        channel.publish(message = expectedPayload03).sync()

        Thread.sleep(2000)

        assertEquals(2, receivesMessageCount.get())
    }

    @Test
    fun testSubscribeWithFilterExpressionCustomMessageTypeWorkaround() {
        val customMessageTypeValue = "alert"
        val filterExpression = "data.customMessageType == '$customMessageTypeValue'"

        val receivesMessageCount = AtomicInteger(0)
        val configWithFilterExpression = getBasicPnConfiguration().apply {
            this.filterExpression = filterExpression
        }.build()

        val pubnub = PubNub.create(configWithFilterExpression)
        registerGuestClient(pubnub)

        val expectedChannelName = randomChannel()
        val channel = pubnub.channel(expectedChannelName)
        val subscription = channel.subscription()

        subscription.onMessage = { message: PNMessageResult ->
            println("-=- received message: ${message.message}")
            receivesMessageCount.incrementAndGet()
        }

        subscription.subscribe()
        Thread.sleep(150)

        val expectedPayload01 = HashMap<String, String>()
        expectedPayload01["text"] = "System Alert"

        expectedPayload01["customMessageType"] = customMessageTypeValue

        val expectedPayload02 =
            JsonObject().apply {
                addProperty("name", "Kate")
                addProperty("age", 30)
                add(
                    "config",
                    JsonObject().apply {
                        addProperty("enabled", "true")
                        addProperty("version", 2)
                    }
                )
            }
        val expectedPayload03 =
            JsonObject().apply {
                addProperty("name", "Tom")
                addProperty("age", 20)
            }

        channel.publish(message = expectedPayload01, customMessageType = customMessageTypeValue).sync()
        channel.publish(message = expectedPayload02, customMessageType = "info").sync()
        channel.publish(message = expectedPayload03, customMessageType = "info").sync()

        Thread.sleep(2000)

        assertEquals(1, receivesMessageCount.get())
    }

    @Test
    fun testTwoAndMoreConsecutiveSubscribeCallFirstWithTimeTokenShouldReceiveMessages() {
        val countDownLatch = CountDownLatch(3)

        // make two pubnub instances
        val pubnub1 = createPubNub {}
        val pubnub2 = createPubNub {}

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

        pubnub1.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                    // nothing here
                }

                override fun message(
                    pubnub: PubNub,
                    event: PNMessageResult,
                ) {
                    assertTrue(
                        listOf(
                            expectedMessage01,
                            expectedMessage02,
                            expectedMessage03,
                        ).contains(event.message.asString),
                    )
                    countDownLatch.countDown()
                }
            },
        )

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
        val expectedChannel = randomChannel()

        pubnub.test {
            subscribe(expectedChannel)
            unsubscribe(listOf(expectedChannel))
        }
    }

    @Test
    fun testUnsubscribeFromAllChannels() {
        val randomChannel = randomChannel()

        pubnub.test {
            subscribe(randomChannel)
            unsubscribeAll()
        }
    }

    @Test
    fun `when eventEngine enabled then subscribe REST call contains "ee" query parameter`() {
        // given
        val success = AtomicBoolean()
        var interceptedUrl: HttpUrl? = null
        clientConfig = {
            heartbeatInterval = 1
            httpLoggingInterceptor =
                HttpLoggingInterceptor {
                    if (it.startsWith("--> GET https://")) {
                        interceptedUrl = it.substringAfter("--> GET ").toHttpUrlOrNull()
                        success.set(true)
                    }
                }.apply { level = HttpLoggingInterceptor.Level.BASIC }
        }
        // when
        try {
            pubnub.subscribe(
                channels = listOf("a"),
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
    fun `when retryConfiguration is defined should get proper status`() {
        val success = AtomicBoolean()

        guestClient =
            createPubNub {
                val notExistingUri =
                    "ps.pndsn_notExisting_URI.com" // we want to trigger UnknownHostException to initiate retry
                origin = notExistingUri
                retryConfiguration = RetryConfiguration.Linear(delayInSec = 1, maxRetryNumber = 2)
                heartbeatInterval = 1
            }

        guestClient.subscribeToBlocking("my.*")

        guestClient.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {
                    assertTrue(pnStatus.category == PNStatusCategory.PNConnectionError)
                    success.set(true)
                }
            },
        )

        success.listen(10)
    }

    @Test
    fun testSubscriptionSet() {
        val countDown = CountDownLatch(6)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01: Subscription = chan01.subscription()
        val sub02 = chan02.subscription()

        sub01.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    countDown.countDown()
                }
            },
        )

        sub02.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    countDown.countDown()
                }
            },
        )

        val subscriptionSetOf =
            pubnub.subscriptionSetOf(
                setOf(sub02, sub01),
            )

        subscriptionSetOf.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    countDown.countDown()
                }
            },
        )

        subscriptionSetOf.subscribe()
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        assertTrue(countDown.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @Test
    fun testSubscriptionSetStartWithOlderTimetoken() {
        val success = CountDownLatch(12)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        sub01.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        sub02.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        val subscriptionSetOf =
            pubnub.subscriptionSetOf(
                setOf(sub02, sub01),
            )

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        subscriptionSetOf.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        subscriptionSetOf.subscribe(SubscriptionCursor((System.currentTimeMillis() - 10_000) * 10_000))
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        assertTrue(success.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @Test
    fun testSubscriptionSetResubscribeWithOlderTimetoken() {
        val success = CountDownLatch(18)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        pubnub.publish(chan01.name, expectedMessage + "01").sync()
        pubnub.publish(chan02.name, expectedMessage + "02").sync()
        pubnub.publish(chan01.name, expectedMessage + "03").sync()

        sub01.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        sub02.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        val subscriptionSetOf =
            pubnub.subscriptionSetOf(
                setOf(sub02, sub01),
            )

        subscriptionSetOf.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        subscriptionSetOf.subscribe()
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage + "04").sync()
        pubnub.publish(chan02.name, expectedMessage + "05").sync()
        pubnub.publish(chan01.name, expectedMessage + "06").sync()

        Thread.sleep(2000)

        subscriptionSetOf.subscribe(SubscriptionCursor((System.currentTimeMillis() - 30_000) * 10_000))

        assertTrue(success.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @Test
    fun `testSubscriptionSet resubscribe unrelated Subscription with older timetoken`() {
        val success = CountDownLatch(6)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        val unrelatedSubscription = chan02.subscription()

        pubnub.publish(chan01.name, expectedMessage + "01").sync()
        pubnub.publish(chan02.name, expectedMessage + "02").sync()
        pubnub.publish(chan01.name, expectedMessage + "03").sync()

        sub01.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        sub02.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        val subscriptionSetOf =
            pubnub.subscriptionSetOf(
                setOf(sub02, sub01),
            )

        subscriptionSetOf.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        subscriptionSetOf.subscribe()
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage + "04").sync()
        pubnub.publish(chan02.name, expectedMessage + "05").sync()
        pubnub.publish(chan01.name, expectedMessage + "06").sync()

        Thread.sleep(2000)

        unrelatedSubscription.subscribe(SubscriptionCursor((System.currentTimeMillis() - 30_000) * 10_000))

        assertTrue(success.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @Test
    fun `testSubscriptionSet resubscribe one of the subscriptions with older timetoken`() {
        val success = CountDownLatch(10)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val chan02 = pubnub.channel(randomChannel() + "02")
        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()

        pubnub.publish(chan01.name, expectedMessage + "01").sync()
        pubnub.publish(chan02.name, expectedMessage + "02").sync()
        pubnub.publish(chan01.name, expectedMessage + "03").sync()

        sub01.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        sub02.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        val subscriptionSet =
            pubnub.subscriptionSetOf(
                setOf(sub02, sub01),
            )

        subscriptionSet.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.countDown()
                }
            },
        )

        subscriptionSet.subscribe()
        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage + "04").sync()
        pubnub.publish(chan02.name, expectedMessage + "05").sync()
        pubnub.publish(chan01.name, expectedMessage + "06").sync()

        Thread.sleep(2000)

        sub02.subscribe(SubscriptionCursor((System.currentTimeMillis() - 30_000) * 10_000))

        assertTrue(success.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @Test
    fun testSubscriptionOnChannelMetadata() {
        val success = AtomicBoolean()
        val channelMetaDataName = randomChannel()
        val channelMetaSubscription = pubnub.channelMetadata(channelMetaDataName).subscription()
        channelMetaSubscription.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.set(true)
                }
            },
        )
        channelMetaSubscription.subscribe()
        Thread.sleep(100)
        guestClient.publish(channelMetaDataName, randomValue()).sync()

        success.listen()
    }

    @Test
    fun testSubscriptionOnUserMetadata() {
        val success = AtomicBoolean()
        val userMetaDataName = randomChannel()
        val channelMetaSubscription = pubnub.userMetadata(userMetaDataName).subscription()
        channelMetaSubscription.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    success.set(true)
                }
            },
        )
        channelMetaSubscription.subscribe()
        Thread.sleep(200)
        guestClient.publish(userMetaDataName, randomValue()).sync()

        success.listen()
    }

    @Test
    fun testSubscriptionWithClose() {
        val countDown = CountDownLatch(2)
        val unsubscribed = CountDownLatch(1)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel() + "01")
        val sub01 = chan01.subscription()

        pubnub.addListener(
            object : StatusListener {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {
                    if (status.category == PNStatusCategory.PNDisconnectedCategory ||
                        status.category == PNStatusCategory.PNSubscriptionChanged
                    ) {
                        unsubscribed.countDown()
                    }
                }
            },
        )

        sub01.use { sub ->
            sub.addListener(
                object : EventListener {
                    override fun message(
                        pubnub: PubNub,
                        result: PNMessageResult,
                    ) {
                        countDown.countDown()
                    }
                },
            )
            sub.subscribe()
            Thread.sleep(2000)
            pubnub.publish(chan01.name, expectedMessage).sync()
            pubnub.publish(chan01.name, expectedMessage).sync()
            assertTrue(countDown.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
        }

        assertTrue(unsubscribed.await(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS))
    }

    @org.junit.jupiter.api.Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun `second consecutive subscribe works with new test helper`() =
        pubnub.test {
            subscribe(listOf("abc"))
            val tt = pubnub.publish("abc", "myMessage").sync().timetoken
            assertEquals(tt, nextMessage().timetoken!!)
            subscribe(listOf("def"))
            val tt2 = pubnub.publish("def", "myMessage").sync().timetoken
            assertEquals(tt2, nextMessage().timetoken!!)
        }

    @Test
    fun testAssigningEventBehaviourToSubscription() {
        val successMessage = AtomicInteger(0)
        val successSignal = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel())

        val subscription = chan01.subscription()

        val onMessage: (PNMessageResult) -> Unit = { successMessage.incrementAndGet() }
        val onSignal: (PNSignalResult) -> Unit = { successSignal.incrementAndGet() }

        subscription.onMessage = onMessage
        subscription.onSignal = onSignal

        subscription.subscribe()

        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.signal(chan01.name, expectedMessage).sync()

        Thread.sleep(1000)
        assertEquals(1, successMessage.get())
        assertEquals(1, successSignal.get())

        subscription.onMessage = null
        subscription.onSignal = null
        pubnub.signal(chan01.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()

        Thread.sleep(1000)
        // successMessage/successSignal remains 1
        assertEquals(1, successMessage.get())
        assertEquals(1, successSignal.get())
    }

    @Test
    fun testSubscribeToChannelGroup() {
        val success = AtomicBoolean()
        val expectedMessage = randomValue()

        // add channels to channelGroup
        val channelGroup = "myChannelGroup"
        val channel01 = "Channel01"
        val pnChannelGroupsAddChannelResult: PNChannelGroupsAddChannelResult =
            pubnub.addChannelsToChannelGroup(
                channels = listOf(channel01, "Channel02"),
                channelGroup = channelGroup,
            ).sync()

        val channelGroupSubscription = pubnub.channelGroup(channelGroup).subscription()
        channelGroupSubscription.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    assertEquals(expectedMessage, result.message.asString)
                    success.set(true)
                }
            },
        )

        channelGroupSubscription.subscribe()
        Thread.sleep(2000)
        pubnub.publish(channel01, expectedMessage).sync()

        success.listen()

        pubnub.deleteChannelGroup(channelGroup).sync()
    }

    @Test
    fun testSubscribeToChannelMetadata() {
        val success = AtomicBoolean()
        val expectedMessage = randomValue()

        // add channels to channelGroup
        val channelMetadataName = randomChannel()
        val channelMetadataSubscription = pubnub.channelMetadata(channelMetadataName).subscription()
        channelMetadataSubscription.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    assertEquals(expectedMessage, result.message.asString)
                    success.set(true)
                }
            },
        )

        channelMetadataSubscription.subscribe()
        Thread.sleep(2000)
        pubnub.publish(channelMetadataName, expectedMessage).sync()

        success.listen()
    }

    @Test
    fun testSubscribeToUserMetadata() {
        val success = AtomicBoolean()
        val expectedMessage = randomValue()

        // add channels to channelGroup
        val userMetadataName = randomChannel()
        val userMetadataSubscription = pubnub.userMetadata(userMetadataName).subscription()
        userMetadataSubscription.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    assertEquals(expectedMessage, result.message.asString)
                    success.set(true)
                }
            },
        )

        userMetadataSubscription.subscribe()
        Thread.sleep(2000)
        pubnub.publish(userMetadataName, expectedMessage).sync()

        success.listen()
    }

    @Test
    fun testSubscribeWithFilter() {
        val success = AtomicBoolean()
        val phraseToLookForInMessage = "abc"
        val expectedMessage = phraseToLookForInMessage + randomValue()
        val channelName = randomChannel()
        val channel = pubnub.channel(channelName)
        val subscription =
            channel.subscription(
                SubscriptionOptions.filter {
                    it is PNMessageResult &&
                        it.message.asString.contains(phraseToLookForInMessage) &&
                        it.publisher!!.contains("client")
                },
            )
        subscription.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    pnMessageResult: PNMessageResult,
                ) {
                    assertEquals(expectedMessage, pnMessageResult.message.asString)
                    success.set(true)
                }
            },
        )
        subscription.subscribe()
        Thread.sleep(2000)

        guestClient.publish(
            channel = channelName,
            message = expectedMessage,
        ).sync()!!

        success.listen()
    }

    @Ignore // For manual use only. It takes long time to run.
    @Test
    fun useAllEffectiveThreadsAndForEachOneSubscribeToManyChannels() {
        val threads = mutableListOf<Thread>()

        val channelsList00 = ('a'..'z').map { "0$it" }
        val channelsList01 = ('a'..'z').map { "1$it" }
        val channelsList02 = ('a'..'z').map { "2$it" }
        val channelsList03 = ('a'..'z').map { "3$it" }
        val channelsList04 = ('a'..'z').map { "4$it" }
        val channelsList05 = ('a'..'z').map { "5$it" }
        val channelsList06 = ('a'..'z').map { "6$it" }
        val channelsList07 = ('a'..'z').map { "7$it" }
        val channelsList08 = ('a'..'z').map { "8$it" }
        val channelsList09 = ('a'..'z').map { "9$it" }

        val thread00 = createThread(channelsList00)
        val thread01 = createThread(channelsList01)
        val thread02 = createThread(channelsList02)
        val thread03 = createThread(channelsList03)
        val thread04 = createThread(channelsList04)
        val thread05 = createThread(channelsList05)
        val thread06 = createThread(channelsList06)
        val thread07 = createThread(channelsList07)
        val thread08 = createThread(channelsList08)
        val thread09 = createThread(channelsList09)
        threads.add(thread00)
        threads.add(thread01)
        threads.add(thread02)
        threads.add(thread03)
        threads.add(thread04)
        threads.add(thread05)
        threads.add(thread06)
        threads.add(thread07)
        threads.add(thread08)
        threads.add(thread09)

        threads.forEach { thread -> thread.start() }

        publishToChannels(channelsList00)
        publishToChannels(channelsList01)
        publishToChannels(channelsList02)
        publishToChannels(channelsList03)
        publishToChannels(channelsList04)
        publishToChannels(channelsList05)
        publishToChannels(channelsList06)
        publishToChannels(channelsList07)
        publishToChannels(channelsList08)
        publishToChannels(channelsList09)

        threads.forEach { thread -> thread.join() }

        Thread.sleep(3000)

        publishToChannels(channelsList00)
        publishToChannels(channelsList01)
        publishToChannels(channelsList02)
        publishToChannels(channelsList03)
        publishToChannels(channelsList04)
        publishToChannels(channelsList05)
        publishToChannels(channelsList06)
        publishToChannels(channelsList07)
        publishToChannels(channelsList08)
        publishToChannels(channelsList09)

        Thread.sleep(3000)

        val allChannelsLists = listOf(
            channelsList00, channelsList01, channelsList02, channelsList03,
            channelsList04, channelsList05, channelsList06, channelsList07,
            channelsList08, channelsList09
        )

        val channelsFromAllThreads = allChannelsLists.flatten()

        assertEquals(pubnub.getSubscribedChannels().toSet(), channelsFromAllThreads.toSet())
    }

    @Test
    fun testAssigningEventBehaviourToSubscriptionSet() {
        val successMessage = AtomicInteger(0)
        val successSignal = AtomicInteger(0)
        val expectedMessage = randomValue()
        val chan01 = pubnub.channel(randomChannel())
        val chan02 = pubnub.channel(randomChannel())

        val sub01 = chan01.subscription()
        val sub02 = chan02.subscription()
        val subscriptionSetOf =
            pubnub.subscriptionSetOf(
                setOf(sub02, sub01),
            )
        subscriptionSetOf.onMessage = { successMessage.incrementAndGet() }
        subscriptionSetOf.onSignal = { successSignal.incrementAndGet() }
        subscriptionSetOf.subscribe()

        Thread.sleep(2000)

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.publish(chan02.name, expectedMessage).sync()
        pubnub.signal(chan01.name, expectedMessage).sync()
        pubnub.signal(chan02.name, expectedMessage).sync()

        Thread.sleep(1000)
        assertEquals(4, successMessage.get())
        assertEquals(2, successSignal.get())

        subscriptionSetOf.onMessage = null
        subscriptionSetOf.onSignal = null

        pubnub.publish(chan01.name, expectedMessage).sync()
        pubnub.signal(chan02.name, expectedMessage).sync()

        Thread.sleep(1000)
        assertEquals(4, successMessage.get())
        assertEquals(2, successSignal.get())
    }

    @Test
    fun testAssigningEventBehaviourToPubNub() {
        val successMessagesCount = AtomicInteger()
        val successSignalCont = AtomicInteger()
        val channel = randomChannel()
        val expectedMessage = randomValue()

        pubnub.onMessage = { successMessagesCount.incrementAndGet() }
        pubnub.onSignal = { successSignalCont.incrementAndGet() }

        pubnub.subscribeToBlocking(channel)

        pubnub.publish(channel, expectedMessage).sync()
        pubnub.signal(channel, expectedMessage).sync()

        Thread.sleep(1000)
        assertEquals(1, successMessagesCount.get())
        assertEquals(1, successSignalCont.get())

        pubnub.onMessage = null
        pubnub.onSignal = null
        pubnub.onPresence = null

        pubnub.publish(channel, expectedMessage).sync()
        pubnub.signal(channel, expectedMessage).sync()

        Thread.sleep(1000)
        assertEquals(1, successMessagesCount.get())
        assertEquals(1, successSignalCont.get())
    }

    @Test
    fun canAddAndRemoveSubscriptionFromSubscriptionSet() {
        val subscription01 = pubnub.channel(randomChannel()).subscription()
        val subscription02 = pubnub.channel(randomChannel()).subscription()
        val subscription03 = pubnub.channel(randomChannel()).subscription()

        val subscriptionSet = pubnub.subscriptionSetOf(subscriptions = setOf(subscription01, subscription02))
        assertEquals(2, subscriptionSet.subscriptions.size)

        subscriptionSet += subscription03
        assertEquals(3, subscriptionSet.subscriptions.size)

        subscriptionSet -= subscription01
        assertEquals(2, subscriptionSet.subscriptions.size)
    }

    @Test
    fun shouldDeduplicateChannelSubscriptionsWhenSubscribingToSameChannelMultipleTimes() {
        // given
        val numberOfSubscribe = 4
        // punbub.subscribe does subscribe to already subscribed channel so only two subscribe calls should occur. Handshake and actual subscribe.
        val countDownLatch = CountDownLatch(2)
        var interceptedUrl: HttpUrl? = null
        val testChannel = randomChannel()

        val customLogger = object : CustomLogger {
            override fun debug(logMessage: LogMessage) {
                if (logMessage.type == LogMessageType.NETWORK_REQUEST) {
                    val networkRequestDetails = logMessage.message as LogMessageContent.NetworkRequest
                    if (networkRequestDetails.path.contains("/v2/subscribe/")) {
                        interceptedUrl = (networkRequestDetails.origin + networkRequestDetails.path).toHttpUrlOrNull()
                        countDownLatch.countDown()
                    }
                }
            }
        }

        clientConfig = {
            customLoggers = listOf(customLogger)
        }

        try {
            repeat(numberOfSubscribe) { iteration ->
                pubnub.subscribe(channels = listOf(testChannel))
                Thread.sleep(150)
                println("Subscribe call ${iteration + 1}/$numberOfSubscribe completed")
            }

            // Wait for the subscribe request to be made
            assertTrue(countDownLatch.await(12000, TimeUnit.MILLISECONDS))

            // then: verify channel appears only once in subscribed channels
            val subscribedChannels = pubnub.getSubscribedChannels()

            assertEquals(1, subscribedChannels.size)
            assertTrue(subscribedChannels.contains(testChannel))

            // then: verify the actual HTTP request only includes the channel once
            assertNotNull("Expected to intercept subscribe URL", interceptedUrl)

            val channelsParam = interceptedUrl!!.encodedPath
                .substringAfter("/subscribe/")
                .substringAfter("/")
                .substringBefore("/")

            val channelList = channelsParam.split(",").filter { it.isNotEmpty() }

            assertEquals(1, channelList.count { it == testChannel })
        } finally {
            pubnub.forceDestroy()
        }
    }

    @Test
    fun heartbeatShouldDeduplicateChannelNameInUrlWhenSubscribingToSameChannelMultipleTimes() {
        // given
        val numberOfSubscribe = 4
        val countDownLatch = CountDownLatch(2) // we want to verify second heartbeat URL
        var interceptedUrl: HttpUrl? = null
        val testChannel = randomChannel()

        val customLogger = object : CustomLogger {
            override fun debug(logMessage: LogMessage) {
                if (logMessage.type == LogMessageType.NETWORK_REQUEST) {
                    val networkRequestDetails = logMessage.message as LogMessageContent.NetworkRequest
                    if (networkRequestDetails.path.contains("/v2/presence/") && networkRequestDetails.path.contains("/heartbeat")) {
                        interceptedUrl = (networkRequestDetails.origin + networkRequestDetails.path).toHttpUrlOrNull()
                        countDownLatch.countDown()
                    }
                }
            }
        }

        clientConfig = {
            customLoggers = listOf(customLogger)
            heartbeatInterval = 5
        }

        try {
            repeat(numberOfSubscribe) { iteration ->
                pubnub.subscribe(channels = listOf(testChannel))
                Thread.sleep(150)
                println("Subscribe call ${iteration + 1}/$numberOfSubscribe completed")
            }

            // Wait for the heartbeat request to be made
            assertTrue(countDownLatch.await(6000, TimeUnit.MILLISECONDS))

            // then: verify the actual HTTP request only includes the channel once
            assertNotNull("Expected to intercept heartbeat URL", interceptedUrl)

            // Extract channel from heartbeat URL: /v2/presence/sub-key/{sub-key}/channel/{channels}/heartbeat
            val channelsParam = interceptedUrl!!.encodedPath
                .substringAfter("/channel/")
                .substringBefore("/heartbeat")

            val channelList = channelsParam.split(",").filter { it.isNotEmpty() }

            assertEquals(1, channelList.count { it == testChannel })
        } finally {
            pubnub.forceDestroy()
        }
    }

    @Test
    fun whenSubscribingToAlreadySubscribedChannelShouldNotResubscribeButShouldEmitSubscriptionChangedStatus() {
        val channelName = randomChannel()
        val channelName02 = randomChannel()
        val expectedMessage = "test_${randomValue()}"

        val connectedLatch = CountDownLatch(1)
        val subscriptionChangedLatch = CountDownLatch(1)
        val messagesLatch = CountDownLatch(2)

        val connectedEventCount = AtomicInteger(0)
        val subscriptionChangedCount = AtomicInteger(0)
        val subscribeHttpRequestCount = AtomicInteger(0)

        // Track messages received by each listener
        val listenerAMessageCount = AtomicInteger(0)
        val listenerBMessageCount = AtomicInteger(0)

        // Custom logger to count HTTP subscribe requests
        val customLogger = object : CustomLogger {
            override fun debug(logMessage: LogMessage) {
                if (logMessage.type == LogMessageType.NETWORK_REQUEST) {
                    val networkRequestDetails = logMessage.message as LogMessageContent.NetworkRequest
                    if (networkRequestDetails.path.contains("/v2/subscribe/")) {
                        subscribeHttpRequestCount.incrementAndGet()
                    }
                }
            }
        }

        clientConfig = {
            customLoggers = listOf(customLogger)
        }

        pubnub.addListener(
            object : StatusListener {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {
                    when (status.category) {
                        PNStatusCategory.PNConnectedCategory -> {
                            connectedEventCount.incrementAndGet()
                            connectedLatch.countDown()
                        }

                        PNStatusCategory.PNSubscriptionChanged -> {
                            subscriptionChangedCount.incrementAndGet()
                            subscriptionChangedLatch.countDown()
                        }

                        else -> {}
                    }
                }
            }
        )

        // First subscription
        val subscriptionSet1 = pubnub.subscriptionSetOf(setOf(channelName, channelName02))
        subscriptionSet1.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    listenerAMessageCount.incrementAndGet()
                    messagesLatch.countDown()
                }
            }
        )

        // Second subscription (SAME channel, different listener)
        val subscriptionSet2 = pubnub.subscriptionSetOf(setOf(channelName))
        subscriptionSet2.addListener(
            object : EventListener {
                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    listenerBMessageCount.incrementAndGet()
                    messagesLatch.countDown()
                }
            }
        )

        try {
            subscriptionSet1.subscribe()
            assertTrue("Failed to receive PNConnectedCategory", connectedLatch.await(5, TimeUnit.SECONDS))

            subscriptionSet2.subscribe()
            assertTrue("Failed to receive PNSubscriptionChanged", subscriptionChangedLatch.await(5, TimeUnit.SECONDS))

            // Give a moment for any potential resubscribe to occur
            Thread.sleep(500)

            // Publish a message - both listeners should receive it
            pubnub.publish(channelName, expectedMessage).sync()
            assertTrue("Failed to receive messages on both listeners", messagesLatch.await(5, TimeUnit.SECONDS))

            // Verify both listeners received the message (this works regardless of the bug)
            assertEquals("ListenerA should receive message", 1, listenerAMessageCount.get())
            assertEquals("ListenerB should receive message", 1, listenerBMessageCount.get())

            assertEquals(
                "Should emit PNSubscriptionChanged status but not resubscribe when channels unchanged",
                1, // Status emitted but no actual resubscribe (no cancel/new receive)
                subscriptionChangedCount.get()
            )

            assertEquals(
                "Should have exactly 1 PNConnectedCategory (initial handshake only)",
                1,
                connectedEventCount.get()
            )

            // Should only have 2 HTTP "/subscribe" requests: handshake + subscribe
            assertEquals(
                "Should have exactly 2 HTTP subscribe requests (handshake + subscribe, NO resubscribe)",
                2,
                subscribeHttpRequestCount.get()
            )
        } finally {
            // Ensure cleanup happens even if assertions fail
            subscriptionSet1.close()
            subscriptionSet2.close()
        }
    }

    @Test
    fun shouldDeduplicateChannelSubscriptionsWhenSubscribingToListOfTheSameChannels() {
        // given
        val countDownLatch = CountDownLatch(2) // Only two subscribe calls should occur. Handshake and actual subscribe.
        var interceptedUrl: HttpUrl? = null
        val testChannel = randomChannel()

        val customLogger = object : CustomLogger {
            override fun debug(logMessage: LogMessage) {
                if (logMessage.type == LogMessageType.NETWORK_REQUEST) {
                    val networkRequestDetails = logMessage.message as LogMessageContent.NetworkRequest
                    if (networkRequestDetails.path.contains("/v2/subscribe/")) {
                        interceptedUrl = (networkRequestDetails.origin + networkRequestDetails.path).toHttpUrlOrNull()
                        countDownLatch.countDown()
                    }
                }
            }
        }

        clientConfig = {
            customLoggers = listOf(customLogger)
        }

        try {
            pubnub.subscribe(channels = listOf(testChannel, testChannel, testChannel))

            // Wait for the subscribe request to be made
            assertTrue(countDownLatch.await(12000, TimeUnit.MILLISECONDS))

            // then: verify channel appears only once in subscribed channels
            val subscribedChannels = pubnub.getSubscribedChannels()

            assertEquals(1, subscribedChannels.size)
            assertTrue(subscribedChannels.contains(testChannel))

            // then: verify the actual HTTP request only includes the channel once
            assertNotNull("Expected to intercept subscribe URL", interceptedUrl)

            val channelsParam = interceptedUrl!!.encodedPath
                .substringAfter("/subscribe/")
                .substringAfter("/")
                .substringBefore("/")

            val channelList = channelsParam.split(",").filter { it.isNotEmpty() }

            assertEquals(1, channelList.count { it == testChannel })
        } finally {
            pubnub.forceDestroy()
        }
    }

    @Test
    fun observerSubscribedToChannelGroupPresenceStreamReceivesJoinEventsButIsNotAnOccupant() {
        val groupName = "grp_${randomValue()}"
        val channelA = "ChannelA_${randomValue()}"
        val channelB = "ChannelB_${randomValue()}"
        pubnub.addChannelsToChannelGroup(
            channels = listOf(channelA, channelB),
            channelGroup = groupName,
        ).sync()

        val joinLatch = CountDownLatch(1)
        val joinEvents = mutableListOf<PNPresenceEventResult>()

        val observerSubscription = pubnub
            .channelGroup("$groupName-pnpres")
            .subscription()
        observerSubscription.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    joinEvents += result
                    if (result.event == "join") {
                        joinLatch.countDown()
                    }
                }
            },
        )
        observerSubscription.subscribe()
        Thread.sleep(2_000)

        val otherClient = createPubNub {}
        try {
            otherClient.subscribeToBlocking(channelA)

            assertTrue(
                "Observer subscribed to $groupName-pnpres should receive join event for ChannelA",
                joinLatch.await(5, TimeUnit.SECONDS),
            )
            assertTrue(
                joinEvents.any { it.event == "join" && it.uuid == otherClient.configuration.userId.value },
            )

            val here = pubnub.hereNow(
                channels = listOf(channelA, channelB),
                includeUUIDs = true,
            ).sync()!!
            val allUuids = here.channels.values.flatMap { channelData -> channelData.occupants.map { it.uuid } }
            val observerUuid = pubnub.configuration.userId.value
            assertFalse(
                "Observer UUID should NOT appear in hereNow occupants of $groupName's channels",
                allUuids.contains(observerUuid),
            )
        } finally {
            otherClient.forceDestroy()
            pubnub.deleteChannelGroup(groupName).sync()
        }
    }

    @Test
    fun directPresenceObserverCanUnsubscribeAndStopsReceivingEvents() {
        val channelName = randomChannel()
        val observer = pubnub.channel("$channelName-pnpres").subscription()

        val presenceEvents = mutableListOf<PNPresenceEventResult>()
        val firstJoinLatch = CountDownLatch(1)
        observer.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    presenceEvents += result
                    if (result.event == "join" && result.channel == channelName) {
                        firstJoinLatch.countDown()
                    }
                }
            },
        )
        observer.subscribe()
        Thread.sleep(1_000)

        // Prove the observer is active: a join event arrives.
        val firstClient = createPubNub {}
        try {
            firstClient.subscribeToBlocking(channelName)
            assertTrue(
                "Observer should receive a join event before unsubscribe",
                firstJoinLatch.await(5, TimeUnit.SECONDS),
            )
        } finally {
            firstClient.forceDestroy()
        }

        // Before the fix, unsubscribeInternal stripped -pnpres from both channel and group lists
        // and then called subscribe.unsubscribe with empty sets — which threw
        // CHANNEL_OR_CHANNEL_GROUP_MISSING.
        observer.unsubscribe()
        Thread.sleep(1_000)

        val eventsAfterUnsubscribe = presenceEvents.size

        // After unsubscribe, a fresh presence event on the same channel must NOT reach the listener.
        val secondClient = createPubNub {}
        try {
            secondClient.subscribeToBlocking(channelName)
            Thread.sleep(3_000)
            val secondUuid = secondClient.configuration.userId.value
            assertTrue(
                "After unsubscribe, observer must not receive further presence events for $secondUuid; " +
                    "got after-unsubscribe events: ${presenceEvents.drop(eventsAfterUnsubscribe)}",
                presenceEvents.drop(eventsAfterUnsubscribe).none { it.uuid == secondUuid },
            )
        } finally {
            secondClient.forceDestroy()
        }
    }

    @Test
    fun unsubscribingSubscriptionWithPresenceRemovesBothBaseAndPresenceFromTransport() {
        val channelName = randomChannel()

        val baseSubscription = pubnub.channel(channelName)
            .subscription(SubscriptionOptions.receivePresenceEvents())

        val presenceEvents = mutableListOf<PNPresenceEventResult>()
        val messageEvents = mutableListOf<PNMessageResult>()
        baseSubscription.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    presenceEvents += result
                }

                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    messageEvents += result
                }
            },
        )
        baseSubscription.subscribe()
        Thread.sleep(2_000)

        // Unsubscribe the combined sub — should tear down both "channel" and "channel-pnpres".
        baseSubscription.unsubscribe()
        Thread.sleep(1_000)

        assertFalse(
            "Base channel should no longer appear in subscribed channels",
            pubnub.getSubscribedChannels().contains(channelName),
        )

        // Nothing the base sub used to receive should reach us anymore.
        val triggerClient = createPubNub {}
        val publisher = createPubNub {}
        try {
            triggerClient.subscribeToBlocking(channelName)
            publisher.publish(channel = channelName, message = "after-unsubscribe").sync()
            Thread.sleep(2_000)

            assertTrue(
                "Unsubscribed sub must not receive further presence events for $channelName; got: $presenceEvents",
                presenceEvents.none {
                    it.channel == channelName && it.uuid == triggerClient.configuration.userId.value
                },
            )
            assertTrue(
                "Unsubscribed sub must not receive further messages; got: $messageEvents",
                messageEvents.none { it.message.asString == "after-unsubscribe" },
            )
        } finally {
            triggerClient.forceDestroy()
            publisher.forceDestroy()
        }
    }

    @Test
    fun unsubscribingBaseChannelMustNotEvictActiveDirectPresenceObserver() {
        val channelName = randomChannel()

        val baseSubscription = pubnub.channel(channelName).subscription()
        baseSubscription.subscribe()

        val observerSubscription = pubnub.channel("$channelName-pnpres").subscription()
        val observerJoinLatch = CountDownLatch(1)
        observerSubscription.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    if (result.event == "join" && result.channel == channelName) {
                        observerJoinLatch.countDown()
                    }
                }
            },
        )
        observerSubscription.subscribe()
        Thread.sleep(2_000)

        baseSubscription.unsubscribe()
        Thread.sleep(1_000)

        // observer should still receive presence events — this is what would fail if the base
        // unsubscribe had silently stripped `$channelName-pnpres` from the transport's subscribe list.
        val triggerClient = createPubNub {}
        try {
            triggerClient.subscribeToBlocking(channelName)
            assertTrue(
                "Observer should still receive a join event for $channelName after base unsubscribe",
                observerJoinLatch.await(5, TimeUnit.SECONDS),
            )
        } finally {
            triggerClient.forceDestroy()
            observerSubscription.unsubscribe()
        }
    }

    @Test
    fun observerSeesPresenceEventsWhileStayingInvisibleToOtherSubscribersAndHereNow() {
        val channelA = randomChannel()

        val observerUuid = pubnub.configuration.userId.value

        // User1 — real subscriber with presence. Tracks every presence event it sees.
        val user1 = createPubNub {}
        val user1Uuid = user1.configuration.userId.value
        val user1Events = mutableListOf<PNPresenceEventResult>()
        val user1OwnJoinLatch = CountDownLatch(1)
        user1.channel(channelA)
            .subscription(SubscriptionOptions.receivePresenceEvents())
            .also { sub ->
                sub.addListener(
                    object : EventListener {
                        override fun presence(
                            pubnub: PubNub,
                            result: PNPresenceEventResult,
                        ) {
                            user1Events += result
                            if (result.event == "join" && result.uuid == user1Uuid) {
                                user1OwnJoinLatch.countDown()
                            }
                        }
                    },
                )
                sub.subscribe()
            }
        assertTrue(
            "User1 should see its own join on $channelA",
            user1OwnJoinLatch.await(5, TimeUnit.SECONDS),
        )

        // Observer — subscribes to the -pnpres stream only. Must stay invisible to others.
        val observerSub = pubnub.channel("$channelA-pnpres").subscription()
        val observerPresenceEvents = mutableListOf<PNPresenceEventResult>()
        observerSub.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    observerPresenceEvents += result
                }
            },
        )
        observerSub.subscribe()
        Thread.sleep(3_000)

        // Snapshot User1's events now — anything added AFTER this index will be events that
        // arrived after the observer subscribed. If the observer is truly invisible, none of
        // them should reference observerUuid (join or leave).
        val user1EventsBeforeObserver = user1Events.size

        // User2 — joins after the observer.
        val user2 = createPubNub {}
        val user2Uuid = user2.configuration.userId.value
        val user2Events = mutableListOf<PNPresenceEventResult>()
        val user2OwnJoinLatch = CountDownLatch(1)
        user2.channel(channelA)
            .subscription(SubscriptionOptions.receivePresenceEvents())
            .also { sub ->
                sub.addListener(
                    object : EventListener {
                        override fun presence(
                            pubnub: PubNub,
                            result: PNPresenceEventResult,
                        ) {
                            user2Events += result
                            if (result.event == "join" && result.uuid == user2Uuid) {
                                user2OwnJoinLatch.countDown()
                            }
                        }
                    },
                )
                sub.subscribe()
            }
        assertTrue(
            "User2 should see its own join on $channelA",
            user2OwnJoinLatch.await(5, TimeUnit.SECONDS),
        )

        // Observer should receive User2's join. Also wait for it to land in the listener.
        val deadline = System.currentTimeMillis() + 5_000
        while (System.currentTimeMillis() < deadline &&
            observerPresenceEvents.none { it.event == "join" && it.uuid == user2Uuid && it.channel == channelA }
        ) {
            Thread.sleep(200)
        }
        assertTrue(
            "Observer should receive a join event for User2($user2Uuid) on $channelA; got: $observerPresenceEvents",
            observerPresenceEvents.any { it.event == "join" && it.uuid == user2Uuid && it.channel == channelA },
        )

        // Snapshot User2's events for the later leave-invisibility check.
        val user2EventsAfterJoin = user2Events.size

        try {
            // hereNow on channelA shows User1 and User2 but NOT the observer.
            val here = pubnub.hereNow(channels = listOf(channelA), includeUUIDs = true).sync()
            val occupants = here.channels[channelA]?.occupants?.map { it.uuid }.orEmpty().toSet()
            assertTrue(
                "hereNow($channelA) should list User1($user1Uuid); got $occupants",
                occupants.contains(user1Uuid),
            )
            assertTrue(
                "hereNow($channelA) should list User2($user2Uuid); got $occupants",
                occupants.contains(user2Uuid),
            )
            assertFalse(
                "hereNow($channelA) must NOT list observer($observerUuid); got $occupants",
                occupants.contains(observerUuid),
            )

            // User1 must not have seen a join for the observer.
            val user1EventsAfterObserver = user1Events.drop(user1EventsBeforeObserver)
            assertTrue(
                "User1 must not have received any join event for observer($observerUuid); got: $user1EventsAfterObserver",
                user1EventsAfterObserver.none { it.event == "join" && it.uuid == observerUuid },
            )

            // Observer leaves.
            observerSub.unsubscribe()

            // Wait long enough that a real leave event would have propagated (typical <1s).
            Thread.sleep(3_000)

            val user1EventsAfterObserverLeave = user1Events.drop(user1EventsBeforeObserver)
            assertTrue(
                "User1 must not have received any leave event for observer($observerUuid); got: $user1EventsAfterObserverLeave",
                user1EventsAfterObserverLeave.none { it.event == "leave" && it.uuid == observerUuid },
            )
            val user2EventsAfterObserverLeave = user2Events.drop(user2EventsAfterJoin)
            assertTrue(
                "User2 must not have received any leave event for observer($observerUuid); got: $user2EventsAfterObserverLeave",
                user2EventsAfterObserverLeave.none { it.event == "leave" && it.uuid == observerUuid },
            )
        } finally {
            user1.forceDestroy()
            user2.forceDestroy()
        }
    }

    @Test
    fun legacySubscribeWithPresenceThenUnsubscribeBaseMustTearDownBothBaseAndPresence() {
        val channelName = randomChannel()

        val presenceEvents = mutableListOf<PNPresenceEventResult>()
        val messageEvents = mutableListOf<PNMessageResult>()
        val ownJoinLatch = CountDownLatch(1)
        val preTriggerJoinLatch = CountDownLatch(1)
        val preTriggerClient = createPubNub {}
        val preTriggerUuid = preTriggerClient.configuration.userId.value
        val ownUuid = pubnub.configuration.userId.value
        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

                override fun presence(
                    pubnub: PubNub,
                    presence: PNPresenceEventResult,
                ) {
                    presenceEvents += presence
                    if (presence.event == "join" && presence.channel == channelName) {
                        if (presence.uuid == ownUuid) {
                            ownJoinLatch.countDown()
                        } else if (presence.uuid == preTriggerUuid) {
                            preTriggerJoinLatch.countDown()
                        }
                    }
                }

                override fun message(
                    pubnub: PubNub,
                    message: PNMessageResult,
                ) {
                    messageEvents += message
                }
            },
        )

        pubnub.subscribe(channels = listOf(channelName), withPresence = true)

        // Wait for our own join to confirm the subscribe has taken effect before testing teardown.
        assertTrue(
            "pubnub should see its own join before proceeding",
            ownJoinLatch.await(10, TimeUnit.SECONDS),
        )

        // Prove the presence twin is genuinely live by receiving someone else's join.
        try {
            preTriggerClient.subscribeToBlocking(channelName)
            assertTrue(
                "Presence twin should be live and deliver join events before unsubscribe",
                preTriggerJoinLatch.await(10, TimeUnit.SECONDS),
            )
        } finally {
            preTriggerClient.forceDestroy()
        }
        Thread.sleep(1_000)

        // Legacy unsubscribe of the base — must tear down "foo" AND "foo-pnpres".
        pubnub.unsubscribe(channels = listOf(channelName))
        Thread.sleep(2_000)

        val presenceBeforeTrigger = presenceEvents.size
        val messagesBeforeTrigger = messageEvents.size

        val postTrigger = createPubNub {}
        val publisher = createPubNub {}
        try {
            postTrigger.subscribeToBlocking(channelName)
            publisher.publish(channel = channelName, message = "after-unsubscribe").sync()
            Thread.sleep(4_000)

            val postTriggerUuid = postTrigger.configuration.userId.value
            val presenceTail = presenceEvents.drop(presenceBeforeTrigger)
            assertTrue(
                "After unsubscribe(listOf($channelName)), the SDK-owned $channelName-pnpres must " +
                    "also be torn down. Expected no new join events; got tail: $presenceTail",
                presenceTail.none { it.event == "join" && it.uuid == postTriggerUuid && it.channel == channelName },
            )
            val messageTail = messageEvents.drop(messagesBeforeTrigger)
            assertTrue(
                "After unsubscribe, base $channelName should no longer deliver messages; got tail: $messageTail",
                messageTail.none { it.message.asString == "after-unsubscribe" },
            )
        } finally {
            postTrigger.forceDestroy()
            publisher.forceDestroy()
        }
    }

    @Test
    fun legacyUnsubscribeOfExplicitPresenceNameMustNotTouchBaseSubscription() {
        val channelName = randomChannel()

        val baseMessagesForChannel = mutableListOf<PNMessageResult>()
        val firstMessageLatch = CountDownLatch(1)
        val afterUnsubMessageLatch = CountDownLatch(1)
        // Tracked as a flag that any listener writes to; this lets us distinguish events that
        // arrived BEFORE the unsubscribe (expected — our own join, for example) from events that
        // arrived AFTER (which would be the regression).
        val trackPresenceAfterUnsub = AtomicBoolean(false)
        val unexpectedJoinUuidAfterUnsub = AtomicBoolean(false)
        val trackedTriggerUuid = AtomicReference<String?>(null)

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(pubnub: PubNub, pnStatus: PNStatus) {}

                override fun message(
                    pubnub: PubNub,
                    message: PNMessageResult,
                ) {
                    if (message.channel == channelName) {
                        baseMessagesForChannel += message
                        when (message.message.asString) {
                            "before-observer-unsub" -> firstMessageLatch.countDown()
                            "after-observer-unsub" -> afterUnsubMessageLatch.countDown()
                        }
                    }
                }

                override fun presence(
                    pubnub: PubNub,
                    presence: PNPresenceEventResult,
                ) {
                    if (!trackPresenceAfterUnsub.get()) {
                        return
                    }
                    if (presence.event == "join" &&
                        presence.channel == channelName &&
                        presence.uuid == trackedTriggerUuid.get()
                    ) {
                        unexpectedJoinUuidAfterUnsub.set(true)
                    }
                }
            },
        )

        // Base subscription (no presence) and an independent -pnpres observer.
        pubnub.subscribe(channels = listOf(channelName))
        pubnub.subscribe(channels = listOf("$channelName-pnpres"))
        Thread.sleep(3_000)

        // Sanity: base is live — it can receive a message.
        val publisher = createPubNub {}
        try {
            publisher.publish(channel = channelName, message = "before-observer-unsub").sync()
            assertTrue(
                "Base subscription must deliver messages before the observer unsubscribe",
                firstMessageLatch.await(5, TimeUnit.SECONDS),
            )

            // Explicit unsubscribe of the presence name — base must stay alive.
            pubnub.unsubscribe(channels = listOf("$channelName-pnpres"))
            // Give the subscribe loop enough time to cycle without -pnpres.
            Thread.sleep(5_000)

            // From now on we're watching for presence events attributed to the trigger.
            trackPresenceAfterUnsub.set(true)

            publisher.publish(channel = channelName, message = "after-observer-unsub").sync()
            assertTrue(
                "Base subscription on $channelName must still deliver messages after " +
                    "unsubscribe(listOf(\"$channelName-pnpres\")); got: $baseMessagesForChannel",
                afterUnsubMessageLatch.await(5, TimeUnit.SECONDS),
            )

            // And the observer stream must be gone — a fresh trigger client's join must not reach us.
            val trigger = createPubNub {}
            trackedTriggerUuid.set(trigger.configuration.userId.value)
            try {
                trigger.subscribeToBlocking(channelName)
                Thread.sleep(5_000)
                assertFalse(
                    "After unsubscribe of $channelName-pnpres, the client should not receive a " +
                        "join event for trigger(${trackedTriggerUuid.get()})",
                    unexpectedJoinUuidAfterUnsub.get(),
                )
            } finally {
                trigger.forceDestroy()
            }
        } finally {
            publisher.forceDestroy()
            pubnub.unsubscribe(channels = listOf(channelName))
        }
    }

    @Test
    fun wildcardSubscriptionWithReceivePresenceEventsReceivesPresenceOnConcreteChannel() {
        val prefix = "wc_${randomValue()}"
        val concreteChannel = "$prefix.room1"

        val wildcardPresenceEvents = mutableListOf<PNPresenceEventResult>()
        val triggerJoinLatch = CountDownLatch(1)

        val wildcardSub = pubnub.channel("$prefix.*")
            .subscription(SubscriptionOptions.receivePresenceEvents())
        wildcardSub.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    wildcardPresenceEvents += result
                    if (result.event == "join" && result.channel == concreteChannel) {
                        triggerJoinLatch.countDown()
                    }
                }
            },
        )
        wildcardSub.subscribe()
        Thread.sleep(2_000)

        val triggerClient = createPubNub {}
        try {
            triggerClient.subscribeToBlocking(concreteChannel)
            assertTrue(
                "Wildcard subscription with receivePresenceEvents() should receive join " +
                    "events for $concreteChannel; got: $wildcardPresenceEvents",
                triggerJoinLatch.await(5, TimeUnit.SECONDS),
            )
            val triggerUuid = triggerClient.configuration.userId.value
            assertTrue(
                "Event list should contain trigger's join specifically; got: $wildcardPresenceEvents",
                wildcardPresenceEvents.any {
                    it.event == "join" && it.channel == concreteChannel && it.uuid == triggerUuid
                },
            )
        } finally {
            triggerClient.forceDestroy()
            wildcardSub.unsubscribe()
        }
    }

    @Test
    fun wildcardSubscriptionMustNotReceivePresenceEventsWhenDirectPresenceObserverExists() {
        val prefix = "wc_${randomValue()}"
        val concreteChannel = "$prefix.room"

        // Wildcard subscription — no presence requested.
        val wildcardPresenceEvents = mutableListOf<PNPresenceEventResult>()
        val wildcardMessageLatch = CountDownLatch(1)
        val wildcardSub = pubnub.channel("$prefix.*").subscription()
        wildcardSub.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    wildcardPresenceEvents += result
                }

                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    wildcardMessageLatch.countDown()
                }
            },
        )
        wildcardSub.subscribe()

        // Direct -pnpres observer on a concrete channel under the wildcard namespace.
        val observerSub = pubnub.channel("$concreteChannel-pnpres").subscription()
        val observerJoinLatch = CountDownLatch(1)
        observerSub.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    if (result.event == "join" && result.channel == concreteChannel) {
                        observerJoinLatch.countDown()
                    }
                }
            },
        )
        observerSub.subscribe()

        Thread.sleep(2_000)

        val triggerClient = createPubNub {}
        val publisher = createPubNub {}
        try {
            triggerClient.subscribeToBlocking(concreteChannel)

            assertTrue(
                "Observer should receive the join event on $concreteChannel",
                observerJoinLatch.await(5, TimeUnit.SECONDS),
            )

            publisher.publish(channel = concreteChannel, message = "hello").sync()
            assertTrue(
                "Wildcard subscription should receive regular messages matching $prefix.*",
                wildcardMessageLatch.await(5, TimeUnit.SECONDS),
            )

            assertTrue(
                "Wildcard subscription must not receive presence events; got: $wildcardPresenceEvents",
                wildcardPresenceEvents.isEmpty(),
            )
        } finally {
            triggerClient.forceDestroy()
            publisher.forceDestroy()
            wildcardSub.unsubscribe()
            observerSub.unsubscribe()
        }
    }

    @Test
    fun plainBaseSubscriptionMustNotReceivePresenceEventsWhenDirectPresenceObserverExists() {
        val channelName = randomChannel()

        // Plain subscription — no receivePresenceEvents() — should NEVER get presence events.
        val plainPresenceEvents = mutableListOf<PNPresenceEventResult>()
        val plainMessageLatch = CountDownLatch(1)
        val plainSub = pubnub.channel(channelName).subscription()
        plainSub.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    plainPresenceEvents += result
                }

                override fun message(
                    pubnub: PubNub,
                    result: PNMessageResult,
                ) {
                    plainMessageLatch.countDown()
                }
            },
        )
        plainSub.subscribe()

        // Direct presence-only observer on the same PubNub instance — its presence -pnpres entry
        // is what the transport actually subscribes to for presence events.
        val observerSub = pubnub.channel("$channelName-pnpres").subscription()
        val observerPresenceLatch = CountDownLatch(1)
        observerSub.addListener(
            object : EventListener {
                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    if (result.event == "join" && result.channel == channelName) {
                        observerPresenceLatch.countDown()
                    }
                }
            },
        )
        observerSub.subscribe()

        Thread.sleep(2_000)

        val triggerClient = createPubNub {}
        val publisher = createPubNub {}
        try {
            // Trigger a presence event by having a new client join — observer must see it.
            triggerClient.subscribeToBlocking(channelName)
            assertTrue(
                "Observer should receive the join event",
                observerPresenceLatch.await(5, TimeUnit.SECONDS),
            )

            // And a regular message so we know the plain listener is actually wired up.
            publisher.publish(channel = channelName, message = "hello").sync()
            assertTrue(
                "Plain subscription should receive regular messages",
                plainMessageLatch.await(5, TimeUnit.SECONDS),
            )

            // Plain subscription MUST NOT have captured any presence events.
            assertTrue(
                "Plain subscription must not receive presence events; got: $plainPresenceEvents",
                plainPresenceEvents.isEmpty(),
            )
        } finally {
            triggerClient.forceDestroy()
            publisher.forceDestroy()
            plainSub.unsubscribe()
            observerSub.unsubscribe()
        }
    }

    @Test
    fun presenceOnlyObserverMustNotIssueHeartbeatEvenWhenHeartbeatIntervalIsNonZero() {
        // Regression guard: with heartbeatInterval > 0 the presence event engine is active
        // (EnabledPresence). A naive presence.joined(emptySet(), emptySet()) for a -pnpres-only
        // subscription would transition the engine into Heartbeating(emptySet(), emptySet()) and
        // fire a Heartbeat effect that HeartbeatEndpoint rejects with CHANNEL_AND_GROUP_MISSING.
        val heartbeatUrls = java.util.Collections.synchronizedList(mutableListOf<String>())
        val leaveUrls = java.util.Collections.synchronizedList(mutableListOf<String>())
        val heartbeatFailureLatch = CountDownLatch(1)

        val heartbeatLogger = object : CustomLogger {
            override fun debug(logMessage: LogMessage) {
                if (logMessage.type == LogMessageType.NETWORK_REQUEST) {
                    val networkRequestDetails = logMessage.message as LogMessageContent.NetworkRequest
                    val fullUrl = networkRequestDetails.origin + networkRequestDetails.path
                    when {
                        networkRequestDetails.path.contains("/v2/presence/") &&
                            networkRequestDetails.path.contains("/heartbeat") ->
                            heartbeatUrls += fullUrl
                        networkRequestDetails.path.contains("/v2/presence/") &&
                            networkRequestDetails.path.contains("/leave") ->
                            leaveUrls += fullUrl
                    }
                }
            }
        }

        clientConfig = {
            heartbeatInterval = 1
            presenceTimeout = 20
            heartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL
            customLoggers = listOf(heartbeatLogger)
        }

        pubnub.addListener(
            object : StatusListener {
                override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                    if (pnStatus.category == PNStatusCategory.PNHeartbeatFailed) {
                        heartbeatFailureLatch.countDown()
                    }
                }
            },
        )

        val channelName = randomChannel()
        val observer = pubnub.channel("$channelName-pnpres").subscription()
        observer.subscribe()

        // Give the heartbeat loop a few cycles to (incorrectly) fire.
        Thread.sleep(6_000)

        assertTrue(
            "Presence-only observer must NOT issue any /v2/presence/.../heartbeat calls; got: $heartbeatUrls",
            heartbeatUrls.isEmpty(),
        )
        assertFalse(
            "Presence-only observer must NOT emit a PNHeartbeatFailed status",
            heartbeatFailureLatch.await(100, TimeUnit.MILLISECONDS),
        )

        observer.unsubscribe()
        Thread.sleep(2_000)

        assertTrue(
            "Presence-only observer must NOT issue any /v2/presence/.../leave calls after unsubscribe; got: $leaveUrls",
            leaveUrls.isEmpty(),
        )
    }

    private fun publishToChannels(channelsList: List<String>) {
        channelsList.forEach { channelName ->
            pubnub.publish(channelName, "-=message to $channelName").sync()
        }
    }

    private fun createThread(channelsList: List<String>) = Thread {
        channelsList.forEach { channelName ->
            val chan01 = pubnub.channel(channelName)
            val sub01 = chan01.subscription()
            sub01.onMessage = { pnMessageResult: PNMessageResult ->
                println("-=message: " + pnMessageResult.message + " channel: " + pnMessageResult.channel + " channelName: " + channelName)
            }
            sub01.subscribe()
        }
    }
}
