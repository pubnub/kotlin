package com.pubnub.docs.configuration.publishAndSubscribe;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.models.consumer.PNPublishResult;

import java.util.Arrays;

public class PublishOthers {
    private void publishWithMetadata() throws PubNubException {
        // snippet.publishWithMetadata
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.publish(Arrays.asList("hello", "there"))
                .customMessageType("text-message")
                .shouldStore(true)
                .meta("meta") // optional meta data object which can be used with the filtering ability.
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("Message published with timetoken: " + publishResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("Error publishing message: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
