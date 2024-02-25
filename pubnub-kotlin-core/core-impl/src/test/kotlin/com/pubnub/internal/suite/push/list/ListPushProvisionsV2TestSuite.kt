package com.pubnub.internal.suite.push.list

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.endpoints.push.ListPushProvisions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

class ListPushProvisionsV2TestSuite :
    com.pubnub.internal.suite.EndpointTestSuite<ListPushProvisions, PNPushListProvisionsResult>() {

    override fun pnOperation() = PNOperationType.PNPushNotificationEnabledChannelsOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): ListPushProvisions {
        return pubnub.auditPushChannelProvisions(
            pushType = PNPushType.APNS2,
            deviceId = "12345",
            topic = "news"
        )
    }

    override fun verifyResultExpectations(result: PNPushListProvisionsResult) {
        assertEquals(2, result.channels.size)
        assertEquals("ch1", result.channels[0])
        assertEquals("ch2", result.channels[1])
    }

    override fun successfulResponseBody(): String {
        return """["ch1", "ch2"]"""
    }

    override fun unsuccessfulResponseBodyList() = listOf(
        """["ch1","ch2",{}]"""
    )

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/12345"))
            .withQueryParam("type", absent())
            .withQueryParam("environment", equalTo("development"))
            .withQueryParam("topic", equalTo("news"))

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNPushListProvisionsResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNPushListProvisionsResult>().apply {
                responseBuilder = { withBody("[]") }
                result = com.pubnub.internal.suite.Result.SUCCESS
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertEquals(0, result.getOrThrow().channels.size)
                }
            }
        )
    }
}
