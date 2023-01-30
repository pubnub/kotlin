package com.pubnub.contract.signal

import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class SignalSteps(private val world: World) {
    @When("I send a signal with {spaceId} space id and {string} message type")
    fun i_send_a_signal_with_space_id_and_message_type(spaceId: SpaceId, messageType: String) {
        try {
            world.pubnub.signal(
                channel = "whatever",
                message = "whatever",
                spaceId = spaceId,
                messageType = MessageType(messageType)
            ).sync()?.let {
                world.responseStatus = 200
            }
        } catch (ex: PubNubException) {
            world.responseStatus = ex.statusCode
            world.pnException = ex
        }
    }
}
