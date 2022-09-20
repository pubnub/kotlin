package com.pubnub.contract.uuidmetadata.state

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata

class UUIDMetadataState(
    var uuidMetadata: PNUUIDMetadata? = null,
    var uuid: String? = null,
    var name: String? = null,
)
