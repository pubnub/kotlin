package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.ReconnectionCallback;
import com.pubnub.api.enums.PNReconnectionPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class DelayedReconnectionManager {
    private static final int DELAY_SECONDS = 3;
    private static final int MILLISECONDS = 1000;

    private final PNReconnectionPolicy pnReconnectionPolicy;
    private ReconnectionCallback callback;
    private PubNub pubnub;

    /**
     * Timer for heartbeat operations.
     */
    protected Timer timer;

    public DelayedReconnectionManager(PubNub pubnub) {
        this.pubnub = pubnub;
        this.pnReconnectionPolicy = pubnub.getConfiguration().getReconnectionPolicy();
    }

    public void scheduleDelayedReconnection() {
        stopHeartbeatTimer();
        if (isReconnectionPolicyUndefined()) {
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callTime();
            }
        }, DELAY_SECONDS * MILLISECONDS);
    }

    public void setReconnectionListener(ReconnectionCallback reconnectionCallback) {
        this.callback = reconnectionCallback;
    }

    private void stopHeartbeatTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private boolean isReconnectionPolicyUndefined() {
        if (pnReconnectionPolicy == null || pnReconnectionPolicy == PNReconnectionPolicy.NONE) {
            log.warn("reconnection policy is disabled, please handle reconnection manually.");
            return true;
        }
        return false;
    }

    private void callTime() {
        stopHeartbeatTimer();
        callback.onReconnection();
    }
}
