package com.pubnub.api.endpoints.presence

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.BaseTest
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetStateEndpointTest : BaseTest() {

    @Test
    fun testOneChannelSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
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

        val result = pubnub.getState().apply {
            channels = listOf("testChannel")
            uuid = "sampleUUID"
        }.sync()!!

        val ch1Data = result.stateByUUID["testChannel"]
        assertEquals(pubnub.mapper.elementToInt(ch1Data!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(ch1Data, "status"), "online")
    }

    @Test
    fun testOneChannelWithoutUUIDSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID"))
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

        val result = pubnub.getState().apply {
            channels = listOf("testChannel")
        }.sync()!!

        val ch1Data = result.stateByUUID["testChannel"]
        assertEquals(pubnub.mapper.elementToInt(ch1Data!!, "age"), 20)
        assertEquals(pubnub.mapper.elementToString(ch1Data, "status"), "online")
    }

    @Test
    fun testFailedPayloadSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(
                    aResponse().withBody(
                        "{ \"status\": 200, \"message\": \"OK\", \"payload\": \"age\" : 20, " +
                                "\"status\" : \"online\"}, \"service\": \"Presence\"}"
                    )
                )
        )

        try {
            pubnub.getState().apply {
                channels = listOf("testChannel")
                uuid = "sampleUUID"
            }.sync()
        } catch (e: Exception) {
            e as PubNubException
            assertEquals(PubNubError.PARSING_ERROR, e.pubnubError)
            println("e.errorMessage ${e.errorMessage}" )
        }

    }
}