package com.pubnub.contract.channelmetadata.step

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.contract.loadChannelMetadata
import io.cucumber.java.en.Given

class GivenSteps(
    private val channelMetadataState: ChannelMetadataState
) {

    @Given("the id for {string} channel")
    fun the_id_for_channel(channelFileName: String) {
        val channel = loadChannelMetadata(channelFileName)
        channelMetadataState.channelId = channel.id
    }

    @Given("the data for {string} channel")
    fun the_data_for_channel(channelFileName: String) {
        val channel = loadChannelMetadata(channelFileName)
        channelMetadataState.channelMetadata = channel
    }
}
