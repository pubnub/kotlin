package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName

/**
 * Sealed class representing different types of log message content.
 * This provides type safety when working with different message types.
 */
sealed class LogMessageContent {
    /**
     * Plain string message for text type logs.
     */
    data class Text(
        @SerializedName("message")
        val message: String
    ) : LogMessageContent()

    /**
     * Dictionary/object message for object type logs.
     */
    data class Object(
        @SerializedName("arguments")
        val arguments: Map<String, Any>? = null,
        @SerializedName("operation")
        val operation: String? = null,
    ) : LogMessageContent()

    /**
     * Error message with type, message, and stack trace.
     */
    data class Error(
        @SerializedName("message")
        val message: ErrorDetails
    ) : LogMessageContent()

    /**
     * Network request message.
     */
    data class NetworkRequest(
        @SerializedName("message")
        val message: NetworkLog.Request
    ) : LogMessageContent()

    /**
     * Network response message.
     */
    data class NetworkResponse(
        @SerializedName("message")
        val message: NetworkLog.Response
    ) : LogMessageContent()
}

data class ErrorDetails(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("stack")
    val stack: List<String>? = null
)
