package com.pubnub.docs.messageReactions;
// https://www.pubnub.com/docs/sdks/java/api-reference/message-actions#fetch-messages-with-paging

// snippet.getMessageActionsWithPagingApp
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;

import java.util.List;
import java.util.function.Consumer;

public class GetMessageActionsWithPagingApp {

    public static void main(String[] args) {
        long startTimetoken = System.currentTimeMillis() * 10_000L;
        getAllMessageActions(
                "my_channel",
                startTimetoken,
                actions -> System.out.println("Next set of actions: " + actions.size())
        );
    }

    /**
     * Fetches 5 message reactions at a time, recursively and in a paged manner.
     *
     * @param channel  The channel where the message is published, to fetch message reactions from.
     * @param start    The timetoken which indicates from where to start fetching message reactions.
     * @param callback Consumer to handle each page of fetched message actions.
     */
    private static void getAllMessageActions(
            String channel,
            long start,
            Consumer<List<PNMessageAction>> callback
    ) {
        UserId userId;
        try {
            userId = new UserId("demoUserId");
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
        PNConfiguration config = PNConfiguration.builder(userId, "demo")
                .publishKey("demo")
                .logVerbosity(PNLogVerbosity.BODY)
                .secure(true)
                .build();

        PubNub pubnub = PubNub.create(config);

        pubnub.getMessageActions()
                .channel(channel)
                .start(start)
                .limit(5)
                .async(result -> result
                        .onSuccess(res -> {
                            if (!res.getActions().isEmpty()) {
                                callback.accept(res.getActions());
                                getAllMessageActions(
                                        channel,
                                        res.getActions().get(0).getActionTimetoken(),
                                        callback
                                );
                            }
                        })
                        .onFailure((PubNubException exception) -> {
                            // handle error
                            System.err.println("Fetch error: " + exception.getMessage());
                        })
                );
    }
}
// snippet.end
