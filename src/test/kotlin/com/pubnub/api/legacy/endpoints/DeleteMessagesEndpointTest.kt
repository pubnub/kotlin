package com.pubnub.api.legacy.endpoints

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.assertPnException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.failTest
import com.pubnub.api.legacy.BaseTest
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class DeleteMessagesEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "error": false,
                          "error_message": ""
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.deleteMessages().apply {
            channels = listOf("mychannel,my_channel")
        }.sync()!!
    }

    @Test
    fun testSyncAuthSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "error": false,
                          "error_message": ""
                        }
                    """.trimIndent()
                    )
                )
        )

        pubnub.configuration.authKey = "authKey"

        pubnub.deleteMessages().apply {
            channels = listOf("mychannel,my_channel")
        }.sync()!!

        val requests = findAll(deleteRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("authKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testFailure() {
        stubFor(
            delete(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 403,
                          "error": false,
                          "error_message": "wut"
                        }
                    """.trimIndent()
                    )
                )
        )

        try {
            pubnub.deleteMessages().apply {
                channels = listOf("mychannel,my_channel")
            }.sync()!!
            failTest()
        } catch (e: Exception) {
            e.printStackTrace()
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testAsyncSuccess() {
        stubFor(
            delete(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "error": false,
                          "error_message": ""
                        }
                    """.trimIndent()
                    )
                )
        )

        val success = AtomicBoolean()

        pubnub.deleteMessages().apply {
            channels = listOf("mychannel,my_channel")
        }.async { result, status ->
            println(status)
            result!!
            assertFalse(status.error)
            assertEquals(PNOperationType.PNDeleteMessagesOperation, status.operation)
            success.set(true)
        }

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(success)
    }

    @Test
    fun testMissingChannel() {
        try {
            pubnub.deleteMessages().sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

}