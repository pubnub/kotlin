package com.pubnub.api.enums

enum class PNPushType(private val value: String) {
    APNS("apns"),
    MPNS("mpns"),
    GCM("gcm"),
    FCM("gcm"),
    APNS2("apns2"),
    ;

    fun toParamString(): String {
        return value.lowercase()
    }

    override fun toString(): String {
        return value
    }
}
