package com.pubnub.internal.models.consumer.objects.uuid

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata

data class PNUUIDMetadataResult(
    val status: Int,
    val data: PNUUIDMetadata?
)
