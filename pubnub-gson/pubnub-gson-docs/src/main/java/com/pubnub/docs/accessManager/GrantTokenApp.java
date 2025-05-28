package com.pubnub.docs.accessManager;
// https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#basic-usage

// snippet.basicGrantToken
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

import java.util.Arrays;

public class GrantTokenApp {
    public static void main(String[] args) throws PubNubException {
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secretKey("yourSecretKey");  // <-- a VALID secretKey is required
        PubNub pubnub = PubNub.create(configBuilder.build());

        pubnub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.name("channel-a").read(),
                        ChannelGrant.name("channel-b").read().write()))
                .async(result -> {
                    result.onSuccess((PNGrantTokenResult grantTokenResult) -> {
                        System.out.println("Token: " + grantTokenResult.getToken());
                    }).onFailure((PubNubException exception) -> {
                        System.out.println("Error granting token: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
