package com.pubnub.api.managers;

import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.ErrorStatus;
import com.pubnub.api.core.PnCallback;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.enums.SubscriptionType;
import com.pubnub.api.core.models.*;
import com.pubnub.api.core.models.consumer_facing.*;
import com.pubnub.api.core.models.server_responses.SubscribeEnvelope;
import com.pubnub.api.core.models.server_responses.SubscribeMessage;
import com.pubnub.api.endpoints.presence.Heartbeat;
import com.pubnub.api.endpoints.presence.Leave;
import com.pubnub.api.endpoints.pubsub.Subscribe;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

import java.util.*;

@Slf4j
public class SubscriptionManager {

    private Map<String, SubscriptionItem> subscribedChannels;
    private Map<String, SubscriptionItem> subscribedChannelGroups;
    private Map<String, Object> stateStorage;
    private List<SubscribeCallback> listeners;
    private Pubnub pubnub;
    private Call<SubscribeEnvelope> subscribeCall;
    private Call<Envelope> heartbeatCall;
    private Long timetoken;
    Timer timer;

    public SubscriptionManager(Pubnub pubnub) {
        this.subscribedChannelGroups = new HashMap<>();
        this.subscribedChannels = new HashMap<>();
        this.pubnub = pubnub;
        this.listeners = new ArrayList<>();
        this.stateStorage = new HashMap<>();

        registerHeartbeatTimer();
    }

    public void addListener(SubscribeCallback listener) {
        listeners.add(listener);
    }

    public void removeListener(SubscribeCallback listener) {
        listeners.remove(listener);
    }

    public final void adaptStateBuilder(List<String> channels, List<String> channelGroups, Object state) {
        for (String channel: channels) {
            stateStorage.put(channel, state);
        }

        for (String channelGroup: channelGroups) {
            stateStorage.put(channelGroup, state);
        }

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
        this.registerHeartbeatTimer();
    }

    public void adaptUnsubscribeBuilder(List<String> channels, List<String> channelGroups) {

        for (String channel: channels) {
            this.subscribedChannels.remove(channel);
            this.stateStorage.remove(channel);
        }

        for (String channelGroup: channelGroups) {
            this.subscribedChannelGroups.remove(channelGroup);
            this.stateStorage.remove(channelGroup);
        }

        Leave.builder().pubnub(pubnub)
            .channels(channels).channelGroups(channelGroups).build()
            .async(new PnCallback<Boolean>() {
            @Override
            public void status(ErrorStatus status) {
                int moose = 10;
            }

            @Override
            public void result(Boolean result) {
                int moose = 11;
            }
        });

        this.startSubscribeLoop();
        this.registerHeartbeatTimer();
    }

    private void registerHeartbeatTimer() {

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                performHeartbeatLoop();
            }
        }, 0, pubnub.getConfiguration().getHeartbeatInterval() * 1000);

    }

    private void startSubscribeLoop() {

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

        subscribeCall = Subscribe.builder().pubnub(pubnub)
                .channels(combinedChannels).channelGroups(combinedChannelGroups).timetoken(timetoken).build()
                .async(new PnCallback<SubscribeEnvelope>() {
                    @Override
                    public void status(ErrorStatus status) {
                        // TODO
                    }

                    @Override
                    public void result(SubscribeEnvelope result) {
                        if (result.getMessages().size() != 0) {
                            processIncomingMessages(result.getMessages());
                        }

                        timetoken = result.getMetadata().getTimetoken();
                        startSubscribeLoop();
                    }
                });
    }

    private void performHeartbeatLoop(){
        log.debug("performingHeartbeatLoop");

        if (heartbeatCall != null && heartbeatCall.isCanceled() == false && heartbeatCall.isExecuted() == false) {
            heartbeatCall.cancel();
        }

        List<String> presenceChannels = new ArrayList<>();
        List<String> presenceChannelGroups = new ArrayList<>();

        for (SubscriptionItem subscriptionItem: subscribedChannels.values()) {
            if  (subscriptionItem.isWithPresence()) {
                presenceChannels.add(subscriptionItem.getName());
            }
        }

        for (SubscriptionItem subscriptionItem: subscribedChannelGroups.values()) {
            if  (subscriptionItem.isWithPresence()) {
                presenceChannelGroups.add(subscriptionItem.getName());
            }
        }

        // if we are not subscribed to any channels with presence, cancel the operation.
        if (presenceChannels.size() != 0 || presenceChannelGroups.size() != 0) {
            heartbeatCall = Heartbeat.builder().pubnub(pubnub)
                    .channels(presenceChannels).channelGroups(presenceChannelGroups).state(stateStorage).build()
                    .async(new PnCallback<Boolean>() {
                        @Override
                        public void status(ErrorStatus status) {
                            // TODO
                            int moose = 10;
                        }

                        @Override
                        public void result(Boolean result) {
                            // TODO
                            int moose = 10;
                        }
                    });
        }


    }

    private void processIncomingMessages(List<SubscribeMessage> messages) {

        for (SubscribeMessage message : messages) {

            String channel = message.getChannel();
            String subscriptionMatch = message.getSubscriptionMatch();

            if (channel.equals(subscriptionMatch)) {
                subscriptionMatch = null;
            }

            if (message.getChannel().contains("-pnpres")) {
                PNPresenceEventResult pnPresenceEventResult = new PNPresenceEventResult();
                PNPresenceEventData pnPresenceEventData = new PNPresenceEventData();
                PNPresenceDetailsData pnPresenceDetailsData = new PNPresenceDetailsData();

                Map<String, Object> presencePayload = (Map<String, Object>) message.getPayload();

                pnPresenceEventData.setPresenceEvent(presencePayload.get("action").toString());
                pnPresenceEventData.setActualChannel((subscriptionMatch != null) ? channel : null);
                pnPresenceEventData.setSubscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel);
                pnPresenceEventData.setPresence(pnPresenceDetailsData);
                pnPresenceEventData.setTimetoken(timetoken);

                pnPresenceDetailsData.setOccupancy((int) presencePayload.get("occupancy"));
                pnPresenceDetailsData.setUuid(presencePayload.get("uuid").toString());
                pnPresenceDetailsData.setTimestamp(Long.valueOf(presencePayload.get("timestamp").toString()));

                pnPresenceEventResult.setData(pnPresenceEventData);
                pnPresenceEventResult.setOperation(PNOperationType.PNSubscribeOperation);

                for (SubscribeCallback subscribeCallback: listeners) {
                    subscribeCallback.presence(this.pubnub, pnPresenceEventResult);
                }
            } else {
                PNMessageResult pnMessageResult = new PNMessageResult();
                PNMessageData pnMessageData = new PNMessageData();

                pnMessageResult.setOperation(PNOperationType.PNSubscribeOperation);
                pnMessageData.setMessage(message.getPayload());

                pnMessageData.setActualChannel((subscriptionMatch != null) ? channel : null);
                pnMessageData.setSubscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel);
                pnMessageData.setTimetoken(timetoken);

                pnMessageResult.setData(pnMessageData);

                for (SubscribeCallback subscribeCallback: listeners) {
                    subscribeCallback.message(this.pubnub, pnMessageResult);
                }

            }
        }
    }

}
