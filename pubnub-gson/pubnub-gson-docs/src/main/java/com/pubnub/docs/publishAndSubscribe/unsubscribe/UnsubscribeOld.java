package com.pubnub.docs.publishAndSubscribe.unsubscribe;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;

public class UnsubscribeOld extends SnippetBase {
    private void unsubscribe() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-14
        PubNub pubNub = createPubNub();

        // snippet.unsubscribe
        pubNub.unsubscribe()
                .channels(Arrays.asList("my_channel"))
                .execute();
        // snippet.end
    }

    private void unsubscribeFromMultipleChannels() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#unsubscribing-from-multiple-channels
        PubNub pubNub = createPubNub();

        // snippet.unsubscribeFromMultipleChannels
        pubNub.unsubscribe()
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3"))
                .execute();
        // snippet.end
    }

    private void unsubscribeFromChannelGroup() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#unsubscribe-from-a-channel-group
        PubNub pubNub = createPubNub();

        // snippet.unsubscribeFromChannelGroup
        pubNub.unsubscribe()
                .channelGroups(Arrays.asList("cg1", "cg2", "cg3"))
                .execute();
        // snippet.end
    }
}
