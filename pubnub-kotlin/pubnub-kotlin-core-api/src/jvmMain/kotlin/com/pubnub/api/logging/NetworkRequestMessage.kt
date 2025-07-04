package com.pubnub.api.logging

import com.google.gson.annotations.SerializedName

data class NetworkRequestMessage(
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
    val identifier: String? = null
)

enum class HttpMethod(val value: String) {
    GET("get"),
    POST("post"),
    PATCH("patch"),
    DELETE("delete");


    companion object {
        fun fromString(method: String): HttpMethod {
            return when (method.lowercase()) {
                "get" -> GET
                "post" -> POST
                "patch" -> PATCH
                "delete" -> DELETE
                else -> throw IllegalStateException("Unknown HTTP method: $method")
            }
        }
    }
}
