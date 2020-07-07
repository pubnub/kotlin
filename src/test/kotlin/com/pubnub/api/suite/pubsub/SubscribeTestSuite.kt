package com.pubnub.api.suite.pubsub

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.SUB
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class SubscribeTestSuite : EndpointTestSuite<Subscribe, SubscribeEnvelope>() {

    override fun telemetryParamName() = ""

    override fun pnOperation() = PNOperationType.PNSubscribeOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): Subscribe {
        return Subscribe(pubnub).apply {
            channels = listOf("ch1")
        }
    }

    override fun verifyResultExpectations(result: SubscribeEnvelope) {
        assertEquals(100, result.metadata.timetoken)
        assertEquals("1", result.metadata.region)
        assertTrue(result.messages.isEmpty())
    }

    override fun successfulResponseBody() = """
        {
          "t": {
            "t": "100",
            "r": 1
          },
          "m": []
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch1/0"))
            .withQueryParam("tt", absent())
            .withQueryParam("tr", absent())
            .withQueryParam("filter-expr", absent())
            .withQueryParam("state", absent())
            .withQueryParam("channel-group", absent())
            .withQueryParam("heartbeat", equalTo("300"))

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()
}
