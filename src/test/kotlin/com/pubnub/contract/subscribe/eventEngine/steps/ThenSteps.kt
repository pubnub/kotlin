package com.pubnub.contract.subscribe.eventEngine.steps

import com.pubnub.contract.subscribe.eventEngine.state.EventEngineState
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertEquals

class ThenSteps(
    private val eventEngineState: EventEngineState
) {

    @Then("I receive the message in my subscribe response")
    fun I_receive_the_message_in_my_subscribe_response() {
        assertEquals("\"hello\"", eventEngineState.responseMessage)
    }

    @Then("I observe the following:")
    fun I_observe_the_following(dataTable: DataTable) {
        val expectedNames = dataTable.asMaps().map { it["type"] to it["name"] }.toList()
        assertThat(eventEngineState.queuedElements, Matchers.`is`(expectedNames))
    }
}
