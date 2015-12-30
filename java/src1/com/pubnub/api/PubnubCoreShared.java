package com.pubnub.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

import static com.pubnub.api.PubnubError.PNERROBJ_SECRET_KEY_MISSING;
import static com.pubnub.api.PubnubError.getErrorObject;

/**
 * Pubnub object facilitates querying channels for messages and listening on
 * channels for presence/message events
 *
 * @author Pubnub
 *
 */

abstract class PubnubCoreShared extends PubnubCoreAsync  implements  PubnubAsyncInterfacePam, PubnubAsyncInterfacePush{

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
    public PubnubCoreShared(String publish_key, String subscribe_key, String secret_key,
                  String cipher_key, boolean ssl_on) {
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
    public PubnubCoreShared(String publish_key, String subscribe_key, String secret_key,
                  boolean ssl_on) {
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
    public PubnubCoreShared(String publish_key, String subscribe_key) {
        super(publish_key, subscribe_key, "", "", false);
    }

    /**
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param ssl
     */
    public PubnubCoreShared(String publish_key, String subscribe_key, boolean ssl) {
        super(publish_key, subscribe_key, "", "", ssl);
    }

    /**
     * @param publish_key
     * @param subscribe_key
     * @param secret_key
     */
    public PubnubCoreShared(String publish_key, String subscribe_key, String secret_key) {
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

    public PubnubCoreShared(String publish_key, String subscribe_key,
                  String secret_key, String cipher_key, boolean ssl_on, String initialization_vector) {
        super(publish_key, subscribe_key, secret_key, cipher_key, ssl_on, initialization_vector);
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

    /**
     * This method sets timeout value for subscribe/presence. Default value is
     * 310000 milliseconds i.e. 310 seconds
     *
     * @param timeout
     *            Timeout value in milliseconds for subscribe/presence
     */
    public void setSubscribeTimeout(int timeout) {
        super.setSubscribeTimeout(timeout);
    }

    /**
     * This method returns timeout value for subscribe/presence.
     *
     * @return Timeout value in milliseconds for subscribe/presence
     */
    public int getSubscribeTimeout() {
        return super.getSubscribeTimeout();
    }

    /**
     * This method set timeout value for non subscribe operations like publish,
     * history, hereNow. Default value is 15000 milliseconds i.e. 15 seconds.
     *
     * @param timeout
     *            Timeout value in milliseconds for Non subscribe operations
     *            like publish, history, hereNow
     */
    public void setNonSubscribeTimeout(int timeout) {
        super.setNonSubscribeTimeout(timeout);
    }
    /**
     * This method returns timeout value for non subscribe operations like publish, history, hereNow
     *
     * @return Timeout value in milliseconds for for Non subscribe operations like publish, history, hereNow
     */
    public int getNonSubscribeTimeout() {
        return super.getNonSubscribeTimeout();
    }

    static String _pamSign(String key, String data) throws PubnubException {
        Mac sha256_HMAC;

        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(),
                    "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hmacData = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            return new String(Base64Encoder.encode(hmacData)).replace('+', '-')
                    .replace('/', '_');
        } catch (InvalidKeyException e1) {
            throw new PubnubException(getErrorObject(PubnubError.PNERROBJ_ULSSIGN_ERROR, 1, "Invalid Key : " + e1.toString()));
        } catch (NoSuchAlgorithmException e1) {
            throw new PubnubException(getErrorObject(PubnubError.PNERROBJ_ULSSIGN_ERROR, 2, "Invalid Algorithm : " + e1.toString()));
        } catch (IllegalStateException e1) {
            throw new PubnubException(getErrorObject(PubnubError.PNERROBJ_ULSSIGN_ERROR, 3, "Invalid State : " + e1.toString()));
        } catch (UnsupportedEncodingException e1) {
            throw new PubnubException(getErrorObject(PubnubError.PNERROBJ_ULSSIGN_ERROR, 4, "Unsupported encoding : " + e1.toString()));
        }
    }


    protected String pamSign(String key, String data) throws PubnubException {
        return _pamSign(key, data);
    }

    /** Grant r/w access based on channel and auth key
     * @param channel
     * @param auth_key
     * @param read
     * @param write
     * @param callback
     */
    public void pamGrant(final String channel, String auth_key, boolean read,
                         boolean write, final Callback callback) {
        pamGrant(channel, auth_key, read, write, -1, callback);
    }

    /** Grant r/w access based on channel
     * @param channel
     * @param read
     * @param write
     * @param callback
     */
    public void pamGrant(final String channel, boolean read,
                         boolean write, final Callback callback) {
        pamGrant(channel, null, read, write, -1, callback);
    }

    /** Grant r/w access based on channel
     * @param channel
     * @param read
     * @param write
     * @param ttl
     * @param callback
     */
    public void pamGrant(final String channel, boolean read,
                         boolean write, int ttl, final Callback callback) {
        pamGrant(channel, null, read, write, ttl, callback);
    }

    /** Grant r/w access based on channel and auth key
     * @param channel
     * @param auth_key
     * @param read
     * @param write
     * @param ttl
     * @param callback
     */
    public void pamGrant(final String channel, String auth_key, boolean read,
                         boolean write, int ttl, Callback callback) {
        _pamGrant(channel, auth_key, read, write, ttl, callback, false);
    }

    public void pamGrantChannelGroup(final String group, boolean read,
                                     boolean management, Callback callback) {
        pamGrantChannelGroup(group, read, management, -1, callback);
    }

    public void pamGrantChannelGroup(final String group, boolean read,
                                     boolean management, int ttl, Callback callback) {
        pamGrantChannelGroup(group, null, read, management, ttl, callback);
    }

    public void pamGrantChannelGroup(final String group, String auth_key, boolean read,
                                     boolean management, Callback callback) {
        pamGrantChannelGroup(group, auth_key, read, management, -1, callback);
    }

    public void pamGrantChannelGroup(final String group, String auth_key, boolean read, boolean management, int ttl,
                                               Callback callback) {
        _pamGrantChannelGroup(group, auth_key, read, management, ttl, callback, false);
    }

    /** ULS Audit
     * @param callback
     */
    public void pamAudit(Callback callback) {
        _pamAudit(null, callback, false);
    }

    /** ULS audit by channel
     * @param channel
     * @param callback
     */
    public void pamAudit(final String channel,
                         Callback callback) {
        _pamAudit(channel, callback, false);
    }

    /** ULS audit by channel and auth key
     * @param channel
     * @param auth_key
     * @param callback
     */
    public void pamAudit(final String channel, String auth_key,
                         Callback callback) {
        _pamAudit(channel, auth_key, callback, false);
    }

    public void pamAuditChannelGroup(final String group, Callback callback) {
        pamAuditChannelGroup(group, null, callback);
    }

    public void pamAuditChannelGroup(final String group, String auth_key, Callback callback) {
        _pamAuditChannelGroup(group, auth_key, callback, false);
    }

    /** ULS revoke by channel and auth key
     * @param channel
     * @param auth_key
     * @param callback
     */
    public void pamRevoke(String channel, String auth_key, Callback callback) {
        pamGrant(channel, auth_key, false, false, callback);
    }

    /** ULS revoke by channel
     * @param channel
     * @param callback
     */
    public void pamRevoke(String channel, Callback callback) {
        pamGrant(channel, null, false, false, callback);
    }

    public void pamRevokeChannelGroup(String group, Callback callback) {
        pamRevokeChannelGroup(group, null, callback);
    }

    public void pamRevokeChannelGroup(String group, String auth_key, Callback callback) {
        pamGrantChannelGroup(group, auth_key, false, false, -1, callback);
    }

    /**
     * Enable Push Notifications (Google Cloud Messaging)
     * @param channel
     *             Channel for which to enable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     */
    public void enablePushNotificationsOnChannel(String channel, String gcmRegistrationId) {
        enablePushNotificationsOnChannels(new String[]{channel}, gcmRegistrationId, null);
    }

    /**
     * Enable Push Notifications (Google Cloud Messaging)
     * @param channels
     *             Channels for which to enable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     */
    public void enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId) {
        enablePushNotificationsOnChannels(channels, gcmRegistrationId, null);
    }

    /**
     * Enable Push Notifications (Google Cloud Messaging)
     * @param channel
     *             Channel for which to enable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     * @param callback
     *             Callback object
     */
    public void enablePushNotificationsOnChannel(String channel, String gcmRegistrationId, Callback callback) {
        enablePushNotificationsOnChannels(new String[]{channel}, gcmRegistrationId, callback);
    }


    /**
     * Enable Push Notifications (Google Cloud Messaging)
     * @param channels
     *             Channels for which to enable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     * @param callback
     *             Callback object
     */
    public void enablePushNotificationsOnChannels(final String[] channels, String gcmRegistrationId, final Callback callback) {
        _enablePushNotificationsOnChannels(channels, gcmRegistrationId, callback, false);
    }

    /**
     * Disable Push Notifications (Google Cloud Messaging)
     * @param channel
     *             Channel for which to disable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     */
    public void disablePushNotificationsOnChannel(String channel, String gcmRegistrationId) {
        disablePushNotificationsOnChannels(new String[]{channel}, gcmRegistrationId, null);
    }

    /**
     * Disable Push Notifications (Google Cloud Messaging)
     * @param channels
     *             Channels for which to disable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     */
    public void disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId) {
        disablePushNotificationsOnChannels(channels, gcmRegistrationId, null);
    }

    /**
     * Disable Push Notifications (Google Cloud Messaging)
     * @param channel
     * @param gcmRegistrationId
     * @param callback
     */
    public void disablePushNotificationsOnChannel(String channel, String gcmRegistrationId, Callback callback) {
        disablePushNotificationsOnChannels(new String[]{channel}, gcmRegistrationId, callback);
    }

    /**
     * Disable Push Notifications (Google Cloud Messaging)
     * @param channel
     * @param callback
     */
    /**
     * @param channels
     *             Channels for which to disable push notifications
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     * @param callback
     *             Callback object
     */
    public void disablePushNotificationsOnChannels(final String[] channels, String gcmRegistrationId, final Callback callback) {
        _disablePushNotificationsOnChannels(channels, gcmRegistrationId, callback, false);
    }


    /**
     * Get channels for which push notification is enabled (Google Cloud Messaging)
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     * @param callback
     *             Callback object
     */
    public void requestPushNotificationEnabledChannelsForDeviceRegistrationId(String gcmRegistrationId, final Callback callback) {
        _requestPushNotificationEnabledChannelsForDeviceRegistrationId(gcmRegistrationId, callback, false);
    }

    /**
     * Disable push notifications for all channels (Google Cloud Messaging)
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     */
    public void removeAllPushNotificationsForDeviceRegistrationId(String gcmRegistrationId) {
        removeAllPushNotificationsForDeviceRegistrationId(gcmRegistrationId, null);
    }

    /**
     * Disable push notifications for all channels (Google Cloud Messaging)
     * @param gcmRegistrationId
     *             Google Cloud Messaging registration id
     * @param callback
     *             Callback object
     */
    public void removeAllPushNotificationsForDeviceRegistrationId(String gcmRegistrationId, final Callback callback) {
        _removeAllPushNotificationsForDeviceRegistrationId(gcmRegistrationId, callback, false);
    }

}
