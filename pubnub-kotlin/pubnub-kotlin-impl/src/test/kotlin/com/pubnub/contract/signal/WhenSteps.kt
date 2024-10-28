package com.pubnub.contract.signal

import com.pubnub.api.PubNubException
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(private val world: World) {
    @When("I send a signal with {string} customMessageType")
    fun i_send_signal_with_custom_message_type(customMessageType: String) {
        try {
            world.pubnub.signal(
                channel = "whatever",
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
