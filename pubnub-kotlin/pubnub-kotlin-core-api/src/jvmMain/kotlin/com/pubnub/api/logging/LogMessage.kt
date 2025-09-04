package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName
import org.slf4j.event.Level
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Represents a structured log message with validation and sanitization.
 *
 * @param pubNubId The PubNub instance identifier
 * @param logLevel The logging level
 * @param location The source location of the log
 * @param type The type of log message (automatically inferred from message content)
 * @param message The log message content
 * @param details Optional additional details (will be sanitized)
 * @param timestamp The timestamp when the log was created
 */
class LogMessage(
    @SerializedName("location")
    val location: String,
    @SerializedName("message")
    val message: LogMessageContent,
    @SerializedName("details")
    val details: String? = null,
    @SerializedName("type")
    val type: LogMessageType = message.inferType(),
    @SerializedName("pubNubId")
    val pubNubId: String? = null, // this value will be set in CompositeLogger
    @SerializedName("logLevel")
    val logLevel: Level? = null, // this value will be set in CompositeLogger
    @SerializedName("timestamp")
    val timestamp: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")),
)

/**
 * Infers the LogMessageType from LogMessageContent.
 */
private fun LogMessageContent.inferType(): LogMessageType = when (this) {
    is LogMessageContent.Text -> LogMessageType.TEXT
    is LogMessageContent.Object -> LogMessageType.OBJECT
    is LogMessageContent.Error -> LogMessageType.ERROR
    is LogMessageContent.NetworkRequest -> LogMessageType.NETWORK_REQUEST
    is LogMessageContent.NetworkResponse -> LogMessageType.NETWORK_RESPONSE
}
