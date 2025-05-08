package com.pubnub.docs.publishAndSubscribe.subscribe;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.entities.Channel;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.v2.subscriptions.SubscriptionCursor;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.SnippetBase;


import java.util.HashSet;
import java.util.Set;

public class SubscribeOthers extends SnippetBase {
    private void createSubscription() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#create-a-subscription

        PubNub pubnub = createPubNub();

        // snippet.createSubscription
        // Entity-based, local-scoped

        // Specify the channel for subscription
        Channel myChannel = pubnub.channel("channelName");

        // Create subscription options, if any
        SubscriptionOptions options = SubscriptionOptions.receivePresenceEvents();

        // Return a Subscription object that is used to establish the subscription
        Subscription subscription = myChannel.subscription(options);

        // Activate the subscription to start receiving events
        subscription.subscribe();
        // snippet.end
    }

    private void subscribeExample() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#subscribe-1

        PubNub pubNub = createPubNub();
        Channel myChannel = pubNub.channel("channelName");
        Subscription subscription = myChannel.subscription();

        Subscription mySubscription01 = pubNub.channel("my_channel01").subscription();
        Subscription mySubscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(mySubscription01);
        subscriptions.add(mySubscription02);

        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(subscriptions);

        // snippet.subscribe
        // For subscription
        subscription.subscribe();
        // For subscription set
        subscriptionSet.subscribe();
        // snippet.end
    }


    private void subscribeSubscriptionSet() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-3

        PubNub pubNub = createPubNub();

        // snippet.subscribeSubscriptionSet
        // Step 1: Create a subscription set
        Subscription mySubscription01 = pubNub.channel("my_channel01").subscription();
        Subscription mySubscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(mySubscription01);
        subscriptions.add(mySubscription02);

        SubscriptionSet mySubscriptionSet = pubNub.subscriptionSetOf(subscriptions);

        // Step 2: Subscribe using the subscription set
        mySubscriptionSet.subscribe();
        // snippet.end
    }

    private void createSubscriptionSetFrom2subscriptions() {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#create-a-subscription-set-from-2-individual-subscriptions

        PubNub pubNub = createPubNub();

        // snippet.createSubscriptionSetFrom2subscriptions
        // Create subscriptions
        Subscription subscription1 = pubNub.channel("channelName").subscription();
        Subscription subscription2 = pubNub.channelGroup("channelGroup").subscription();
        Subscription subscription3 = pubNub.channel("channelName03").subscription();

        // Combine into a subscription set
        SubscriptionSet subscriptionSet = subscription1.plus(subscription2);

        // Add another subscription to the set
        subscriptionSet.add(subscription3);

        // Remove a subscription from the set
        subscriptionSet.remove(subscription3);
        // snippet.end
    }

    private void subscribeWithTimetoken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-4

        PubNub pubNub = createPubNub();

        // snippet.subscribeWithTimetoken
        // Create subscription options, if any
        SubscriptionOptions options = SubscriptionOptions.receivePresenceEvents();

        // Define the channels to subscribe to
        Subscription subscription01 = pubNub.channel("my_channel01").subscription(options);
        Subscription subscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(subscription01);
        subscriptions.add(subscription02);

        // Create a subscription set with specified channels and subscription options
        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(subscriptions);

        // Define the timetoken for where the subscription should start
        Long yourTimeToken = 100000000000L; // Directly using Long type

        // Subscribe to the created SubscriptionSet with the desired timetoken
        subscriptionSet.subscribe(new SubscriptionCursor(yourTimeToken));
        // snippet.end
    }
}
