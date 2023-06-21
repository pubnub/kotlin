package com.pubnub.contract.subscribe.eventEngine.steps

import com.pubnub.contract.subscribe.eventEngine.state.EventEngineState
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Then
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
        val expectedDataList = dataTable.asMaps()
        val actualDataEntryList = eventEngineState.eventEngineHappenings
        val conversionMap = createConversionMap()

        for (i in 0 until expectedDataList.size) {
            val expectedDataMap = expectedDataList.get(i)
            val expectedHappeningType = expectedDataMap.get("type")
            val expectedHappeningName = expectedDataMap.get("name")

            val actualDataEntry = actualDataEntryList.get(i)
            val actualHappeningType = actualDataEntry.get("type")
            val actualHappeningName = actualDataEntry.get("name")
            val actualHappeningNameConverted = conversionMap.get(actualHappeningName)
            assertEquals(expectedHappeningType, actualHappeningType)
            assertEquals(expectedHappeningName, actualHappeningNameConverted)
        }
    }

    private fun createConversionMap(): HashMap<String, String> {
        val conversionMap = HashMap<String, String>()
        conversionMap.put("SubscriptionChanged", "SUBSCRIPTION_CHANGED")
        conversionMap.put("Handshake", "HANDSHAKE")
        conversionMap.put("HandshakeSuccess", "HANDSHAKE_SUCCESS")
        conversionMap.put("CancelHandshake", "CANCEL_HANDSHAKE")
        conversionMap.put("EmitStatus", "EMIT_STATUS")
        conversionMap.put("ReceiveMessages", "RECEIVE_MESSAGES")
        conversionMap.put("ReceiveSuccess", "RECEIVE_SUCCESS")
        conversionMap.put("CancelReceiveMessages", "CANCEL_RECEIVE_EVENTS")
        conversionMap.put("EmitMessages", "EMIT_MESSAGES")
        conversionMap.put("EmitStatus", "EMIT_STATUS")
        return conversionMap
    }
}
