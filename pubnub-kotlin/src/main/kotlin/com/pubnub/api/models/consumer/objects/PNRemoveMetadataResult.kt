package com.pubnub.api.models.consumer.objects

import com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult

data class PNRemoveMetadataResult(val status: Int) {
    companion object {
        fun from(data: PNRemoveMetadataResult): com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult {
            return com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult(data.status)
        }
    }
}
