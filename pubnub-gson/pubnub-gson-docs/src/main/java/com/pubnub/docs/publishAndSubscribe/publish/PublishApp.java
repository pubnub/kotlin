package com.pubnub.docs.publishAndSubscribe.publish;

// snippet.publishMain
import com.google.gson.JsonObject;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.models.consumer.PNPublishResult;

public class PublishApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create a channel to publish to
        Channel channel = pubnub.channel("myChannel");

        // Create a message to publish
        JsonObject position = new JsonObject();
        position.addProperty("lat", 32L);
        position.addProperty("lng", 32L);

        // Publish the message
        channel.publish(position)
                .customMessageType("text-message")
                .async(result -> {
                    result.onSuccess((PNPublishResult res) -> {
                        System.out.println("Publish timetoken: " + res.getTimetoken());
                    }).onFailure((PubNubException exception) -> {
                        System.out.println("Error publishing message: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
