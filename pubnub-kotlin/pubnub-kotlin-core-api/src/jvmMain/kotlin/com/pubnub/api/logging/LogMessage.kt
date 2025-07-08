package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName
import org.slf4j.event.Level
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LogMessage(
    @SerializedName("pubNubId")
    val pubNubId: String,
    @SerializedName("logLevel")
    val logLevel: Level,
    @SerializedName("location")
    val location: String,
    @SerializedName("type")
    val type: LogMessageType,
    @SerializedName("message")
    val message: LogMessageContent,
    @SerializedName("details")
    val details: String? = null,
    @SerializedName("timestamp")
    val timestamp: String = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")),
)
