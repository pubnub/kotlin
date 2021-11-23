package com.pubnub.contract.messageaction.step

import com.pubnub.contract.messageaction.state.MessageActionState
import io.cucumber.java.en.Then
import org.junit.Assert.assertNotNull

class ThenSteps(private val messageActionState: MessageActionState) {

    @Then("the response contains pagination info")
    fun the_response_contains_pagination_info() {
        assertNotNull(messageActionState.getMessageActionResult)
        assertNotNull(messageActionState.getMessageActionResult?.page)
    }
}