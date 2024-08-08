package com.pubnub.internal.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue

data class PNMember(
    val uuid: PNUUIDMetadata,
    val custom: PatchValue<Map<String, Any>?>? = null,
    val updated: String,
    val eTag: String,
    val status: PatchValue<String?>? = null,
) {
    data class Partial(
        val uuidId: String,
        override val custom: Any? = null,
        override val status: String? = null,
    ) : MemberInput {
        override val uuid: String = uuidId
    }
}
