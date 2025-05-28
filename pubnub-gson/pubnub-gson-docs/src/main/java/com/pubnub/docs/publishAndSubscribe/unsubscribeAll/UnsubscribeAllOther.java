package com.pubnub.docs.publishAndSubscribe.unsubscribeAll;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.SnippetBase;

public class UnsubscribeAllOther extends SnippetBase {

    private void unsubscribeAll() throws PubNubException {


        PubNub pubNub = createPubNub();
        Subscription subscription = pubNub.channel("my_channel").subscription();

        // snippet.unsubscribeAllMethod
        pubNub.unsubscribeAll();
        // snippet.end

        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-9

        // snippet.unsubscribeAllBasic
        // Subscribe to channels
        Subscription mySubscription01 = pubNub.channel("my_channel").subscription(SubscriptionOptions.receivePresenceEvents());

        // Subscribe to a channel group
        Subscription mySubscription02 = pubNub.channelGroup("my_channel_group").subscription();

        // Later, when you want to unsubscribe from all subscriptions
        pubNub.unsubscribeAll();
        // snippet.end
    }
}
