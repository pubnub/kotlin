@file:Suppress("ktlint")

package com.pubnub.docs.channelGroups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.createGroup
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.MessagingKit.awaitGroup
import com.pubnub.docs.support.MessagingKit.groupChannels
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page channel-groups (docs/sdks/kotlin/api-reference/channel-groups.md). One test per snippet.
 * The text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions
 * below it.
 */
@ExtendWith(LiveExtension::class)
class ChannelGroupsDocsTest {
    @Test
    fun addChannelsToChannelGroupMain(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        Cleanup.record("channel_group", "demo-channel-group")
        val out = Stdout.captureForTest()

        com.pubnub.docs.channelGroups.samples.main()

        Wait.eventually("the sample printed that it added the channels") {
            out.assertPrinted("Successfully added channels to channel group 'demo-channel-group'")
        }
        Wait.eventually("the sample listed the channels") {
            val listed = out.lines("Channels in group 'demo-channel-group': ").singleOrNull()
            assertTrue(listed != null, "the sample did not print the list of channels once")
            for (channel in listOf("demo-channel-a", "demo-channel-b", "demo-channel-c")) {
                assertTrue(listed!!.contains(channel), "the printed list does not name $channel")
            }
        }
        Wait.eventually("the sample deleted the group") {
            out.assertPrinted("Successfully deleted channel group 'demo-channel-group'")
        }
        assertTrue(out.lines("Failed to").isEmpty(), "the sample reported a failure")
        awaitGroup(reader, "demo-channel-group")
    }

    @Test
    fun deleteChannelGroup(clients: LiveClients, recorder: Recorder) {
        val pubNub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(reader, "cg1", "ch1", "ch2")
        awaitGroup(reader, "cg1", "ch1", "ch2")

        // snippet.deleteChannelGroup
        pubNub.deleteChannelGroup(
            channelGroup = "cg1"
        ).async { result -> }
        // snippet.end

        recorder.awaitOk("deleteChannelGroup")
        awaitGroup(reader, "cg1")
    }

    @Test
    fun listChannelsForChannelGroup(clients: LiveClients, recorder: Recorder) {
        val pubNub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(reader, "cg1", "ch1", "ch2", "ch3")
        awaitGroup(reader, "cg1", "ch1", "ch2", "ch3")

        // snippet.listChannelsForChannelGroup
        pubNub.listChannelsForChannelGroup(
            channelGroup = "cg1"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val listed = recorder.awaitOk("listChannelsForChannelGroup") as PNChannelGroupsAllChannelsResult
        assertEquals(listOf("ch1", "ch2", "ch3"), listed.channels.sorted())
    }

    @Test
    fun removeChannelsFromChannelGroup(clients: LiveClients, recorder: Recorder) {
        val pubNub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        createGroup(reader, "cg1", "ch1", "ch2", "ch3")
        awaitGroup(reader, "cg1", "ch1", "ch2", "ch3")

        // snippet.removeChannelsFromChannelGroup
        pubNub.removeChannelsFromChannelGroup(
            channels = listOf("ch1", "ch2"),
            channelGroup = "cg1"
        ).async { result -> }
        // snippet.end

        recorder.awaitOk("removeChannelsFromChannelGroup")
        awaitGroup(reader, "cg1", "ch3")
        assertEquals(listOf("ch3"), groupChannels(reader, "cg1"))
    }

    private companion object {
        const val OPEN = "open"
    }
}
