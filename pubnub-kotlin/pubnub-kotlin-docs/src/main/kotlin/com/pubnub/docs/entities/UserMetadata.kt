package com.pubnub.docs.entities
// https://www.pubnub.com/docs/sdks/kotlin/entities/user-metadata#basic-usage

// snippet.userMetadata
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.enums.PNLogVerbosity

fun main() {
    println("PubNub UserMetadata Entity Example")
    println("================================")

    // 1. Configure PubNub
    val userId = UserId("user-metadata-entity-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        subscribeKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. Create a User Metadata entity
    val userMetadataName = "user-123"
    val userMetadata = pubnub.userMetadata(userMetadataName)
    println("Created UserMetadata entity for: $userMetadataName")

    // 4. Set user metadata
    println("\nSetting user metadata...")
    pubnub.setUUIDMetadata(
        uuid = userMetadataName,
        name = "Jane Smith",
        email = "jane@example.com",
        includeCustom = true,
        custom = mapOf(
            "role" to "Developer",
            "department" to "Engineering",
            "location" to "New York"
        )
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: User metadata set")
            println("UUID: ${response.data?.id}")
            println("Name: ${response.data?.name}")
            println("Email: ${response.data?.email}")
            println("Custom: ${response.data?.custom}")
        }.onFailure { exception ->
            println("ERROR: Failed to set user metadata")
            println("Error details: ${exception.message}")
        }
    }

    // 5. Wait for operation to complete
    Thread.sleep(2000)

    // 6. Define a subscription to the user metadata
    val userMetadataSubscription = userMetadata.subscription()
    userMetadataSubscription.onMessage = { result ->
        println("\nReceived message on UserMetadata subscription: ${result.message.asString}")
    }

    userMetadataSubscription.subscribe()
    Thread.sleep(2000)

    // 7. Publish a message to the user metadata
    pubnub.publish(userMetadataName, "Some message").sync()

    // Clean up
    pubnub.destroy()
    println("\nPubNub connection closed")
}
// snippet.end
