package com.pubnub.api.suite.push.list

import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.internal.endpoints.push.ListPushProvisions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.OptionalScenario
import com.pubnub.api.suite.Result
import com.pubnub.api.suite.SUB
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

class ListPushProvisionsV1TestSuite : EndpointTestSuite<ListPushProvisions, PNPushListProvisionsResult>() {

    override fun telemetryParamName() = "l_push"

    override fun pnOperation() = PNOperationType.PNPushNotificationEnabledChannelsOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): ListPushProvisions {
        return pubnub.auditPushChannelProvisions(
            pushType = PNPushType.FCM,
            deviceId = "12345"
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
        get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/12345"))
            .withQueryParam("type", equalTo("gcm"))
            .withQueryParam("environment", absent())
            .withQueryParam("topic", absent())

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()

    override fun optionalScenarioList(): List<OptionalScenario<PNPushListProvisionsResult>> {
        return listOf(
            OptionalScenario<PNPushListProvisionsResult>().apply {
                responseBuilder = { withBody("[]") }
                result = Result.SUCCESS
                additionalChecks = { status: PNStatus, result: PNPushListProvisionsResult? ->
                    assertFalse(status.error)
                    assertEquals(0, result!!.channels.size)
                }
            }
        )
    }
}
