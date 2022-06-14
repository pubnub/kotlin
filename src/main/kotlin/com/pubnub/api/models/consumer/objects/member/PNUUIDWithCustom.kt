package com.pubnub.api.models.consumer.objects.member

interface MemberInput {
    val uuid: String
    val custom: Any?
    val status: String?
}

@Deprecated(
    message = "Use PNMember.Partial",
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "PNMember.Partial(uuidId = uuid, custom = custom)",
        "com.pubnub.api.models.consumer.objects.member.PNMember"
    )
)
data class PNUUIDWithCustom(
    val uuid: String,
    val custom: Any? = null
) {
    companion object {
        @Deprecated(
            message = "Use PNMember.Partial",
            level = DeprecationLevel.WARNING,
            replaceWith = ReplaceWith(
                "PNMember.Partial(uuidId = uuid, custom = custom)",
                "com.pubnub.api.models.consumer.objects.member.PNMember"
            )
        )
        fun of(
            uuid: String,
            custom: Any? = null
        ) = PNUUIDWithCustom(uuid = uuid, custom = custom)
    }
}
