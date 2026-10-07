package com.pubnub.docs.dataSync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.entities.DataSyncEntity;
import com.pubnub.api.java.v2.subscriptions.Subscription;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncChannelEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncEntityEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncMembershipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncRelationshipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDeleteDataSyncUserEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncChannelEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncEntityEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncMembershipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncRelationshipEventMessage;
import com.pubnub.api.models.consumer.pubsub.datasync.PNSetDataSyncUserEventMessage;
import com.pubnub.docs.SnippetBase;
import org.jetbrains.annotations.NotNull;

public class DataSyncEventsOther extends SnippetBase {
    private void dataSyncEventListener() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#listen-for-events

        PubNub pubnub = createPubNub();

        // snippet.dataSyncEventListener
        Subscription subscription = pubnub.channel("product-sneaker-42").subscription();

        subscription.addListener(new EventListener() {
            @Override
            public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
                PNDataSyncEventMessage message = result.getExtractedMessage();
                if (message instanceof PNSetDataSyncUserEventMessage) {
                    PNSetDataSyncUserEventMessage set = (PNSetDataSyncUserEventMessage) message;
                    System.out.println("User " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncChannelEventMessage) {
                    PNSetDataSyncChannelEventMessage set = (PNSetDataSyncChannelEventMessage) message;
                    System.out.println("Channel " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncMembershipEventMessage) {
                    PNSetDataSyncMembershipEventMessage set = (PNSetDataSyncMembershipEventMessage) message;
                    System.out.println("Membership " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncEntityEventMessage) {
                    PNSetDataSyncEntityEventMessage set = (PNSetDataSyncEntityEventMessage) message;
                    System.out.println("Entity " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNSetDataSyncRelationshipEventMessage) {
                    PNSetDataSyncRelationshipEventMessage set = (PNSetDataSyncRelationshipEventMessage) message;
                    System.out.println("Relationship " + set.getData().getId() + " " + set.getEvent());
                } else if (message instanceof PNDeleteDataSyncUserEventMessage) {
                    PNDeleteDataSyncUserEventMessage deleted = (PNDeleteDataSyncUserEventMessage) message;
                    System.out.println("User " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncChannelEventMessage) {
                    PNDeleteDataSyncChannelEventMessage deleted = (PNDeleteDataSyncChannelEventMessage) message;
                    System.out.println("Channel " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncMembershipEventMessage) {
                    PNDeleteDataSyncMembershipEventMessage deleted = (PNDeleteDataSyncMembershipEventMessage) message;
                    System.out.println("Membership " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncEntityEventMessage) {
                    PNDeleteDataSyncEntityEventMessage deleted = (PNDeleteDataSyncEntityEventMessage) message;
                    System.out.println("Entity " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else if (message instanceof PNDeleteDataSyncRelationshipEventMessage) {
                    PNDeleteDataSyncRelationshipEventMessage deleted = (PNDeleteDataSyncRelationshipEventMessage) message;
                    System.out.println("Relationship " + deleted.getId() + " deleted at " + deleted.getDeletedAt());
                } else {
                    System.out.println("Unknown DataSync event of type " + message.getType());
                }
            }
        });

        subscription.subscribe();
        // snippet.end
    }

    private void subscribeToProjectionChannels() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/data-sync#subscribe-to-projection-channels

        PubNub pubnub = createPubNub();

        // snippet.subscribeToProjectionChannels
        DataSyncEntity entity = pubnub.dataSyncEntity("product-sneaker-42");

        Subscription defaultSubscription = entity.subscription();
        Subscription adminSubscription = entity.subscription("admin");

        defaultSubscription.subscribe();
        adminSubscription.subscribe();
        // snippet.end
    }
}
