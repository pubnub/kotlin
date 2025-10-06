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
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("message")
        val message: String,
        @SerializedName("stack")
        val stack: List<String>? = null
    ) : LogMessageContent()

    /**
     * Network request message.
     */
    data class NetworkRequest(
        @SerializedName("origin")
        val origin: String,
        @SerializedName("path")
        val path: String,
        @SerializedName("query")
        val query: Map<String, String>? = null,
        @SerializedName("method")
        val method: HttpMethod,
        @SerializedName("headers")
        val headers: Map<String, String>? = null,
        @SerializedName("formData")
        val formData: Map<String, String>? = null,
        @SerializedName("body")
        val body: String? = null,
        @SerializedName("timeout")
        val timeout: Long? = null,
        @SerializedName("identifier")
        val identifier: String? = null,
        @SerializedName("canceled")
        val canceled: Boolean = false,
        @SerializedName("failed")
        val failed: Boolean = false
    ) : LogMessageContent()

    /**
     * Network response message.
     */
    data class NetworkResponse(
        @SerializedName("url")
        val url: String,
        @SerializedName("status")
        val status: Int,
        @SerializedName("headers")
        val headers: Map<String, String>? = null,
        @SerializedName("body")
        val body: String? = null
    ) : LogMessageContent()
}
