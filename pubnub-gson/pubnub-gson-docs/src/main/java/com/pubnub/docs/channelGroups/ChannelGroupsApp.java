package com.pubnub.docs.channelGroups;

// https://www.pubnub.com/docs/sdks/java/api-reference/channel-groups#add-channels-1
// snippet.ChannelGroupsApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult;

import java.util.Arrays;

public class ChannelGroupsApp {
    public static void main(String[] args) throws PubNubException {
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.addChannelsToChannelGroup()
                .channelGroup("cg1")
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .async(result -> {
                    result.onSuccess((PNChannelGroupsAddChannelResult res) -> {
                        System.out.println("Channels added to channel group successfully.");
                    }).onFailure((PubNubException exception) -> {
                        System.out.println("Error adding channels to channel group: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
