package com.pubnub.api.hackathon

import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.managers.MapperManager
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import org.awaitility.kotlin.await
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import java.time.Duration

class BetterPubNubTest {

    @Test
    fun batching() {
        val initialConfiguration = PNConfiguration(PubNub.generateUUID()).apply {
            subscribeKey = Keys.subscribeKey
            publishKey = Keys.publishKey
            logVerbosity = PNLogVerbosity.BODY
            batchingNumberMessages = 3
            batchingMaxWindow = 500
            secure = false
        }

        val pubnub = betterPubNub(initialConfiguration)
        pubnub.subscribe(channels = listOf("ch1"))
        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {

            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                println(pnMessageResult)
            }
        })
        Thread.sleep(100)
        val printIt = { r: PNPublishResult?, s: PNStatus -> println(s) }
        pubnub.publish(channel = "ch1", message = "What's up, doc?1").async(printIt)
        pubnub.publish(channel = "ch1", message = "What's up, doc?2").async(printIt)
        pubnub.publish(channel = "ch1", message = "What's up, doc?3").async(printIt)
        pubnub.publish(channel = "ch1", message = "What's up, doc?4").async(printIt)
        Thread.sleep(700)
    }

    @Test
    fun remoteConfigChange() {
        val initialConfiguration = PNConfiguration(PubNub.generateUUID()).apply {
            subscribeKey = Keys.subscribeKey
            publishKey = Keys.publishKey
            logVerbosity = PNLogVerbosity.BODY
            secure = false
        }

        val pubnub = betterPubNub(initialConfiguration)
        val expectedOrigin = "ps13.pndsn.com"
        val sdkBatchingMaxWindow = 15L
        val sdkBatchingNumberMessages = 10
        val value = mapOf(
            "sdk_batching_max_window" to sdkBatchingMaxWindow,
            "sdk_reconnection_policy" to null,
            "sdk_log_policy" to null,
            "sdk_batching_number_messages" to sdkBatchingNumberMessages,
            "sdk_origin" to expectedOrigin
        )

        Thread.sleep(100)

        pubnub.publish(
            channel = initialConfiguration.subscribeKey,
            message = value
        ).sync()

        await.atMost(Duration.ofSeconds(5)).untilAsserted {
            Assert.assertEquals(expectedOrigin, pubnub.configuration.origin)
            Assert.assertEquals(sdkBatchingMaxWindow, pubnub.configuration.batchingMaxWindow)
            Assert.assertEquals(sdkBatchingNumberMessages, pubnub.configuration.batchingNumberMessages)
        }
    }

    @Test
    fun testRemoteControlOfBatching() {

    }

    @Ignore
    @Test
    fun integrateWithActualPortal() {
        val initialConfiguration = PNConfiguration(PubNub.generateUUID()).apply {
            subscribeKey = Keys.pamSubKey
            publishKey = Keys.pamPubKey
            logVerbosity = PNLogVerbosity.BODY
            secure = false
            origin = "ingress-http.pdx1.aws.int.ps.pn"
        }

        val pubnub = betterPubNub(initialConfiguration)
        Thread.sleep(500)
        Thread.sleep(500_000)
    }

    @Test
    fun aaa() {
        val expectedOrigin = "thisisorigin"
        val sdkBatchingMaxWindow = 15L
        val sdkBatchingNumberMessages = 10
        val jsonValueOfConfig =
            """{"sdk_batching_max_window":$sdkBatchingMaxWindow,"sdk_reconnection_policy":null,"sdk_log_policy":null,"sdk_batching_number_messages":$sdkBatchingNumberMessages,"sdk_origin":"$expectedOrigin"}"""

        val mapperManager = MapperManager()
        val value = mapperManager.fromJson(jsonValueOfConfig, RemoteConfiguration::class.java)
        Assert.assertEquals(expectedOrigin, value.origin)
        Assert.assertEquals(sdkBatchingNumberMessages, value.batchingNumberMessages)
        Assert.assertEquals(sdkBatchingMaxWindow, value.batchingMaxWindow)
    }
}