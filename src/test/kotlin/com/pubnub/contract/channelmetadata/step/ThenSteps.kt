package com.pubnub.contract.channelmetadata.step

import com.pubnub.contract.channelmetadata.state.ChannelMetadataState
import com.pubnub.contract.loadChannelMetadata
import io.cucumber.java.en.Then
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.Is.`is` as iz

class ThenSteps(
    private val channelMetadataState: ChannelMetadataState
) {

    @Then("the channel metadata for {string} channel")
    fun the_channel_metadata_for_channel(channelFileName: String) {
        val channelMetadata = loadChannelMetadata(channelFileName)
        assertThat(channelMetadataState.channelMetadata, iz(channelMetadata))
    }

    @Then("the channel metadata for {string} channel contains updated")
    fun the_channel_metadata_for_channel_contains_updated(@Suppress("UNUSED_PARAMETER") channelFileName: String) {
        assertThat(channelMetadataState.channelMetadata?.updated, iz(Matchers.notNullValue()))
    }

    @Then("the response contains list with {string} and {string} channel metadata")
    fun the_response_contains_list_with_and_channel_metadata(firstChannelName: String, secondChannelName: String) {
        val firstChannel = loadChannelMetadata(firstChannelName)
        val secondChannel = loadChannelMetadata(secondChannelName)
        assertThat(
            channelMetadataState.channelMetadatas,
            Matchers.containsInAnyOrder(firstChannel, secondChannel)
        )
    }
}
