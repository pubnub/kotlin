@file:Suppress("ktlint")

package com.pubnub.docs.messageActions

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.MessagingKit
import com.pubnub.docs.support.MessagingKit.clearActions
import com.pubnub.docs.support.MessagingKit.publishStored
import com.pubnub.docs.support.MessagingKit.resetHistory
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page message-actions (docs/sdks/kotlin/api-reference/message-actions.md). One test per snippet.
 * The text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions
 * below it. An action needs a stored message, so the tests publish one first.
 */
@ExtendWith(LiveExtension::class)
class MessageActionsDocsTest {
    @Test
    fun getMessageActionsWithPaging(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "my_channel")
        clearActions(reader, "my_channel")
        val out = Stdout.captureForTest()

        com.pubnub.docs.messageActions.samples.paging.main()

        Wait.eventually("the sample printed both pages") {
            out.assertPrinted("Fetched 1 message actions:")
            out.assertPrinted("Fetched 0 message actions:")
        }
        out.assertPrinted("Action Type: someother")
        out.assertPrinted("Action Value: smiley")
        out.assertPrinted("UUID: message-action-demo-user")
        assertTrue(out.lines("Error fetching message actions").isEmpty(), "the sample reported an error")
        // the printed message timetoken is the one of the message that the sample published
        val printed = out.lines("Message Timetoken: ").single().substringAfter("Message Timetoken: ").trim().toLong()
        val stored = storedItem(reader, "my_channel", printed)
        assertEquals("Hello, world!", stored.message.asString)
        assertEquals(1, MessagingKit.actionCount(reader, "my_channel"))
    }

    @Test
    fun messageReactionsMain(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "demo-channel")
        clearActions(reader, "demo-channel")
        val out = Stdout.captureForTest()

        com.pubnub.docs.messageActions.samples.reactions.main()

        // Docs finding: the sample's message timetoken (2019) names no message of the keyset, and the service accepts the action
        out.assertPrinted("No existing message actions found.")
        out.assertPrinted("Message action added successfully!")
        out.assertPrinted("  • Type: reaction")
        out.assertPrinted("  • Value: smiley_face")
        out.assertPrinted("  • Publisher UUID: message-action-demo-user")
        out.assertPrinted("  • Message Timetoken: 15701761818730000")
        assertTrue(out.lines("Error").isEmpty(), "the sample reported an error")
        val actionTimetoken = out.lines("  • Action Timetoken: ").single().substringAfter(": ").trim().toLong()
        Wait.settled("the service lists the new action") {
            val actions = reader.getMessageActions("demo-channel").sync().actions
            assertEquals(listOf(actionTimetoken), actions.map { it.actionTimetoken })
            assertEquals("message-action-demo-user", actions.single().uuid)
        }
    }

    @Test
    fun getMessageActionsBasic(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "my_channel")
        clearActions(reader, "my_channel")
        val published = publishStored(reader, "my_channel", "Message with a reaction")
        val added = reader.addMessageAction("my_channel", PNMessageAction("reaction", "smiley_face", published.timetoken)).sync()
        Wait.settled("the service lists the action") {
            assertEquals(listOf(added.actionTimetoken), reader.getMessageActions("my_channel").sync().actions.map { it.actionTimetoken })
        }

        // snippet.getMessageActionsBasic
        pubnub.getMessageActions(
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val listed = recorder.awaitOk("getMessageActions") as PNGetMessageActionsResult
        val action = listed.actions.single()
        assertEquals("reaction", action.type)
        assertEquals("smiley_face", action.value)
        assertEquals("docs-receiver", action.uuid)
        assertEquals(published.timetoken, action.messageTimetoken)
        assertEquals(added.actionTimetoken, action.actionTimetoken)
    }

    @Test
    fun removeMessageActionBasic(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        clearActions(reader, "my_channel")

        // snippet.removeMessageActionBasic
        pubnub.removeMessageAction(
            channel = "my_channel",
            messageTimetoken = 15701761818730000L,
            actionTimetoken = 15701775691010000L
        ).async { result: Result<PNRemoveMessageActionResult> ->
            result.onSuccess { res: PNRemoveMessageActionResult ->
                // result has no actionable data
                // it's enough to check if the status itself is not an error
            }.onFailure { e: PubNubException ->
                // do something with the exception
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        // Docs finding: the two timetokens of the sample name no action, so the service answers 400 "No matching message actions
        // to delete". A control with the real timetokens of an action shows that the same call removes it.
        val error = recorder.awaitFailure("removeMessageAction") as PubNubException
        assertEquals(400, error.statusCode)
        assertTrue(error.message!!.contains("No matching message actions to delete"), "the service refused for another reason")
        resetHistory(reader, "my_channel")
        val published = publishStored(reader, "my_channel", "Message with a reaction")
        val added = reader.addMessageAction("my_channel", PNMessageAction("reaction", "smiley_face", published.timetoken)).sync()
        Wait.settled("the service lists the action") { assertEquals(1, MessagingKit.actionCount(reader, "my_channel")) }
        reader.removeMessageAction("my_channel", published.timetoken, added.actionTimetoken!!).sync()
        Wait.settled("the removed action is no longer listed") { assertEquals(0, MessagingKit.actionCount(reader, "my_channel")) }
    }

    private companion object {
        const val OPEN = "open"
    }
}
