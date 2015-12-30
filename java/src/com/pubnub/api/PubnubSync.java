package com.pubnub.api;

import org.json.JSONObject;

import java.util.UUID;

/**
 * PubnubSync object facilitates querying channels for messages and listening on
 * channels for presence/message events
 *
 * @author Pubnub
 *
 */
public class PubnubSync extends PubnubCoreSync implements PubnubSyncInterfacePam, PubnubSyncInterfacePush {

    /**
     * Pubnub Constructor
     *
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param secret_key
     *            Secret Key
     * @param cipher_key
     *            Cipher Key
     * @param ssl_on
     *            SSL on ?
     */
    public PubnubSync(String publish_key, String subscribe_key, String secret_key, String cipher_key, boolean ssl_on) {
        super(publish_key, subscribe_key, secret_key, cipher_key, ssl_on);
    }

    /**
     * Pubnub Constructor
     *
     * @param publish_key
     *            Publish key
     * @param subscribe_key
     *            Subscribe Key
     * @param secret_key
     *            Secret Key
     * @param ssl_on
     *            SSL on ?
     */
    public PubnubSync(String publish_key, String subscribe_key, String secret_key, boolean ssl_on) {
        super(publish_key, subscribe_key, secret_key, "", ssl_on);
    }

    /**
     * Pubnub Constructor
     *
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     */
    public PubnubSync(String publish_key, String subscribe_key) {
        super(publish_key, subscribe_key, "", "", false);
    }

    /**
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param ssl
     */
    public PubnubSync(String publish_key, String subscribe_key, boolean ssl) {
        super(publish_key, subscribe_key, "", "", ssl);
    }

    /**
     * @param publish_key
     * @param subscribe_key
     * @param secret_key
     */
    public PubnubSync(String publish_key, String subscribe_key, String secret_key) {
        super(publish_key, subscribe_key, secret_key, "", false);
    }

    /**
     *
     * Constructor for Pubnub Class
     *
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param secret_key
     *            Secret Key
     * @param cipher_key
     *            Cipher Key
     * @param ssl_on
     *            SSL enabled ?
     * @param initialization_vector
     *            Initialization vector
     */

    public PubnubSync(String publish_key, String subscribe_key, String secret_key, String cipher_key, boolean ssl_on,
            String initialization_vector) {
        super(publish_key, subscribe_key, secret_key, cipher_key, ssl_on, initialization_vector);
    }

    @Override
    public Object enablePushNotificationsOnChannel(String channel, String gcmRegistrationId) {
        return _enablePushNotificationsOnChannels(new String[] { channel }, gcmRegistrationId, null, true);
    }

    @Override
    public Object enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId) {
        return _enablePushNotificationsOnChannels(channels, gcmRegistrationId, null, true);
    }

    @Override
    public Object disablePushNotificationsOnChannel(String channel, String gcmRegistrationId) {
        return _disablePushNotificationsOnChannels(new String[] { channel }, gcmRegistrationId, null, true);
    }

    @Override
    public Object disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId) {
        return _disablePushNotificationsOnChannels(channels, gcmRegistrationId, null, true);
    }

    protected String getUserAgent() {
        return "Java-Sync/" + VERSION;
    }

    /**
     * Sets value for UUID
     *
     * @param uuid
     *            UUID value for Pubnub client
     */
    public void setUUID(UUID uuid) {
        this.UUID = uuid.toString();
    }

    public String uuid() {
        return java.util.UUID.randomUUID().toString();
    }

}
