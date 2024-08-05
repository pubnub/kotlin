package com.pubnub.contract.channelmetadata.state

import com.pubnub.api.models.consumer.objects.channel.NewPNChannelMetadata

class ChannelMetadataState {
    var channelMetadatas: Collection<NewPNChannelMetadata>? = null
    var channelMetadata: NewPNChannelMetadata? = null
    lateinit var channelId: String
    var name: String? = null
}
