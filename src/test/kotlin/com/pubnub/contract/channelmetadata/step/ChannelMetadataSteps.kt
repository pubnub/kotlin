package com.pubnub.contract.channelmetadata.step

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.contract.loadChannelMetadata
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.Is

class ChannelMetadataSteps(
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

    @Then("the channel metadata for {string} channel")
    fun the_channel_metadata_for_channel(channelFileName: String) {
        val channelMetadata = loadChannelMetadata(channelFileName)
        MatcherAssert.assertThat(channelMetadataState.channelMetadata, Is.`is`(channelMetadata))
    }

    @Then("the channel metadata for {string} channel contains updated")
    fun the_channel_metadata_for_channel_contains_updated(@Suppress("UNUSED_PARAMETER") channelFileName: String) {
        MatcherAssert.assertThat(channelMetadataState.channelMetadata?.updated, Is.`is`(Matchers.notNullValue()))
    }

    @Then("the response contains list with {string} and {string} channel metadata")
    fun the_response_contains_list_with_and_channel_metadata(firstChannelName: String, secondChannelName: String) {
        val firstChannel = loadChannelMetadata(firstChannelName)
        val secondChannel = loadChannelMetadata(secondChannelName)
        MatcherAssert.assertThat(
            channelMetadataState.channelMetadatas,
            Matchers.containsInAnyOrder(firstChannel, secondChannel)
        )
    }
}
