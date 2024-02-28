package com.pubnub.api.enums

import java.util.Locale

enum class PNPushType(s: String) {
    APNS("apns"),
    MPNS("mpns"),
    GCM("gcm"),
    FCM("gcm"),
    APNS2("apns2"),
    ;

    private val value: String = s

    fun toParamString(): String {
        return value.lowercase(Locale.US)
    }

    override fun toString(): String {
        return value
    }
}
