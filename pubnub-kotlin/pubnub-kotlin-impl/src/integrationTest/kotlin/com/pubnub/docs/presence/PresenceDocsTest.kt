@file:Suppress("ktlint")

package com.pubnub.docs.presence

import com.google.gson.JsonObject
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.docs.support.Kit.awaitNoOccupants
import com.pubnub.docs.support.Kit.createGroup
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.MessagingKit.awaitGroup
import com.pubnub.docs.support.MessagingKit.awaitPresent
import com.pubnub.docs.support.MessagingKit.joinWithState
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page presence (docs/sdks/kotlin/api-reference/presence.md). One test per snippet. The text
 * between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions below it.
 * The joiner is a second client (`docs-joiner`): the service lists a user as present only after it subscribed.
 */
@ExtendWith(LiveExtension::class)
class PresenceDocsTest {
    @Test
    fun hereNowMain(clients: LiveClients) {
        joinWithState(clients, "docs-joiner", listOf("demo-channel-1"), mapOf("mood" to "happy"))
        val out = Stdout.captureForTest()

        com.pubnub.docs.presence.samples.main()

        assertTrue(out.lines("ERROR").isEmpty(), "the sample reported an error")
        Wait.eventually("the sample printed all three reads") {
            // offset = 10 skips the one occupant: the occupancy counts it, the list of uuids is empty
            out.assertPrinted("Occupancy: 1")
            out.assertPrinted("UUIDs: []")
            out.assertPrinted("Total Occupancy: 1")
            out.assertPrinted("UUIDs present: [docs-joiner]")
            out.assertPrinted("UUID: docs-joiner")
            out.assertPrinted("State: {\"mood\":\"happy\"}")
        }
    }

    @Test
    fun hereNowForChannelGroups(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(reader, "cg1", "ch1")
        createGroup(reader, "cg2", "ch2")
        createGroup(reader, "cg3", "ch3")
        awaitGroup(reader, "cg1", "ch1")
        joinWithState(clients, "docs-joiner", listOf("ch1"), mapOf("isTyping" to true))

        // snippet.hereNowForChannelGroups
        pubnub.hereNow(
            channelGroups = listOf("cg1", "cg2", "cg3"), // who is present on those channels groups
            includeState = true, // include state with request (false by default)
            includeUUIDs = true // if false, only shows occupancy count
        ).async { result: Result<PNHereNowResult> ->
            result.onSuccess { res: PNHereNowResult ->
                res.totalOccupancy
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        val here = recorder.awaitOk("hereNow") as PNHereNowResult
        assertEquals(1, here.totalOccupancy)
        val occupant = here.channels["ch1"]!!.occupants.single()
        assertEquals("docs-joiner", occupant.uuid)
        assertEquals(true, occupant.state!!.asJsonObject["isTyping"].asBoolean)
    }

    @Test
    fun hereNowReturnOccupancyOnly(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        joinWithState(clients, "docs-joiner", listOf("my_channel"), mapOf("isTyping" to true))

        // snippet.hereNowReturnOccupancyOnly
        pubnub.hereNow(
            channels = listOf("my_channel"), // who is present on those channels?
            includeUUIDs = false, // if false, only shows occupancy count
            includeState = false // include state with request (false by default)
        ).async { result: Result<PNHereNowResult> ->
            result.onSuccess { res: PNHereNowResult ->
                res.channels.values.forEach { channelData ->
                    channelData.channelName // ch1
                    channelData.occupancy // 3
                }
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        val here = recorder.awaitOk("hereNow") as PNHereNowResult
        val channelData = here.channels.values.single()
        assertEquals("my_channel", channelData.channelName)
        assertEquals(1, channelData.occupancy)
        assertTrue(channelData.occupants.isEmpty(), "the occupants are listed although includeUUIDs is false")
    }

    @Test
    fun hereNowReturningState(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        joinWithState(clients, "docs-joiner", listOf("ch1", "ch2"), mapOf("isTyping" to true))

        // snippet.hereNowReturningState
        pubnub.hereNow(
            channels = listOf("ch1", "ch2"),
            includeState = true
        ).async { result: Result<PNHereNowResult> ->
            result.onSuccess { res: PNHereNowResult ->
                res.channels.values.forEach { channelData ->
                    channelData.channelName // ch1
                    channelData.occupancy // 3
                    channelData.occupants.forEach { o ->
                        o.uuid // some_uuid, returned by default
                        o.state // {"data":{"isTyping":true}}, requested
                    }
                }
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        val here = recorder.awaitOk("hereNow") as PNHereNowResult
        assertEquals(2, here.totalOccupancy)
        for (channel in listOf("ch1", "ch2")) {
            val channelData = here.channels[channel]!!
            assertEquals(1, channelData.occupancy)
            val occupant = channelData.occupants.single()
            assertEquals("docs-joiner", occupant.uuid)
            assertEquals(true, occupant.state!!.asJsonObject["isTyping"].asBoolean)
        }
    }

    @Test
    fun getState(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "such_uuid")
        owner.setPresenceState(channels = listOf("ch1", "ch2", "ch3"), state = mapOf("is_typing" to true)).sync()

        // snippet.getState
        pubnub.getPresenceState(
            channels = listOf("ch1", "ch2", "ch3"), // channels to fetch state for
            uuid = "such_uuid" // uuid of user to fetch, or own uuid by default
        ).async { result -> }
        // snippet.end

        val state = recorder.awaitOk("getPresenceState") as PNGetStateResult
        // Several channels come back under the one key "channels"
        assertEquals(setOf("channels"), state.stateByUUID.keys)
        val byChannel = state.stateByUUID["channels"]!!.asJsonObject
        assertEquals(setOf("ch1", "ch2", "ch3"), byChannel.keySet())
        for (channel in listOf("ch1", "ch2", "ch3")) {
            assertEquals(true, byChannel[channel].asJsonObject["is_typing"].asBoolean)
        }
    }

    @Test
    fun getStateForUuid(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "such_uuid")
        owner.setPresenceState(channels = listOf("ch1", "ch2"), state = mapOf("is_typing" to true)).sync()
        val out = Stdout.captureForTest()

        // snippet.getStateForUuid
        pubnub.getPresenceState(
            channels = listOf("ch1", "ch2"), // channels to fetch state for
            uuid = "such_uuid" // uuid of user to fetch, or own uuid by default
        ).async { result ->
            result.onFailure { exception ->
                exception.printStackTrace()
            }.onSuccess { value ->
                value.stateByUUID.forEach { (channel, state) ->
                    println("Channel: $channel, state: $state")
                }
            }
        }
        // snippet.end

        val state = recorder.awaitOk("getPresenceState") as PNGetStateResult
        assertEquals(setOf("channels"), state.stateByUUID.keys)
        Wait.eventually("the sample printed the state of both channels") {
            out.assertPrinted("Channel: channels, state: {\"ch1\":{\"is_typing\":true},\"ch2\":{\"is_typing\":true}}")
        }
    }

    @Test
    fun setState(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")

        // snippet.setState
        pubnub.setPresenceState(
            channels = listOf("my_channel"),
            state = mapOf("is_typing" to "true")
            // if no uuid supplied, own is used
        ).async { result -> }
        // snippet.end

        val set = recorder.awaitOk("setPresenceState") as PNSetStateResult
        assertEquals("true", set.state.asJsonObject["is_typing"].asString)
        Wait.settled("the state of myUserId is stored") {
            val read = reader.getPresenceState(channels = listOf("my_channel"), uuid = "myUserId").sync()
            assertEquals("true", read.stateByUUID["my_channel"]!!.asJsonObject["is_typing"].asString)
        }
    }

    @Test
    fun setStateForChannelsInChannelGroup(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(reader, "cg1", "ch4")
        createGroup(reader, "cg2", "ch5")
        createGroup(reader, "cg3", "ch6")

        // snippet.setStateForChannelsInChannelGroup
        pubnub.setPresenceState(
            channels = listOf("ch1", "ch2", "ch3"), // apply on those channels
            channelGroups = listOf("cg1", "cg2", "cg3"), // apply on those channel groups
            state = JsonObject().apply { addProperty("is_typing", true) }
        ).async { result ->
            result.onSuccess { res ->
                res.state // {"data":{"is_typing":true}}
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        val set = recorder.awaitOk("setPresenceState") as PNSetStateResult
        assertEquals(true, set.state.asJsonObject["is_typing"].asBoolean)
        Wait.settled("the state of myUserId is stored on the channels and the groups") {
            val read =
                reader.getPresenceState(channels = listOf("ch1", "ch2", "ch3"), channelGroups = listOf("cg1", "cg2", "cg3"), uuid = "myUserId").sync()
            val byChannel = read.stateByUUID["channels"]!!.asJsonObject
            for (channel in listOf("ch1", "ch2", "ch3")) {
                assertEquals(true, byChannel[channel].asJsonObject["is_typing"].asBoolean, "the state is missing on a channel")
            }
        }
    }

    @Test
    fun whereNowGetListOfChannelsOtherUserIsSubscribeTo(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        awaitNoOccupants(reader, "ch1")
        val other = clients.kotlinClient(OPEN, "someUuid")
        subscribeAndConnect(other, listOf("ch1"))
        awaitPresent(reader, "ch1", "someUuid")
        Wait.settled("the service lists ch1 for someUuid") {
            assertEquals(listOf("ch1"), reader.whereNow("someUuid").sync().channels)
        }

        // snippet.whereNowGetListOfChannelsOtherUserIsSubscribeTo
        pubnub.whereNow(
            uuid = "someUuid"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val where = recorder.awaitOk("whereNow") as PNWhereNowResult
        assertEquals(listOf("ch1"), where.channels)
    }

    @Test
    fun whereNowGetListOfChannelsUuidIsSubscribedTo(clients: LiveClients, recorder: Recorder) {
        val client = clients.kotlinClient(OPEN, "myUserId")
        val pubnub = recorder.wrap(client)
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        awaitNoOccupants(reader, "ch1", "ch2")
        subscribeAndConnect(client, listOf("ch1", "ch2"))
        awaitPresent(reader, "ch1", "myUserId")
        awaitPresent(reader, "ch2", "myUserId")
        Wait.settled("the service lists both channels for myUserId") {
            assertEquals(listOf("ch1", "ch2"), reader.whereNow("myUserId").sync().channels.sorted())
        }

        // snippet.whereNowGetListOfChannelsUuidIsSubscribedTo
        pubnub.whereNow()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val where = recorder.awaitOk("whereNow") as PNWhereNowResult
        assertEquals(listOf("ch1", "ch2"), where.channels.sorted())
    }

    private companion object {
        const val OPEN = "open"
    }
}
