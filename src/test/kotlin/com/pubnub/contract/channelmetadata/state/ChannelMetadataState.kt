package com.pubnub.contract.channelmetadata.state

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata

class ChannelMetadataState {
    var channelMetadatas: Collection<PNChannelMetadata>? = null
    var channelMetadata: PNChannelMetadata? = null
    var channelId: String? = null
    var name: String? = null
}
