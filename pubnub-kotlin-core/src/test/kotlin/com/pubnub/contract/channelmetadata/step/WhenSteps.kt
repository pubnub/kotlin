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
        world.pubnub.getChannelMetadata(channel = channelMetadataState.channelId).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            world.responseStatus = it.status
        }
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
        ).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the channel metadata with custom")
    fun i_get_the_channel_metadata_with_custom() {
        world.pubnub.getChannelMetadata(
            channel = channelMetadataState.channelId, includeCustom = true
        ).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I remove the channel metadata")
    fun i_remove_the_channel_metadata() {
        world.responseStatus = world.pubnub.removeChannelMetadata(
            channel = channelMetadataState.channelId
        ).sync()?.status
    }

    @When("I get all channel metadata")
    fun i_get_all_channel_metadata() {
        world.pubnub.getAllChannelMetadata().sync()?.let {
            channelMetadataState.channelMetadatas = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get all channel metadata with custom")
    fun i_get_all_channel_metadata_with_custom() {
        world.pubnub.getAllChannelMetadata(
            includeCustom = true
        ).sync()?.let {
            channelMetadataState.channelMetadatas = it.data
            world.responseStatus = it.status
        }
    }
}
