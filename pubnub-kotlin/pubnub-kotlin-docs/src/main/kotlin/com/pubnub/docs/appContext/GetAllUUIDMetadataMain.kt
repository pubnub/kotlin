package com.pubnub.docs.appContext
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage

// snippet.getAllUUIDMetadataMain

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration

/**
 * This example demonstrates how to use the getAllUUIDMetadata method in the PubNub Kotlin SDK.
 *
 * The App Context (Objects) API allows you to store metadata about users (UUIDs) in PubNub's
 * database without setting up your own backend.
 */
fun main() {
    println("PubNub UUID Metadata Example")
    println("============================")

    // 1. Configure PubNub
    val userId = UserId("uuid-metadata-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    // 3. First, set up some sample user metadata
    setupSampleUsers(pubnub)

    // 4. Demonstrate getting all UUID metadata
    getAllUUIDMetadataBasic(pubnub)

    // 5. Demonstrate getting all UUID metadata with filtering
    getAllUUIDMetadataWithFilter(pubnub)

    // 6. Get metadata for a specific UUID
    getSpecificUUIDMetadata(pubnub)

    // 7. Clean up sample user metadata
    cleanupUserMetadata(pubnub)

    // Clean up
    pubnub.destroy()
}

/**
 * Set up sample user metadata for the demo
 */
private fun setupSampleUsers(pubnub: PubNub) {
    println("\n# Setting up sample user metadata")

    // Create a few users with metadata
    val users = listOf(
        Triple("user-1", "Alice", "Developer"),
        Triple("user-2", "Bob", "Manager"),
        Triple("user-3", "Charlie", "Support")
    )

    users.forEach { (userId, name, role) ->
        println("\nSetting up metadata for $name ($userId)")

        pubnub.setUUIDMetadata(
            uuid = userId,
            name = name,
            includeCustom = true,
            custom = mapOf("role" to role, "active" to true),
            email = "$name@example.com".lowercase()
        ).async { result ->
            result.onSuccess { response ->
                println("SUCCESS: Created/updated user metadata for ${response.data?.id}")
                println("Name: ${response.data?.name}")
                println("Email: ${response.data?.email}")
                println("Custom: ${response.data?.custom}")
            }.onFailure { exception ->
                println("ERROR: Failed to set user metadata")
                println("Error details: ${exception.message}")
            }
        }
    }

    // Wait for a moment to ensure all operations are complete
    println("\nWaiting for metadata operations to complete...")
    Thread.sleep(2000)
}

/**
 * Demonstrates a basic call to getAllUUIDMetadata
 */
private fun getAllUUIDMetadataBasic(pubnub: PubNub) {
    println("\n# Getting All UUID Metadata (Basic)")

    pubnub.getAllUUIDMetadata(
        includeCustom = true,  // Include custom fields
        includeCount = true    // Include total count in response
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved UUID metadata objects")

            if (response.data != null) {
                response.data.forEachIndexed { index, metadata ->
                    println("\nUser ${index + 1}:")
                    println("UUID: ${metadata.id}")
                    println("Name: ${metadata.name}")
                    println("Email: ${metadata.email}")
                    println("Custom: ${metadata.custom}")
                }
            }

            // Check if there are more results available (pagination)
            if (response.next != null) {
                println("\nMore results available. Use the 'next' page token to retrieve them.")
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get all UUID metadata")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Demonstrates an advanced call to getAllUUIDMetadata with filtering
 */
private fun getAllUUIDMetadataWithFilter(pubnub: PubNub) {
    println("\n# Getting All UUID Metadata (with Filter)")

    // Create a filter to find users with a specific role
    val filter = "custom.role == 'Developer'"

    pubnub.getAllUUIDMetadata(
        filter = filter,        // Filter expression
        limit = 10,             // Maximum number of results
        includeCustom = true,   // Include custom fields
        includeCount = true     // Include total count in response
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved filtered UUID metadata objects")
            println("Filter: \"$filter\"")

            if (response.data != null) {
                response.data.forEachIndexed { index, metadata ->
                    println("\nUser ${index + 1}:")
                    println("UUID: ${metadata.id}")
                    println("Name: ${metadata.name}")
                    println("Email: ${metadata.email}")
                    println("Custom: ${metadata.custom}")
                }
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get filtered UUID metadata")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

/**
 * Get metadata for a specific UUID
 */
private fun getSpecificUUIDMetadata(pubnub: PubNub) {
    println("\n# Getting Metadata for a Specific UUID")

    val userId = "user-2" // Get metadata for Bob

    pubnub.getUUIDMetadata(
        uuid = userId,
        includeCustom = true
    ).async { result ->
        result.onSuccess { response ->
            println("SUCCESS: Retrieved metadata for UUID: $userId")
            if (response.data != null) {
                println("UUID: ${response.data.id}")
                println("Name: ${response.data.name}")
                println("Email: ${response.data.email}")
                println("External ID: ${response.data.externalId}")
                println("Profile URL: ${response.data.profileUrl}")
                println("Updated: ${response.data.updated}")
                println("Custom: ${response.data.custom}")
            } else {
                println("No metadata found for UUID: $userId")
            }
        }.onFailure { exception ->
            println("ERROR: Failed to get metadata for UUID: $userId")
            println("Error details: ${exception.message}")
        }
    }

    // Wait for the operation to complete
    Thread.sleep(2000)
}

private fun cleanupUserMetadata(pubnub: PubNub) {
    val users = listOf("user-1", "user-2", "user-3")

    users.forEach { userId ->
        println("\nCleaning up metadata for $userId")

        pubnub.removeUUIDMetadata(
            uuid = userId
        ).async { result ->
            result.onSuccess { response ->
                println("SUCCESS: Removed user metadata for $userId")
            }.onFailure { exception ->
                println("ERROR: Failed to remove user metadata for $userId")
                println("Error details: ${exception.message}")
            }
        }
        Thread.sleep(2000)
    }
}

// snippet.end
