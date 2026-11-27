@file:Suppress("ktlint")

package com.pubnub.docs.basicUsage

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.assertTimetoken
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Stdout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live test of the complete example on the Kotlin landing page (docs/sdks/kotlin.md). The four ids of the page are the
 * regions of one file (`samples/BasicUsageMain.kt`), so they run together: one call of `main`. The sample is called once,
 * as written. A second client proves what the service did.
 */
@ExtendWith(LiveExtension::class)
class BasicUsageDocsTest {
    private val OPEN = "open"

    @Test
    fun initializePubNubBasic(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived = armMessage(receiver, "myChannel", "Hello, world")
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")
        val out = Stdout.captureForTest()

        // Regions: initializePubNubBasic, setupEventListenersBasic, createSubscriptionBasic and publishMessagesBasic.
        com.pubnub.docs.basicUsage.samples.main()

        val message = arrived.await() as PNMessageResult
        assertEquals("Hello, world", message.message.asJsonObject["msg"].asString)
        assertEquals("myUserId", message.publisher)
        assertTimetoken(message.timetoken)

        // initializePubNubBasic and the print between the regions
        out.assertPrinted("Message to send: {\"msg\":\"Hello, world\"}")
        // setupEventListenersBasic: the status listener saw the connect
        out.assertPrinted("Connected/Reconnected")
        assertTrue(out.lines("Disconnected/Unexpectedly Disconnected").isEmpty(), "the sample reported a disconnect")
        // createSubscriptionBasic: the subscription of the sample got its own message
        out.assertPrinted("Received message {\"msg\":\"Hello, world\"}")
        // publishMessagesBasic
        out.assertPrinted("Message sent, timetoken: ${message.timetoken}")
        assertTrue(out.lines("Error while publishing").isEmpty(), "the sample reported a failed publish")
        storedItem(receiver, "myChannel", message.timetoken!!)
    }
}
