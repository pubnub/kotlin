package com.pubnub.docs.messagePersistence;
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/storage-and-playback#paging-history-responses

// snippet.FetchMessagesWithPagingApp

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FetchMessagesWithPagingApp {

    public static void main(String[] args) {
        long timetokenOfLastMessage = 17_474_884_096_044_353L;
        List<String> channels = Arrays.asList("kotlin-ch1", "kotlin-ch2");

        getAllMessages(channels, timetokenOfLastMessage + 1, res -> {
            res.getChannels().forEach((channel, msgs) -> {
                System.out.println("Channel " + channel);
                msgs.forEach(System.out::println);
            });
        });
    }

    private static void getAllMessages(
            List<String> channels,
            long startTimestamp,
            Consumer<PNFetchMessagesResult> callback
    ) {
        UserId userId = null;
        try {
            userId = new UserId("history-demo-user");
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
        PNConfiguration config = PNConfiguration.builder(userId, "demo")
                .publishKey("demo")
                .build();

        PubNub pubnub = PubNub.create(config);

        pubnub.fetchMessages()
                .channels(channels)
                .start(startTimestamp)
                .maximumPerChannel(25)
                .async(result -> result
                        .onSuccess((PNFetchMessagesResult res) -> {
                            // res is your PNFetchMessagesResult
                            if (!res.getChannels().isEmpty()) {
                                // hand off to user callback
                                callback.accept(res);

                                // find next (smaller) timetoken and recurse
                                long nextStart = startTimestamp;
                                for (List<PNFetchMessageItem> msgs : res.getChannels().values()) {
                                    for (PNFetchMessageItem msg : msgs) {
                                        Long tt = msg.getTimetoken();
                                        if (tt != null && tt < nextStart) {
                                            nextStart = tt;
                                        }
                                    }
                                }
                                getAllMessages(channels, nextStart, callback);
                            }
                        })
                        .onFailure((PubNubException exception) -> {
                            // handle error
                            System.err.println("Fetch error: " + exception.getMessage());
                        })
                );
    }
}
//snippet.end
