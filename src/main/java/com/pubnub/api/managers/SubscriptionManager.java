package com.pubnub.api.managers;

import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.ErrorStatus;
import com.pubnub.api.core.PnCallback;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.enums.SubscriptionType;
import com.pubnub.api.core.models.*;
import com.pubnub.api.core.models.server_responses.SubscribeEnvelope;
import com.pubnub.api.core.models.server_responses.SubscribeMessage;
import com.pubnub.api.endpoints.pubsub.Subscribe;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionManager {

    private Map<String, SubscriptionItem> subscribedChannels;
    private Map<String, SubscriptionItem> subscribedChannelGroups;
    private List<SubscribeCallback> listeners;
    private Pubnub pubnub;
    private Call<SubscribeEnvelope> subscribeCall;
    private String timetoken;

    public SubscriptionManager(Pubnub pubnub) {
        this.subscribedChannelGroups = new HashMap<>();
        this.subscribedChannels = new HashMap<>();
        this.pubnub = pubnub;
        this.listeners = new ArrayList<>();
    }

    public void addListener(SubscribeCallback listener) {
        listeners.add(listener);
    }

    public void removeListener(SubscribeCallback listener) {
        listeners.remove(listener);
    }

    public void startSubscribeLoop() {

        if (subscribeCall != null && !subscribeCall.isExecuted() && !subscribeCall.isCanceled()) {
            subscribeCall.cancel();
        }

        List<String> combinedChannels = new ArrayList<>();
        List<String> combinedChannelGroups = new ArrayList<>();

        for (SubscriptionItem subscriptionItem: subscribedChannels.values()) {
            combinedChannels.add(subscriptionItem.getName());

            if  (subscriptionItem.isWithPresence()) {
                combinedChannels.add(subscriptionItem.getName().concat("-pnpres"));
            }
        }

        for (SubscriptionItem subscriptionItem: subscribedChannelGroups.values()) {
            combinedChannelGroups.add(subscriptionItem.getName());

            if  (subscriptionItem.isWithPresence()) {
                combinedChannelGroups.add(subscriptionItem.getName().concat("-pnpres"));
            }
        }

        subscribeCall = Subscribe.builder()
                .pubnub(pubnub)
                .channels(combinedChannels)
                .channelGroups(combinedChannelGroups)
                .timetoken(timetoken)
                .build()
                .async(new PnCallback<SubscribeEnvelope>() {
            @Override
            public void status(ErrorStatus status) {
                int moose = 10;
            }

            @Override
            public void result(SubscribeEnvelope result) {
                timetoken = result.getMetadata().getTimetoken();

                if (result.getMessages().size() != 0) {
                    processIncomingMessages(result.getMessages());
                }

                startSubscribeLoop();
            }
        });
    }

    public final void adaptSubscribeBuilder(List<String> channels, List<String> channelGroups, boolean withPresence) {
        for (String channel : channels) {
            SubscriptionItem subscriptionItem = new SubscriptionItem();
            subscriptionItem.setName(channel);
            subscriptionItem.setWithPresence(withPresence);
            subscriptionItem.setType(SubscriptionType.CHANNEL);
            subscribedChannels.put(channel, subscriptionItem);
        }

        for (String channelGroup : channelGroups) {
            SubscriptionItem subscriptionItem = new SubscriptionItem();
            subscriptionItem.setName(channelGroup);
            subscriptionItem.setWithPresence(withPresence);
            subscriptionItem.setType(SubscriptionType.CHANNEL_GROUP);
            subscribedChannelGroups.put(channelGroup, subscriptionItem);
        }

        this.startSubscribeLoop();

    }

    public void adaptUnsubscribeBuilder(List<String> channels, List<String> channelGroups) {

        for (String channel: channels) {
            this.subscribedChannels.remove(channel);
        }

        for (String channelGroup: channelGroups) {
            this.subscribedChannelGroups.remove(channelGroup);
        }

        pubnub.leave().channels(channels).channelGroups(channelGroups).build().async(new PnCallback<Boolean>() {
            @Override
            public void status(ErrorStatus status) {
                int moose = 10;
            }

            @Override
            public void result(Boolean result) {
                int moose = 11;
            }
        });
    }

    private void processIncomingMessages(List<SubscribeMessage> messages) {

        for (SubscribeMessage message : messages) {
            if (message.getChannel().contains("-pnpres")) {
                // TODO
            } else {
                PNMessageResult pnMessageResult = new PNMessageResult();
                PNMessageData pnMessageData = new PNMessageData();

                pnMessageResult.setOperation(PNOperationType.PNSubscribeOperation);
                pnMessageData.setMessage(message.getPayload());

                String channel = message.getChannel();
                String subscriptionMatch = message.getSubscriptionMatch();

                if (channel.equals(subscriptionMatch)) {
                    subscriptionMatch = null;
                }

                pnMessageData.setActualChannel((subscriptionMatch != null) ? channel : null);
                pnMessageData.setSubscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel);
                pnMessageResult.setData(pnMessageData);

                for (SubscribeCallback subscribeCallback: listeners) {
                    subscribeCallback.message(this.pubnub, pnMessageResult);
                }

            }
        }
    }
}
