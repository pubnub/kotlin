package com.pubnub.api.enums

enum class PNPushEnvironment {
    DEVELOPMENT,
    PRODUCTION,
    ;

    fun toParamString(): String {
        return name.lowercase()
    }
}
