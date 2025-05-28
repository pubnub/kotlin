package com.pubnub.docs.channelGroups

import com.pubnub.docs.SnippetBase

class ChannelGroupsOthers : SnippetBase() {
    private fun listChannelsForChannelGroup() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/channel-groups#basic-usage-1

        val pubNub = createPubNub()

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
    }

    private fun removeChannelsFromChannelGroup() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/channel-groups#basic-usage-2

        val pubNub = createPubNub()

        // snippet.removeChannelsFromChannelGroup
        pubNub.removeChannelsFromChannelGroup(
            channels = listOf("ch1", "ch2"),
            channelGroup = "cg1"
        ).async { result -> }
        // snippet.end
    }

    private fun deleteChannelGroup() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/channel-groups#basic-usage-3

        val pubNub = createPubNub()

        // snippet.deleteChannelGroup
        pubNub.deleteChannelGroup(
            channelGroup = "cg1"
        ).async { result -> }
        // snippet.end
    }
}
