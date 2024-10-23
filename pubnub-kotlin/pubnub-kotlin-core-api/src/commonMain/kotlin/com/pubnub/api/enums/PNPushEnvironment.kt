package com.pubnub.api.enums

enum class PNPushEnvironment {
    DEVELOPMENT,
    PRODUCTION,
    ;

    fun toParamString(): String {
        return name.lowercase()
    }

    companion object {
        fun fromParamString(paramString: String): PNPushEnvironment {
            return entries.first { it.toParamString() == paramString }
        }
    }
}
