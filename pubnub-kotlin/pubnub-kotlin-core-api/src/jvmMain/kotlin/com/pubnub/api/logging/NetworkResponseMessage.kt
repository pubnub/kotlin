package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName

data class NetworkResponseMessage(
    @SerializedName("url")
    val url: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("headers")
    val headers: Map<String, String>? = null,
    @SerializedName("body")
    val body: String? = null
)