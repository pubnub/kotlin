package com.pubnub.api.models.consumer.objects.member

data class PNUUIDWithCustom internal constructor(
    val uuid: String,
    val custom: Any? = null
) {
    companion object {
        fun of(
            uuid: String,
            custom: Any? = null
        ) = PNUUIDWithCustom(uuid = uuid, custom = custom)
    }
}
