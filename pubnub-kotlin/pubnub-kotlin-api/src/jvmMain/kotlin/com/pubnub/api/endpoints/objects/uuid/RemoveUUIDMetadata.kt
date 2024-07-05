package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

interface RemoveUUIDMetadata : com.pubnub.kmp.endpoints.objects.uuid.RemoveUUIDMetadata, Endpoint<PNRemoveMetadataResult> {
    val uuid: String?
}
