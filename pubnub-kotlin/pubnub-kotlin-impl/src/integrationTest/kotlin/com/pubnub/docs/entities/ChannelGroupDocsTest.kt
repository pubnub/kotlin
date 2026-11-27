@file:Suppress("ktlint")

package com.pubnub.docs.entities

import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.createGroup
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.MessagingKit.awaitGroup
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page entities/channel-group (docs/sdks/kotlin/entities/channel-group.md). One test per
 * snippet. The text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the
 * assertions below it.
 */
@ExtendWith(LiveExtension::class)
class ChannelGroupDocsTest {
    @Test
    fun channelGroup(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        // The sample subscribes to the group at once after its own add call (it does not wait for the add), so the group is
        // created before it runs. The sample's add is then a repeat of the same channels and must still succeed.
        createGroup(reader, "demo-entity-group", "chat-marketing", "chat-engineering", "chat-support")
        awaitGroup(reader, "demo-entity-group", "chat-marketing", "chat-engineering", "chat-support")
        Cleanup.record("history", "chat-marketing")
        val out = Stdout.captureForTest()

        com.pubnub.docs.entities.samples.channelgroup.main()

        out.assertPrinted("Created ChannelGroup entity for: demo-entity-group")
        Wait.eventually("the sample printed that it added the channels") {
            out.assertPrinted("SUCCESS: Added channels to group 'demo-entity-group':")
            out.assertPrinted("- chat-marketing")
            out.assertPrinted("- chat-engineering")
            out.assertPrinted("- chat-support")
        }
        // the sample subscribed to the group, so the message to a channel of the group reached its own listener
        Wait.eventually("the listener of the group subscription received the message") {
            out.assertPrinted("Received message: Important message")
        }
        assertTrue(out.lines("ERROR").isEmpty(), "the sample reported an error")
        awaitGroup(reader, "demo-entity-group", "chat-marketing", "chat-engineering", "chat-support")
    }

    private companion object {
        const val OPEN = "open"
    }
}
