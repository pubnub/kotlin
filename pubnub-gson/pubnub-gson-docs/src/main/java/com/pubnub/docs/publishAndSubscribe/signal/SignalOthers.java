package com.pubnub.docs.publishAndSubscribe.signal;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.models.consumer.PNPublishResult;

public class SignalOthers {
    private void signalMessage() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#signal-a-message-to-a-channel

        // snippet.signalMessage
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        configBuilder.publishKey("demo");

        PNConfiguration pnConfiguration = configBuilder.build();
        PubNub pubnub = PubNub.create(pnConfiguration);
        Channel channel = pubnub.channel("myChannel");

        channel.signal("Hello everyone!")
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult publishResult) -> {
                        Long timetoken = publishResult.getTimetoken(); // signal message timetoken
                    }).onFailure( (PubNubException exception) -> {
                        exception.printStackTrace();
                    });
                });
        // snippet.end
    }
}
