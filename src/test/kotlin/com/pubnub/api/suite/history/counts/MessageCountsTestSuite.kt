package com.pubnub.api.suite.history.counts

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.SUB
import org.junit.jupiter.api.Assertions.assertEquals

class MessageCountsTestSuite : EndpointTestSuite<MessageCounts, PNMessageCountResult>() {

    override fun telemetryParamName() = "l_mc"

    override fun pnOperation() = PNOperationType.PNMessageCountOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): MessageCounts {
        return pubnub.messageCounts().apply {
            channels = listOf("ch1")
            channelsTimetoken = listOf(1588284000000)
        }
    }

    override fun verifyResultExpectations(result: PNMessageCountResult) {
        assertEquals(1, result.channels.keys.size)
        assertEquals(5L, result.channels["ch1"])
    }

    override fun successfulResponseBody() = """
        {
         "status": 200,
         "error": false,
         "error_message": "",
         "channels": {
          "ch1": 5
         }
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/ch1"))
            .withQueryParam("timetoken", equalTo("1588284000000"))
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

}