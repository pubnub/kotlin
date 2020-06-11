package com.pubnub.api.suite.presence

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.suite.AUTH
import com.pubnub.api.suite.EndpointTestSuite
import com.pubnub.api.suite.SUB
import org.junit.jupiter.api.Assertions.assertEquals

class HereNowTestSuite : EndpointTestSuite<HereNow, PNHereNowResult>() {

    override fun telemetryParamName() = "l_pres"

    override fun pnOperation() = PNOperationType.PNHereNowOperation

    override fun requiredKeys() = SUB + AUTH

    override fun snippet(): HereNow {
        return pubnub.hereNow().apply {
            channels = listOf("ch1")
        }
    }

    override fun verifyResultExpectations(result: PNHereNowResult) {
        assertEquals(1, result.totalChannels)
        assertEquals(1, result.totalOccupancy)
        assertEquals(1, result.channels.size)
        assertEquals("user_1", result.channels["ch1"]!!.occupants[0].uuid)
    }

    override fun successfulResponseBody() = """
        {
         "status": 200,
         "message": "OK",
         "occupancy": 1,
         "uuids": [
          "user_1"
         ],
         "service": "Presence"
        }
    """.trimIndent()

    override fun unsuccessfulResponseBodyList() = listOf(
        """{"occupancy": 0, "uuids": null}""",
        """{"payload": {"channels": null, "total_channels": 0, "total_occupancy": 0}}""",
        """{"payload": {}}""",
        """{"payload": null}"""
    )

    override fun mappingBuilder() =
        get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1"))
            .withQueryParam("state", absent())
            .withQueryParam("disable_uuids", absent())
            .withQueryParam("channel-group", absent())

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

}