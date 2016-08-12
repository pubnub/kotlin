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

    private ReconnectionCallback callback;
    private PubNub pubnub;
    private static final int MINEXPONENTIALBACKOFF = 1;
    private static final int MAXEXPONENTIALBACKOFF = 32;
    private int timerInterval;
    private int connectionErrors = 1;
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

        registerHeartbeatTimer();
    }


    private void registerHeartbeatTimer() {
        // make sure only one timer is running at a time.
        stopHeartbeatTimer();

        timer = new Timer();

        if (pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.EXPONENTIAL) {
            timerInterval = (int) (Math.pow(2, connectionErrors) - 1);
            if (timerInterval > MAXEXPONENTIALBACKOFF) {
                timerInterval = MINEXPONENTIALBACKOFF;
                connectionErrors = 1;
                log.debug("timerInterval > MAXEXPONENTIALBACKOFF at: " + Calendar.getInstance().getTime().toString());
            } else if (timerInterval < 1) {
                timerInterval = MINEXPONENTIALBACKOFF;
            }
            log.debug("timerInterval = " + String.valueOf(timerInterval) + " at: " + Calendar.getInstance().getTime().toString());
        } else {
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
        pubnub.time().async(new PNCallback<PNTimeResult>() {
            @Override
            public void onResponse(PNTimeResult result, PNStatus status) {
                if (!status.isError()) {
                    connectionErrors = 1;
                    stopHeartbeatTimer();
                    callback.onReconnection();
                } else if (pubnub.getConfiguration().getReconnectionPolicy() == PNReconnectionPolicy.EXPONENTIAL) {
                    log.debug("callTime() at: " + Calendar.getInstance().getTime().toString());
                    stopHeartbeatTimer();
                    connectionErrors++;
                    registerHeartbeatTimer();
                }
            }
        });
    }
}
