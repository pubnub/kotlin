package com.pubnub.docs.miscellaneous
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/misc#create-push-payload-1

// snippet.createPushPayloadMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper
import com.pubnub.api.v2.PNConfiguration

/**
 * This example demonstrates how to use the PushPayloadHelper class in PubNub Kotlin SDK.
 *
 * PushPayloadHelper creates properly formatted payloads for FCM (Android) and APNS (iOS)
 * push notifications to deliver to mobile devices.
 *
 * Note: This is a compilation-only example as it requires valid push notification tokens
 * to actually send notifications to devices.
 */
fun main() {
    println("PubNub PushPayloadHelper Example")
    println("================================")

    // 1. Configure PubNub
    val userId = UserId("push-helper-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. Create basic push notification payloads
    createBasicPushPayload(pubnub)

    // 4. Create advanced push notification payloads
    createAdvancedPushPayload(pubnub)

    // Clean up
    pubnub.destroy()
}

/**
 * Creates a basic push notification payload with simple alerts
 */
fun createBasicPushPayload(pubnub: PubNub) {
    println("\n# Basic Push Notification Payload Example")

    // Create an instance of PushPayloadHelper
    val pushPayloadHelper = PushPayloadHelper()

    // Basic FCM V2 payload for Android
    val fcmPayloadV2 = PushPayloadHelper.FCMPayloadV2().apply {
        notification = PushPayloadHelper.FCMPayloadV2.Notification().apply {
            title = "Basic Notification"
            body = "This is a simple push notification"
        }
    }
    pushPayloadHelper.fcmPayloadV2 = fcmPayloadV2

    // Basic APNS payload for iOS
    val apnsPayload = PushPayloadHelper.APNSPayload().apply {
        aps = PushPayloadHelper.APNSPayload.APS().apply {
            // Simple string alert
            alert = "This is a simple push notification"
            badge = 1
            sound = "default"
        }
    }
    pushPayloadHelper.apnsPayload = apnsPayload

    // Add common payload for regular PubNub subscribers
    pushPayloadHelper.commonPayload = mapOf(
        "message" to "This is a simple push notification",
        "type" to "alert"
    )

    // Build the final payload
    val payload = pushPayloadHelper.build()

    println("Basic Push Payload Structure:")
    println("- FCM (Android): ${payload["pn_fcm"] != null}")
    println("- APNS (iOS): ${payload["pn_apns"] != null}")
    println("- Common fields: ${payload.keys.filter { it != "pn_fcm" && it != "pn_apns" }}")

    // In a real application, you would publish this payload
    // to a channel where devices are registered for push notifications
    println("\nTo send this notification, you would use:")
    println("pubnub.publish(")
    println("    channel = \"notifications_channel\",")
    println("    message = payload")
    println(").async { result -> /* Handle result */ }")
}

/**
 * Creates an advanced push notification payload with rich content and targeting
 */
fun createAdvancedPushPayload(pubnub: PubNub) {
    println("\n# Advanced Push Notification Payload Example")

    // Create an instance of PushPayloadHelper
    val pushPayloadHelper = PushPayloadHelper()

    // Advanced FCM V2 payload with additional features
    val fcmV2 = PushPayloadHelper.FCMPayloadV2().apply {
        // Basic notification part
        notification = PushPayloadHelper.FCMPayloadV2.Notification().apply {
            title = "Meeting Reminder"
            body = "Your meeting with the team starts in 15 minutes"
            image = "https://example.com/meeting-image.png"
        }

        // Android specific configuration
        android = PushPayloadHelper.FCMPayloadV2.AndroidConfig().apply {
            priority = PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidMessagePriority.HIGH
            ttl = "3600s" // Notification expires after 1 hour

            // Android specific notification settings
            notification = PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification().apply {
                clickAction = "OPEN_MEETING_ACTIVITY"
                color = "#4285F4"
                channelId = "meeting_reminders"
                sound = "meeting_alert"
                // Set higher priority for this notification
                notificationPriority =
                    PushPayloadHelper.FCMPayloadV2.AndroidConfig.AndroidNotification.NotificationPriority.PRIORITY_HIGH
            }
        }

        // Data payload that will be delivered to the app
        data = mapOf(
            "meeting_id" to "12345",
            "room" to "Conference Room A",
            "start_time" to "2025-03-25T14:15:00Z",
            "participants" to "Alice,Bob,Charlie"
        )
    }
    pushPayloadHelper.fcmPayloadV2 = fcmV2

    // Advanced APNS payload for iOS
    val apnsPayload = PushPayloadHelper.APNSPayload().apply {
        // APS is the Apple Push Notification Service payload
        aps = PushPayloadHelper.APNSPayload.APS().apply {
            // Create a structured alert with title and body instead of simple string
            val alertMap = mapOf(
                "title" to "Meeting Reminder",
                "body" to "Your meeting with the team starts in 15 minutes",
                "subtitle" to "Calendar Notification"
            )
            alert = alertMap

            badge = 3
            sound = "meeting_alert.aiff"
        }

        // Custom APNS payload fields
        custom = mapOf(
            "meeting_id" to "12345",
            "room" to "Conference Room A",
            "content-available" to 1,
            "category" to "MEETING_INVITATION",
            "thread-id" to "calendar-events"
        )

        // APNS2 configurations for targeting specific apps/devices
        apns2Configurations = listOf(
            PushPayloadHelper.APNSPayload.APNS2Configuration().apply {
                collapseId = "meeting-12345" // Group related notifications
                expiration = "2025-03-25T14:30:00Z" // When the notification expires
                version = "v2"
                targets = listOf(
                    PushPayloadHelper.APNSPayload.APNS2Configuration.Target().apply {
                        environment = PNPushEnvironment.DEVELOPMENT // or PRODUCTION
                        topic = "com.example.calendar" // Your app's bundle ID
                        // Exclude specific devices if needed
                        excludeDevices = listOf("device-token-to-exclude-1", "device-token-to-exclude-2")
                    }
                )
            }
        )
    }
    pushPayloadHelper.apnsPayload = apnsPayload

    // Common payload for all subscribers (both mobile push and regular PubNub)
    pushPayloadHelper.commonPayload = mapOf(
        "message" to "Your meeting with the team starts in 15 minutes",
        "meeting_id" to "12345",
        "type" to "meeting_reminder",
        "action_required" to true
    )

    // Build the final payload
    val payload = pushPayloadHelper.build()

    println("Advanced Push Payload Created:")
    println("- Contains FCM payload: ${payload["pn_fcm"] != null}")
    println("- Contains APNS payload: ${payload["pn_apns"] != null}")
    println("- Common fields count: ${payload.keys.filter { it !in listOf("pn_fcm", "pn_apns") }.size}")

    // Examples of using the payload
    println("\nTo send this notification to all subscribers on a channel:")
    println("pubnub.publish(")
    println("    channel = \"team_notifications\",")
    println("    message = payload")
    println(").async { result -> /* Handle response */ }")

    // Output note about testing
    println("\nNote: To actually receive these notifications on devices, you would need:")
    println("1. Valid FCM/APNs credentials configured in the PubNub Admin Portal")
    println("2. Devices registered for push notifications using pubnub.addPushNotificationsOnChannels()")
    println("3. Valid device tokens for the target devices")
}
// snippet.end
