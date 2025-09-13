package com.pubnub.docs.basicUsage;

// snippet.basicPubSubApp
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.callbacks.StatusListener;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.PNStatus;

public class BasicUsageApp {
    public static void main(String[] args) throws PubNubException {
        // snippet.initializePubNub
        // Initialize PubNub
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        // publishKey from Admin Portal (only required if publishing)
        configBuilder.publishKey("demo");
        configBuilder.secure(true);
        PubNub pubnub = PubNub.create(configBuilder.build());
        // snippet.end

        // Define a channel name
        final String channelName = "myChannel";

        // snippet.createChannel
        // Create a channel using the PubNub instance
        Channel channel = pubnub.channel(channelName);

        // Create a subscription for the channel
        Subscription subscription = channel.subscription();
        // snippet.end

        // snippet.setupEventListeners
        // Add status listener to monitor connection state
        pubnub.addListener(new StatusListener() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio/connectivity is lost
                    System.out.println("Connection lost");
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc
                    System.out.println("Connected to PubNub");

                    // snippet.publishMessage
                    // Create message payload using Gson to be sent when connected
                    final JsonObject messageJsonObject = new JsonObject();
                    JsonElement jsonElement = new JsonPrimitive("Hello World");
                    messageJsonObject.add("msg", jsonElement);

                    System.out.println("Message to send: " + messageJsonObject.toString());

                    // Publish the message when connected
                    channel.publish(messageJsonObject)
                            .customMessageType("text-message")
                            .async(result -> {
                                result.onSuccess(res -> {
                                    // Message successfully published to specified channel.
                                    System.out.println("Message published successfully with timetoken: " + res.getTimetoken());
                                }).onFailure(exception -> {
                                    // Request processing failed.
                                    // Handle message publish error
                                    System.out.println("Publish failed: " + exception.getMessage());
                                });
                            });
                    // snippet.end
                }
                // Handle other categories as needed
            }
        });
        // snippet.end

        // snippet.setupMessageListener
        // Set up a message listener for the subscription
        subscription.setOnMessage(pnMessageResult -> {
            // Handle new message stored in message.message
            System.out.println("Received on channel: " + pnMessageResult.getChannel());

            JsonElement receivedMessageObject = pnMessageResult.getMessage();
            System.out.println("Received message: " + receivedMessageObject.toString());

            // Extract desired parts of the payload, using Gson
            String msg = pnMessageResult.getMessage().getAsJsonObject().get("msg").getAsString();
            System.out.println("The content of the message is: " + msg);
        });
        // snippet.end

        // snippet.subscribeToChannel
        // Subscribe to the channel to start receiving messages
        subscription.subscribe();
        System.out.println("Subscribed to channel: " + channelName);
        // snippet.end

        // snippet.addDelay
        // Add a delay so the program doesn't exit immediately
        // This gives time to receive published messages
        try {
            System.out.println("Waiting for messages. Program will exit in 60 seconds...");
            Thread.sleep(60000); // 60 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Clean up before exiting
        pubnub.disconnect();
        System.out.println("Disconnected from PubNub");
        // snippet.end
    }
}
// snippet.end 