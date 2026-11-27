@file:Suppress("ktlint")

package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.docs.support.DataSyncKit
import com.pubnub.docs.support.DataSyncKit.ALICE
import com.pubnub.docs.support.DataSyncKit.ROLE
import com.pubnub.docs.support.DataSyncKit.SNEAKER
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the DataSync listener samples of the Kotlin docs page publish-and-subscribe
 * (docs/sdks/kotlin/api-reference/publish-and-subscribe.md, "Add DataSync listeners"). The samples subscribe to Alice and to the
 * sneaker and print every event they get. The test changes the object through a second client and waits for the printed line,
 * so the line proves that the listener of the sample, and not only the subscription, works.
 */
@ExtendWith(LiveExtension::class)
class DataSyncSubscribeDocsTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun prepare() {
            DataSyncKit.resetPage()
            DataSyncKit.seedObserved()
        }

        @AfterAll
        @JvmStatic
        fun finish() = DataSyncKit.finishPage()
    }

    private fun assertPrinted(out: Stdout, text: String) =
        Wait.eventually("the sample printed \"$text\"", timeoutMs = 30_000) { out.assertPrinted(text) }

    @Test
    fun addDataSyncListener(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val out = Stdout.captureForTest()
        val ready = Wait.connected(client, listOf(SNEAKER))

        // snippet.addDataSyncListener
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription()

        subscription.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
            }
        })

        subscription.subscribe()
        // snippet.end

        ready.await()
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 71.0, 5)
        assertPrinted(out, "DataSync event on product-sneaker-42: entity")
    }

    @Test
    fun addDataSyncListenerLambda(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val out = Stdout.captureForTest()
        val ready = Wait.connected(client, listOf(SNEAKER))

        // snippet.addDataSyncListenerLambda
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription()

        subscription.onDataSync = { result ->
            println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
        }

        subscription.subscribe()
        // snippet.end

        ready.await()
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 72.0, 5)
        assertPrinted(out, "DataSync event on product-sneaker-42: entity")
    }

    @Test
    fun addDataSyncListenerClient(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val out = Stdout.captureForTest()
        val ready = Wait.connected(client, listOf(SNEAKER))

        // snippet.addDataSyncListenerClient
        pubnub.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
            }
        })

        pubnub.dataSyncEntity("product-sneaker-42").subscription().subscribe()
        // snippet.end

        ready.await()
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 73.0, 5)
        assertPrinted(out, "DataSync event on product-sneaker-42: entity")
    }

    @Test
    fun addDataSyncListenerSubscriptionSet(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val out = Stdout.captureForTest()
        val ready = Wait.connected(client, listOf(ALICE, SNEAKER))

        // snippet.addDataSyncListenerSubscriptionSet
        val subscriptionSet = pubnub.subscriptionSetOf(
            setOf(
                pubnub.dataSyncUser("user-alice").subscription(),
                pubnub.dataSyncEntity("product-sneaker-42").subscription()
            )
        )

        subscriptionSet.addListener(object : EventListener {
            override fun dataSync(pubnub: PubNub, result: PNDataSyncEventResult) {
                println("DataSync event on ${result.channel}: ${result.extractedMessage.type}")
            }
        })

        subscriptionSet.subscribe()
        // snippet.end

        ready.await()
        // one set, two kinds of object: a change of the user and a change of the product both reach the one listener
        DataSyncKit.setUser(server, ALICE, "Alice C.", "shopper")
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 74.0, 5)
        assertPrinted(out, "DataSync event on user-alice: user")
        assertPrinted(out, "DataSync event on product-sneaker-42: entity")
        assertTrue(out.lines("Failed").isEmpty())
    }
}
