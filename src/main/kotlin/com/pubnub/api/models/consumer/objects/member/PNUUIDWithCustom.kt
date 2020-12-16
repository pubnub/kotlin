package com.pubnub.api.models.consumer.objects.member

data class PNUUIDWithCustom(
    val uuid: String,
    val custom: Any? = null
) {
    companion object {
        @Deprecated(
            message = "Use constructor instead",
            level = DeprecationLevel.WARNING,
            replaceWith = ReplaceWith(
                "PNUUIDWithCustom(uuid = uuid, custom = custom)",
                "com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom"
            )
        )
        fun of(
            uuid: String,
            custom: Any? = null
        ) = PNUUIDWithCustom(uuid = uuid, custom = custom)
    }
}
