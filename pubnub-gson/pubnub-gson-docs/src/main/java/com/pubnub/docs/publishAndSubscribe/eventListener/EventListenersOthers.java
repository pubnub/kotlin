package com.pubnub.docs.publishAndSubscribe.eventListener;

import com.google.gson.JsonElement;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.java.v2.subscriptions.SubscriptionSet;
import com.pubnub.api.models.consumer.files.PNDownloadableFile;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.v2.subscriptions.SubscriptionOptions;
import com.pubnub.docs.SnippetBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
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

    private void setEventListenersOneEventType() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-6

        PubNub pubNub = createPubNub();
        Subscription subscription = pubNub.channel("my_channel").subscription();

        // snippet.setEventListeners
        subscription.setOnMessage((PNMessageResult pnMessageResult) -> {
            JsonElement message = pnMessageResult.getMessage();
            String channel1 = pnMessageResult.getChannel();
            String publisher = pnMessageResult.getPublisher();
            String subscription1 = pnMessageResult.getSubscription();
            String customMessageType = pnMessageResult.getCustomMessageType();
            Long timetoken = pnMessageResult.getTimetoken();
        });

        subscription.setOnSignal((PNSignalResult pnSignalResult) -> {
            JsonElement message = pnSignalResult.getMessage();
            String channel = pnSignalResult.getChannel();
            String publisher = pnSignalResult.getPublisher();
            String subscription1 = pnSignalResult.getSubscription();
            String customMessageType = pnSignalResult.getCustomMessageType();
            Long timetoken = pnSignalResult.getTimetoken();
        });

        subscription.setOnMessageAction((PNMessageActionResult pnMessageActionResult) -> {
            PNMessageAction messageAction = pnMessageActionResult.getMessageAction();
            String channel = pnMessageActionResult.getChannel();
            String event = pnMessageActionResult.getEvent();
            String publisher = pnMessageActionResult.getPublisher();
            String subscription1 = pnMessageActionResult.getSubscription();
            Long timetoken = pnMessageActionResult.getTimetoken();
        });

        subscription.setOnFile((PNFileEventResult pnFileEventResult) -> {
            JsonElement message = (JsonElement) pnFileEventResult.getMessage();
            PNDownloadableFile file = pnFileEventResult.getFile();
            String channel = pnFileEventResult.getChannel();
            String publisher = pnFileEventResult.getPublisher();
            String subscription1 = pnFileEventResult.getSubscription();
            String customMessageType = pnFileEventResult.getCustomMessageType();
            Long timetoken = pnFileEventResult.getTimetoken();
        });

        subscription.setOnUuidMetadata((PNUUIDMetadataResult pnUUIDMetadataResult) -> {
            String event = pnUUIDMetadataResult.getEvent();
            PNUUIDMetadata data = pnUUIDMetadataResult.getData();
            String channel = pnUUIDMetadataResult.getChannel();
            String publisher = pnUUIDMetadataResult.getPublisher();
            String subscription1 = pnUUIDMetadataResult.getSubscription();
            Long timetoken = pnUUIDMetadataResult.getTimetoken();
        });

        subscription.setOnChannelMetadata((PNChannelMetadataResult pnChannelMetadataResult) -> {
            String event = pnChannelMetadataResult.getEvent();
            PNChannelMetadata data = pnChannelMetadataResult.getData();
            String channel = pnChannelMetadataResult.getChannel();
            String publisher = pnChannelMetadataResult.getPublisher();
            String subscription1 = pnChannelMetadataResult.getSubscription();
            Long timetoken = pnChannelMetadataResult.getTimetoken();
        });

        subscription.setOnMembership((PNMembershipResult pnMembershipResult) -> {
            String event = pnMembershipResult.getEvent();
            PNMembership data = pnMembershipResult.getData();
            String channel = pnMembershipResult.getChannel();
            String publisher = pnMembershipResult.getPublisher();
            String subscription1 = pnMembershipResult.getSubscription();
            Long timetoken = pnMembershipResult.getTimetoken();
        });

        subscription.setOnPresence((PNPresenceEventResult pnPresenceEventResult) -> {
            String event = pnPresenceEventResult.getEvent();
            Integer occupancy = pnPresenceEventResult.getOccupancy();
            String channel = pnPresenceEventResult.getChannel();
            List<String> join = pnPresenceEventResult.getJoin();
            List<String> leave = pnPresenceEventResult.getLeave();
            JsonElement state = pnPresenceEventResult.getState();
            List<String> timeout = pnPresenceEventResult.getTimeout();
            String subscription1 = pnPresenceEventResult.getSubscription();
            Long timetoken = pnPresenceEventResult.getTimetoken();
        });
        // snippet.end
    }

    private void addConnectionStatusListener() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/publish-and-subscribe#basic-usage-7

        PubNub pubNub = createPubNub();

        // snippet.addConnectionStatusListener
        pubNub.addListener((pubnub, status) -> {
            // Handle connection status updates
            System.out.println("Connection Status: " + status.getCategory());
        });
        // snippet.end
    }
}
