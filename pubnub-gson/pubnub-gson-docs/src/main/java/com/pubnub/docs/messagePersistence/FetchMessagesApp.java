package com.pubnub.docs.messagePersistence;
// https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#basic-usage

// snippet.fetchMessagesApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.models.consumer.history.PNFetchMessageItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FetchMessagesApp {
    public static void main(String[] args) throws PubNubException {
        // Configure PubNub instance
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("demoUserId"), "demo");
        configBuilder.publishKey("demo");
        configBuilder.logVerbosity(PNLogVerbosity.BODY);
        configBuilder.secure(true);

        PubNub pubnub = PubNub.create(configBuilder.build());

        // Fetch historical messages
        pubnub.fetchMessages()
                .channels(Arrays.asList("my_channel"))
                .maximumPerChannel(25)
                .includeMessageActions(true)
                .includeMeta(true)
                .includeMessageType(true)
                .includeCustomMessageType(true)
                .includeUUID(true)
                .async(result -> {
                    result.onSuccess(res -> {
                        Map<String, List<PNFetchMessageItem>> channels = res.getChannels();
                        for (PNFetchMessageItem messageItem : channels.get("my_channel")) {
                            System.out.println("Message: " + messageItem.getMessage());
                            System.out.println("Meta: " + messageItem.getMeta());
                            System.out.println("Timetoken: " + messageItem.getTimetoken());
                            System.out.println("Message Type: " + messageItem.getMessageType());
                            System.out.println("UUID: " + messageItem.getUuid());
                            Map<String, Map<String, List<PNFetchMessageItem.Action>>> actions = messageItem.getActions();
                            for (String type : actions.keySet()) {
                                System.out.println("Action type: " + type);
                                for (String value : actions.get(type).keySet()) {
                                    System.out.println("Action value: " + value);
                                    for (PNFetchMessageItem.Action action : actions.get(type).get(value)) {
                                        System.out.println("Action timetoken: " + action.getActionTimetoken());
                                        System.out.println("Action publisher: " + action.getUuid());
                                    }
                                }
                            }
                        }
                    }).onFailure(exception -> {
                        System.out.println("Error fetching messages: " + exception.getMessage());
                    });
                });
    }
}
// snippet.end
