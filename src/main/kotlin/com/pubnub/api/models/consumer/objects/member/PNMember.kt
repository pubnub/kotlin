package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata

data class PNMember(
    val uuid: PNUUIDMetadata?,
    val custom: Any? = null,
    val updated: String,
    val eTag: String,
    val status: String?
) {
    data class Partial(
        val uuidId: String,
        override val custom: Any?,
        override val status: String?,

    ) : MemberInput {
        override val uuid: String = uuidId
    }
}
