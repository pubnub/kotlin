package com.pubnub.contract.publish.step

import com.pubnub.api.PubNubException
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(private val world: World) {
    @When("I publish a message")
    fun i_publish_a_message() {
        try {
            world.pubnub
                .publish(
                    channel = "channel",
                    message = "hello"
                ).sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I publish a message with JSON metadata")
    fun i_publish_a_message_with_json_metadata() {
        try {
            world.pubnub
                .publish(
                    channel = "channel",
                    message = "hello",
                    meta = mapOf("this is" to "json meta")
                ).sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I publish a message with string metadata")
    fun i_publish_a_message_with_string_metadata() {
        try {
            world.pubnub
                .publish(
                    channel = "channel",
                    message = "hello",
                    meta = "string meta"
                ).sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }

    @When("I send a signal")
    fun i_send_a_signal() {
        try {
            world.pubnub
                .signal(
                    channel = "channel",
                    message = "signal message"
                ).sync()
        } catch (ex: PubNubException) {
            world.pnException = ex
        }
    }
}
