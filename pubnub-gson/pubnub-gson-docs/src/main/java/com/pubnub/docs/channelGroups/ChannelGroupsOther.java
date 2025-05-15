package com.pubnub.docs.channelGroups;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class ChannelGroupsOther extends SnippetBase {
    private void listChannelsForChannelGroup() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/channel-groups#list-channels-1

        PubNub pubNub = createPubNub();

        // snippet.listChannelsForChannelGroup
        pubNub.listChannelsForChannelGroup()
                .channelGroup("cg1")
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void removeChannelsFromChannelGroup () throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/channel-groups#remove-channels-1

        PubNub pubNub = createPubNub();

        // snippet.removeChannelsFromChannelGroup
        pubNub.removeChannelsFromChannelGroup()
                .channelGroup("family")
                .channels(Arrays.asList("son"))
                .sync();
        // snippet.end
    }

    private void deleteChannelGroup() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/channel-groups#delete-channel-group-1

        PubNub pubNub = createPubNub();

        // snippet.deleteChannelGroup
        pubNub.deleteChannelGroup()
                .channelGroup("family")
                .async(result -> { /* check result */ });
        // snippet.end
    }
}
