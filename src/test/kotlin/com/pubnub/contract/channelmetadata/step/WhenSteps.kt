package com.pubnub.contract.channelmetadata.step

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import io.cucumber.java.en.When

class WhenSteps(
    private val channelMetadataState: ChannelMetadataState
) {

    @When("I get the channel metadata")
    fun i_get_the_channel_metadata() {
        channelMetadataState.pubnub.getChannelMetadata(channel = channelMetadataState.channelId).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            channelMetadataState.responseStatus = it.status
        }
    }

    @When("I set the channel metadata")
    fun i_set_the_channel_metadata() {
        val channelMetadata = channelMetadataState.channelMetadata!!
        channelMetadataState.pubnub.setChannelMetadata(
            channel = channelMetadata.id,
            name = channelMetadata.name,
            description = channelMetadata.description,
            custom = channelMetadata.custom,
            type = channelMetadata.type,
            status = channelMetadata.status
        ).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            channelMetadataState.responseStatus = it.status
        }
    }

    @When("I get the channel metadata with custom")
    fun i_get_the_channel_metadata_with_custom() {
        channelMetadataState.pubnub.getChannelMetadata(
            channel = channelMetadataState.channelId, includeCustom = true
        ).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            channelMetadataState.responseStatus = it.status
        }
    }

    @When("I remove the channel metadata")
    fun i_remove_the_channel_metadata() {
        channelMetadataState.responseStatus = channelMetadataState.pubnub.removeChannelMetadata(
            channel = channelMetadataState.channelId
        ).sync()?.status
    }

    @When("I get all channel metadata")
    fun i_get_all_channel_metadata() {
        channelMetadataState.pubnub.getAllChannelMetadata().sync()?.let {
            channelMetadataState.channelMetadatas = it.data
            channelMetadataState.responseStatus = it.status
        }
    }

    @When("I get all channel metadata with custom")
    fun i_get_all_channel_metadata_with_custom() {
        channelMetadataState.pubnub.getAllChannelMetadata(
            includeCustom = true
        ).sync()?.let {
            channelMetadataState.channelMetadatas = it.data
            channelMetadataState.responseStatus = it.status
        }
    }
}
