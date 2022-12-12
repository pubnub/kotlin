package com.pubnub.contract.publish.step

import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
) {

    @When("I publish message with {string} space id and {string} message type")
    fun i_publish_message_with_space_id_and_message_type(spaceId: String, messageType: String) {
        try {
            world.pubnub.publish(
                channel = "whatever",
                message = "whatever",
                spaceId = SpaceId(spaceId),
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
