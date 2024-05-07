package com.pubnub.api.enums

enum class PNPushType(s: String) {
    APNS("apns"),
    MPNS("mpns"),
    GCM("gcm"),
    FCM("gcm"),
    APNS2("apns2"),
    ;

    private val value: String = s

    fun toParamString(): String {
        return value.lowercase()
    }

    override fun toString(): String {
        return value
    }
}
