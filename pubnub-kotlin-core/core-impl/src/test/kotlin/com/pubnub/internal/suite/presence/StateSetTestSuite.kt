package com.pubnub.internal.suite.presence

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.JsonObject
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.endpoints.presence.SetState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class StateSetTestSuite : com.pubnub.internal.suite.EndpointTestSuite<SetState, PNSetStateResult>() {
    override fun pnOperation() = PNOperationType.PNSetStateOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): SetState =
        pubnub.setPresenceState(
            channels = listOf("ch1"),
            state = mapOf("text" to "hello"),
        )

    override fun verifyResultExpectations(result: PNSetStateResult) {
        assertEquals(JsonObject().apply { addProperty("text", "hello") }, result.state)
    }

    override fun successfulResponseBody() =
        """
        {
          "status": 200,
          "message": "OK",
          "payload": {
            "text": "hello"
          },
          "service": "Presence"
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() =
        listOf(
            """{"payload":null}""",
        )

    override fun mappingBuilder(): MappingBuilder =
        get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data"))
            .withQueryParam("state", equalTo("""{"text":"hello"}"""))

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNSetStateResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNSetStateResult>().apply {
                result = com.pubnub.internal.suite.Result.SUCCESS
                responseBuilder = { withBody("""{"payload":{}}""") }
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertTrue(result.getOrThrow().state.asJsonObject.keySet().isEmpty())
                }
            },
            com.pubnub.internal.suite.OptionalScenario<PNSetStateResult>().apply {
                result = com.pubnub.internal.suite.Result.FAIL
                responseBuilder = { withBody("""{"payload":null}""") }
                additionalChecks = { result ->
                    assertTrue(result.isFailure)
                }
            },
        )
    }
}
