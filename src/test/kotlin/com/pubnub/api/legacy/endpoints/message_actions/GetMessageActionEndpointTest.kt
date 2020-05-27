package com.pubnub.api.legacy.endpoints.message_actions

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

class GetMessageActionEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley",
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        val result = pubnub.getMessageActions().apply {
            channel = "coolChannel"
        }.sync()!!

        assertEquals(result.actions.size, 1)
        assertEquals(result.actions[0].messageTimetoken, 123)
        assertEquals(result.actions[0].type, "emoji")
        assertEquals(result.actions[0].uuid, "someUuid")
        assertEquals(result.actions[0].value, "smiley")
        assertEquals(result.actions[0].actionTimetoken, 1000)
    }

    @Test
    fun testSyncSuccessMultipleActions() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody("""
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "uuid_1",
                              "value": "❤️",
                              "actionTimetoken": "1000"
                            },
                            {
                              "messageTimetoken": "456",
                              "type": "reaction",
                              "uuid": "uuid_2",
                              "value": "👍",
                              "actionTimetoken": "2000"
                            },
                            {
                              "messageTimetoken": "789",
                              "type": "emoji",
                              "uuid": "uuid_2",
                              "value": "😄",
                              "actionTimetoken": "3000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        val result = pubnub.getMessageActions().apply {
            channel = "coolChannel"
        }.sync()!!

        assertEquals(result.actions.size, 3)

        assertEquals(result.actions[0].messageTimetoken, 123)
        assertEquals(result.actions[0].type, "emoji")
        assertEquals(result.actions[0].uuid, "uuid_1")
        assertEquals(result.actions[0].value, "❤️")
        assertEquals(result.actions[0].actionTimetoken, 1000)

        assertEquals(result.actions[1].messageTimetoken, 456)
        assertEquals(result.actions[1].type, "reaction")
        assertEquals(result.actions[1].uuid, "uuid_2")
        assertEquals(result.actions[1].value, "👍")
        assertEquals(result.actions[1].actionTimetoken, 2000)

        assertEquals(result.actions[2].messageTimetoken, 789)
        assertEquals(result.actions[2].type, "emoji")
        assertEquals(result.actions[2].uuid, "uuid_2")
        assertEquals(result.actions[2].value, "😄")
        assertEquals(result.actions[2].actionTimetoken, 3000)
    }


    @Test
    fun testAsyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley",
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        val success = AtomicBoolean()

        pubnub.getMessageActions().apply {
            channel = "coolChannel"
        }.async { result, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNGetMessageActions, status.operation)
            assertEquals(result!!.actions.size, 1)
            assertEquals(result.actions[0].messageTimetoken, 123)
            assertEquals(result.actions[0].type, "emoji")
            assertEquals(result.actions[0].uuid, "someUuid")
            assertEquals(result.actions[0].value, "smiley")
            assertEquals(result.actions[0].actionTimetoken, 1000)
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testMalformedJson() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley"
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.getMessageActions().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testEmptyBody() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.getMessageActions().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNoBody() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(noContent())
        )

        try {
            pubnub.getMessageActions().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNoData() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.getMessageActions().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testEmptyData() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200
                          "data": []
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.getMessageActions().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }


    @Test
    fun testNoChannel() {
        try {
            pubnub.getMessageActions().apply {

            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testBlankChannel() {
        try {
            pubnub.getMessageActions().apply {
                channel = " "
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testInvalidSubKey() {
        pubnub.configuration.subscribeKey = " "
        try {
            pubnub.getMessageActions().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testAuthKeyRequired() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley",
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "authKey"

        pubnub.getMessageActions().apply {
            channel = "coolChannel"
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        assertEquals("authKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOptionalParams() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .withQueryParam("limit", matching("10"))
                .withQueryParam("start", matching("15"))
                .withQueryParam("end", matching("20"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley",
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "authKey"

        pubnub.getMessageActions().apply {
            channel = "coolChannel"
            limit = 10
            start = 15
            end = 20
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        assertEquals("10", requests[0].queryParameter("limit").firstValue())
        assertEquals("15", requests[0].queryParameter("start").firstValue())
        assertEquals("20", requests[0].queryParameter("end").firstValue())
    }

    @Test
    fun testNoOptionalParams() {
        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .withQueryParam("limit", absent())
                .withQueryParam("start", absent())
                .withQueryParam("end", absent())
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley",
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "authKey"

        pubnub.getMessageActions().apply {
            channel = "coolChannel"
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        assertFalse(requests[0].queryParameter("limit").isPresent)
        assertFalse(requests[0].queryParameter("start").isPresent)
        assertFalse(requests[0].queryParameter("end").isPresent)
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            get(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": [
                            {
                              "messageTimetoken": "123",
                              "type": "emoji",
                              "uuid": "someUuid",
                              "value": "smiley",
                              "actionTimetoken": "1000"
                            }
                          ]
                        }
                    """.trimIndent()
                    )
                )
        )

        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )

        lateinit var telemetryParamName: String

        pubnub.getMessageActions().apply {
            channel = "coolChannel"
        }.async { _, status ->
            println(status)
            assertFalse(status.error)
            assertEquals(PNOperationType.PNGetMessageActions, status.operation)
            telemetryParamName = "l_${status.operation.queryParam}"
            assertEquals("l_msga", telemetryParamName)
            success.set(true)
        }

        success.listen()

        pubnub.time().async { _, status ->
            assertFalse(status.error)
            assertNotNull(status.param(telemetryParamName))
            success.set(true)
        }

        success.listen()
    }


}