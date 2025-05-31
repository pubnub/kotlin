package com.pubnub.docs.entities;
// https://www.pubnub.com/docs/sdks/java/entities/user-metadata#basic-usage

// snippet.userMetadataApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.java.v2.entities.UserMetadata;

public class UserMetadataApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create a UserMetadata entity
        UserMetadata userMetadata = pubnub.userMetadata("userId1");

        System.out.println("User Metadata created for user ID: " + userMetadata.getId());
    }
}
// snippet.end
