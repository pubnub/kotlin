package com.pubnub.api.enums

enum class PNPushType(private val value: String) {
    @Deprecated(
        replaceWith = ReplaceWith("FCM"),
        message = "GCM is deprecated. Use FCM instead."
    )
    GCM("gcm"),
    APNS("apns"),
    MPNS("mpns"),
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
