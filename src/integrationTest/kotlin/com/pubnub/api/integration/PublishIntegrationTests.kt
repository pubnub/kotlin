package com.pubnub.api.integration

import com.google.gson.JsonObject
import com.pubnub.api.CommonUtils.DEFAULT_LISTEN_DURATION
import com.pubnub.api.CommonUtils.generateMessage
import com.pubnub.api.CommonUtils.generatePayload
import com.pubnub.api.CommonUtils.generatePayloadJSON
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.CommonUtils.randomValue
import com.pubnub.api.CommonUtils.retry
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.asyncRetry
import com.pubnub.api.await
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.subscribeToBlocking
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class PublishIntegrationTests : BaseIntegrationTest() {

    lateinit var guestClient: PubNub

    override fun onBefore() {
        guestClient = createPubNub()
    }

    @Test
    fun testPublishMessage() {
        val expectedChannel = randomChannel()

        pubnub.publish(
            channel = expectedChannel,
            message = generatePayload()
        ).await { _, status ->
            assertFalse(status.error)
            assertEquals(status.uuid, pubnub.configuration.userId.value)
        }
    }

    @Test
    fun testPublishMessageHistory() {
        val expectedChannel = randomChannel()

        val expectedPayload = JSONObject().apply {
            put("name", "joe")
            put("age", 48)
        }

        val convertedPayload = pubnub.mapper.convertValue(expectedPayload, JsonObject::class.java)

        pubnub.publish(
            channel = expectedChannel,
            message = expectedPayload
        ).sync()!!

        pubnub.fetchMessages(
            channels = listOf(expectedChannel),
            page = PNBoundedPage(
                limit = 1
            )
        ).asyncRetry { result, status ->
            assertFalse(status.error)
            assertEquals(1, result!!.channels.size)
            assertEquals(1, result.channels[expectedChannel]!!.size)
            assertEquals(convertedPayload, result.channels[expectedChannel]!![0].message)
        }
    }

    @Test
    fun testPublishMessageNoHistory() {
        val expectedChannel = randomChannel()
        val messagePayload = generateMessage(pubnub)

        pubnub.publish(
            message = messagePayload,
            channel = expectedChannel,
            shouldStore = false
        ).await { _, status ->
            assertFalse(status.error)
            assertEquals(status.uuid, pubnub.configuration.userId.value)
        }

        pubnub.history(
            count = 1,
            channel = expectedChannel
        ).asyncRetry { result, status ->
            assertFalse(status.error)
            assertEquals(0, result!!.messages.size)
        }
    }

    @Test
    fun testReceiveMessage() {
        val success = AtomicBoolean()
        val expectedChannel = randomChannel()
        val messagePayload = generateMessage(pubnub)

        val observer = createPubNub()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.affectedChannels.contains(expectedChannel)
                ) {
                    observer.publish(
                        message = messagePayload,
                        channel = expectedChannel
                    ).async { _, status ->
                        assertFalse(status.error)
                    }
                }
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                assertEquals(expectedChannel, pnMessageResult.channel)
                assertEquals(observer.configuration.userId.value, pnMessageResult.publisher)
                assertEquals(messagePayload, pnMessageResult.message)
                success.set(true)
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        success.listen()
    }

    @org.junit.jupiter.api.Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun testReceiveMessageV2() {
        val expectedChannel = randomChannel()
        val messagePayload = generateMessage(pubnub)
        val observer = createPubNub()
        pubnub.test {
            subscribe(expectedChannel)
            observer.publish(
                message = messagePayload,
                channel = expectedChannel
            ).sync()

            val pnMessageResult = nextMessage()
            assertEquals(expectedChannel, pnMessageResult.channel)
            assertEquals(observer.configuration.userId.value, pnMessageResult.publisher)
            assertEquals(messagePayload, pnMessageResult.message)
        }
    }

    @Test
    fun testEventListenerApi() {

        val success = AtomicBoolean()
        val expectedChannel = randomChannel()
        val messagePayload = generateMessage(pubnub)

        val observer = createPubNub()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.affectedChannels.contains(expectedChannel)
                ) {
                    observer.publish(
                        message = messagePayload,
                        channel = expectedChannel
                    ).async { _, status ->
                        assertFalse(status.error)
                    }
                }
            }
        })

        val testChannel = pubnub.channel(expectedChannel)
        val testSubscription = testChannel.subscription(SubscriptionOptions.receivePresenceEvents())
        testSubscription.addListener(object : EventListener {

            override fun message(pubnub: PubNub, result: PNMessageResult) {
                assertEquals(expectedChannel, result.channel)
                assertEquals(observer.configuration.userId.value, result.publisher)
                assertEquals(messagePayload, result.message)
                success.set(true)
            }
        })
        testSubscription.subscribe()

        success.listen()

        testSubscription.unsubscribe()
        assertEquals(emptyList<String>(), pubnub.getSubscribedChannels())
    }

    @Test
    fun testReceiveUnencryptedMessageWithCryptoEnabled() {
        val success = AtomicInteger(0)
        val expectedChannel = randomChannel()
        val messagePayload = generateMessage(pubnub)
        val sender = pubnub

        val observer = createPubNub(
            getBasicPnConfiguration().apply {
                cryptoModule = CryptoModule.createAesCbcCryptoModule("test", false)
            }
        )

        observer.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.affectedChannels.contains(expectedChannel)
                ) {
                    sender.publish(
                        message = messagePayload,
                        channel = expectedChannel
                    ).async { _, status ->
                        assertFalse(status.error)
                        observer.publish(
                            message = messagePayload,
                            channel = expectedChannel
                        ).async { _, status2 ->
                            assertFalse(status2.error)
                        }
                    }
                }
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                if (success.get() == 0) {
                    assertEquals(expectedChannel, pnMessageResult.channel)
                    assertEquals(sender.configuration.userId.value, pnMessageResult.publisher)
                    assertEquals(messagePayload, pnMessageResult.message)
                    assertEquals(PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED, pnMessageResult.error)
                    success.incrementAndGet()
                } else if (success.get() == 1) {
                    assertEquals(expectedChannel, pnMessageResult.channel)
                    assertEquals(observer.configuration.userId.value, pnMessageResult.publisher)
                    assertEquals(messagePayload, pnMessageResult.message)
                    assertNull(pnMessageResult.error)
                    success.incrementAndGet()
                }
            }
        })

        observer.subscribeToBlocking(expectedChannel)

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .untilAtomic(success, Matchers.greaterThanOrEqualTo(2))
    }

    @Test
    fun testOrgJsonObject_Get_History() {
        val expectedChannel = randomChannel()

        val expectedPayload = JSONObject().apply {
            put("name", "John Doe")
            put("city", "San Francisco")
        }

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel,
            usePost = true
        ).sync()!!

        retry {
            pubnub.history(
                channel = expectedChannel,
                count = 1
            ).sync()!!.run {
                assertEquals(
                    expectedPayload.toString(),
                    JSONObject(messages[0].entry.toString()).toString()
                )
            }
        }
    }

    @Test
    fun testOrgJsonObject_Post_History() {
        val expectedChannel = randomChannel()
        val expectedPayload = generatePayloadJSON()

        pubnub.publish(
            channel = expectedChannel,
            message = expectedPayload,
            usePost = true
        ).sync()!!

        retry {
            pubnub.history(
                channel = expectedChannel,
                count = 1
            ).sync()!!.run {
                assertEquals(
                    expectedPayload.toString(),
                    JSONObject(messages[0].entry.toString()).toString()
                )
            }
        }
    }

    @Test
    fun testOrgJsonObject_Get_Receive() {
        val expectedChannel = randomChannel()
        val expectedPayload = generatePayloadJSON()
        val success = AtomicBoolean()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                success.set(expectedPayload.toString() == JSONObject(pnMessageResult.message.toString()).toString())
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel
        ).sync()!!

        success.listen()
    }

    @Test
    fun testOrgJsonObject_Post_Receive() {
        val expectedChannel = randomChannel()
        val expectedPayload = generatePayloadJSON()
        val success = AtomicBoolean()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                success.set(expectedPayload.toString() == JSONObject(pnMessageResult.message.toString()).toString())
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel,
            usePost = true
        ).sync()!!

        success.listen()
    }

    @Test
    fun testOrgJsonArray_Get_History() {
        val expectedChannel = randomChannel()
        val expectedPayload = JSONArray().apply {
            repeat(2) { put(generatePayloadJSON()) }
        }

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel
        ).sync()!!

        retry {
            pubnub.history(
                channel = expectedChannel,
                count = 1
            ).sync()!!.run {
                assertEquals(
                    expectedPayload.toString(),
                    JSONArray(messages[0].entry.toString()).toString()
                )
            }
        }
    }

    @Test
    fun testOrgJsonArray_Post_History() {
        val expectedChannel = randomChannel()
        val expectedPayload = JSONArray().apply {
            repeat(2) { put(generatePayloadJSON()) }
        }

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel,
            usePost = true
        ).sync()!!

        retry {
            pubnub.history(
                channel = expectedChannel,
                count = 1
            ).sync()!!.run {
                assertEquals(
                    expectedPayload.toString(),
                    JSONArray(messages[0].entry.toString()).toString()
                )
            }
        }
    }

    @Test
    fun testOrgJsonArray_Get_Receive() {
        val expectedChannel = randomChannel()
        val expectedPayload = JSONArray().apply {
            repeat(2) { put(generatePayloadJSON()) }
        }

        val success = AtomicBoolean()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                success.set(expectedPayload.toString() == JSONArray(pnMessageResult.message.toString()).toString())
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel
        ).sync()

        success.listen()
    }

    @Test
    fun testOrgJsonArray_Post_Receive() {
        val expectedChannel = randomChannel()
        val expectedPayload = JSONArray().apply {
            repeat(2) { put(generatePayloadJSON()) }
        }

        val success = AtomicBoolean()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                success.set(expectedPayload.toString() == JSONArray(pnMessageResult.message.toString()).toString())
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.publish(
            message = expectedPayload,
            channel = expectedChannel,
            usePost = true
        ).sync()!!

        success.listen()
    }

    @Test
    fun testOrgJson_Combo() {
        val expectedChannel = randomChannel()

        val expectedPayload = JSONObject().apply {
            put("key_1", generatePayloadJSON())
            put("key_2", generatePayloadJSON())
        }
        expectedPayload.put("z_1", JSONObject(expectedPayload.toString()))
        expectedPayload.put("a_2", JSONObject(expectedPayload.toString()))
        expectedPayload.put("d_3", JSONObject(expectedPayload.toString()))
        expectedPayload.put("z_array", JSONArray().apply { repeat(3) { put(generatePayloadJSON()) } })

        val count = AtomicInteger()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                assertEquals(
                    expectedPayload.toString(),
                    JSONObject(pnMessageResult.message.toString()).toString()
                )
                count.incrementAndGet()
            }
        })

        pubnub.subscribeToBlocking(expectedChannel)

        pubnub.publish(
            channel = expectedChannel,
            message = expectedPayload,
            usePost = true
        ).sync()

        wait()

        pubnub.fetchMessages(
            channels = listOf(expectedChannel),
            page = PNBoundedPage(
                limit = 1
            )
        ).sync()!!.run {
            assertEquals(
                expectedPayload.toString(),
                JSONObject(channels[expectedChannel]!![0].message.toString()).toString()
            )
            count.incrementAndGet()
        }

        Awaitility.await()
            .atMost(Durations.ONE_MINUTE)
            .untilAtomic(count, IsEqual.equalTo(2))
    }

    @Test
    fun testSubscriptionSet() {
        val success = AtomicInteger(0)
        val expectedMessage = randomValue()
        val randomChannelName01 = randomChannel()
        val randomChannelName02 = randomChannel()
        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            channels = setOf(randomChannelName01, randomChannelName02),
            options = SubscriptionOptions.receivePresenceEvents()
        )

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                success.incrementAndGet()
            }
        })

        subscriptionSetOf.subscribe()
        Thread.sleep(2000)

        guestClient.publish(
            channel = randomChannelName01,
            message = expectedMessage
        ).sync()
        guestClient.publish(
            channel = randomChannelName02,
            message = expectedMessage
        ).sync()

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .untilAtomic(success, Matchers.equalTo(2))
    }

    @Test
    fun testSubscribeToSingleChannelUsingNewEventListeners_channelNameWithWildcard() {
        val success = AtomicInteger()
        val expectedMessage = randomValue()
        val randomChannelName01 = "myChannel.${randomChannel()}"
        val randomChannelName02 = "myChannel.${randomChannel()}"
        val myChannel: Channel = pubnub.channel("myChannel.*")

        val subscription = myChannel.subscription()
        subscription.addListener(object : EventListener {
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                success.incrementAndGet()
            }
        })

        subscription.subscribe()
        Thread.sleep(2000)

        guestClient.publish(
            channel = randomChannelName01,
            message = expectedMessage
        ).sync()
        guestClient.publish(
            channel = randomChannelName02,
            message = expectedMessage
        ).sync()

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .untilAtomic(success, Matchers.equalTo(2))
    }

    @Test
    fun `when Subscription in Set is not subscribed then events are not delivered`() {
        val success = AtomicBoolean()
        val failure = AtomicBoolean()
        val expectedMessage = randomValue()
        val randomChannelName01 = randomChannel()
        val randomChannelName02 = randomChannel()
        val subscriptionSetOf: SubscriptionSet = pubnub.subscriptionSetOf(
            channels = setOf(randomChannelName01, randomChannelName02),
            options = SubscriptionOptions.receivePresenceEvents()
        )

        subscriptionSetOf.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                failure.set(true)
            }
        })

        val sub = pubnub.channel(randomChannelName01).subscription()
        sub.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                success.set(true)
            }
        })
        sub.subscribe()
        Thread.sleep(2000)

        guestClient.publish(
            channel = randomChannelName01,
            message = expectedMessage
        ).sync()

        success.listen()
        Thread.sleep(2000)
        assertFalse(failure.get())
    }

    @Test
    fun `when reconnecting with lower timetoken then existing subscriptions deliver events monotonically`() {
        val expectedMessage = randomValue()
        val randomChannelName01 = randomChannel()
        val success = AtomicInteger(0)
        val failure = AtomicReference<Exception>(null)

        val result0 = pubnub.publish(randomChannelName01, expectedMessage).sync()!!

        pubnub.channel(randomChannelName01).subscription().apply {
            addListener(object : EventListener {
                var highestTimetokenSeen = 0L
                override fun message(pubnub: PubNub, result: PNMessageResult) {
                    if (result.timetoken!! <= highestTimetokenSeen) {
                        failure.set(IllegalStateException("Message timetoken ${result.timetoken} lower than already seen: $highestTimetokenSeen"))
                        return
                    }
                    highestTimetokenSeen = result.timetoken!!
                    success.incrementAndGet()
                }
            })
            subscribe()
        }
        Thread.sleep(2000)

        val result1 = pubnub.publish(randomChannelName01, expectedMessage).sync()

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .untilAtomic(success, Matchers.equalTo(1))

        pubnub.channel(randomChannelName01).subscription().apply {
            addListener(object : EventListener {
                var highestTimetokenSeen = 0L
                override fun message(pubnub: PubNub, result: PNMessageResult) {
                    if (result.timetoken!! <= highestTimetokenSeen) {
                        throw IllegalStateException("Message timetoken ${result.timetoken} lower than already seen: $highestTimetokenSeen")
                    }
                    highestTimetokenSeen = result.timetoken!!
                    success.incrementAndGet()
                }
            })
            subscribe(SubscriptionCursor(result0.timetoken - 1))
        }
        Thread.sleep(2000)

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .untilAtomic(success, Matchers.equalTo(3))

        pubnub.publish(randomChannelName01, expectedMessage).sync()

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .untilAtomic(success, Matchers.equalTo(5))

        Thread.sleep(2000)
        if (failure.get() != null) {
            throw failure.get()
        }
        assertEquals(5, success.get())
    }
}
