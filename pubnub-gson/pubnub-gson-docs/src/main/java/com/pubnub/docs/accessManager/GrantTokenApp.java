package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.v2.PNConfiguration;

import java.util.Arrays;

public class GrantTokenApp {
    public static void main(String[] args) throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#basic-usage

        // snippet.basicGrantToken
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("yourSecretKey");  // <-- a VALID secretKey is required
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.grantToken()
                .ttl(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.name("channel-a").read(),
                        ChannelGrant.name("channel-b").read().write()))
                .async(result -> { /* check result */ });
        // snippet.end
    }
}
