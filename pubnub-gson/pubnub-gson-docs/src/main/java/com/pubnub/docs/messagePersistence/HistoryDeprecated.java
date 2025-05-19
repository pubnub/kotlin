package com.pubnub.docs.messagePersistence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.docs.SnippetBase;

public class HistoryDeprecated extends SnippetBase {
    private void historyThreeOldestMessages() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#use-history-to-retrieve-the-three-oldest-messages-by-retrieving-from-the-time-line-in-reverse

        PubNub pubNub = createPubNub();

        // snippet.historyThreeOldestMessages
        pubNub.history()
                .channel("my_channel") // where to fetch history from
                .count(3) // how many items to fetch
                .reverse(true) // should go in reverse?
                .async(result -> {
                    result.onSuccess((PNHistoryResult res) -> {
                        for (PNHistoryItemResult pnHistoryItemResult: res.getMessages()) {
                            pnHistoryItemResult.getEntry(); // custom JSON structure for message
                        }
                    });
                });
        // snippet.end
    }

    private void historyRetrieveMessagesNewerThanGivenTimetoken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#use-history-to-retrieve-messages-newer-than-a-given-timetoken-by-paging-from-oldest-message-to-newest-message-starting-at-a-single-point-in-time-exclusive

        PubNub pubNub = createPubNub();

        // snippet.historyRetrieveMessagesNewerThanGivenTimetoken
        pubNub.history()
                .channel("my_channel") // where to fetch history from
                .start(13847168620721752L) // first timestamp
                .reverse(true) // should go in reverse?
                .async(result -> {
                    result.onSuccess((PNHistoryResult res) -> {
                        for (PNHistoryItemResult pnHistoryItemResult: res.getMessages()) {
                            pnHistoryItemResult.getEntry(); // custom JSON structure for message
                        }
                    });
                });
        // snippet.end
    }

    private void historyRetrieveMessagesUntilGivenTimetoken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#use-history-to-retrieve-messages-until-a-given-timetoken-by-paging-from-newest-message-to-oldest-message-until-a-specific-end-point-in-time-inclusive

        PubNub pubNub = createPubNub();

        // snippet.historyRetrieveMessagesUntilGivenTimetoken
        pubNub.history()
                .channel("my_channel") // where to fetch history from
                .count(100) // how many items to fetch
                .start(-1L) // first timestamp
                .end(13847168819178600L) // last timestamp
                .reverse(true) // should go in reverse?
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void historyWithIncludeTimetoken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#include-timetoken-in-history-response
        PubNub pubNub = createPubNub();

        // snippet.historyWithIncludeTimetoken
        pubNub.history()
                .channel("history_channel") // where to fetch history from
                .count(100) // how many items to fetch
                .includeTimetoken(true) // include timetoken with each entry
                .async(result -> { /* check result */ });
        // snippet.end

    }
}
