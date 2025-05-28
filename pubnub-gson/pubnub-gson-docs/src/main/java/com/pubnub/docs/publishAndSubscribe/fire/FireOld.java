package com.pubnub.docs.publishAndSubscribe.fire;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class FireOld extends SnippetBase {

    private void fireMessageToChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#fire-a-message-to-a-channel-1

        PubNub pubNub = createPubNub();

        // snippet.fireMessageToChannel
        pubNub.fire()
                .message(Arrays.asList("hello", "there"))
                .channel("my_channel")
                .usePOST(true)
                .async(result -> {
                    result.onSuccess((PNPublishResult fireResult) -> {
                        System.out.println("publish worked! timetoken: " + fireResult.getTimetoken());
                    }).onFailure( (PubNubException exception) -> {
                        System.out.println("error happened while calling fire: " + exception.getMessage());
                    });
                });
        // snippet.end
    }
}
