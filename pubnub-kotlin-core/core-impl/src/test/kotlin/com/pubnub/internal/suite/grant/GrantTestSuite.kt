package com.pubnub.internal.suite.grant

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.internal.endpoints.access.Grant
import com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult
import org.junit.Assert.assertEquals

class GrantTestSuite : com.pubnub.internal.suite.EndpointTestSuite<Grant, PNAccessManagerGrantResult>() {

    override fun onBefore() {
        super.onBefore()
        pubnub.configuration.secretKey = "mySecretKey"
    }

    override fun pnOperation() = PNOperationType.PNAccessManagerGrant

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.SEC

    override fun snippet(): Grant {
        return pubnub.grant().apply {
        }
    }

    override fun verifyResultExpectations(result: PNAccessManagerGrantResult) {
        assertEquals("mySubscribeKey", result.subscribeKey)
        assertEquals(0, result.channels.size)
        assertEquals(0, result.channelGroups.size)
        assertEquals("subkey", result.level)
        assertEquals(1, result.ttl)
    }

    override fun successfulResponseBody() = """
        {
         "message": "Success",
         "payload": {
          "level": "subkey",
          "subscribe_key": "mySubscribeKey",
          "ttl": 1,
          "r": 0,
          "w": 0,
          "m": 0,
          "d": 0
         },
         "service": "Access Manager",
         "status": 200
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = listOf(
        """{"payload":{}}""",
        """{"payload":null}"""
    )

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
            .withQueryParam("r", equalTo("0"))
            .withQueryParam("w", equalTo("0"))
            .withQueryParam("d", equalTo("0"))
            .withQueryParam("m", equalTo("0"))
            .withQueryParam("ttl", equalTo("-1"))
            .withQueryParam("signature", matching(".*"))
    }

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()
}
