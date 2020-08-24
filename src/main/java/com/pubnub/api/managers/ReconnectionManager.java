package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.ReconnectionCallback;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.PNTimeResult;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public class ReconnectionManager {

    private static final int LINEAR_INTERVAL = 3;
    private static final int MIN_EXPONENTIAL_BACKOFF = 1;
    private static final int MAX_EXPONENTIAL_BACKOFF = 32;

    private static final int MILLISECONDS = 1000;

    private ReconnectionCallback callback;
    private PubNub pubnub;

    private int exponentialMultiplier = 1;
    private int failedCalls = 0;

    private PNReconnectionPolicy pnReconnectionPolicy;
    private int maxConnectionRetries;

    /**
     * Timer for heartbeat operations.
     */
    private Timer timer;

    public ReconnectionManager(PubNub pubnub) {
        this.pubnub = pubnub;
        this.pnReconnectionPolicy = pubnub.getConfiguration().getReconnectionPolicy();
        this.maxConnectionRetries = pubnub.getConfiguration().getMaximumReconnectionRetries();
    }

    public void setReconnectionListener(ReconnectionCallback reconnectionCallback) {
        this.callback = reconnectionCallback;
    }

    public void startPolling() {
        if (isReconnectionPolicyUndefined()) {
            return;
        }

        exponentialMultiplier = 1;
        failedCalls = 0;

        registerHeartbeatTimer();
    }

    private void registerHeartbeatTimer() {
        // make sure only one timer is running at a time.
        stopHeartbeatTimer();

        if (isReconnectionPolicyUndefined()) {
            return;
        }

        if (maxConnectionRetries != -1 && failedCalls >= maxConnectionRetries) { // _what's -1?
            callback.onMaxReconnectionExhaustion();
            return;
        }

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callTime();
            }
        }, getNextInterval() * MILLISECONDS);
    }

    int getNextInterval() {
        int timerInterval = LINEAR_INTERVAL;
        failedCalls++;

        if (pnReconnectionPolicy == PNReconnectionPolicy.EXPONENTIAL) {
            exponentialMultiplier++;
            timerInterval = (int) (Math.pow(2, exponentialMultiplier) - 1);
            if (timerInterval > MAX_EXPONENTIAL_BACKOFF) {
                timerInterval = MIN_EXPONENTIAL_BACKOFF;
                exponentialMultiplier = 1;
                log.debug("timerInterval > MAXEXPONENTIALBACKOFF at: " + Calendar.getInstance().getTime().toString());
            } else if (timerInterval < 1) {
                timerInterval = MIN_EXPONENTIAL_BACKOFF;
            }
            log.debug("timerInterval = " + timerInterval + " at: " + Calendar.getInstance().getTime().toString());
        }

        if (pnReconnectionPolicy == PNReconnectionPolicy.LINEAR) {
            timerInterval = LINEAR_INTERVAL;
        }

        return timerInterval;
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
            public void onResponse(PNTimeResult result, @NotNull PNStatus status) {
                if (!status.isError()) {
                    stopHeartbeatTimer();
                    callback.onReconnection();
                } else {
                    log.debug("callTime() at: " + Calendar.getInstance().getTime().toString());
                    registerHeartbeatTimer();
                }
            }
        });
    }

    private boolean isReconnectionPolicyUndefined() {
        if (pnReconnectionPolicy == null || pnReconnectionPolicy == PNReconnectionPolicy.NONE) {
            log.warn("reconnection policy is disabled, please handle reconnection manually.");
            return true;
        }
        return false;
    }
}
