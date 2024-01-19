package com.pubnub.api.integration

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pubnub.api.CommonUtils
import com.pubnub.api.CommonUtils.emoji
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.history.PNHistoryItemResult
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import org.awaitility.kotlin.await
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.aMapWithSize
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.Duration

class HistoryIntegrationTest : BaseIntegrationTest() {

    @Test
    fun historySingleScenario() {
        val channel = randomChannel()
        val expectedMeta = JsonObject().also { it.add("thisIsMeta", JsonPrimitive("thisIsMetaValue")) }
        val expectedMessage = CommonUtils.generatePayload()

        val result = pubnub.publish(
            channel = channel,
            message = expectedMessage,
            meta = expectedMeta,
            shouldStore = true,
            ttl = 60
        ).sync()!!

        var historyResult: PNHistoryResult? = null

        await
            .pollInterval(Duration.ofMillis(1_000))
            .pollDelay(Duration.ofMillis(1_000))
            .atMost(Duration.ofMillis(10_000))
            .untilAsserted {
                historyResult = pubnub.history(
                    channel = channel,
                    includeMeta = true,
                    includeTimetoken = true,
                ).sync()

                assertNotNull(historyResult)
                assertThat(historyResult?.messages, hasSize(not(0)))
            }

        val expectedHistoryResultChannels =
            listOf(PNHistoryItemResult(entry = expectedMessage, timetoken = result.timetoken, meta = expectedMeta))

        assertEquals(expectedHistoryResultChannels, historyResult?.messages)
    }

    @Test
    fun `when reading unencrypted message from history using pubNub with configured encryption should log error and return error in response and return unencrypted message`() {
        val pnConfigurationWithCrypto = getBasicPnConfiguration()
        val cipherKey = "enigma"
        pnConfigurationWithCrypto.cryptoModule =
            CryptoModule.createAesCbcCryptoModule(cipherKey = cipherKey, randomIv = false)
        val pubNubWithCrypto = PubNub(pnConfigurationWithCrypto)

        val channel = randomChannel()
        val expectedMeta = JsonObject().also { it.add("thisIsMeta", JsonPrimitive("thisIsMetaValue")) }
        val expectedMessage = "this is not encrypted message"

        val result = pubnub.publish(
            channel = channel,
            message = expectedMessage,
            meta = expectedMeta,
            shouldStore = true,
            ttl = 60
        ).sync()!!

        var historyResult: PNHistoryResult? = null

        await
            .pollInterval(Duration.ofMillis(1_000))
            .pollDelay(Duration.ofMillis(1_000))
            .atMost(Duration.ofMillis(10_000))
            .untilAsserted {
                historyResult = pubNubWithCrypto.history(
                    channel = channel,
                    includeMeta = true,
                    includeTimetoken = true,
                ).sync()

                assertNotNull(historyResult)
                assertThat(historyResult?.messages, hasSize(not(0)))
            }

        val expectedHistoryResultChannels =
            listOf(
                PNHistoryItemResult(
                    entry = JsonPrimitive(expectedMessage),
                    timetoken = result.timetoken,
                    meta = expectedMeta,
                    error = PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED
                )
            )

        assertEquals(expectedHistoryResultChannels, historyResult?.messages)
    }

    @Test
    fun fetchMessagesSingleScenario() {
        val channel = randomChannel()
        val expectedMeta = JsonObject().also { it.add("thisIsMeta", JsonPrimitive("thisIsMetaValue")) }
        val expectedMessage = CommonUtils.generatePayload()
        val expectedAction = "reaction"
        val expectedActionValue = emoji()

        val result = pubnub.publish(
            channel = channel,
            message = expectedMessage,
            meta = expectedMeta,
            shouldStore = true,
            ttl = 60
        ).sync()!!

        val actionResult = pubnub.addMessageAction(
            channel = channel,
            messageAction = PNMessageAction(
                type = expectedAction,
                value = expectedActionValue,
                messageTimetoken = result.timetoken
            )
        ).sync()!!

        var fetchResult: PNFetchMessagesResult? = null

        await
            .pollInterval(Duration.ofMillis(1_000))
            .pollDelay(Duration.ofMillis(1_000))
            .atMost(Duration.ofMillis(10_000))
            .untilAsserted {
                fetchResult = pubnub.fetchMessages(
                    includeMeta = true,
                    includeMessageActions = true,
                    includeMessageType = true,
                    includeUUID = true,
                    channels = listOf(channel)
                ).sync()
                assertNotNull(fetchResult)
                assertThat(fetchResult?.channels, aMapWithSize(not(0)))
            }

        val expectedItem = PNFetchMessageItem(
            uuid = pubnub.configuration.userId.value,
            message = expectedMessage,
            timetoken = result.timetoken,
            meta = expectedMeta,
            messageType = HistoryMessageType.Message,
            actions = mapOf(
                expectedAction to mapOf(
                    expectedActionValue to listOf(
                        PNFetchMessageItem.Action(
                            actionTimetoken = actionResult.actionTimetoken.toString(),
                            uuid = pubnub.configuration.userId.value
                        )
                    )
                )
            ),
        )

        val expectedChannelsResponse: Map<String, List<PNFetchMessageItem>> =
            mapOf(
                channel to listOf(
                    expectedItem
                )
            )

        val fetchMessageItem = fetchResult!!
        assertEquals(expectedChannelsResponse, fetchMessageItem.channels)

        val fetchResultWithoutActions = pubnub.fetchMessages(
            includeMeta = true,
            includeMessageActions = false,
            includeMessageType = true,
            includeUUID = true,
            channels = listOf(channel)
        ).sync()!!

        assertEquals(
            mapOf(
                channel to listOf(
                    expectedItem.copy(actions = null)
                )
            ),
            fetchResultWithoutActions.channels
        )
    }

    @Test
    fun `when fetching unencrypted message using pubNub with configured encryption should log error and return error in respone and return unencrypted message`() {
        val pnConfigurationWithCrypto = getBasicPnConfiguration()
        val cipherKey = "enigma"
        pnConfigurationWithCrypto.cryptoModule =
            CryptoModule.createLegacyCryptoModule(cipherKey = cipherKey, randomIv = true)
        val pubNubWithCrypto = PubNub(pnConfigurationWithCrypto)

        val channel = randomChannel()
        val expectedMeta = JsonObject().also { it.add("thisIsMeta", JsonPrimitive("thisIsMetaValue")) }
        val expectedMessage = CommonUtils.generatePayload()

        val result = pubnub.publish(
            channel = channel,
            message = expectedMessage,
            meta = expectedMeta,
            shouldStore = true,
            ttl = 60
        ).sync()!!

        var fetchResult: PNFetchMessagesResult? = null

        await
            .pollInterval(Duration.ofMillis(1_000))
            .pollDelay(Duration.ofMillis(1_000))
            .atMost(Duration.ofMillis(10_000))
            .untilAsserted {
                fetchResult = pubNubWithCrypto.fetchMessages(
                    includeMeta = true,
                    includeMessageActions = true,
                    includeMessageType = true,
                    includeUUID = true,
                    channels = listOf(channel)
                ).sync()
                assertNotNull(fetchResult)
                assertThat(fetchResult?.channels, aMapWithSize(not(0)))
            }

        val expectedItem = PNFetchMessageItem(
            uuid = pubnub.configuration.userId.value,
            message = expectedMessage,
            timetoken = result.timetoken,
            meta = expectedMeta,
            messageType = HistoryMessageType.Message,
            actions = emptyMap<String, Map<String, List<PNFetchMessageItem.Action>>>(),
            error = PubNubError.CRYPTO_IS_CONFIGURED_BUT_MESSAGE_IS_NOT_ENCRYPTED
        )

        val expectedChannelsResponse: Map<String, List<PNFetchMessageItem>> =
            mapOf(
                channel to listOf(
                    expectedItem
                )
            )

        val fetchMessageItem = fetchResult!!
        assertEquals(expectedChannelsResponse, fetchMessageItem.channels)
    }

    @Test
    fun always() {
        assertEquals(
            JsonObject().apply {
                add("a", JsonPrimitive("v"))
                add("b", JsonPrimitive("v"))
            },
            JsonObject().apply {
                add("b", JsonPrimitive("v"))
                add("a", JsonPrimitive("v"))
            }
        )
    }
}
