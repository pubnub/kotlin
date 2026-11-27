@file:Suppress("ktlint")

package com.pubnub.docs.entities

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.assertTimetoken
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page entities/channel (docs/sdks/kotlin/entities/channel.md). One test per snippet. The text
 * between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions below it.
 */
@ExtendWith(LiveExtension::class)
class ChannelDocsTest {
    @Test
    fun channelMain(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "demo-entity-channel", "Hello from Channel entity!")
        subscribeAndConnect(receiver, listOf("demo-entity-channel"))
        Cleanup.record("history", "demo-entity-channel")
        val out = Stdout.captureForTest()

        com.pubnub.docs.entities.samples.channel.main()

        val message = arrived.await() as PNMessageResult
        val body = message.message.asJsonObject
        assertEquals("Hello from Channel entity!", body["text"].asString)
        assertEquals("channel-entity-demo-user", body["sender"].asString)
        assertTrue(body["timestamp"].asLong > 0, "the timestamp of the message is not a number")
        assertEquals("channel-entity-demo-user", message.publisher)
        assertTimetoken(message.timetoken)
        out.assertPrinted("SUCCESS: Message published via Channel entity")
        out.assertPrinted("Timetoken: ${message.timetoken}")
        assertTrue(out.lines("ERROR").isEmpty(), "the sample reported an error")
        storedItem(receiver, "demo-entity-channel", message.timetoken!!)
    }

    private companion object {
        const val OPEN = "open"
    }
}
