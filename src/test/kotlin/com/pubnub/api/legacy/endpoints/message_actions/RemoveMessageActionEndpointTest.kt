package com.pubnub.api.legacy.endpoints.message_actions

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

class RemoveMessageActionEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.removeMessageAction().apply {
            channel = "coolChannel"
            messageTimetoken = 123L
            actionTimetoken = 100L
        }.sync()!!
    }

    @Test
    fun testAsyncSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        val success = AtomicBoolean()

        pubnub.removeMessageAction().apply {
            channel = "coolChannel"
            messageTimetoken = 123L
            actionTimetoken = 100L
        }.async { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNAddMessageAction, status.operation)
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testMalformedJson() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 123L
                actionTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }


    @Test
    fun testEmptyBody() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 123L
                actionTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNoBody() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(noContent())
        )

        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 123L
                actionTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNoData() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
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
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 123L
                actionTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNullData() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": null
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 123L
                actionTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNoChannel() {
        try {
            pubnub.removeMessageAction().apply {

            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testBlankChannel() {
        try {
            pubnub.removeMessageAction().apply {
                channel = " "
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testNoMessageTimetoken() {
        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_TIMETOKEN_MISSING, e)
        }
    }

    @Test
    fun testNoActionTimetoken() {
        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_ACTION_TIMETOKEN_MISSING, e)
        }
    }

    @Test
    fun testInvalidSubKey() {
        pubnub.configuration.subscribeKey = " "
        try {
            pubnub.removeMessageAction().apply {
                channel = "coolChannel"
                messageTimetoken = 123L
                actionTimetoken = 100L
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testAuthKeyRequired() {
        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "authKey"

        pubnub.removeMessageAction().apply {
            channel = "coolChannel"
            messageTimetoken = 123L
            actionTimetoken = 100L
        }.sync()!!

        val requests = findAll(deleteRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        assertEquals("authKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            delete(urlPathEqualTo("/v1/message-actions/mySubscribeKey/channel/coolChannel/message/123/action/100"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "data": {}
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

        pubnub.removeMessageAction().apply {
            channel = "coolChannel"
            messageTimetoken = 123L
            actionTimetoken = 100L
        }.async { _, status ->
            println(status)
            assertFalse(status.error)
            assertEquals(PNOperationType.PNAddMessageAction, status.operation)
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