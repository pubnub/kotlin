package com.pubnub.internal.suite.message_actions

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import org.junit.Assert.assertEquals

class GetMessageActionsTestSuite :
    com.pubnub.internal.suite.CoreEndpointTestSuite<GetMessageActions, PNGetMessageActionsResult>() {
    override fun pnOperation() = PNOperationType.PNGetMessageActions

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): GetMessageActions =
        pubnub.getMessageActions(
            channel = "ch1",
        )

    override fun successfulResponseBody() =
        """
        {
          "status": 200,
          "data": [
            {
              "messageTimetoken": "100",
              "type": "reaction",
              "uuid": "abc",
              "value": "smiley",
              "actionTimetoken": "200"
            }
          ]
        }
        """.trimIndent()

    override fun unsuccessfulResponseBodyList() =
        listOf(
            """{"status":200,"data":null}""",
            """{"status":200}""",
        )

    override fun verifyResultExpectations(result: PNGetMessageActionsResult) {
        assertEquals(1, result.actions.size)
        result.actions[0].apply {
            assertEquals(100L, messageTimetoken)
            assertEquals("reaction", type)
            assertEquals("abc", uuid)
            assertEquals("smiley", value)
            assertEquals(200L, actionTimetoken)
        }
    }

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/ch1"))
            .withQueryParam("start", absent())
            .withQueryParam("end", absent())
            .withQueryParam("limit", absent())

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()
}
