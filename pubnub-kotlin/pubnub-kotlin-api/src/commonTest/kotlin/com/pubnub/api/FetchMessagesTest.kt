package com.pubnub.api

import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FetchMessagesTest : BaseIntegrationTest() {
    private val channel = randomString()

    @Test
    fun fetchMessages() = runTest {
        val expectedMeta = mapOf(randomString() to randomString())
        val expectedMessage = randomString()
        val expectedAction = randomString()
        val expectedActionValue = randomString()

        val result = pubnub.publish(
            channel = channel,
            message = expectedMessage,
            meta = expectedMeta,
            shouldStore = true,
            ttl = 60,
        ).await()

        val actionResult = pubnub.addMessageAction(
            channel = channel,
            messageAction = PNMessageAction(
                type = expectedAction,
                value = expectedActionValue,
                messageTimetoken = result.timetoken,
            ),
        ).await()

        val fetchResult = pubnub.fetchMessages(
            includeMeta = true,
            includeMessageActions = true,
            includeMessageType = true,
            includeUUID = true,
            channels = listOf(channel),
        ).await()
        assertTrue { fetchResult.channels.isNotEmpty() }


        val expectedItem = PNFetchMessageItem(
            uuid = pubnub.configuration.userId.value,
            message = createJsonElement(expectedMessage),
            timetoken = result.timetoken,
            meta = createJsonElement(expectedMeta),
            messageType = HistoryMessageType.Message,
            actions = mapOf(
                expectedAction to mapOf(
                    expectedActionValue to listOf(
                        PNFetchMessageItem.Action(
                            actionTimetoken = actionResult.actionTimetoken.toString(),
                            uuid = pubnub.configuration.userId.value,
                        ),
                    ),
                ),
            ),
        )

        val expectedChannelsResponse: Map<String, List<PNFetchMessageItem>> = mapOf(
            channel to listOf(
                expectedItem,
            ),
        )

        assertEquals(expectedChannelsResponse, fetchResult.channels)

        val fetchResultWithoutActions = pubnub.fetchMessages(
            includeMeta = true,
            includeMessageActions = false,
            includeMessageType = true,
            includeUUID = true,
            channels = listOf(channel),
        ).await()

        assertEquals(
            mapOf(
                channel to listOf(
                    expectedItem.copy(actions = null),
                ),
            ),
            fetchResultWithoutActions.channels,
        )
    }

}