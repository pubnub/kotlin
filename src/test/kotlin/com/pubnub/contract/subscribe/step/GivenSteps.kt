package com.pubnub.contract.subscribe.step

import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.contract.state.World
import io.cucumber.java.en.Given

class GivenSteps(private val world: World) {

    @Given("exponential reconnection policy")
    fun exponential_reconnection_policy() {
        world.configuration.apply {
            reconnectionPolicy = PNReconnectionPolicy.EXPONENTIAL
        }
    }
}
