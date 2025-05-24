package com.pubnub.docs.messageReactions;
// https://www.pubnub.com/docs/sdks/java/api-reference/message-actions#basic-usage

// snippet.messageReactionsApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;

public class MessageReactionsApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Create a message action
        PNMessageAction messageAction = new PNMessageAction()
                .setType("reaction")
                .setValue("smiley_face")
                .setMessageTimetoken(15701761818730000L); // Replace with actual message timetoken

        // Add message action
        pubnub.addMessageAction()
                .channel("my_channel")
                .messageAction(messageAction)
                .async(result -> {
                    result.onSuccess(res -> {
                        System.out.println("Type: " + res.getType());
                        System.out.println("Value: " + res.getValue());
                        System.out.println("UUID: " + res.getUuid());
                        System.out.println("Action Timetoken: " + res.getActionTimetoken());
                        System.out.println("Message Timetoken: " + res.getMessageTimetoken());
                    }).onFailure(exception -> {
                        System.out.println("Error adding message action: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
