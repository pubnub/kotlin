package com.pubnub.internal.logging.networkLogging

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent

/**
 * Formatter for log messages
 */
object LogMessageFormatter {
    private val prettyGson: Gson by lazy {
        GsonBuilder().setPrettyPrinting().create()
    }

    /**
     * Formats message content based on type
     */
    fun formatMessageContent(content: LogMessageContent): String {
        return try {
            when (content) {
                is LogMessageContent.Text -> content.message
                is LogMessageContent.Object -> prettyGson.toJson(content.message)
                is LogMessageContent.Error -> {
                    val err = content.message
                    "Error(type=${err.type}, message=${err.message}, stack=${err.stack?.joinToString("\n")})"
                }
                is LogMessageContent.NetworkRequest -> {
                    "NetworkRequest:\n${prettyGson.toJson(content.message)}"
                }
                is LogMessageContent.NetworkResponse -> {
                    "NetworkResponse:\n${prettyGson.toJson(content.message)}"
                }
                else -> content.toString()
            }
        } catch (e: Exception) {
            "Failed to format message content: ${e.message}"
        }
    }
}

/**
 * Extension function to create a simplified string representation of LogMessage.
 * Uses cached Gson instances for better performance.
 */
fun LogMessage.simplified(): String {
    val messageContent = LogMessageFormatter.formatMessageContent(this.message)
    return "pnInstanceId: $pubNubId location: $location details: ${details ?: ""}\n$messageContent"
}
