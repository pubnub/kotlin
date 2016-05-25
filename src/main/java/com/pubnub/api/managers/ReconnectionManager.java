package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.ReconnectionCallback;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.PNTimeResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public class ReconnectionManager {

    private ReconnectionCallback callback;
    private PubNub pubnub;
    private int INTERVAL = 3000;

    /**
     * Timer for heartbeat operations.
     */
    private Timer timer;

    public ReconnectionManager(PubNub pubnubInstance) {
        this.pubnub = pubnubInstance;
    }

    public ReconnectionManager setReconnectionListener(ReconnectionCallback callback) {
        this.callback = callback;
        return this;
    }

    public void startPolling() {
        if (this.pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.NONE) {
            log.warn("reconnection policy is disabled, please handle reconnection manually.");
            return;
        }

        registerHeartbeatTimer();
    }


    private void registerHeartbeatTimer() {
        // make sure only one timer is running at a time.
        stopHeartbeatTimer();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callTime();
            }
        }, 0, INTERVAL);

    }

    private void stopHeartbeatTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void callTime() {
        pubnub.time().async(new PNCallback<PNTimeResult>() {
            @Override
            public void onResponse(PNTimeResult result, PNStatus status) {
                if (!status.isError()) {
                    stopHeartbeatTimer();
                    callback.onReconnection();
                }
            }
        });
    }

}
