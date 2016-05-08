package com.pubnub.api.managers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubnub.api.Crypto;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.dto.StateOperation;
import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.builder.dto.UnsubscribeOperation;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.presence.Heartbeat;
import com.pubnub.api.endpoints.presence.Leave;
import com.pubnub.api.endpoints.pubsub.Subscribe;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.server.Envelope;
import com.pubnub.api.models.server.PresenceEnvelope;
import com.pubnub.api.models.server.SubscribeEnvelope;
import com.pubnub.api.models.server.SubscribeMessage;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;

import java.io.IOException;
import java.util.*;

@Slf4j
public class SubscriptionManager {


    private List<SubscribeCallback> listeners;
    private PubNub pubnub;
    private Call<SubscribeEnvelope> subscribeCall;
    private Call<Envelope> heartbeatCall;
    /**
     * Store the latest timetoken to subscribe with, null by default to get the latest timetoken.
     */
    private Long timetoken;

    /**
     * Keep track of Region to support PSV2 specification.
     */
    private String region;

    /**
     * Timer for heartbeat operations.
     */
    private Timer timer;

    private ObjectMapper mapper;


    private StateManager subscriptionState;

    public SubscriptionManager(PubNub pubnub) {

        this.subscriptionState = new StateManager();
        this.pubnub = pubnub;
        this.listeners = new ArrayList<>();

        this.mapper = new ObjectMapper();

    }


    public final synchronized void addListener(SubscribeCallback listener) {
        listeners.add(listener);
    }

    public final synchronized void removeListener(SubscribeCallback listener) {
        listeners.remove(listener);
    }

    public final synchronized void reconnect() {
        this.startSubscribeLoop();
        this.registerHeartbeatTimer();
    }

    public synchronized void stop() {
        stopHeartbeatTimer();
        stopSubscribeLoop();
    }

    public final synchronized void adaptStateBuilder(final StateOperation stateOperation) {
        this.subscriptionState.adaptStateBuilder(stateOperation);
        reconnect();
    }

    public final synchronized void adaptSubscribeBuilder(final SubscribeOperation subscribeOperation) {
        this.subscriptionState.adaptSubscribeBuilder(subscribeOperation);

        if (subscribeOperation.getTimetoken() != null) {
            this.timetoken = subscribeOperation.getTimetoken();
        }

        reconnect();
    }

    public final synchronized void adaptUnsubscribeBuilder(final UnsubscribeOperation unsubscribeOperation) {
        this.subscriptionState.adaptUnsubscribeBuilder(unsubscribeOperation);

        new Leave(pubnub)
            .channels(unsubscribeOperation.getChannels()).channelGroups(unsubscribeOperation.getChannelGroups())
            .async(new PNCallback<Boolean>() {
                @Override
                public void onResponse(final Boolean result, final PNStatus status) {
                    announce(status);
                }
        });

        reconnect();
    }

    private void registerHeartbeatTimer() {
        // make sure only one timer is running at a time.
        stopHeartbeatTimer();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                performHeartbeatLoop();
            }
        }, 0, pubnub.getConfiguration().getHeartbeatInterval() * 1000);

    }

    private void stopHeartbeatTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void startSubscribeLoop() {
        // this function can be called from different points, make sure any old loop is closed
        stopSubscribeLoop();

        List<String> combinedChannels = this.subscriptionState.prepareChannelList(true);
        List<String> combinedChannelGroups = this.subscriptionState.prepareChannelGroupList(true);

        // do not start the subscribe loop if we have no channels to subscribe to.
        if (combinedChannels.isEmpty() && combinedChannelGroups.isEmpty()) {
            return;
        }

        subscribeCall =  new Subscribe(pubnub)
                .channels(combinedChannels).channelGroups(combinedChannelGroups)
                .timetoken(timetoken).region(region)
                .filterExpression(pubnub.getConfiguration().getFilterExpression())
                .async(new PNCallback<SubscribeEnvelope>() {
                    @Override
                    public void onResponse(final SubscribeEnvelope result, final PNStatus status) {
                        if (status.isError()) {

                            if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                                startSubscribeLoop();
                            } else {
                                announce(status);
                            }

                            return;
                        }

                        if (result.getMessages().size() != 0) {
                            processIncomingMessages(result.getMessages());
                        }

                        timetoken = result.getMetadata().getTimetoken();
                        region = result.getMetadata().getRegion();
                        startSubscribeLoop();
                    }
                });
    }

    private void stopSubscribeLoop() {
        if (subscribeCall != null && !subscribeCall.isExecuted() && !subscribeCall.isCanceled()) {
            subscribeCall.cancel();
        }
    }

    private void performHeartbeatLoop() {
        if (heartbeatCall != null && !heartbeatCall.isCanceled() && !heartbeatCall.isExecuted()) {
            heartbeatCall.cancel();
        }

        List<String> presenceChannels = this.subscriptionState.prepareChannelList(false);
        List<String> presenceChannelGroups = this.subscriptionState.prepareChannelGroupList(false);
        Map<String, Object> stateStorage = this.subscriptionState.createStatePayload();

        // do not start the loop if we do not have any presence channels or channel groups enabled.
        if (presenceChannels.isEmpty() && presenceChannelGroups.isEmpty()) {
            return;
        }

        heartbeatCall = new Heartbeat(pubnub)
                .channels(presenceChannels).channelGroups(presenceChannelGroups).state(stateStorage)
                .async(new PNCallback<Boolean>() {
                    @Override
                    public void onResponse(Boolean result, PNStatus status) {
                        PNHeartbeatNotificationOptions heartbeatVerbosity = pubnub
                                .getConfiguration().getHeartbeatNotificationOptions();

                        if (status.isError()) {
                            if (heartbeatVerbosity == PNHeartbeatNotificationOptions.All
                                    || heartbeatVerbosity == PNHeartbeatNotificationOptions.Failures) {
                                announce(status);
                            }

                        } else {
                            if (heartbeatVerbosity == PNHeartbeatNotificationOptions.All) {
                                announce(status);
                            }
                        }
                    }
                });
    }

    private void processIncomingMessages(final List<SubscribeMessage> messages) {

        for (SubscribeMessage message : messages) {

            String channel = message.getChannel();
            String subscriptionMatch = message.getSubscriptionMatch();

            if (channel.equals(subscriptionMatch)) {
                subscriptionMatch = null;
            }

            if (message.getChannel().contains("-pnpres")) {
                PresenceEnvelope presencePayload = mapper.convertValue(message.getPayload(), PresenceEnvelope.class);

                PNPresenceEventResult pnPresenceEventResult = PNPresenceEventResult.builder()
                        .event(presencePayload.getAction())
                        .actualChannel((subscriptionMatch != null) ? channel : null)
                        .subscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel)
                        .timetoken(timetoken)
                        .occupancy(presencePayload.getOccupancy())
                        .uuid(presencePayload.getUuid())
                        .timestamp(presencePayload.getTimestamp())
                        .build();

                announce(pnPresenceEventResult);
            } else {
                Object extractedMessage = processMessage(message.getPayload());

                if (extractedMessage == null) {
                    log.debug("unable to parse payload on #processIncomingMessages");
                }

                PNMessageResult pnMessageResult = PNMessageResult.builder()
                    .message(extractedMessage)
                    .actualChannel((subscriptionMatch != null) ? channel : null)
                    .subscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel)
                    .timetoken(timetoken)
                    .build();


                announce(pnMessageResult);
            }
        }
    }

    private Object processMessage(final Object input) {
        if (pubnub.getConfiguration().getCipherKey() == null) {
            return input;
        }

        Crypto crypto = new Crypto(pubnub.getConfiguration().getCipherKey());
        String outputText;
        Object outputObject;

        try {
            outputText = crypto.decrypt(input.toString());
        } catch (PubNubException e) {
            PNStatus pnStatus = PNStatus.builder().error(true)
                    .errorData(new PNErrorData(e.getMessage(), e))
                    .operation(PNOperationType.PNSubscribeOperation)
                    .category(PNStatusCategory.PNDecryptionErrorCategory)
                    .build();

            announce(pnStatus);
            return null;
        }

        try {
            outputObject = mapper.readValue(outputText, JsonNode.class);
        } catch (IOException e) {
            PNStatus pnStatus = PNStatus.builder().error(true)
                    .errorData(new PNErrorData(e.getMessage(), e))
                    .operation(PNOperationType.PNSubscribeOperation)
                    .category(PNStatusCategory.PNMalformedResponseCategory)
                    .build();

            announce(pnStatus);
            return null;
        }

        return outputObject;
    }

    /**
     * announce a PNStatus to listeners.
     * @param status PNStatus which will be broadcast to listeners.
     */
    private void announce(final PNStatus status) {
        for (SubscribeCallback subscribeCallback: listeners) {
            subscribeCallback.status(this.pubnub, status);
        }
    }

    private void announce(final PNMessageResult message) {
        for (SubscribeCallback subscribeCallback: listeners) {
            subscribeCallback.message(this.pubnub, message);
        }
    }

    private void announce(final PNPresenceEventResult presence) {
        for (SubscribeCallback subscribeCallback: listeners) {
            subscribeCallback.presence(this.pubnub, presence);
        }
    }

}
