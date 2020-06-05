package com.pubnub.api.integration

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNOperationType.PNTimeOperation
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.enums.PNStatusCategory.PNUnknownCategory
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance.Lifecycle
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@TestInstance(Lifecycle.PER_CLASS)
class AppTest {

    lateinit var pubnub: PubNub
    lateinit var pubKey: String
    lateinit var subKey: String

    @BeforeAll
    fun extractProperties() {
        val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
        val properties = Properties()
        properties.load(inputStream)
        pubKey = properties.getProperty("pub_key")
        subKey = properties.getProperty("sub_key")
    }

    @BeforeEach
    fun initPubnub() {
        pubnub = PubNub(
            PNConfiguration().apply {
                subscribeKey = subKey
                publishKey = pubKey
                logVerbosity = PNLogVerbosity.BODY
            }
        )
    }

    @AfterEach
    fun cleanUp() {
        pubnub.forceDestroy()
    }

    @Test
    fun testPublishSync() {
        pubnub.publish().apply {
            channel = UUID.randomUUID().toString()
            message = UUID.randomUUID().toString()
        }.sync().let {
            Assertions.assertNotNull(it)
        }
    }

    @Test
    fun testPublishAsync() {
        val success = AtomicBoolean()

        pubnub.publish().apply {
            channel = UUID.randomUUID().toString()
            message = UUID.randomUUID().toString()
        }.async { result, status ->
            assertFalse(status.error)
            result!!.timetoken
            success.set(true)
        }

        success.listen()

        success.set(false)

        Thread.sleep(2000)

        pubnub.publish().apply {
            channel = UUID.randomUUID().toString()
            message = UUID.randomUUID().toString()
        }.async { result, status ->
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

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                assertTrue(pnStatus.operation == PNOperationType.PNSubscribeOperation)
                assertTrue(pnStatus.category == PNStatusCategory.PNConnectedCategory)
                assertTrue(pnStatus.affectedChannels.contains(expectedChannel))
                success.set(true)
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}
            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {}
            override fun user(pubnub: PubNub, pnUserResult: PNUserResult) {}
            override fun space(pubnub: PubNub, pnSpaceResult: PNSpaceResult) {}
            override fun membership(pubnub: PubNub, pnMembershipResult: PNMembershipResult) {}
            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}
        })

        val subscribe = pubnub.subscribe()
        subscribe.channels = listOf(expectedChannel)
        subscribe.withPresence = true

        subscribe.execute()

        success.listen()
    }

    @Test
    fun testSynchronizedAccess() {
        val size = 500
        var counter = 0

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                counter++
                val time = SimpleDateFormat("HH:mm:ss:SSS").format(System.currentTimeMillis())
                println("$time ${pnStatus.authKey} [$counter]")
            }
        })

        val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        repeat(size) {
            pool.execute {
                pubnub.subscriptionManager.listenerManager.announce(
                    PNStatus(
                        category = PNUnknownCategory,
                        error = false,
                        operation = PNTimeOperation
                    ).apply {
                        authKey = Thread.currentThread().name
                    }
                )
            }
        }

        Awaitility.await()
            .atMost(Durations.TEN_SECONDS)
            .conditionEvaluationListener {
                if (it.remainingTimeInMS < 300) {
                    println("Almost done. Counter value: $counter")
                }
            }
            .until { counter == size }
    }

    @Test
    fun testHereNow() {
        val expectedChannels = listOf(UUID.randomUUID().toString())

        pubnub.subscribe().apply {
            channels = expectedChannels
            withPresence = true
        }.execute()

        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollDelay(Durations.ONE_SECOND)
            .pollInterval(Durations.ONE_SECOND)
            .with()
            .until {
                pubnub.whereNow().apply {
                    uuid = pubnub.configuration.uuid
                }.sync()!!
                    .channels
                    .containsAll(expectedChannels)
            }

        pubnub.hereNow().apply {
            channels = expectedChannels
            includeUUIDs = false
            includeState = false
        }.sync()
    }

}
