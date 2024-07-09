package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.kmp.CustomObject

data class PNMember(
    val uuid: PNUUIDMetadata?,
    val custom: Map<String, Any?>? = null,
    val updated: String,
    val eTag: String,
    val status: String?,
) {
    data class Partial(
        val uuidId: String,
        override val custom: CustomObject? = null,
        override val status: String? = null,
    ) : MemberInput {
        override val uuid: String = uuidId
    }
}
