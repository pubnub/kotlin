package com.pubnub.api.managers;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.ReconnectionCallback;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.PNTimeResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public class ReconnectionManager {

    private static final int INTERVAL = 3;
    private static final int MINEXPONENTIALBACKOFF = 1;
    private static final int MAXEXPONENTIALBACKOFF = 32;

    private ReconnectionCallback callback;
    private PubNub pubnub;

    private int exponentialMultiplier = 1;
    private int failedCalls = 0;
    private static final int MILLISECONDS = 1000;

    /**
     * Timer for heartbeat operations.
     */
    private Timer timer;

    public ReconnectionManager(PubNub pubnubInstance) {
        this.pubnub = pubnubInstance;
    }

    public ReconnectionManager setReconnectionListener(ReconnectionCallback reconnectionCallback) {
        this.callback = reconnectionCallback;
        return this;
    }

    public void startPolling() {
        if (this.pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.NONE) {
            log.warn("reconnection policy is disabled, please handle reconnection manually.");
            return;
        }

        exponentialMultiplier = 1;
        failedCalls = 0;

        registerHeartbeatTimer();
    }


    private void registerHeartbeatTimer() {
        // make sure only one timer is running at a time.
        stopHeartbeatTimer();

        if (this.pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.NONE) {
            log.warn("reconnection policy is disabled, please handle reconnection manually.");
            return;
        }

        int  maxRetries = this.pubnub.getConfiguration().getMaximumReconnectionRetries();
        if (maxRetries != -1 && failedCalls >= maxRetries) {
            callback.onMaxReconnectionExhaustion();
            return;
        }

        timer = new Timer();
        int timerInterval = INTERVAL;

        if (pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.EXPONENTIAL) {
            timerInterval = (int) (Math.pow(2, exponentialMultiplier) - 1);
            if (timerInterval > MAXEXPONENTIALBACKOFF) {
                timerInterval = MINEXPONENTIALBACKOFF;
                exponentialMultiplier = 1;
                log.debug("timerInterval > MAXEXPONENTIALBACKOFF at: " + Calendar.getInstance().getTime().toString());
            } else if (timerInterval < 1) {
                timerInterval = MINEXPONENTIALBACKOFF;
            }
            log.debug("timerInterval = " + String.valueOf(timerInterval) + " at: " + Calendar.getInstance().getTime().toString());
        }

        if (pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.LINEAR) {
            timerInterval = INTERVAL;
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callTime();
            }
        }, timerInterval * MILLISECONDS, timerInterval * MILLISECONDS);
    }

    private void stopHeartbeatTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void callTime() {
        try {
            pubnub.time().async(new PNCallback<PNTimeResult>() {
                @Override
                public void onResponse(PNTimeResult result, PNStatus status) {
                    if (!status.isError()) {
                        stopHeartbeatTimer();
                        callback.onReconnection();
                    } else {
                        log.debug("callTime() at: " + Calendar.getInstance().getTime().toString());
                        exponentialMultiplier++;
                        failedCalls++;
                        registerHeartbeatTimer();
                    }
                }
            });
        } catch (Exception error) {
            //
        }
    }
}
