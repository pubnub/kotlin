package com.pubnub.contract.history.step

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.contract.history.state.HistoryState
import com.pubnub.contract.state.World
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains

class WhenSteps(private val world: World, private val historyState: HistoryState) {
    @When("I fetch message history for {string} channel")
    fun i_fetch_message_history_for_channel(channelName: String) {
        try {
            historyState.messages = world.pubnub.fetchMessages(
                channels = listOf(channelName),
                includeCustomMessageType = true
            ).sync()
            world.responseStatus = 200
        } catch (ex: PubNubException) {
            world.responseStatus = ex.statusCode
            world.pnException = ex
        }
    }

    @When("I fetch message history with include_custom_message_type set to false for {string} channel") // todo fix it
    fun i_fetch_message_history_with_includeCustomMessageType_set_to_false_for_channel(channelName: String) {
        try {
            historyState.messages = world.pubnub.fetchMessages(
                channels = listOf(channelName),
                includeCustomMessageType = false
            ).sync()
            world.responseStatus = 200
        } catch (ex: PubNubException) {
            world.responseStatus = ex.statusCode
            world.pnException = ex
        }
    }

    @Then("history response contains messages without customMessageType")
    fun history_response_contains_messages_without_customMessageType() {
        val customMessageTypesInResponse: List<String?> =
            historyState.messages!!.channels.values.flatMap { message -> message.map { it.customMessageType } }
        println()
    }

    @Then("history response contains messages with {string} and {string} message types")
    fun history_response_contains_messages_with_message_type(
        messageTypeOfFirstMessage: String,
        messageTypeOfSecondMessage: String
    ) {
        val historyMessageTypesInResponse: List<HistoryMessageType?> =
            historyState.messages!!.channels.values.flatMap { message -> message.map { it.messageType } }

        assertThat(
            historyMessageTypesInResponse,
            contains(
                HistoryMessageType.of(messageTypeOfFirstMessage.toInt()),
                HistoryMessageType.of(messageTypeOfSecondMessage.toInt())
            )
        )
    }

    @Then("history response contains messages with {string} and {string} types")
    fun history_response_contains_messages_with_customMessageType(
        customMessageTypeOfFirstMessage: String,
        customMessageTypeOfSecondMessage: String
    ) {
        val customMessageTypesInResponse: List<String?> =
            historyState.messages!!.channels.values.flatMap { message -> message.map { it.customMessageType } }

        assertThat(
            customMessageTypesInResponse,
            contains(customMessageTypeOfFirstMessage, customMessageTypeOfSecondMessage),
        )
    }
}
