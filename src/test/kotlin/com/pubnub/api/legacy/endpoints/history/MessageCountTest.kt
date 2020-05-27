package com.pubnub.api.legacy.endpoints.history

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MessageCountTest : BaseTest() {

    @Test
    fun testSyncDisabled() {
        val payload =
            """
                [
                  "Use of the history API requires the Storage & Playback which is not enabled
                   for this subscribe key.Login to your PubNub Dashboard Account and enable Storage & Playback.
                   Contact support @pubnub.com if you require further assistance.",
                  0,
                  0
                ]
            """.trimIndent()

        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(aResponse().withBody(payload))
        )

        try {
            pubnub.messageCounts().apply {
                channels = listOf("my_channel")
                channelsTimetoken = listOf(10000L)
            }.sync()
        } catch (ex: PubNubException) {
            assertEquals("History is disabled", ex.errorMessage)
        }
    }

    @Test
    @Throws(PubNubException::class)
    fun testSingleChannelWithSingleToken() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19
                             }
                            }
                        """.trimIndent()
                    )
                )
        )

        val response = pubnub.messageCounts().apply {
            channels = listOf("my_channel")
            channelsTimetoken = listOf(10000L)
        }.sync()!!

        assertEquals(response.channels.size, 1)
        assertFalse(response.channels.containsKey("channel_does_not_exist"))
        assertTrue(response.channels.containsKey("my_channel"))
        for ((key, value) in response.channels) {
            assertEquals("my_channel", key)
            assertEquals(java.lang.Long.valueOf("19"), value)
        }
    }

    @Test
    fun testSingleChannelWithMultipleTokens() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19
                             }
                            }
                        """.trimIndent()
                    )
                )
        )
        var exception: PubNubException? = null
        try {
            pubnub.messageCounts().apply {
                channels = listOf("my_channel")
                channelsTimetoken = listOf(10000L, 20000L)
            }.sync()
        } catch (e: PubNubException) {
            exception = e
        } finally {
            assertNotNull(exception)
            assertEquals(
                PubNubError.CHANNELS_TIMETOKEN_MISMATCH.message,
                exception!!.pubnubError!!.message
            )
        }
    }

    @Test
    @Throws(PubNubException::class)
    fun testMultipleChannelsWithSingleToken() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel,new_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19,
                              "new_channel": 5
                             }
                            }
                        """.trimIndent()
                    )
                )
        )

        val response = pubnub.messageCounts().apply {
            channels = listOf("my_channel", "new_channel")
            channelsTimetoken = listOf(10000L)
        }.sync()!!

        assertEquals(response.channels.size, 2)
        assertFalse(response.channels.containsKey("channel_does_not_exist"))
        assertTrue(response.channels.containsKey("my_channel"))
        assertTrue(response.channels.containsKey("new_channel"))
        for ((key, value) in response.channels) {
            if (key == "my_channel") {
                assertEquals(java.lang.Long.valueOf("19"), value)
            } else if (key == "new_channel") {
                assertEquals(java.lang.Long.valueOf("5"), value)
            }
        }
    }

    @Test
    @Throws(PubNubException::class)
    fun testMultipleChannelsWithMultipleTokens() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel,new_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19,
                              "new_channel": 5
                             }
                            }
                        """.trimIndent()
                    )
                )
        )

        val response = pubnub.messageCounts().apply {
            channels = listOf("my_channel", "new_channel")
            channelsTimetoken = listOf(10000L, 20000L)
        }.sync()!!

        assertEquals(response.channels.size, 2)
        assertFalse(response.channels.containsKey("channel_does_not_exist"))
        assertTrue(response.channels.containsKey("my_channel"))
        assertTrue(response.channels.containsKey("new_channel"))
        for ((key, value) in response.channels) {
            if (key == "my_channel") {
                assertEquals(java.lang.Long.valueOf("19"), value)
            } else if (key == "new_channel") {
                assertEquals(java.lang.Long.valueOf("5"), value)
            }
        }
    }

    @Test
    fun testWithoutTimeToken() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19
                             }
                            }
                        """.trimIndent()
                    )
                )
        )
        var exception: PubNubException? = null
        try {
            pubnub.messageCounts().apply {
                channels = listOf("my_channel")
            }.sync()
        } catch (ex: PubNubException) {
            exception = ex
        } finally {
            assertNotNull(exception)
            assertEquals(
                PubNubError.TIMETOKEN_MISSING.message,
                exception!!.pubnubError!!.message
            )
        }
    }

    @Test
    fun testWithoutChannelsSingleToken() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19,
                              "new_channel": 5
                             }
                            }
                        """.trimIndent()
                    )
                )
        )
        var exception: PubNubException? = null
        try {
            pubnub.messageCounts().apply {
                channelsTimetoken = listOf(10000L)
            }.sync()
        } catch (ex: PubNubException) {
            exception = ex
        } finally {
            assertNotNull(exception)
            assertEquals(
                PubNubError.CHANNEL_MISSING.message,
                exception!!.pubnubError!!.message
            )
        }
    }

    @Test
    fun testWithoutChannelsMultipleTokens() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                             "status": 200,
                             "error": false,
                             "error_message": "",
                             "channels": {
                              "my_channel": 19,
                              "new_channel": 5
                             }
                            }
                        """.trimIndent()
                    )
                )
        )
        var exception: PubNubException? = null
        try {
            pubnub.messageCounts().apply {
                channelsTimetoken = listOf(10000L, 20000L)
            }.sync()
        } catch (ex: PubNubException) {
            exception = ex
        } finally {
            assertNotNull(exception)
            assertEquals(
                PubNubError.CHANNEL_MISSING.message,
                exception!!.pubnubError!!.message
            )
        }
    }

    @Test
    fun testChannelWithSingleEmptyToken() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/message-counts/my_channel"))
                .willReturn(
                    aResponse().withBody("""
                        {
                         "status": 200,
                         "error": false,
                         "error_message": "",
                         "channels": {
                          "my_channel": 19
                         }
                        }
                    """.trimIndent())
                )
        )
        var exception: PubNubException? = null
        try {
            pubnub.messageCounts().apply {
                channels = listOf("my_channel")
            }.sync()
        } catch (ex: PubNubException) {
            exception = ex
        } finally {
            assertNotNull(exception)
            assertEquals(
                PubNubError.TIMETOKEN_MISSING.message,
                exception!!.pubnubError!!.message
            )
        }
    }

}