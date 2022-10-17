package com.pubnub.contract.uuidmetadata.state

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata

class UUIDMetadataState {
    var uuidMetadatas: Collection<PNUUIDMetadata>? = null
    var uuidMetadata: PNUUIDMetadata? = null
    var uuid: String? = null
}
