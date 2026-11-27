package com.pubnub.docs.entities.samples;

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
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());
        // snippet.hide
        com.pubnub.docs.support.LiveClients.trackCurrent(pubnub);
        // snippet.show

        // Create a Channel entity
        Channel singleChannel = pubnub.channel("myChannel");

        System.out.println("Channel created: " + singleChannel.getName());
    }
}
// snippet.end
