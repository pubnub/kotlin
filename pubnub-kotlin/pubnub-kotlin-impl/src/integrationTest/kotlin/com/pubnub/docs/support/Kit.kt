package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.function.Executable
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger

/**
 * Arrange and assert helpers for publish and subscribe style tests, on Kotlin clients (the second clients of a test, and the
 * sample's own client in a Kotlin test). Java tests use them for their second clients, see `JavaWait` for the Java client of a
 * sample. Nothing here is part of a sample. Every `arm...` call registers its wait before the action, call `await` on the
 * result after it.
 */
object Kit {
    /** A message text that no earlier run and no other test published. */
    @JvmStatic
    fun probe(label: String): String = "$label-${UUID.randomUUID()}"

    // The three below are for Java tests: a Java lambda cannot throw the checked PubNubException of `sync()`, a Callable or an
    // Executable can.

    /** [Retry.retryTransient] for a Java lambda. */
    @JvmStatic
    fun <T> retry(
        label: String,
        call: Callable<T>,
    ): T = Retry.retryTransient(label = label) { call.call() }

    /** [Cleanup.cleanupAfterTest] for a Java lambda. */
    @JvmStatic
    fun cleanup(
        label: String,
        call: Callable<*>,
    ) = Cleanup.cleanupAfterTest(label) { call.call() }

    /** [Wait.eventually] for a Java lambda that holds assertions. */
    @JvmStatic
    fun eventually(
        what: String,
        run: Executable,
    ) = Wait.eventually(what) { run.execute() }

    @JvmStatic
    fun assertTimetoken(timetoken: Long?) {
        assertTrue(timetoken != null && timetoken.toString().length == 17, "the timetoken is not 17 digits")
    }

    /** Subscribes [client] and returns when the service confirmed the subscription of every name. */
    @JvmStatic
    @JvmOverloads
    fun subscribeAndConnect(
        client: PubNub,
        channels: List<String> = emptyList(),
        groups: List<String> = emptyList(),
        presence: Boolean = false,
    ) {
        val connected = Wait.connected(client, channels, groups)
        client.subscribe(channels = channels, channelGroups = groups, withPresence = presence)
        connected.await()
    }

    @JvmStatic
    fun armMessage(
        client: PubNub,
        channel: String,
        contains: String,
    ): Wait.Expectation =
        Wait.expectEvent(client, "message") { event ->
            event is PNMessageResult && event.channel == channel && event.message.toString().contains(contains)
        }

    @JvmStatic
    fun armSignal(
        client: PubNub,
        channel: String,
        contains: String,
    ): Wait.Expectation =
        Wait.expectEvent(client, "signal") { event ->
            event is PNSignalResult && event.channel == channel && event.message.toString().contains(contains)
        }

    /** A presence event [action] of [user] on [channel]. */
    @JvmStatic
    fun armPresence(
        client: PubNub,
        action: String,
        user: String,
        channel: String,
    ): Wait.Expectation =
        Wait.expectEvent(client, "presence") { event ->
            event is PNPresenceEventResult && event.event == action && event.uuid == user && event.channel == channel
        }

    /** A presence event [action] on [channel] from anybody. */
    @JvmStatic
    fun armPresenceEvent(
        client: PubNub,
        action: String,
        channel: String,
    ): Wait.Expectation =
        Wait.expectEvent(client, "presence") { event ->
            event is PNPresenceEventResult && event.event == action && event.channel == channel
        }

    /** Waits until nobody is present on [channels], so that an occupancy in an event is exact. */
    @JvmStatic
    fun awaitNoOccupants(
        client: PubNub,
        vararg channels: String,
    ) {
        // Leaves of the clients of the earlier tests can take a while, most of all after the crowd of the interval tests.
        Wait.settled("no occupants on ${channels.toList()}", timeoutMs = 60_000) {
            val here = client.hereNow(channels = channels.toList()).sync()
            val who = here.channels.values.joinToString("; ") { "${it.channelName}: ${it.occupants.map { o -> o.uuid }}" }
            assertEquals(0, here.totalOccupancy, "the channels still have occupants ($who)")
        }
    }

    @JvmStatic
    fun publishFrom(
        sender: PubNub,
        channel: String,
        message: Any,
        shouldStore: Boolean,
    ): PNPublishResult =
        Retry.retryTransient(label = "publish to $channel") {
            sender.publish(channel, message, shouldStore = shouldStore).sync()
        }

    @JvmStatic
    fun signalFrom(
        sender: PubNub,
        channel: String,
        message: Any,
    ): PNPublishResult = Retry.retryTransient(label = "signal to $channel") { sender.signal(channel, message).sync() }

    /**
     * Removes the presence of [user] from [channels] and [groups] after a sample that subscribed and unsubscribed in the same
     * breath: the leave can reach the service before the join of the handshake, which then stays for the presence timeout of
     * 300 seconds and spoils the occupancy of the next test. A client with the same user id joins and leaves again.
     */
    @JvmStatic
    @JvmOverloads
    fun leaveAs(
        clients: LiveClients,
        user: String,
        channels: List<String> = emptyList(),
        groups: List<String> = emptyList(),
    ) {
        val ghost = clients.kotlinClient("open", user)
        subscribeAndConnect(ghost, channels, groups)
        ghost.unsubscribeAll()
        Wait.sleepToProve(2000)
    }

    /** Creates [group] with [channels] and removes it after the test. */
    @JvmStatic
    fun createGroup(
        client: PubNub,
        group: String,
        vararg channels: String,
    ) {
        Retry.retryTransient(label = "create $group") { client.addChannelsToChannelGroup(channels.toList(), group).sync() }
        Cleanup.record("channel_group", group)
    }

    /** The item of the history of [channel] with [timetoken]. History can lag the realtime delivery, so it is awaited. */
    @JvmStatic
    fun storedItem(
        client: PubNub,
        channel: String,
        timetoken: Long,
    ): PNFetchMessageItem =
        Wait.eventually("the message is in the history of $channel") {
            val items =
                client.fetchMessages(listOf(channel), includeMeta = true, includeCustomMessageType = true)
                    .sync().channels[channel].orEmpty()
            items.firstOrNull { it.timetoken == timetoken } ?: throw AssertionError("the history of $channel does not list the message")
        }

    /** Proves a message is not stored: waits two seconds, then reads the history once. */
    @JvmStatic
    fun assertNotStored(
        client: PubNub,
        channel: String,
        timetoken: Long,
    ) {
        Wait.sleepToProve(2000)
        val items = client.fetchMessages(listOf(channel)).sync().channels[channel].orEmpty()
        assertTrue(items.none { it.timetoken == timetoken }, "a message that must not be stored is in the history of $channel")
    }

    /** Proves no message with [contains] is stored on [channel]: waits two seconds, then reads the history once. */
    @JvmStatic
    fun assertNoStoredText(
        client: PubNub,
        channel: String,
        contains: String,
    ) {
        Wait.sleepToProve(2000)
        val items = client.fetchMessages(listOf(channel)).sync().channels[channel].orEmpty()
        assertTrue(
            items.none { it.message.toString().contains(contains) },
            "a message that must not be stored is in the history of $channel",
        )
    }

    /** Fails when the armed event arrives within [millis]. Call it only after a control client proved that the service delivers. */
    @JvmStatic
    @JvmOverloads
    fun assertNothingArrives(
        expectation: Wait.Expectation,
        message: String,
        millis: Long = 3000,
    ) {
        val seen =
            try {
                expectation.await(millis)
            } catch (e: AssertionError) {
                null
            }
        assertNull(seen, message)
    }

    /** Proves a handler that was removed does not run again: waits, then compares its counter. */
    @JvmStatic
    fun assertNothingHandled(
        handled: AtomicInteger,
        expected: Int,
    ) {
        Wait.sleepToProve(1500)
        assertEquals(expected, handled.get(), "a handler that was removed ran again")
    }

    /**
     * Joins [size] + 1 users `docs-crowd-N` to [channel], more than the announce max of the open keyset, so that the keyset
     * switches to interval presence events.
     */
    @JvmStatic
    fun joinCrowd(
        clients: LiveClients,
        channel: String,
        size: Int,
    ) {
        for (index in 1..size + 1) {
            val member = clients.kotlinClient("open", "docs-crowd-$index")
            subscribeAndConnect(member, listOf(channel))
        }
    }
}
