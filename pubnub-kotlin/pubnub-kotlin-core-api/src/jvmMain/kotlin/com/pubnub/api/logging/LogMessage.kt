package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName
import org.slf4j.event.Level

class LogMessage(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("pubNubId")
    val pubNubId: String,
    @SerializedName("logLevel")
    val logLevel: Level,
    @SerializedName("location")
    val location: String,
    @SerializedName("type")
    val type: LogMessageType,
    @SerializedName("message")
    val message: LogMessageContent, // Changed to use the sealed class
    @SerializedName("details")
    val details: String? = null
)