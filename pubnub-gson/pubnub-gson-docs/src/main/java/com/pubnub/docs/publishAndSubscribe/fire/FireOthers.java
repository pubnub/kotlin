package com.pubnub.docs.publishAndSubscribe.fire;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.models.consumer.PNPublishResult;

import java.util.Arrays;

public class FireOthers {
    private void fireMessage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#fire-a-message-to-a-channel

        // snippet.fireMessage
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.fire(Arrays.asList("hello", "there"))
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        System.out.println("publish worked! timetoken: " + publishResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("error happened while publishing: " + exception.toString());
                    });
                });
        // snippet.end
    }

}
