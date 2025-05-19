package com.pubnub.docs.mobilePush

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper
import com.pubnub.api.v2.PNConfiguration

/**
 * This example demonstrates how to use Mobile Push Notifications in the PubNub Kotlin SDK.
 *
 * Mobile Push Notifications feature enables PubNub to send notifications to mobile devices
 * via FCM (Firebase Cloud Messaging) for Android or APNs (Apple Push Notification service) for iOS.
 *
 * Note: This is a compilation-only example as it requires valid push notification tokens
 * and Mobile Push Notifications add-on enabled in the PubNub Admin Portal.
 */
fun main() {
    println("PubNub Mobile Push Notifications Example")
    println("========================================")

    // 1. Configure PubNub
    val userId = UserId("push-notifications-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. Example device tokens (in a real application, these would be obtained from FCM/APNs)
    val fcmDeviceToken = "fcm-device-token-example" // This would be an actual FCM token in a real app
    val apnsDeviceToken = "apns-device-token-example" // This would be an actual APNs token in a real app

    // 4. Demonstrate adding device to channels for push notifications
    registerDeviceForPushNotifications(pubnub, fcmDeviceToken, apnsDeviceToken)

    // 5. Demonstrate listing channels registered for push notifications
    listPushNotificationChannels(pubnub, fcmDeviceToken, apnsDeviceToken)

    // 6. Demonstrate removing device from push notification channels
    unregisterDeviceFromPushNotifications(pubnub, fcmDeviceToken, apnsDeviceToken)

    // 7. Demonstrate publishing a message with push notification payload
    publishMessageWithPushNotification(pubnub)

    // Clean up
    pubnub.destroy()
}

/**
 * Demonstrates how to register a device to receive push notifications on specific channels
 */
fun registerDeviceForPushNotifications(pubnub: PubNub, fcmToken: String, apnsToken: String) {
    println("\n# Registering Device for Push Notifications")

    // Example channels
    val notificationChannels = listOf("alerts", "news", "messages")

    // For FCM (Android)
    println("\n## Registering FCM Device Token")
    println("Channels: $notificationChannels")
    println("Device Token: $fcmToken (example token)")

    // Add FCM device to channels
    // In a real application, you would get this token from FirebaseMessaging.getInstance().token
    pubnub.addPushNotificationsOnChannels(
        pushType = PNPushType.FCM,
        channels = notificationChannels,
        deviceId = fcmToken
    ).async { result ->
        result.onSuccess { response: PNPushAddChannelResult ->
            println("SUCCESS: FCM device registered for push notifications on channels: $notificationChannels")
        }.onFailure { exception ->
            println("ERROR: Failed to register FCM device for push notifications")
            println("Error details: ${exception.message}")
        }
    }

    // For APNS (iOS)
    println("\n## Registering APNS Device Token")
    println("Channels: $notificationChannels")
    println("Device Token: $apnsToken (example token)")
    println("Topic: com.example.myapp (App Bundle ID)")

    // Add APNS device to channels
    // In a real iOS application, this token would be obtained from:
    // - UIApplication.shared.registerForRemoteNotifications()
    // - application(_:didRegisterForRemoteNotificationsWithDeviceToken:) delegate method
    pubnub.addPushNotificationsOnChannels(
        pushType = PNPushType.APNS2,
        channels = notificationChannels,
        deviceId = apnsToken,
        topic = "com.example.myapp", // This would be your actual app's bundle ID
        environment = PNPushEnvironment.DEVELOPMENT // Use PRODUCTION for App Store apps
    ).async { result ->
        result.onSuccess { response: PNPushAddChannelResult ->
            println("SUCCESS: APNS device registered for push notifications on channels: $notificationChannels")
        }.onFailure { exception ->
            println("ERROR: Failed to register APNS device for push notifications")
            println("Error details: ${exception.message}")
        }
    }

    println("\nNote: In a real application, tokens would be obtained from:")
    println("- Android: FirebaseMessaging.getInstance().token")
    println("- iOS: UIApplication.shared.registerForRemoteNotifications() callback")
}

/**
 * Demonstrates how to list channels where a device is registered for push notifications
 */
fun listPushNotificationChannels(pubnub: PubNub, fcmToken: String, apnsToken: String) {
    println("\n# Listing Push Notification Channels for Device")

    // For FCM (Android)
    println("\n## FCM Device Channels")
    println("Device Token: $fcmToken (example token)")

    pubnub.auditPushChannelProvisions(
        pushType = PNPushType.FCM,
        deviceId = fcmToken
    ).async { result ->
        result.onSuccess { response: PNPushListProvisionsResult ->
            val channels = response.channels ?: emptyList()
            println("SUCCESS: FCM device is registered for push notifications on channels:")
            if (channels.isEmpty()) {
                println("No channels found.")
            } else {
                channels.forEach { channel ->
                    println("- $channel")
                }
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get channels for FCM device")
            println("Error details: ${exception.message}")
        }
    }

    // For APNS (iOS)
    println("\n## APNS Device Channels")
    println("Device Token: $apnsToken (example token)")
    println("Topic: com.example.myapp (App Bundle ID)")

    pubnub.auditPushChannelProvisions(
        pushType = PNPushType.APNS2,
        deviceId = apnsToken,
        topic = "com.example.myapp", // This would be your actual app's bundle ID
        environment = PNPushEnvironment.DEVELOPMENT
    ).async { result ->
        result.onSuccess { response: PNPushListProvisionsResult ->
            val channels = response.channels ?: emptyList()
            println("SUCCESS: APNS device is registered for push notifications on channels:")
            if (channels.isEmpty()) {
                println("No channels found.")
            } else {
                channels.forEach { channel ->
                    println("- $channel")
                }
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get channels for APNS device")
            println("Error details: ${exception.message}")
        }
    }
}

/**
 * Demonstrates how to remove a device from push notification channels
 */
fun unregisterDeviceFromPushNotifications(pubnub: PubNub, fcmToken: String, apnsToken: String) {
    println("\n# Removing Device from Push Notification Channels")

    // Example channels to unregister
    val channelsToRemove = listOf("alerts", "news")

    // For FCM (Android) - remove from specific channels
    println("\n## Removing FCM Device from Specific Channels")
    println("Channels to remove: $channelsToRemove")
    println("Device Token: $fcmToken (example token)")

    pubnub.removePushNotificationsFromChannels(
        pushType = PNPushType.FCM,
        channels = channelsToRemove,
        deviceId = fcmToken
    ).async { result ->
        result.onSuccess { response: PNPushRemoveChannelResult ->
            println("SUCCESS: FCM device removed from push notification channels: $channelsToRemove")
        }.onFailure { exception ->
            println("ERROR: Failed to remove FCM device from push notification channels")
            println("Error details: ${exception.message}")
        }
    }

    // For APNS (iOS) - remove from specific channels
    println("\n## Removing APNS Device from Specific Channels")
    println("Channels to remove: $channelsToRemove")
    println("Device Token: $apnsToken (example token)")
    println("Topic: com.example.myapp (App Bundle ID)")

    pubnub.removePushNotificationsFromChannels(
        pushType = PNPushType.APNS2,
        channels = channelsToRemove,
        deviceId = apnsToken,
        topic = "com.example.myapp",
        environment = PNPushEnvironment.DEVELOPMENT
    ).async { result ->
        result.onSuccess { response: PNPushRemoveChannelResult ->
            println("SUCCESS: APNS device removed from push notification channels: $channelsToRemove")
        }.onFailure { exception ->
            println("ERROR: Failed to remove APNS device from push notification channels")
            println("Error details: ${exception.message}")
        }
    }

    // For FCM (Android) - remove from all channels
    println("\n## Removing FCM Device from All Channels")
    println("Device Token: $fcmToken (example token)")

    // Note: The method name is actually removeAllPushNotificationsFromDeviceWithPushToken
    pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
        pushType = PNPushType.FCM,
        deviceId = fcmToken
    ).async { result ->
        result.onSuccess { response: PNPushRemoveAllChannelsResult ->
            println("SUCCESS: FCM device removed from all push notification channels")
        }.onFailure { exception ->
            println("ERROR: Failed to remove FCM device from all push notification channels")
            println("Error details: ${exception.message}")
        }
    }

    // For APNS (iOS) - remove from all channels
    println("\n## Removing APNS Device from All Channels")
    println("Device Token: $apnsToken (example token)")
    println("Topic: com.example.myapp (App Bundle ID)")

    pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
        pushType = PNPushType.APNS2,
        deviceId = apnsToken,
        topic = "com.example.myapp",
        environment = PNPushEnvironment.DEVELOPMENT
    ).async { result ->
        result.onSuccess { response: PNPushRemoveAllChannelsResult ->
            println("SUCCESS: APNS device removed from all push notification channels")
        }.onFailure { exception ->
            println("ERROR: Failed to remove APNS device from all push notification channels")
            println("Error details: ${exception.message}")
        }
    }
}

/**
 * Demonstrates how to publish a message with push notification payload
 */
fun publishMessageWithPushNotification(pubnub: PubNub) {
    println("\n# Publishing Message with Push Notification Payload")
    println("\nChannel: messages")

    // Create a PushPayloadHelper instance
    val pushPayloadHelper = PushPayloadHelper()

    // Set up FCM payload (for Android devices)
    val fcmPayloadV2 = PushPayloadHelper.FCMPayloadV2().apply {
        notification = PushPayloadHelper.FCMPayloadV2.Notification().apply {
            title = "New Message"
            body = "You have received a new message from Alice"
            image = "https://example.com/notification-image.png"
        }

        // Add data fields that the app can use
        data = mapOf(
            "sender_id" to "user-123",
            "message_id" to "msg-456",
            "conversation_id" to "conv-789"
        )
    }
    pushPayloadHelper.fcmPayloadV2 = fcmPayloadV2

    // Set up APNS payload (for iOS devices)
    val apnsPayload = PushPayloadHelper.APNSPayload().apply {
        aps = PushPayloadHelper.APNSPayload.APS().apply {
            val alertMap = mapOf(
                "title" to "New Message",
                "body" to "You have received a new message from Alice",
                "subtitle" to "Messages"
            )
            alert = alertMap
            badge = 1
            sound = "default"
        }

        // Custom APNS fields
        custom = mapOf(
            "sender_id" to "user-123",
            "message_id" to "msg-456",
            "conversation_id" to "conv-789",
            "category" to "NEW_MESSAGE"
        )
    }
    pushPayloadHelper.apnsPayload = apnsPayload

    // Add the regular message content
    // This will be received by all subscribers, including those not using push notifications
    pushPayloadHelper.commonPayload = mapOf(
        "text" to "Hi there! How are you doing today?",
        "sender" to "Alice",
        "timestamp" to System.currentTimeMillis()
    )

    // Build the complete payload
    val payload = pushPayloadHelper.build()

    // Publish the message with push notification payload
    pubnub.publish(
        channel = "messages",
        message = payload
    ).async { result ->
        result.onSuccess { response: PNPublishResult ->
            println("SUCCESS: Message with push notification published")
            println("Timetoken: ${response.timetoken}")
            println("\nPayload structure:")
            println("- FCM payload: ${payload["pn_fcm"] != null}")
            println("- APNS payload: ${payload["pn_apns"] != null}")
            println("- Regular message fields: ${payload.keys.filter { it !in listOf("pn_fcm", "pn_apns") }}")
        }.onFailure { exception ->
            println("ERROR: Failed to publish message with push notification")
            println("Error details: ${exception.message}")
        }
    }

    println("\nNote: In a real application, the push notification will be delivered to:")
    println("1. Android devices registered for push on the 'messages' channel via FCM")
    println("2. iOS devices registered for push on the 'messages' channel via APNs")
    println("3. Regular PubNub subscribers will receive the full message")
}
