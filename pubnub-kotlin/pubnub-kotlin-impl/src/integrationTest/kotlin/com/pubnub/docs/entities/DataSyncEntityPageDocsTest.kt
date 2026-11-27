@file:Suppress("ktlint")

package com.pubnub.docs.entities

import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncSetEventType
import com.pubnub.docs.support.DataSyncKit
import com.pubnub.docs.support.DataSyncKit.ALICE
import com.pubnub.docs.support.DataSyncKit.ROLE
import com.pubnub.docs.support.DataSyncKit.SNEAKER
import com.pubnub.docs.support.DataSyncKit.SUMMER
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin DataSync entity pages data-sync-user, data-sync-channel and data-sync-entity
 * (docs/sdks/kotlin/entities/data-sync-*.md). The samples only create a subscription and subscribe, so each test arms a listener
 * on the same client, changes the object through a second client and waits for the event on the channel of the subscription.
 */
@ExtendWith(LiveExtension::class)
class DataSyncEntityPageDocsTest {
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

    @Test
    fun dataSyncUserSubscription(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val events = DataSyncKit.Events(client)
        val ready = Wait.connected(client, listOf(ALICE))

        // snippet.dataSyncUserSubscription
        val subscription = pubnub.dataSyncUser("user-alice").subscription()
        subscription.subscribe()
        // snippet.end

        ready.await()
        assertEquals(setOf(ALICE), client.getSubscribedChannels().filterNot { it.endsWith("-pnpres") }.toSet())
        DataSyncKit.setUser(server, ALICE, "Alice D.", "shopper")
        val event = events.await("the update of Alice on her channel") { it.channel == ALICE && DataSyncKit.isSet(it, "user", ALICE, PNDataSyncSetEventType.UPDATE) }
        assertEquals("user", event.extractedMessage.type)
        events.close()
    }

    @Test
    fun dataSyncChannelSubscription(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val events = DataSyncKit.Events(client)
        val ready = Wait.connected(client, listOf(SUMMER))

        // snippet.dataSyncChannelSubscription
        val subscription = pubnub.dataSyncChannel("channel-summer-sale").subscription()
        subscription.subscribe()
        // snippet.end

        ready.await()
        assertEquals(setOf(SUMMER), client.getSubscribedChannels().filterNot { it.endsWith("-pnpres") }.toSet())
        DataSyncKit.setChannel(server, SUMMER, "Summer Sale 2027", "promotion")
        val event = events.await("the update of the channel on its own channel") { it.channel == SUMMER && DataSyncKit.isSet(it, "channel", SUMMER, PNDataSyncSetEventType.UPDATE) }
        assertEquals("channel", event.extractedMessage.type)
        events.close()
    }

    @Test
    fun dataSyncEntitySubscription(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val events = DataSyncKit.Events(client)
        val ready = Wait.connected(client, listOf(SNEAKER))

        // snippet.dataSyncEntitySubscription
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription()
        subscription.subscribe()
        // snippet.end

        ready.await()
        assertEquals(setOf(SNEAKER), client.getSubscribedChannels().filterNot { it.endsWith("-pnpres") }.toSet())
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 66.0, 4)
        val event = events.await("the update of the product on its own channel") { it.channel == SNEAKER && DataSyncKit.isSet(it, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE) }
        assertEquals("entity", event.extractedMessage.type)
        events.close()
    }

    /** Not a sample: the page says `default` and `__default__` observe the entity itself and that a blank projection throws. */
    @Test
    fun defaultProjectionNamesObserveTheEntityItself(clients: LiveClients) {
        val client = clients.server(ROLE, "docs-listener")
        for (name in listOf("default", "__default__")) {
            val ready = Wait.connected(client, listOf(SNEAKER))
            val subscription = client.dataSyncEntity(SNEAKER).subscription(name)
            subscription.subscribe()
            ready.await()
            assertEquals(setOf(SNEAKER), client.getSubscribedChannels().filterNot { it.endsWith("-pnpres") }.toSet(), "projection $name")
            subscription.unsubscribe()
        }
        assertThrows<IllegalArgumentException> { client.dataSyncEntity(SNEAKER).subscription("") }
    }

    @Test
    fun dataSyncProjectionSubscription(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(ROLE, "docs-server")
        val client = clients.server(ROLE, "docs-listener")
        val pubnub = recorder.wrap(client)
        val projection = "__admin__$SNEAKER"
        val events = DataSyncKit.Events(client)
        val ready = Wait.connected(client, listOf(projection))

        // snippet.dataSyncProjectionSubscription
        val subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription("admin")
        subscription.subscribe()
        // snippet.end

        ready.await()
        // the projection has its own channel, and the subscription is on that channel only
        assertEquals(setOf(projection), client.getSubscribedChannels().filterNot { it.endsWith("-pnpres") }.toSet())
        DataSyncKit.setProduct(server, SNEAKER, "Retro Sneaker", 67.0, 4)
        val event = events.await("the update of the product on the admin projection channel") { it.channel == projection && DataSyncKit.isSet(it, "entity", SNEAKER, PNDataSyncSetEventType.UPDATE) }
        assertEquals("product", event.extractedMessage.className)
        events.close()
    }
}
