package com.pubnub.docs.messagePersistence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class DeleteMessages extends SnippetBase {
    private void deleteMessages() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#basic-usage-1

        PubNub pubNub = createPubNub();

        // snippet.deleteMessages
        pubNub.deleteMessages()
                .channels(Arrays.asList("channel_1", "channel_2"))
                .start(1460693607379L)
                .end(1460893617271L)
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void deleteSpecificMessagesFromHistory() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#delete-specific-message-from-history

        PubNub pubNub = createPubNub();

        // snippet.deleteSpecificMessages
        pubNub.deleteMessages()
                .channels(Arrays.asList("channel_1"))
                .start(15526611838554309L)
                .end(15526611838554310L)
                .async(result -> { /* check result */ });
        // snippet.end
    }
}
