package com.pubnub.api.legacy.endpoints.presence

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.JsonObject
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.presence.eventengine.data.PresenceData
import org.junit.Assert.assertEquals
import org.junit.Test

class StateSetEndpointEETest : BaseTest() {
    override fun onBefore() {
        initConfiguration()
        config.enableEventEngine = true
        initPubNub()
    }

    @Test
    fun applyStateForChannelSetsPresenceData() {
        val presenceData = PresenceData()

        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("state", equalToJson("""{"age":20}"""))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "payload": {
                            "age": 20,
                            "status": "online"
                          },
                          "service": "Presence"
                        }
                        """.trimIndent()
                    )
                )
        )

        SetState(
            channels = listOf("testChannel"),
            channelGroups = listOf(),
            state = mapOf("age" to 20),
            pubnub = pubnub,
            presenceData = presenceData
        ).sync()

        assertEquals(
            mapOf(
                "testChannel" to JsonObject().apply {
                    addProperty("age", 20)
                }
            ),
            presenceData.channelStates
        )
    }
}
