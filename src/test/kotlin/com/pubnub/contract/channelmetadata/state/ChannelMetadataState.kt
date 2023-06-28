package com.pubnub.contract.channelmetadata.state

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.contract.state.World
import com.pubnub.contract.state.WorldState

class ChannelMetadataState(world: World) : WorldState by world {
    var channelMetadatas: Collection<PNChannelMetadata>? = null
    var channelMetadata: PNChannelMetadata? = null
    lateinit var channelId: String
    var name: String? = null
}
