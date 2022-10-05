package com.pubnub.contract.channelmetadata.step

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val channelMetadataState: ChannelMetadataState
) {

    @When("I get the channel metadata")
    fun i_get_the_channel_metadata() {
        channelMetadataState.channelMetadata =
            world.pubnub.getChannelMetadata(channel = channelMetadataState.channelId!!).sync()!!.data
    }

    @When("I set the channel metadata")
    fun i_set_the_channel_metadata() {
        val channelMetadata = channelMetadataState.channelMetadata!!
        world.pubnub.setChannelMetadata(
            channel = channelMetadata.id,
            name = channelMetadata.name,
            description = channelMetadata.description,
            custom = channelMetadata.custom,
            type = channelMetadata.type,
            status = channelMetadata.status
        ).sync()
    }

    @When("I get the channel metadata with custom")
    fun i_get_the_channel_metadata_with_custom() {
        channelMetadataState.channelMetadata = world.pubnub.getChannelMetadata(
            channel = channelMetadataState.channelId!!, includeCustom = true
        ).sync()!!.data
    }

    @When("I remove the channel metadata")
    fun i_remove_the_channel_metadata() {
        world.pubnub.removeChannelMetadata(
            channel = channelMetadataState.channelId!!
        ).sync()!!
    }

    @When("I get all channel metadata")
    fun i_get_all_channel_metadata() {
        channelMetadataState.channelMetadatas = world.pubnub.getAllChannelMetadata().sync()!!.data
    }

    @When("I get all channel metadata with custom")
    fun i_get_all_channel_metadata_with_custom() {
        channelMetadataState.channelMetadatas = world.pubnub.getAllChannelMetadata(
            includeCustom = true
        ).sync()!!.data
    }
}
