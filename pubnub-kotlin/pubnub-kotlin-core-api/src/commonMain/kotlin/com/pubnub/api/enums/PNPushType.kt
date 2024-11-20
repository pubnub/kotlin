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

    companion object {
        fun fromParamString(paramString: String): PNPushType {
            return entries.first { it.toParamString() == paramString }
        }
    }

    override fun toString(): String {
        return value
    }
}
