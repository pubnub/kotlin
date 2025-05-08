package com.pubnub.docs.publishAndSubscribe.eventListener;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.SnippetBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EventListenersOthers extends SnippetBase {
    private void addEventListeners() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-5

        PubNub pubNub = createPubNub();


        // snippet.addEventListeners
        // Create a subscription to a specific channel
        Subscription subscription = pubNub.channel("my_channel").subscription(SubscriptionOptions.receivePresenceEvents());

        subscription.addListener(new EventListener() {
            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult result) {
                // Log or process message
                System.out.println("Message: " + result.getMessage());
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult result) {
                // Handle presence updates
                // requires a subscription with presence
                System.out.println("Presence userId: " + result.getUuid() + ", Event: " + result.getEvent());
            }

            @Override
            public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult result) {
                // Handle signals
                System.out.println("Signal: " + result.getMessage());
            }

            @Override
            public void messageAction(@NotNull PubNub pubNub, @NotNull PNMessageActionResult result) {
                // Handle message reactions
                System.out.println("Message Reaction: " + result.getData());
            }

            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult result) {
                // Handle file events
                System.out.println("File: " + result.getFile().getName());
            }

            @Override
            public void uuid(@NotNull PubNub pubnub, @NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {
                // Handle uuid metadata events
                System.out.println("UUID: " + pnUUIDMetadataResult.getData().getName());
            }

            @Override
            public void channel(@NotNull PubNub pubnub, @NotNull PNChannelMetadataResult pnChannelMetadataResult) {
                // Handle channel metadata events
                System.out.println("Channel: " + pnChannelMetadataResult.getData().getName());
            }

            @Override
            public void membership(@NotNull PubNub pubnub, @NotNull PNMembershipResult pnMembershipResult) {
                // Handle membership metadata events
                System.out.println("Channel: " + pnMembershipResult.getData().getChannel().getName() + " UUID: "
                        + pnMembershipResult.getData().getUuid());
            }
        });

        // Activate the subscription to start receiving events
        subscription.subscribe();

        // Print a status when successfully subscribed
        System.out.println("Subscribed to channel 'my_channel'");


        Subscription subscription02 = pubNub.channel("my_channel02").subscription();
        Set<Subscription> subscriptionsSet = new HashSet<>();
        subscriptionsSet.add(subscription);
        subscriptionsSet.add(subscription02);

        SubscriptionSet subscriptionSet = pubNub.subscriptionSetOf(subscriptionsSet);

        // add global listener that listen on all subscribed channels
        pubNub.addListener(new EventListener() {
            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                System.out.println("Message: " + message.getMessage());
            }
        });

        // Activate the subscriptionSet to start receiving events
        subscriptionSet.subscribe();
        // snippet.end
    }
}
