package com.pubnub.contract.history

import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.contract.state.World
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasItems

class HistorySteps(
    private val world: World,
    private val historyState: HistoryState
) {

    @When("I fetch message history for {string} channel")
    fun i_fetch_message_history_for_channel(channel: String) {
        world.pubnub.fetchMessages(channels = listOf(channel))
            .sync()?.also {
                historyState.fetchMessagesResult = it
                world.responseStatus = 200
            }
    }

    @When("I fetch message history with 'includeMessageType' set to {boolean} for {string} channel")
    fun i_fetch_message_history_with_include_message_type_set_to_value_for_channel(value: Boolean, channel: String) {
        world.pubnub.fetchMessages(
            channels = listOf(channel),
            includeMessageType = value
        ).sync()?.also {
            historyState.fetchMessagesResult = it
            world.responseStatus = 200
        }
    }

    @When("I fetch message history with 'includeSpaceId' set to {boolean} for {string} channel")
    fun i_fetch_message_history_with_include_space_id_set_to_value_for_channel(value: Boolean, channel: String) {
        world.pubnub.fetchMessages(
            channels = listOf(channel),
            includeSpaceId = value
        ).sync()?.also {
            historyState.fetchMessagesResult = it
            world.responseStatus = 200
        }
    }

    @Then("history response contains messages without message types")
    fun history_response_contains_messages_without_message_types() {
        assertThat(
            historyState.fetchMessagesResult?.allFetchMessageItems()?.mapNotNull { it.messageType },
            empty()
        )
    }

    @Then("history response contains messages with message types")
    fun history_response_contains_messages_with_message_types() {
        assertThat(
            historyState.fetchMessagesResult?.allFetchMessageItems()?.map { it.messageType }?.filter { it == null },
            empty()
        )
    }

    @Then("history response contains messages without space ids")
    fun history_response_contains_messages_without_space_ids() {
        assertThat(
            historyState.fetchMessagesResult?.allFetchMessageItems()?.mapNotNull { it.spaceId },
            empty()
        )
    }

    @Then("history response contains messages with space ids")
    fun history_response_contains_messages_with_space_ids() {
        assertThat(
            historyState.fetchMessagesResult?.allFetchMessageItems()?.map { it.spaceId }?.filter { it == null },
            empty()
        )
    }

    @Then("history response contains messages with {string} and {string} message types")
    fun history_response_contains_messages_with_and_message_types(firstMessageType: String, secondMessageType: String) {
        assertThat(
            historyState.fetchMessagesResult?.allFetchMessageItems()?.map { it.messageType?.value },
            hasItems(firstMessageType, secondMessageType)
        )
    }

    private fun PNFetchMessagesResult.allFetchMessageItems(): List<PNFetchMessageItem> {
        return channels.values.flatten()
    }
}
