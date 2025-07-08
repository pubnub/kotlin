package com.pubnub.internal.logging.networkLogging

import com.google.gson.GsonBuilder
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent

fun LogMessage.simplified(): String {
    val messageContent = when (val content = this.message) {
        is LogMessageContent.Text -> content.message
        is LogMessageContent.Object -> GsonBuilder().setPrettyPrinting().create().toJson(content.message)
        is LogMessageContent.Error -> {
            val err = content.message
            "Error(type=${err.type}, message=${err.message}, stack=${err.stack?.joinToString("\n")})"
        }
        is LogMessageContent.NetworkRequest -> {
            val gson = GsonBuilder().setPrettyPrinting().create()
            "NetworkRequest:\n${gson.toJson(content.message)}"
        }
        is LogMessageContent.NetworkResponse -> {
            val gson = GsonBuilder().setPrettyPrinting().create()
            "NetworkResponse:\n${gson.toJson(content.message)}"
        }
        else -> content.toString()
    }
    return "pnInstanceId: $pubNubId location: $location details: ${details ?: ""}\n$messageContent"
}
