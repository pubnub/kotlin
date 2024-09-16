package com.pubnub.internal.suite.history

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

class DeleteMessagesTestSuite : com.pubnub.internal.suite.CoreEndpointTestSuite<DeleteMessages, PNDeleteMessagesResult>() {
    override fun pnOperation() = PNOperationType.PNDeleteMessagesOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): DeleteMessages =
        pubnub.deleteMessages(
            channels = listOf("ch1"),
        )

    override fun verifyResultExpectations(result: PNDeleteMessagesResult) {
    }

    override fun successfulResponseBody() =
        """
        {
         "status": 200,
         "error": false,
         "error_message": ""
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return delete(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/ch1"))
            .withQueryParam("start", absent())
            .withQueryParam("end", absent())
    }

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()

    override fun voidResponse() = true
}
