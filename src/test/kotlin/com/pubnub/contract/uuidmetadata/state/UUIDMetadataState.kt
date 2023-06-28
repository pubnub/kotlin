package com.pubnub.contract.uuidmetadata.state

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.contract.state.World
import com.pubnub.contract.state.WorldState

class UUIDMetadataState(world: World) : WorldState by world {
    var uuidMetadatas: Collection<PNUUIDMetadata>? = null
    var uuidMetadata: PNUUIDMetadata? = null
    var uuid: String? = null
}
