package com.pubnub.test.integration

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MessageActionsTest : BaseIntegrationTest() {
    private val channel = randomString()

    @Test
    @Ignore // TODO flaky
    fun add_get_remove_MessageAction() = runTest {
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

        val actions = pubnub.getMessageActions(
            channel,
            PNBoundedPage(
                start = actionResult.actionTimetoken!! + 1,
                end = actionResult.actionTimetoken!!
            )
        ).await()

        assertTrue { actions.actions.isNotEmpty() }
        val foundAction = actions.actions.single { it.actionTimetoken == actionResult.actionTimetoken }
        assertEquals(expectedActionValue, foundAction.value)
        assertEquals(actionResult.messageTimetoken, foundAction.messageTimetoken)
        assertEquals(expectedAction, foundAction.type)
        assertEquals(config.userId.value, foundAction.uuid)

        pubnub.removeMessageAction(channel, foundAction.messageTimetoken, foundAction.actionTimetoken!!).await()

        val actionsAfterDelete = pubnub.getMessageActions(
            channel,
            PNBoundedPage(
                start = actionResult.actionTimetoken!! + 1,
                end = actionResult.actionTimetoken!!
            )
        ).await()

        assertEquals(emptyList(), actionsAfterDelete.actions)
    }
}
