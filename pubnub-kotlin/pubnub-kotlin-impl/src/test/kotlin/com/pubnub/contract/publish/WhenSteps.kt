package com.pubnub.contract.publish

import com.pubnub.api.PubNubException
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(private val world: World) {
    @When("I publish message with {string} customMessageType")
    fun i_publish_message_with_custom_message_type(customMessageType: String){
       try {
           world.pubnub.publish(
               channel = "whatever",
               message = "whatever",
               customMessageType = customMessageType
           ).sync().let {
               world.responseStatus = 200
           }
       } catch (ex: PubNubException){
           world.responseStatus = ex.statusCode
           world.pnException = ex
       }
    }
}