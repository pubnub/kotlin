package com.pubnub.docs.entities;
// https://www.pubnub.com/docs/sdks/java/entities/channel-group#basic-usage

// snippet.channelGroupApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.v2.entities.ChannelGroup;

public class ChannelGroupApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create a ChannelGroup entity
        ChannelGroup myChannelGroup = pubnub.channelGroup("myGroup");

        System.out.println("Channel Group created: " + myChannelGroup.getName());
    }
}
// snippet.end
