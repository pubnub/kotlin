package com.pubnub.api;

import org.json.JSONObject;

/**
 * Abstract class to be subclassed by objects being passed as callbacks to
 * Pubnub APIs Default implementation for all methods is blank
 *
 * @author Pubnub
 *
 */
public abstract class Callback {

    /**
     * This callback will be invoked when a message is received on the channel
     *
     * @param channel
     *            Channel Name
     * @param message
     *            Message
     *
     */
    public void successCallback(String channel, Object message) {

    }

    /**
     * This callback will be invoked when a message is received on the channel
     *
     * @param channel
     *            Channel Name
     * @param message
     *            Message
     * @param timetoken
     *            Timetoken
     */
    public void successCallback(String channel, Object message, String timetoken) {

    }

    /**
     * This callback will be invoked when a message is received on the channel
     *
     * @param message
     *            Message
     */
    public void successCallbackV2(String channel, Object message, JSONObject envelope) {

    }


    void successWrapperCallback(String channel, Object message, String timetoken) {
        successCallback(channel, message);
        successCallback(channel, message, timetoken);
    }

    void successWrapperCallbackV2(String channel, Object message, JSONObject envelope, String timetoken) {
        successCallback(channel, message);
        successCallback(channel, message, timetoken);
        successCallbackV2(channel, message, envelope);
    }

    /**
     * This callback will be invoked when an error occurs
     *
     * @param channel
     *            Channel Name
     * @param error
     *            error
     */
    public void errorCallback(String channel, PubnubError error) {
        errorCallback(channel,error.toString());
    }

    /**
     * This callback will be invoked when an error occurs
     *
     * @param channel
     *            Channel Name
     * @param message
     *            Message
     *@deprecated as of version 3.5.2 and will be removed with 3.6.0 .
     *            Replaced by {@link #errorCallback(String channel, PubnubError error)}
     */
    public void errorCallback(String channel, Object message) {

    }

    /**
     * This callback will be invoked on getting connected to a channel
     *
     * @param channel
     *            Channel Name
     */
    public void connectCallback(String channel, Object message) {
    }

    /**
     * This callback is invoked on getting reconnected to a channel after
     * getting disconnected
     *
     * @param channel
     *            Channel Name
     */
    public void reconnectCallback(String channel, Object message) {
    }

    /**
     * This callback is invoked on getting disconnected from a channel
     *
     * @param channel
     *            Channel Name
     */
    public void disconnectCallback(String channel, Object message) {
    }

}
