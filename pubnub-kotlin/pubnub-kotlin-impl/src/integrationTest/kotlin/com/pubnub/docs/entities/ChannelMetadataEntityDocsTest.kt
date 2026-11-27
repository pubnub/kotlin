@file:Suppress("ktlint")

package com.pubnub.docs.entities

import com.pubnub.docs.support.AppContextKit
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Stdout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live test of the Kotlin docs page entities/channel-metadata (docs/sdks/kotlin/entities/channel-metadata.md). The one sample
 * is a "Main" sample that keeps its own client, so it sits in `samples/` and this test calls it and asserts after it. The name
 * of the class keeps it apart from the tests of the other entity pages.
 */
@ExtendWith(LiveExtension::class)
class ChannelMetadataEntityDocsTest {
    @Test
    fun channelMetadata(clients: LiveClients) {
        val reader = clients.kotlinClient("open", "docs-reader")
        AppContextKit.cleanupAfterTest(reader, emptyList(), listOf("team-chat"))
        AppContextKit.clearChannel(reader, "team-chat")
        // the sample publishes one message to the channel team-chat
        Cleanup.record("history", "team-chat")
        val out = Stdout.captureForTest()

        com.pubnub.docs.entities.samples.channelMetadataMain.main()

        assertTrue(out.lines("ERROR:").isEmpty(), "the sample printed an error: ${out.lines("ERROR:")}")
        out.assertPrinted("Created ChannelMetadata entity for: team-chat")
        out.assertPrinted("SUCCESS: Channel metadata set")
        out.assertPrinted("Channel ID: team-chat")
        out.assertPrinted("Team Chat")
        // the subscription of the entity delivers the message the sample publishes to the channel
        out.assertPrinted("Received message on channel metadata subscription: Some message")
        out.assertPrinted("PubNub connection closed")
        // the service stored what the sample set
        val stored = AppContextKit.awaitChannel(reader, "team-chat", "Team Chat")
        assertEquals("Internal team communication channel", stored.description?.value)
        assertEquals("private", stored.custom?.value?.get("type"))
        assertEquals("internal", stored.custom?.value?.get("category"))
    }
}
