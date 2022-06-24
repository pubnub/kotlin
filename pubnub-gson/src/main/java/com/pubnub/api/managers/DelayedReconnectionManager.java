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
    private Timer timer;

    public DelayedReconnectionManager(PubNub pubnub) {
        this.pubnub = pubnub;
        this.pnReconnectionPolicy = pubnub.getConfiguration().getReconnectionPolicy();
    }

    public void scheduleDelayedReconnection() {
        stop();
        if (isReconnectionPolicyUndefined()) {
            return;
        }

        timer = new Timer("Delayed Reconnection Manager timer", true);
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

    void stop() {
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
        stop();
        callback.onReconnection();
    }
}
