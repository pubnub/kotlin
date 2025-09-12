package com.pubnub.docs.entities;
// https://www.pubnub.com/docs/sdks/java/entities/channel-metadata#basic-usage

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
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create a ChannelMetadata entity
        ChannelMetadata metadata = pubnub.channelMetadata("myChannel");

        System.out.println("Channel Metadata created: " + metadata.getId());
    }
}
// snippet.end
