package com.pubnub.internal.suite.presence

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.internal.endpoints.presence.Heartbeat
import org.junit.Assert.assertTrue

class HeartbeatTestSuite : com.pubnub.internal.suite.EndpointTestSuite<Heartbeat, Boolean>() {

    override fun pnOperation() = PNOperationType.PNHeartbeatOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): Heartbeat {
        return Heartbeat(
            pubnub = pubnub,
            channels = listOf("ch1")
        )
    }

    override fun verifyResultExpectations(result: Boolean) {
        assertTrue(result)
    }

    override fun successfulResponseBody() = """
        {  
            "status": 200,
            "message": "OK",  
            "service": "Presence"
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder =
        get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/heartbeat"))
            .withQueryParam("heartbeat", equalTo(pubnub.configuration.presenceTimeout.toString()))

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun voidResponse() = true
}
