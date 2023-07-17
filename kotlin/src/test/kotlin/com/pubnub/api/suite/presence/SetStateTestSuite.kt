package com.pubnub.api.suite.presence

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.JsonObject
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.OptionalScenario
import com.pubnub.api.suite.Result
import com.pubnub.api.suite.SUB
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class SetStateTestSuite : EndpointTestSuite<SetState, PNSetStateResult>() {

    override fun telemetryParamName() = "l_pres"

    override fun pnOperation() = PNOperationType.PNSetStateOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): SetState =
        pubnub.setPresenceState(
            channels = listOf("ch1"),
            state = mapOf("text" to "hello")
        )

    override fun verifyResultExpectations(result: PNSetStateResult) {
        assertEquals(JsonObject().apply { addProperty("text", "hello") }, result.state)
    }

    override fun successfulResponseBody() = """
        {
          "status": 200,
          "message": "OK",
          "payload": {
            "text": "hello"
          },
          "service": "Presence"
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = listOf(
        """{"payload":null}"""
    )

    override fun mappingBuilder(): MappingBuilder =
        get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data"))
            .withQueryParam("state", equalTo("""{"text":"hello"}"""))

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun optionalScenarioList(): List<OptionalScenario<PNSetStateResult>> {
        return listOf(
            OptionalScenario<PNSetStateResult>().apply {
                result = Result.SUCCESS
                responseBuilder = { withBody("""{"payload":{}}""") }
                additionalChecks = { pnStatus: PNStatus, result: PNSetStateResult? ->
                    assertFalse(pnStatus.error)
                    assertTrue(result!!.state.asJsonObject.keySet().isEmpty())
                }
            },
            OptionalScenario<PNSetStateResult>().apply {
                result = Result.FAIL
                responseBuilder = { withBody("""{"payload":null}""") }
                additionalChecks = { pnStatus: PNStatus, result: PNSetStateResult? ->
                    assertTrue(pnStatus.error)
                    assertNull(result)
                }
            }
        )
    }
}
