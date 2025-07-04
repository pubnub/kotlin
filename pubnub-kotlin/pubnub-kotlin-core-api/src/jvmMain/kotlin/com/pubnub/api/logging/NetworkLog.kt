package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName

sealed class NetworkLog {
    data class Request(
        @SerializedName("message")
        val message: NetworkRequestMessage,
        @SerializedName("canceled")
        val canceled: Boolean = false,
        @SerializedName("failed")
        val failed: Boolean = false
    ) : NetworkLog()

    data class Response(
        @SerializedName("message")
        val message: NetworkResponseMessage
    ) : NetworkLog()
}
