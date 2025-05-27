package com.pubnub.docs.accessManager

// https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#basic-usage
// snippet.grantTokenMain

import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

fun main() {
    // REPLACE THESE WITH YOUR ACTUAL KEYS from your PubNub Admin Portal
    // For demonstration purposes only - these won't work without your own keys
    val mySubscribeKey = "demo" // Replace with actual subscribe key
    val myPublishKey = "demo" // Replace with actual publish key
    val mySecretKey = "mySecretKey" // Replace with actual secret key

    // Configure PubNub with secret key (needed for token generation)
    // Note: Only server-side applications should use the secret key
    val config = com.pubnub.api.v2.PNConfiguration.builder(UserId("server-admin"), mySubscribeKey).apply {
        publishKey = myPublishKey
        secretKey = mySecretKey // Secret key is required for generating tokens
    }

    val adminPubNub = PubNub.create(config.build())

    println("Step 1: Generate a token for a client")
    println("Note: This requires valid PubNub keys with Access Manager enabled")

    // Demo channel we'll use
    val myChannel = "token-demo-channel"

    // Generate a token with specific permissions for a client
    adminPubNub.grantToken(
        ttl = 15, // 15 minutes token lifetime
        authorizedUUID = "client-user", // The client that will use this token
        channels = listOf(
            // Grant read/write access to our demo channel
            ChannelGrant.name(name = myChannel, read = true, write = true),
            // Grant read-only access to any channel matching "readonly-*"
            ChannelGrant.pattern(pattern = "^readonly-.*$", read = true)
        )
    ).async { result ->
        result.onFailure { exception ->
            println("Failed to generate token: ${exception.message}")
            println("\nNOTE: If you see 'Invalid signature' error, make sure:")
            println("1. You've replaced the demo keys with your actual PubNub keys")
            println("2. Access Manager is enabled for your keys in the PubNub Admin Portal")
            println("3. Your secret key is correct")
            exception.printStackTrace()
        }.onSuccess { tokenResult ->
            println("✅ Token generated successfully!")
            val token = tokenResult.token
            println("Token: $token")

            // Step 2: Now create a client PubNub instance that uses this token
            useTokenAsClient(token, myChannel, mySubscribeKey, myPublishKey)
        }
    }

    // Keep the program running long enough for async operations
    Thread.sleep(20000)

    // Clean up
    adminPubNub.destroy()
}

/**
 * This function demonstrates using a token as a client
 */
fun useTokenAsClient(token: String, channelName: String, subscribeKey: String, publishKey: String) {
    println("\nStep 2: Initialize client with the token")

    // Configure client PubNub instance with the token
    val clientConfig = com.pubnub.api.v2.PNConfiguration.builder(UserId("client-user"), subscribeKey).apply {
        this.publishKey = publishKey
        // Set the token that we received from the server
        authToken = token
    }

    val clientPubNub = PubNub.create(clientConfig.build())

    // Define the message we'll publish
    val myMessage = JsonObject().apply {
        addProperty("sender", "client-user")
        addProperty("content", "Hello from a token-authorized client!")
        addProperty("timestamp", System.currentTimeMillis())
    }

    println("Message to send: $myMessage")

    // Add listeners for connection status
    clientPubNub.addListener(object : StatusListener {
        override fun status(pubnub: PubNub, status: PNStatus) {
            when (status.category) {
                PNStatusCategory.PNConnectedCategory -> println("Connected/Reconnected")
                PNStatusCategory.PNDisconnectedCategory,
                PNStatusCategory.PNUnexpectedDisconnectCategory -> println("Disconnected/Unexpectedly Disconnected")
                else -> {
                    if (status.error) {
                        println("Error status: ${status.category}")
                    }
                }
            }
        }
    })

    // Setup channel subscription
    val channel = clientPubNub.channel(channelName)
    val options = SubscriptionOptions.receivePresenceEvents()
    val subscription = channel.subscription(options)

    // Add a listener
    subscription.addListener(object : EventListener {
        override fun message(pubnub: PubNub, result: PNMessageResult) {
            println("📩 Received message: ${result.message.asJsonObject}")
        }

        override fun presence(pubnub: PubNub, result: PNPresenceEventResult) {
            println("Presence event: $result")
        }
    })

    // Subscribe to the channel
    println("\nStep 3: Subscribe to channel $channelName")
    subscription.subscribe()

    // Wait for connection to establish
    Thread.sleep(3000)

    // Publish a message
    println("\nStep 4: Publish a message to $channelName")
    channel.publish(myMessage).async { result ->
        result.onFailure { exception ->
            println("❌ Error while publishing: ${exception.message}")
            exception.printStackTrace()
        }.onSuccess { value ->
            println("✅ Message sent successfully, timetoken: ${value.timetoken}")
        }
    }

    // Wait to see if we receive the message
    Thread.sleep(5000)

    // Test if we can publish to a channel we don't have access to
    println("\nStep 5: Try to publish to a channel we don't have write access to")
    val restrictedChannel = clientPubNub.channel("restricted-channel")
    restrictedChannel.publish(myMessage).async { result ->
        result.onFailure { exception ->
            println("Expected error (we don't have access): ${exception.message}")
        }.onSuccess { value ->
            println("This shouldn't succeed as we don't have access to this channel")
        }
    }

    // Clean up before exiting
    Thread.sleep(5000)
    subscription.unsubscribe()
    clientPubNub.destroy()
    println("\n✅ Example completed")
}
// snippet.end
