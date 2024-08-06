package com.pubnub.contract.channelmetadata.step

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val channelMetadataState: ChannelMetadataState,
) {
    @When("I get the channel metadata")
    fun i_get_the_channel_metadata() {
        world.pubnub.pubNubCore.getChannelMetadata(channel = channelMetadataState.channelId).sync()?.let {
            channelMetadataState.channelMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I set the channel metadata")
    fun i_set_the_channel_metadata() {
        val channelMetadata = channelMetadataState.channelMetadata!!
        world.pubnub.pubNubCore.setChannelMetadata(
            channel = channelMetadata.id,
            name = channelMetadata.name?.value,
            description = channelMetadata.description?.value,
            custom = channelMetadata.custom?.value,
            type = channelMetadata.type?.value,
            status = channelMetadata.status?.value,
        ).sync().let {
            channelMetadataState.channelMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the channel metadata with custom")
    fun i_get_the_channel_metadata_with_custom() {
        world.pubnub.pubNubCore.getChannelMetadata(
            channel = channelMetadataState.channelId,
            includeCustom = true,
        ).sync().let {
            channelMetadataState.channelMetadata = it.data
            world.responseStatus = it.status
        }
    }

    @When("I remove the channel metadata")
    fun i_remove_the_channel_metadata() {
        world.responseStatus =
            world.pubnub.pubNubCore.removeChannelMetadata(
                channel = channelMetadataState.channelId,
            ).sync().status
    }

    @When("I get all channel metadata")
    fun i_get_all_channel_metadata() {
        world.pubnub.pubNubCore.getAllChannelMetadata().sync().let {
            channelMetadataState.channelMetadatas = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get all channel metadata with custom")
    fun i_get_all_channel_metadata_with_custom() {
        world.pubnub.pubNubCore.getAllChannelMetadata(
            includeCustom = true,
        ).sync().let {
            channelMetadataState.channelMetadatas = it.data
            world.responseStatus = it.status
        }
    }
}
