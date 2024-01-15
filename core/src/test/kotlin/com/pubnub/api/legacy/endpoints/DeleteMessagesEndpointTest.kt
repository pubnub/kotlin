package com.pubnub.api.legacy.endpoints

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.CommonUtils.failTest
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import org.awaitility.Awaitility
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
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

        pubnub.deleteMessages(
            channels = listOf("mychannel,my_channel")
        ).sync()!!
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

        pubnub.deleteMessages(
            channels = listOf("mychannel,my_channel")
        ).sync()!!

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
            pubnub.deleteMessages(
                channels = listOf("mychannel,my_channel")
            ).sync()!!
        } catch (e: Exception) {
            failTest()
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

        pubnub.deleteMessages(
            channels = listOf("mychannel,my_channel")
        ).async { result, status ->
            result!!
            assertFalse(status.error)
            assertEquals(PNOperationType.PNDeleteMessagesOperation, status.operation)
            success.set(true)
        }

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(success)
    }
}
