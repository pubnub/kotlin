package com.pubnub.docs.publishAndSubscribe.unsubscribe;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.docs.SnippetBase;

import java.util.HashSet;

public class UnsubscribeOthers extends SnippetBase {
    private void unsubscribe() throws PubNubException {
        PubNub pubNub = createPubNub();
        Subscription subscription = pubNub.channel("my_channel").subscription();
        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(new HashSet<>());


        https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#methods-7

        // snippet.unsubscribeMethod
        // For subscription
        subscription.unsubscribe();
        // For subscription set
        subscriptionSet.unsubscribe();

        // snippet.end

        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-8

        // snippet.unsubscribeBasic
        // Subscribe to a channel
        subscription.subscribe();

        // Unsubscribe from that channel
        subscription.unsubscribe();
        // snippet.end
    }

}
