package com.pubnub.docs.messageReactions
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/message-actions#basic-usage

// snippet.messageReactionsMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.message_actions.PNMessageAction

fun main() {
    println("PubNub addMessageAction() Example")
    println("================================")

    // 1. Configure PubNub
    val userId = UserId("message-action-demo-user")
    val config = PNConfiguration.builder(userId, "demo").apply {
        publishKey = "demo"
        logVerbosity = PNLogVerbosity.BODY // Enable debug logging for visibility
    }.build()

    // 2. Create PubNub instance
    val pubnub = PubNub.create(config)

    try {
        // In a real app, you would have the timetoken of a message you want to react to
        // For this example, we'll use a sample timetoken
        val messageTimetoken = 15701761818730000L
        val channel = "demo-channel"

        println("Working with message with timetoken: $messageTimetoken")

        // First, check if there are any existing message actions we need to clean up
        println("\nChecking for existing message actions...")

        pubnub.getMessageActions(
            channel = channel
        ).async { getResult ->
            getResult.onSuccess { response ->
                // Check if there are any existing message actions for our message
                val existingActions = response.actions.filter {
                    it.messageTimetoken?.toLong() == messageTimetoken &&
                            it.type == "reaction" &&
                            it.value == "smiley_face"
                }

                if (existingActions.isNotEmpty()) {
                    println("Found ${existingActions.size} existing message actions. Removing them...")

                    // Remove each existing action
                    var actionsRemoved = 0
                    existingActions.forEach { action ->
                        pubnub.removeMessageAction(
                            channel = channel,
                            messageTimetoken = action.messageTimetoken?.toLong() ?: 0L,
                            actionTimetoken = action.actionTimetoken?.toLong() ?: 0L
                        ).async { removeResult ->
                            removeResult.onSuccess {
                                actionsRemoved++
                                println("Removed message action with timetoken: ${action.actionTimetoken}")

                                // When all actions are removed, add the new action
                                if (actionsRemoved == existingActions.size) {
                                    addNewMessageAction(pubnub, channel, messageTimetoken)
                                }
                            }.onFailure { exception ->
                                println("Error removing message action: ${exception.message}")
                                // Try to add the new action anyway
                                addNewMessageAction(pubnub, channel, messageTimetoken)
                            }
                        }
                    }
                } else {
                    println("No existing message actions found.")
                    addNewMessageAction(pubnub, channel, messageTimetoken)
                }
            }.onFailure { exception ->
                println("Error checking message actions: ${exception.message}")
                // Try to add the new action anyway
                addNewMessageAction(pubnub, channel, messageTimetoken)
            }
        }

        // Keep the program running to allow async operations to complete
        println("\nWaiting for async operations to complete...")
        Thread.sleep(10000)

    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Helper function to add a new message action
 */
fun addNewMessageAction(pubnub: PubNub, channel: String, messageTimetoken: Long) {
    println("\nAdding a new message action...")

    // Create a message action
    val messageAction = PNMessageAction(
        type = "reaction",           // Type of reaction (can be any string)
        value = "smiley_face",       // Value of reaction (can be any string)
        messageTimetoken = messageTimetoken  // Timetoken of the message to add the reaction to
    )

    // Add the message action
    pubnub.addMessageAction(
        channel = channel,
        messageAction = messageAction
    ).async { result ->
        result.onSuccess { response ->
            println("\nMessage action added successfully!")
            println("  • Type: ${response.type}")
            println("  • Value: ${response.value}")
            println("  • Publisher UUID: ${response.uuid}")
            println("  • Message Timetoken: ${response.messageTimetoken}")
            println("  • Action Timetoken: ${response.actionTimetoken}")

            println("\nFor reference, to get all message actions, you would use:")
            println("pubnub.getMessageActions(")
            println("    channel = \"$channel\"")
            println("    // Optional: add paging parameters")
            println(")")

            // Clean up resources
            pubnub.destroy()

        }.onFailure { exception ->
            println("Error adding message action: ${exception.message}")
            exception.printStackTrace()
            pubnub.destroy()
        }
    }
}
// snippet.end
