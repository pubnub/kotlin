package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

actual interface RemoveUUIDMetadata : Endpoint<PNRemoveMetadataResult> {
    val uuid: String?
}
