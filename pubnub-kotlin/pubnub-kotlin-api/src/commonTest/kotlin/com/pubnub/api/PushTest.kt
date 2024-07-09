package com.pubnub.api

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PushTest : BaseIntegrationTest() {
    private val channels = listOf(randomString(), randomString())

    private val deviceId =
        generateSequence { (0..9).random() }.take(70).toList().shuffled().joinToString(separator = "")
    private val topic = randomString()
    private val mutex = Mutex() // for some reason without this on JS tests run concurrently with before/after and fail

    @BeforeTest
    override fun before() {
        super.before()
        runTest {
            mutex.withLock {
                pubnub.removeAllPushNotificationsFromDeviceWithPushToken(PNPushType.FCM, deviceId).await()
                pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                    PNPushType.APNS2,
                    deviceId,
                    topic,
                    PNPushEnvironment.PRODUCTION
                ).await()
            }
        }
    }

    @AfterTest
    override fun after() {
        runTest {
            mutex.withLock {
                pubnub.removePushNotificationsFromChannels(PNPushType.FCM, channels, deviceId).await()
                pubnub.removePushNotificationsFromChannels(
                    PNPushType.APNS2,
                    channels,
                    deviceId,
                    topic,
                    PNPushEnvironment.PRODUCTION
                ).await()
            }
        }
        super.after()
    }

    @Test
    fun add_audit_removePushNotificationsOnChannels_fcm() = runTest {
        mutex.withLock {
            pubnub.addPushNotificationsOnChannels(PNPushType.FCM, channels, deviceId).await()

            val result = pubnub.auditPushChannelProvisions(PNPushType.FCM, deviceId).await()
            assertEquals(channels.toSet(), result.channels.toSet())

            pubnub.removePushNotificationsFromChannels(
                PNPushType.FCM,
                channels,
                deviceId,
            ).await()

            withContext(Dispatchers.Default) {
                delay(1000)
            }

            assertEquals(
                emptyList(),
                pubnub.auditPushChannelProvisions(PNPushType.FCM, deviceId).await().channels
            )
        }
    }

    @Test
    fun add_audit_removePushNotificationsOnChannels_apns() = runTest {
        mutex.withLock {
            pubnub.addPushNotificationsOnChannels(
                PNPushType.APNS2,
                channels,
                deviceId,
                topic,
                PNPushEnvironment.PRODUCTION
            ).await()

            val result =
                pubnub.auditPushChannelProvisions(PNPushType.APNS2, deviceId, topic, PNPushEnvironment.PRODUCTION)
                    .await()
            assertEquals(channels.toSet(), result.channels.toSet())

            pubnub.removePushNotificationsFromChannels(
                PNPushType.APNS2,
                channels,
                deviceId,
                topic,
                PNPushEnvironment.PRODUCTION
            ).await()

            assertTrue {
                pubnub.auditPushChannelProvisions(PNPushType.APNS2, deviceId, topic, PNPushEnvironment.PRODUCTION)
                    .await().channels.isEmpty()
            }
        }
    }

    @Test
    fun add_audit_removeAllPushNotifications_fcm() = runTest {
        mutex.withLock {
            pubnub.addPushNotificationsOnChannels(PNPushType.FCM, channels, deviceId).await()

            val result = pubnub.auditPushChannelProvisions(PNPushType.FCM, deviceId).await()
            assertEquals(channels.toSet(), result.channels.toSet())

            pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                PNPushType.FCM,
                deviceId,
            ).await()

            assertEquals(emptyList(), pubnub.auditPushChannelProvisions(PNPushType.FCM, deviceId).await().channels)
        }
    }

    @Test
    fun add_audit_removeAllPushNotifications_apns() = runTest {
        mutex.withLock {
            pubnub.addPushNotificationsOnChannels(
                PNPushType.APNS2,
                channels,
                deviceId,
                topic,
                PNPushEnvironment.PRODUCTION
            ).await()

            val result =
                pubnub.auditPushChannelProvisions(PNPushType.APNS2, deviceId, topic, PNPushEnvironment.PRODUCTION)
                    .await()
            assertEquals(channels.toSet(), result.channels.toSet())

            pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
                PNPushType.APNS2,
                deviceId,
                topic,
                PNPushEnvironment.PRODUCTION
            ).await()

            withContext(Dispatchers.Default) {
                delay(1000)
            }

            assertTrue {
                pubnub.auditPushChannelProvisions(PNPushType.APNS2, deviceId, topic, PNPushEnvironment.PRODUCTION)
                    .await().channels.isEmpty()
            }
        }
    }
}
