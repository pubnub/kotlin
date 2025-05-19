package com.pubnub.docs.messagePersistence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DeprecatedHistoryApp {
    public static void main(String[] args) {
        long timetokenOfLastMessage = 17_474_884_096_044_353L;
        String channel = "kotlin-ch1"; // Can only fetch history for one channel at a time.

        getAllMessages(channel, timetokenOfLastMessage + 1, 100, res -> {
            for (PNHistoryItemResult item : res.getMessages()) {
                System.out.println(item.getEntry().toString()); // your JSON payload
            }
        });
    }

    private static void getAllMessages(
            String channel,
            long startTimestamp,
            int count,
            Consumer<PNHistoryResult> callback
    ) {
        UserId userId;
        try {
            userId = new UserId("history-demo-user");
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }

        PNConfiguration config = PNConfiguration.builder(userId, "demo")
                .publishKey("demo")
                .build();

        PubNub pubnub = PubNub.create(config);

        pubnub.history()
                .channel(channel)
                .count(count)
                .start(startTimestamp)
                .reverse(false)
                .includeTimetoken(true)
                .async(result -> result
                        .onSuccess((PNHistoryResult res) -> {
                            if (!res.getMessages().isEmpty()) {
                                callback.accept(res);

                                // Recurse with newer start timestamp
                                long nextStart = res.getMessages().stream()
                                        .map(PNHistoryItemResult::getTimetoken)
                                        .filter(tt -> tt != null && tt > 0)
                                        .min(Long::compareTo)
                                        .orElse(startTimestamp);

                                // Stop recursion if no new earlier timetoken
                                if (nextStart < startTimestamp) {
                                    getAllMessages(channel, nextStart, count, callback);
                                }
                            }
                        })
                        .onFailure((PubNubException exception) -> {
                            System.err.println("Fetch error: " + exception.getMessage());
                        })
                );
    }
}
