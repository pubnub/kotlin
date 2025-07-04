package com.pubnub.api.logging

enum class LogMessageType(val type: String) {
    TEXT("text"),
    OBJECT("object"),
    ERROR("error"),
    NETWORK_REQUEST("network-request"),
    NETWORK_RESPONSE("network-response")
}
