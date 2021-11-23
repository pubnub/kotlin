package com.pubnub.contract.messageaction.step

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.contract.messageaction.state.MessageActionState
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

private const val messageTimetoken = 1234567890123L
private const val channel = "channel"

class WhenSteps(private val world: World, private val messageActionState: MessageActionState) {
    @When("I add a message action")
    fun i_add_a_message_action() {
        try {
            world.pubnub
                .addMessageAction(
                    channel = channel,
                    messageAction = PNMessageAction(type = "type", value = "value", messageTimetoken = messageTimetoken)
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex;
        }
    }

    @When("I fetch message actions")
    fun i_fetch_message_actions() {
        try {
            messageActionState.getMessageActionResult = world.pubnub
                .getMessageActions(
                    channel = channel
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex;
        }
    }

    @When("I delete a message action")
    fun i_delete_a_message_action() {
        try {
            world.pubnub
                .removeMessageAction(
                    channel = channel,
                    messageTimetoken = messageTimetoken,
                    actionTimetoken = 1234567890123
                )
                .sync()
        } catch (ex: PubNubException) {
            world.pnException = ex;
        }
    }
}