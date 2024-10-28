package com.pubnub.contract.file

import com.pubnub.api.PubNubException
import com.pubnub.contract.state.World
import io.cucumber.java.en.When
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

class WhenSteps(private val world: World) {
    @When("I send a file with {string} customMessageType")
    fun i_send_file_with_customMessageType(customMessageType: String) {
        try {
            world.pubnub.sendFile(
                channel = "whatever",
                fileName = "whatever",
                inputStream = ByteArrayInputStream("whatever".toByteArray(StandardCharsets.UTF_8)),
                message = "whatever",
                customMessageType = customMessageType
            ).sync().let {
                world.responseStatus = 200
            }
        } catch (ex: PubNubException) {
            world.responseStatus = ex.statusCode
            world.pnException = ex
        }
    }
}
