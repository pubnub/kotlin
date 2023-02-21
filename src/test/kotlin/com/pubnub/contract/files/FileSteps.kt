package com.pubnub.contract.files

import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.contract.state.World
import io.cucumber.java.en.When
import java.util.UUID

class FileSteps(val world: World) {

    @When("I send a file with {spaceId} space id and {string} message type")
    fun i_send_a_file_with_space_id_and_message_type(spaceId: SpaceId, messageTypeString: String) {
        try {
            world.pubnub.sendFile(
                channel = "channel - ${UUID.randomUUID().toString().take(6)}",
                message = "This is message",
                messageType = MessageType(messageTypeString),
                inputStream = "This is file content".byteInputStream(),
                spaceId = spaceId,
                fileName = "fileName",
            ).sync()?.let {
                world.responseStatus = it.status
            }
        } catch (ex: PubNubException) {
            world.responseStatus = ex.statusCode
            world.pnException = ex
        }
    }
}
