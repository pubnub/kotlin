package com.pubnub.docs.entities.samples;

// snippet.channelMetadataApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.java.v2.entities.ChannelMetadata;

public class ChannelMetadataApp {
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

        // Create a ChannelMetadata entity
        ChannelMetadata metadata = pubnub.channelMetadata("myChannel");

        System.out.println("Channel Metadata created: " + metadata.getId());
    }
}
// snippet.end
