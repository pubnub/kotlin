package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.builder.dto.StateOperation;
import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.builder.dto.UnsubscribeOperation;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.ReconnectionCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.presence.Heartbeat;
import com.pubnub.api.endpoints.presence.Leave;
import com.pubnub.api.endpoints.pubsub.Subscribe;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.server.SubscribeEnvelope;
import com.pubnub.api.models.server.SubscribeMessage;
import com.pubnub.api.workers.SubscribeMessageWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SubscriptionManager {

    private static final int HEARTBEAT_INTERVAL_MULTIPLIER = 1000;

    private PubNub pubnub;
    private Subscribe subscribeCall;
    private Heartbeat heartbeatCall;

    private LinkedBlockingQueue<SubscribeMessage> messageQueue;

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

    private StateManager subscriptionState;
    private ListenerManager listenerManager;
    private ReconnectionManager reconnectionManager;
    private RetrofitManager retrofitManager;

    private Thread consumerThread;

    /**
     * lever to indicate if an announcement to the user about the subscription should be made.
     * the announcement happens only after the channel mix has been changed.
     */
    private boolean subscriptionStatusAnnounced;

    public SubscriptionManager(PubNub pubnubInstance, RetrofitManager retrofitManagerInstance) {
        this.pubnub = pubnubInstance;

        this.subscriptionStatusAnnounced = false;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.subscriptionState = new StateManager();

        this.listenerManager = new ListenerManager(this.pubnub);
        this.reconnectionManager = new ReconnectionManager(this.pubnub);
        this.retrofitManager = retrofitManagerInstance;

        this.timetoken = 0L;

        this.reconnectionManager.setReconnectionListener(new ReconnectionCallback() {
            @Override
            public void onReconnection() {
                reconnect();
                PNStatus pnStatus = PNStatus.builder()
                        .error(false)
                        .category(PNStatusCategory.PNReconnectedCategory)
                        .build();

                subscriptionStatusAnnounced = true;
                listenerManager.announce(pnStatus);
            }
        });

        consumerThread = new Thread(new SubscribeMessageWorker(this.pubnub, listenerManager, messageQueue));
        consumerThread.start();
    }

    public final void addListener(SubscribeCallback listener) {
        listenerManager.addListener(listener);
    }

    public final void removeListener(SubscribeCallback listener) {
        listenerManager.removeListener(listener);
    }


    public final synchronized void reconnect() {
        this.startSubscribeLoop();
        this.registerHeartbeatTimer();
    }

    public final synchronized void disconnect() {
        stopHeartbeatTimer();
        stopSubscribeLoop();
    }


    public synchronized void stop() {
        disconnect();
        consumerThread.interrupt();
    }

    public final synchronized void adaptStateBuilder(final StateOperation stateOperation) {
        this.subscriptionState.adaptStateBuilder(stateOperation);
        reconnect();
    }

    public final synchronized void adaptSubscribeBuilder(final SubscribeOperation subscribeOperation) {
        this.subscriptionState.adaptSubscribeBuilder(subscribeOperation);
        // the channel mix changed, on the successful subscribe, there is going to be announcement.
        this.subscriptionStatusAnnounced = false;

        if (subscribeOperation.getTimetoken() != null) {
            this.timetoken = subscribeOperation.getTimetoken();
        }

        reconnect();
    }

    public final synchronized void adaptUnsubscribeBuilder(final UnsubscribeOperation unsubscribeOperation) {
        this.subscriptionState.adaptUnsubscribeBuilder(unsubscribeOperation);

        new Leave(pubnub, this.retrofitManager.getTransactionInstance())
            .channels(unsubscribeOperation.getChannels()).channelGroups(unsubscribeOperation.getChannelGroups())
            .async(new PNCallback<Boolean>() {
                @Override
                public void onResponse(final Boolean result, final PNStatus status) {
                    listenerManager.announce(status);
                }
        });

        // if we unsubscribed from all the channels, reset the timetoken back to zero and remove the region.
        if (this.subscriptionState.isEmpty()) {
            region = null;
            timetoken = 0L;
        }

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
        }, 0, pubnub.getConfiguration().getHeartbeatInterval() * HEARTBEAT_INTERVAL_MULTIPLIER);

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

        subscribeCall = new Subscribe(pubnub, this.retrofitManager.getSubscriptionInstance())
                .channels(combinedChannels).channelGroups(combinedChannelGroups)
                .timetoken(timetoken).region(region)
                .filterExpression(pubnub.getConfiguration().getFilterExpression());

        subscribeCall.async(new PNCallback<SubscribeEnvelope>() {
            @Override
            public void onResponse(final SubscribeEnvelope result, final PNStatus status) {
                if (status.isError()) {

                    if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                        startSubscribeLoop();
                    } else {
                        disconnect();
                        listenerManager.announce(status);

                        // stop all announcements and ask the reconnection manager to start polling for connection restoration..
                        reconnectionManager.startPolling();
                    }

                    return;
                }

                if (!subscriptionStatusAnnounced) {
                    PNStatus pnStatus = PNStatus.builder()
                            .error(false)
                            .category(PNStatusCategory.PNConnectedCategory)
                            .statusCode(status.getStatusCode())
                            .authKey(status.getAuthKey())
                            .operation(status.getOperation())
                            .clientRequest(status.getClientRequest())
                            .origin(status.getOrigin())
                            .tlsEnabled(status.isTlsEnabled())
                            .build();

                    subscriptionStatusAnnounced = true;
                    listenerManager.announce(pnStatus);
                }

                if (result.getMessages().size() != 0) {
                    messageQueue.addAll(result.getMessages());
                }

                timetoken = result.getMetadata().getTimetoken();
                region = result.getMetadata().getRegion();
                startSubscribeLoop();
            }
        });

    }

    private void stopSubscribeLoop() {
        if (subscribeCall != null) {
            subscribeCall.silentCancel();
        }
    }

    private void performHeartbeatLoop() {
        if (heartbeatCall != null) {
            heartbeatCall.silentCancel();
        }

        List<String> presenceChannels = this.subscriptionState.prepareChannelList(false);
        List<String> presenceChannelGroups = this.subscriptionState.prepareChannelGroupList(false);
        Map<String, Object> stateStorage = this.subscriptionState.createStatePayload();

        // do not start the loop if we do not have any presence channels or channel groups enabled.
        if (presenceChannels.isEmpty() && presenceChannelGroups.isEmpty()) {
            return;
        }

        heartbeatCall = new Heartbeat(pubnub, this.retrofitManager.getTransactionInstance())
                .channels(presenceChannels).channelGroups(presenceChannelGroups).state(stateStorage);

        heartbeatCall.async(new PNCallback<Boolean>() {
            @Override
            public void onResponse(Boolean result, PNStatus status) {
                PNHeartbeatNotificationOptions heartbeatVerbosity = pubnub
                        .getConfiguration().getHeartbeatNotificationOptions();

                if (status.isError()) {
                    if (heartbeatVerbosity == PNHeartbeatNotificationOptions.ALL
                            || heartbeatVerbosity == PNHeartbeatNotificationOptions.FAILURES) {
                        listenerManager.announce(status);
                    }

                } else {
                    if (heartbeatVerbosity == PNHeartbeatNotificationOptions.ALL) {
                        listenerManager.announce(status);
                    }
                }
            }
        });

    }

    public final synchronized List<String> getSubscribedChannels() {
        return subscriptionState.prepareChannelList(false);
    }

    public final synchronized List<String> getSubscribedChannelGroups() {
        return subscriptionState.prepareChannelGroupList(false);
    }

    public final synchronized void unsubscribeAll() {
        adaptUnsubscribeBuilder(UnsubscribeOperation.builder()
                .channelGroups(subscriptionState.prepareChannelGroupList(false))
                .channels(subscriptionState.prepareChannelList(false))
                .build());
    }
}
