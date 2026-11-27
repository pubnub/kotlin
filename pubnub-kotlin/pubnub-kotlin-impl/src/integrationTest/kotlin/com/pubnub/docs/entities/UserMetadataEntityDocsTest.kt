@file:Suppress("ktlint")

package com.pubnub.docs.entities

import com.pubnub.docs.support.AppContextKit
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Stdout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live test of the Kotlin docs page entities/user-metadata (docs/sdks/kotlin/entities/user-metadata.md). The one sample is a
 * "Main" sample that keeps its own client, so it sits in `samples/` and this test calls it and asserts after it. The name of
 * the class keeps it apart from the tests of the other entity pages.
 */
@ExtendWith(LiveExtension::class)
class UserMetadataEntityDocsTest {
    @Test
    fun userMetadata(clients: LiveClients) {
        val reader = clients.kotlinClient("open", "docs-reader")
        AppContextKit.cleanupAfterTest(reader, listOf("user-123"), emptyList())
        AppContextKit.clearUser(reader, "user-123")
        // the sample publishes one message to the channel user-123
        Cleanup.record("history", "user-123")
        val out = Stdout.captureForTest()

        com.pubnub.docs.entities.samples.userMetadataMain.main()

        assertTrue(out.lines("ERROR:").isEmpty(), "the sample printed an error: ${out.lines("ERROR:")}")
        out.assertPrinted("Created UserMetadata entity for: user-123")
        out.assertPrinted("SUCCESS: User metadata set")
        out.assertPrinted("UUID: user-123")
        out.assertPrinted("Jane Smith")
        out.assertPrinted("jane@example.com")
        // Docs finding: the sample destroys the client right after the publish, with no wait as in the ChannelMetadata sample,
        // so its onMessage handler cannot be asserted: whether the message still arrives is a race, and the test does not assert it.
        out.assertPrinted("PubNub connection closed")
        // the service stored what the sample set
        val stored = AppContextKit.awaitUser(reader, "user-123", "Jane Smith")
        assertEquals("jane@example.com", stored.email?.value)
        assertEquals("Developer", stored.custom?.value?.get("role"))
        assertEquals("Engineering", stored.custom?.value?.get("department"))
        assertEquals("New York", stored.custom?.value?.get("location"))
        assertNotNull(stored.updated)
    }
}
