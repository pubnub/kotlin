package com.pubnub.docs.entities;
// https://www.pubnub.com/docs/sdks/java/entities/channel#basic-usage
// snippet.channelApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.java.v2.entities.Channel;

public class ChannelApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create a Channel entity
        Channel singleChannel = pubnub.channel("myChannel");

        System.out.println("Channel created: " + singleChannel.getName());
    }
}
// snippet.end
