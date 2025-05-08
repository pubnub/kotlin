package com.pubnub.docs.publishAndSubscribe.signal;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.docs.SnippetBase;

public class SignalOld extends SnippetBase {

    private void signalMessageToChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#signal-a-message-to-a-channel-1

        PubNub pubNub = createPubNub();

        // snippet.signalMessageToChannel
        pubNub.signal()
                .message("Hello everyone!")
                .channel("bar")
                .async(result -> {
                    result.onSuccess((PNPublishResult signalResult) -> {
                        Long timetoken = signalResult.getTimetoken(); // signal message timetoken
                    }).onFailure((PubNubException exception) -> {
                        System.out.println("error happened while calling signal: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
