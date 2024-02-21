package com.pubnub.api.legacy.endpoints.history

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.internal.endpoints.FetchMessages
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random.Default.nextInt

class FetchMessagesEndpointTest : BaseTest() {

    companion object {
        private const val MAX_FOR_FETCH_MESSAGES = 100
        private const val MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS = 25
        private const val MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES = 25
        private const val EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES = 100
        private const val EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES = 25
        private const val EXPECTED_DEFAULT_MESSAGES_WITH_ACTIONS = 25
        private const val EXPECTED_MAX_MESSAGES_WITH_ACTIONS = 25
        private const val DEFAULT_INCLUDE_MESSAGE_ACTIONS = false
    }

    val channel = "mychannel"

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel")).willReturn(
                aResponse().withBody(
                    """
                            {
                              "status": 200,
                              "error": false,
                              "error_message": "",
                              "channels": {
                                "my_channel": [
                                  {
                                    "message": "hihi",
                                    "uuid": "my-uuid",
                                    "timetoken": "14698320467224036"
                                  },
                                  {
                                    "message": "Hey",
                                    "uuid": "my-uuid",
                                    "timetoken": "14698320468265639"
                                  }
                                ],
                                "mychannel": [
                                  {
                                    "message": "sample message",
                                    "uuid": "my-uuid",
                                    "timetoken": "14369823849575729"
                                  }
                                ]
                              }
                            }
                    """.trimIndent()
                )
            )
        )

        val response = pubnub.fetchMessages(
            channels = listOf("mychannel", "my_channel"),
            page = PNBoundedPage(
                limit = 25
            )
        ).sync()

        assertEquals(response.channels.size, 2)

        assertTrue(response.channels.containsKey("mychannel"))
        assertTrue(response.channels.containsKey("my_channel"))

        assertEquals(response.channels["mychannel"]!!.size, 1)
        assertEquals(response.channels["my_channel"]!!.size, 2)

        assertEquals(response.channels["mychannel"]!!.count { it.uuid == "my-uuid" }, 1)
        assertEquals(response.channels["my_channel"]!!.count { it.uuid == "my-uuid" }, 2)
    }

    @Test
    fun testSyncWithoutUUIDSuccess() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel")).willReturn(
                aResponse().withBody(
                    """
                            {
                              "status": 200,
                              "error": false,
                              "error_message": "",
                              "channels": {
                                "my_channel": [
                                  {
                                    "message": "hihi",
                                    "timetoken": "14698320467224036"
                                  },
                                  {
                                    "message": "Hey",
                                    "timetoken": "14698320468265639"
                                  }
                                ],
                                "mychannel": [
                                  {
                                    "message": "sample message",
                                    "timetoken": "14369823849575729"
                                  }
                                ]
                              }
                            }
                    """.trimIndent()
                )
            )
        )

        val response = pubnub.fetchMessages(
            channels = listOf("mychannel", "my_channel"),
            page = PNBoundedPage(
                limit = 25
            ),
            includeUUID = false
        ).sync()

        assertEquals(response.channels.size, 2)
        assertTrue(response.channels.containsKey("mychannel"))
        assertTrue(response.channels.containsKey("my_channel"))

        assertEquals(response.channels["mychannel"]!!.size, 1)
        assertEquals(response.channels["my_channel"]!!.size, 2)

        assertEquals(response.channels["mychannel"]!!.count { it.uuid == null }, 1)
        assertEquals(response.channels["my_channel"]!!.count { it.uuid == null }, 2)
    }

    @Test
    fun testSyncAuthSuccess() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel")).willReturn(
                aResponse().withBody(
                    """
                            {
                              "status": 200,
                              "error": false,
                              "error_message": "",
                              "channels": {
                                "my_channel": [
                                  {
                                    "message": "hihi",
                                    "timetoken": "14698320467224036"
                                  },
                                  {
                                    "message": "Hey",
                                    "timetoken": "14698320468265639"
                                  }
                                ],
                                "mychannel": [
                                  {
                                    "message": "sample message",
                                    "timetoken": "14369823849575729"
                                  }
                                ]
                              }
                            }
                    """.trimIndent()
                )
            )
        )

        pubnub.configuration.authKey = "authKey"

        val response = pubnub.fetchMessages(
            channels = listOf("mychannel", "my_channel"),
            page = PNBoundedPage(
                limit = 25
            )
        ).sync()

        assertEquals(response.channels.size, 2)
        assertTrue(response.channels.containsKey("mychannel"))
        assertTrue(response.channels.containsKey("my_channel"))
        assertEquals(response.channels["mychannel"]!!.size, 1)
        assertEquals(response.channels["my_channel"]!!.size, 2)
        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals("authKey", requests[0].queryParameter("auth").firstValue())
        assertEquals(1, requests.size)
    }

    @Test
    fun testSyncEncryptedSuccess() {
        pubnub.configuration.cipherKey = "testCipher"
        pubnub.configuration.useRandomInitializationVector = false

        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel")).willReturn(
                aResponse().withBody(
                    """
                            {
                              "status": 200,
                              "error": false,
                              "error_message": "",
                              "channels": {
                                "my_channel": [
                                  {
                                    "message": "jC/yJ2y99BeYFYMQ7c53pg==",
                                    "timetoken": "14797423056306675"
                                  }
                                ],
                                "mychannel": [
                                  {
                                    "message": "jC/yJ2y99BeYFYMQ7c53pg==",
                                    "timetoken": "14797423056306675"
                                  }
                                ]
                              }
                            }
                    """.trimIndent()
                )
            )
        )

        val response = pubnub.fetchMessages(
            channels = listOf("mychannel", "my_channel"),
            page = PNBoundedPage(
                limit = 25
            )
        ).sync()

        assertEquals(response.channels.size, 2)
        assertTrue(response.channels.containsKey("mychannel"))
        assertTrue(response.channels.containsKey("my_channel"))
        assertEquals(response.channels["mychannel"]!!.size, 1)
        assertEquals(response.channels["my_channel"]!!.size, 1)
    }

    @Test
    fun forSingleChannelFetchMessagesAlwaysPassMaxWhenItIsInBound() {
        // given
        val maximumPerChannel = randomInt(MAX_FOR_FETCH_MESSAGES)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(maximumPerChannel))
    }

    @Test
    fun forSingleChannelFetchMessagesAlwaysPassDefaultWhenNonPositiveMaxSpecified() {
        // given
        val maximumPerChannel = -(randomInt(100) - 1)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES))
    }

    @Test
    fun forSingleChannelFetchMessagesAlwaysPassDefaultWhenMaxNotSpecified() {
        // given
        val maximumPerChannel = null

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES))
    }

    @Test
    fun forSingleChannelFetchMessagesAlwaysPassDefaultMaxWhenMaxExceeds() {
        // given
        val maximumPerChannel = MAX_FOR_FETCH_MESSAGES + randomInt(MAX_FOR_FETCH_MESSAGES)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES))
    }

    @Test
    fun forMultipleChannelsFetchMessagesAlwaysPassMaxWhenItIsInBound() {
        // given
        val maximumPerChannel = randomInt(MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 2
        )

        // then
        assertThat(result, equalTo(maximumPerChannel))
    }

    @Test
    fun forMultipleChannelsFetchMessagesAlwaysPassDefaultWhenNonPositiveMaxSpecified() {
        // given
        val maximumPerChannel = -(randomInt(100) - 1)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 2
        )

        // then
        assertThat(result, equalTo(EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES))
    }

    @Test
    fun forMultipleChannelsFetchMessagesAlwaysPassDefaultWhenMaxNotSpecified() {
        // given
        val maximumPerChannel = null

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 2
        )

        // then
        assertThat(result, equalTo(EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES))
    }

    @Test
    fun forMultipleChannelsFetchMessagesAlwaysPassDefaultMaxWhenMaxExceeds() {
        // given
        val maximumPerChannel =
            MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES + randomInt(MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel,
            includeMessageActions = DEFAULT_INCLUDE_MESSAGE_ACTIONS,
            numberOfChannels = 2
        )

        // then
        assertThat(result, equalTo(EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES))
    }

    @Test
    fun forSingleChannelFetchMessagesWithActionAlwaysPassMaxWhenItIsInBound() {
        // given
        val maximumPerChannel = randomInt(MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel, includeMessageActions = true, numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(maximumPerChannel))
    }

    @Test
    fun forSingleChannelFetchMessagesWithActionAlwaysPassDefaultWhenMaxNotSpecified() {
        // given
        val maximumPerChannel = null

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel, includeMessageActions = true, numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(EXPECTED_DEFAULT_MESSAGES_WITH_ACTIONS))
    }

    @Test
    fun forSingleChannelFetchMessagesWithActionAlwaysPassDefaultMaxWhenMaxExceeds() {
        // given
        val maximumPerChannel = MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS + randomInt(MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS)

        // when
        val result = FetchMessages.effectiveMax(
            maximumPerChannel = maximumPerChannel, includeMessageActions = true, numberOfChannels = 1
        )

        // then
        assertThat(result, equalTo(EXPECTED_MAX_MESSAGES_WITH_ACTIONS))
    }

    @Test
    fun testIncludeMessageTypeQueryParamIsPassedInFetchMessages() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/$channel")).willReturn(
                aResponse().withBody(
                    responseWithOneMessageForChannel(channel)
                )
            )
        )

        pubnub.fetchMessages(
            channels = listOf(channel), includeMessageType = true
        ).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertThat(
            requests[0].queryParams.map { (k, v) -> k to v.firstValue() }.toMap(),
            allOf(
                hasEntry(FetchMessages.INCLUDE_MESSAGE_TYPE_QUERY_PARAM, "true")
            )
        )
    }

    @Test
    fun testMissingIncludeMessageTypeQueryParamIsSetToTrue() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/$channel")).willReturn(
                aResponse().withBody(
                    responseWithOneMessageForChannel(channel)
                )
            )
        )

        pubnub.fetchMessages(
            channels = listOf(channel)
        ).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertThat(
            requests[0].queryParams.map { (k, v) -> k to v.firstValue() }.toMap(),
            allOf(
                hasEntry(FetchMessages.INCLUDE_MESSAGE_TYPE_QUERY_PARAM, "true"),
            )
        )
    }

    @Test
    fun testMessageTypesAreProperlyDeserialized() {
        stubFor(
            get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/$channel")).willReturn(
                aResponse().withBody(
                    responseWithMessagesForChannelWithMessageType(channel)
                )
            )
        )

        val response = pubnub.fetchMessages(
            channels = listOf(channel), includeMessageType = true
        ).sync()

        assertThat(
            response.channels.values.flatMap { items -> items.map { it.messageType } },
            contains(HistoryMessageType.Message, HistoryMessageType.File)
        )
    }

    private fun randomInt(max: Int): Int = nextInt(max) + 1

    private fun responseWithOneMessageForChannel(channel: String): String {
        return """{
          "status": 200,
          "error": false,
          "error_message": "",
          "channels": {
            "$channel": [
              {
                "message": "thisIsMessage",
                "timetoken": "14797423056306675"
              }
            ]
          }
        }
        """.trimIndent()
    }

    private fun responseWithMessagesForChannelWithMessageType(channel: String): String {
        return """{
          "status": 200,
          "error": false,
          "error_message": "",
          "channels": {
            "$channel": [
              {
                "message": "thisIsMessage1",
                "timetoken": "14797423056306675",
                "message_type": 0
              },
              {
                "message": "thisIsMessage2",
                "timetoken": "14797423056306676",
                "message_type": 4
              }
            ]
          }
        }
        """.trimIndent()
    }
}
