package com.pubnub.api.legacy

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class PubNubTest : BaseTest() {

    override fun onBefore() {
        clearConfiguration()
        initPubNub()
        config.subscribeKey = "demo"
        config.publishKey = "demo"
    }

    @Test
    fun testCreateSuccess() {
        assertEquals(true, pubnub.configuration.secure)
        assertEquals("https://ps.pndsn.com", pubnub.baseUrl())
    }

    @Test
    @Throws(PubNubException::class)
    fun testEncryptCustomKey() {
        assertEquals("iALQtn3PfIXe74CT/wrS7g==", pubnub.encrypt("test1", "cipherKey").trim())
    }

    @Test
    @Throws(PubNubException::class)
    fun testEncryptConfigurationKey() {
        config.cipherKey = "cipherKey"
        initPubNub()
        assertEquals("iALQtn3PfIXe74CT/wrS7g==", pubnub.encrypt("test1").trim())
    }

    @Test
    @Throws(PubNubException::class)
    fun testDecryptCustomKey() {
        assertEquals("test1", pubnub.decrypt("iALQtn3PfIXe74CT/wrS7g==", "cipherKey").trim())
    }

    @Test
    @Throws(PubNubException::class)
    fun testDecryptConfigurationKey() {
        config.cipherKey = "cipherKey"
        initPubNub()
        assertEquals("test1", pubnub.decrypt("iALQtn3PfIXe74CT/wrS7g==").trim())
    }

    @Test
    fun testconfig() {
        config.subscribeTimeout = 3000
        config.connectTimeout = 4000
        config.nonSubscribeRequestTimeout = 5000
        config.reconnectionPolicy = PNReconnectionPolicy.NONE
        initPubNub()

        assertEquals("https://ps.pndsn.com", pubnub.baseUrl())
        assertEquals(3000, config.subscribeTimeout)
        assertEquals(4000, config.connectTimeout)
        assertEquals(5000, config.nonSubscribeRequestTimeout)
    }

    @Test
    fun getVersionAndTimeStamp() {
        val version = pubnub.version
        val timeStamp = pubnub.timestamp()
        assertEquals("5.0.2", version)
        assertTrue(timeStamp > 0)
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
                        category = PNStatusCategory.PNUnknownCategory,
                        error = false,
                        operation = PNOperationType.PNTimeOperation
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
}
