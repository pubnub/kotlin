package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;
import com.pubnub.docs.SnippetBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class DataSyncSubscribeOther extends SnippetBase {
    private void dataSyncUserSubscription() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#create-a-subscription-datasync-user

        PubNub pubnub = createPubNub();

        // snippet.dataSyncUserSubscription
        Subscription subscription = pubnub.dataSyncUser("user-alice").subscription();
        subscription.subscribe();
        // snippet.end
    }

    private void dataSyncChannelSubscription() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#create-a-subscription-datasync-channel

        PubNub pubnub = createPubNub();

        // snippet.dataSyncChannelSubscription
        Subscription subscription = pubnub.dataSyncChannel("channel-summer-sale").subscription();
        subscription.subscribe();
        // snippet.end
    }

    private void dataSyncEntitySubscription() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#create-a-subscription-datasync-entity

        PubNub pubnub = createPubNub();

        // snippet.dataSyncEntitySubscription
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription();
        subscription.subscribe();
        // snippet.end
    }

    private void dataSyncProjectionSubscription() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#create-a-subscription-datasync-projection

        PubNub pubnub = createPubNub();

        // snippet.dataSyncProjectionSubscription
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription("admin");
        subscription.subscribe();
        // snippet.end
    }

    private void addDataSyncListener() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#add-datasync-listeners

        PubNub pubnub = createPubNub();

        // snippet.addDataSyncListener
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription();

        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType());
            }
        });

        subscription.subscribe();
        // snippet.end
    }

    private void addDataSyncListenerLambda() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#add-datasync-listeners

        PubNub pubnub = createPubNub();

        // snippet.addDataSyncListenerLambda
        Subscription subscription = pubnub.dataSyncEntity("product-sneaker-42").subscription();

        subscription.setOnDataSync(result ->
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType()));

        subscription.subscribe();
        // snippet.end
    }

    private void addDataSyncListenerClient() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#add-datasync-listeners

        PubNub pubnub = createPubNub();

        // snippet.addDataSyncListenerClient
        pubnub.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType());
            }
        });

        pubnub.dataSyncEntity("product-sneaker-42").subscription().subscribe();
        // snippet.end
    }

    private void addDataSyncListenerSubscriptionSet() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#add-datasync-listeners

        PubNub pubnub = createPubNub();

        // snippet.addDataSyncListenerSubscriptionSet
        Set<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(pubnub.dataSyncUser("user-alice").subscription());
        subscriptions.add(pubnub.dataSyncEntity("product-sneaker-42").subscription());

        SubscriptionSet subscriptionSet = pubnub.subscriptionSetOf(subscriptions);

        subscriptionSet.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                System.out.println("DataSync event on " + result.getChannel() + ": " + result.getExtractedMessage().getType());
            }
        });

        subscriptionSet.subscribe();
        // snippet.end
    }
}
