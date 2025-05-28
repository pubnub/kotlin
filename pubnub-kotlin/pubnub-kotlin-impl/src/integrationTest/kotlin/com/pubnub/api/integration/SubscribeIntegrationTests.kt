package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
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
import com.pubnub.test.listen
import com.pubnub.test.subscribeToBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

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
                ) {}

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
                ) {}

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
