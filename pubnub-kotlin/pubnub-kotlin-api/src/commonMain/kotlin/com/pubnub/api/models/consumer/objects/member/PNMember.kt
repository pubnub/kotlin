package com.pubnub.api.models.consumer.objects.member

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue
import com.pubnub.kmp.CustomObject

data class PNMember(
    val uuid: PNUUIDMetadata, // todo change to userId?
    val custom: PatchValue<Map<String, Any?>?>? = null,
    val updated: String,
    val eTag: String,
    val status: PatchValue<String?>?,
    val type: PatchValue<String?>?,
) {
    data class Partial(
        val uuidId: String,
        override val custom: CustomObject? = null,
        override val status: String? = null,
        override val type: String? = null
    ) : MemberInput {
        override val uuid: String = uuidId
    }

    // let's not make this public for now, but keep the implementation around in case it's needed
    private operator fun plus(update: PNMember): PNMember {
        return PNMember(
            uuid + update.uuid,
            update.custom ?: custom,
            update.updated,
            update.eTag,
            update.status ?: status,
            update.type ?: type
        )
    }
}
