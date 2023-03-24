package com.pubnub.contract.signal

import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class SignalSteps(private val world: World) {
    @When("I send a signal with {spaceId} space id and {string} type")
    fun i_send_a_signal_with_space_id_and_type(spaceId: SpaceId, type: String) {
        try {
            world.pubnub.signal(
                channel = "whatever",
                message = "whatever",
                spaceId = spaceId,
                type = type
            ).sync()?.let {
                world.responseStatus = 200
            }
        } catch (ex: PubNubException) {
            world.responseStatus = ex.statusCode
            world.pnException = ex
        }
    }
}
