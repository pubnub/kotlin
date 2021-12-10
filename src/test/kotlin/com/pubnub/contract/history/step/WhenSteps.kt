package com.pubnub.contract.history.step

import com.pubnub.api.PubNubException
import com.pubnub.contract.history.state.HistoryState
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val historyState: HistoryState
) {
    @When("I fetch message history for single channel")
    fun i_fetch_message_history_for_single_channel() {
        try {
            historyState.pnFetchMessagesResult = world.pubnub
                .fetchMessages(channels = listOf("channel"))
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I fetch message history for multiple channels")
    fun i_fetch_message_history_for_multiple_channels() {
        try {
            historyState.pnFetchMessagesResult = world.pubnub
                .fetchMessages(channels = listOf("channel1", "channel2"))
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I fetch message history with message actions")
    fun i_fetch_message_history_with_message_actions() {
        try {
            historyState.pnFetchMessagesResult = world.pubnub
                .fetchMessages(
                    channels = listOf("channel1"),
                    includeMessageActions = true
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }
}