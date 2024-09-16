package com.pubnub.internal.suite.presence

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.v2.callbacks.getOrThrow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

class GetStateTestSuite : com.pubnub.internal.suite.CoreEndpointTestSuite<GetState, PNGetStateResult>() {
    override fun pnOperation() = PNOperationType.PNGetState

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): GetState =
        pubnub.getPresenceState(
            channels = listOf("ch1"),
        )

    override fun verifyResultExpectations(result: PNGetStateResult) {
        assertEquals(1, result.stateByUUID.keys.size)
        assertEquals(JsonObject().apply { addProperty("text", "hello") }, result.stateByUUID["ch1"])
    }

    override fun successfulResponseBody() =
        """
        {
         "status": 200,
         "message": "OK",
         "payload": {
          "text": "hello"
         },
         "uuid": "myUUID",
         "channel": "ch1",
         "service": "Presence"
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder = get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID"))

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNGetStateResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNGetStateResult>().apply {
                responseBuilder = { withBody("""{"payload":{}}""") }
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertEquals(JsonObject(), result.getOrThrow().stateByUUID["ch1"])
                }
            },
            com.pubnub.internal.suite.OptionalScenario<PNGetStateResult>().apply {
                responseBuilder = { withBody("""{"payload":null}""") }
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertEquals(JsonNull.INSTANCE, result.getOrThrow().stateByUUID["ch1"])
                }
            },
        )
    }
}
