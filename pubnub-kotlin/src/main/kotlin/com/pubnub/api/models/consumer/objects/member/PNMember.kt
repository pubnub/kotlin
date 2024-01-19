package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.internal.models.consumer.objects.member.PNMember

data class PNMember(
    val uuid: PNUUIDMetadata?,
    val custom: Any? = null,
    val updated: String,
    val eTag: String,
    val status: String?
) {
    data class Partial(
        val uuidId: String,
        override val custom: Any? = null,
        override val status: String? = null,

    ) : MemberInput {
        override val uuid: String = uuidId
    }

    companion object {
        fun from(data: PNMember): com.pubnub.api.models.consumer.objects.member.PNMember {
            return com.pubnub.api.models.consumer.objects.member.PNMember(
                data.uuid,
                data.custom,
                data.updated,
                data.eTag,
                data.status
            )
        }
    }
}
