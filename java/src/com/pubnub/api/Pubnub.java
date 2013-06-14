package com.pubnub.api;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.UUID;

import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Pubnub object facilitates querying channels for messages and listening on
 * channels for presence/message events
 *
 * @author Pubnub
 *
 */

public class Pubnub extends PubnubCore {

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
    public Pubnub(String publish_key, String subscribe_key, String secret_key,
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
    public Pubnub(String publish_key, String subscribe_key, String secret_key,
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
    public Pubnub(String publish_key, String subscribe_key) {
        super(publish_key, subscribe_key, "", "", false);
    }

    /**
     * @param publish_key
     *            Publish Key
     * @param subscribe_key
     *            Subscribe Key
     * @param ssl
     */
    public Pubnub(String publish_key, String subscribe_key, boolean ssl) {
        super(publish_key, subscribe_key, "", "", ssl);
    }

    /**
     * @param publish_key
     * @param subscribe_key
     * @param secret_key
     */
    public Pubnub(String publish_key, String subscribe_key, String secret_key) {
        super(publish_key, subscribe_key, secret_key, "", false);
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

    protected String uuid() {
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

    protected String getUserAgent() {
        return "Java/3.4";
    }

    /**
     * Send a message to a channel.
     *
     * @param args
     *            Hashtable containing channel name, message.
     * @param callback
     *            object of sub class of Callback class
     * @deprecated As of version 3.5.2 .           
     */
    @Deprecated @Override
    public void publish(Hashtable args, Callback callback) {
        super.publish(args, callback);
    }

    /**
     * Send a message to a channel.
     *
     * @param args
     *            Hashtable containing channel name, message, callback
     * @deprecated As of version 3.5.2 .
     */
    @Deprecated @Override
    public void publish(Hashtable args) {
        super.publish(args);
    }

    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param end
     *            End time
     * @param count
     *            Upper limit on number of messages to be returned
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel, long start, long end, 
     *            int count, boolean reverse, Callback callback)}
     */
    @Deprecated @Override
    public void detailedHistory(final String channel, long start, long end,
            int count, boolean reverse, final Callback callback) {
        super.detailedHistory(channel, start, end, count, reverse, callback);
    }
    /**
     *
     * Read History for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param end
     *            End time
     * @param count
     *            Upper limit on number of messages to be returned
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     */
    public void history(final String channel, long start, long end,
            int count, boolean reverse, final Callback callback) {
        super.detailedHistory(channel, start, end, count, reverse, callback);
    }

    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel, long start, boolean reverse, Callback callback)}
     */
    @Deprecated @Override
    public void detailedHistory(String channel, long start, boolean reverse,
            Callback callback) {
        super.detailedHistory(channel, start, reverse, callback);
    }

    /**
     *
     * Read history for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     */
    public void history(String channel, long start, boolean reverse,
            Callback callback) {
        super.detailedHistory(channel, start, reverse, callback);
    }

    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param end
     *            End time
     * @param callback
     *            Callback
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel, long start, long end, Callback callback)}
     */
    @Deprecated @Override
    public void detailedHistory(String channel, long start, long end,
            Callback callback) {
        super.detailedHistory(channel, start, end, callback);
    }
    /**
     *
     * Read History for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param end
     *            End time
     * @param callback
     *            Callback
     */

    public void history(String channel, long start, long end,
            Callback callback) {
        super.detailedHistory(channel, start, end, callback);
    }



    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param end
     *            End time
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel, long start, long end, boolean reverse, Callback callback)}
     */
    @Deprecated @Override
    public void detailedHistory(String channel, long start, long end,
            boolean reverse, Callback callback) {
        super.detailedHistory(channel, start, end, reverse, callback);
    }

    /**
     *
     * Read History for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param start
     *            Start time
     * @param end
     *            End time
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     */
    public void history(String channel, long start, long end,
            boolean reverse, Callback callback) {
        super.detailedHistory(channel, start, end, reverse, callback);
    }


    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param count
     *            Upper limit on number of messages to be returned
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel,
     *            int count, boolean reverse, Callback callback)}
     */
    @Deprecated @Override
    public void detailedHistory(String channel, int count, boolean reverse,
            Callback callback) {
        super.detailedHistory(channel, count, reverse, callback);
    }

    /**
     *
     * Read History for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param count
     *            Upper limit on number of messages to be returned
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     */
    public void history(String channel, int count, boolean reverse,
            Callback callback) {
        super.detailedHistory(channel, count, reverse, callback);
    }


    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel, boolean reverse, Callback callback)}
     */
    @Deprecated
    public void detailedHistory(String channel, boolean reverse,
            Callback callback) {
        super.detailedHistory(channel, reverse, callback);
    }

    /**
     *
     * Read History for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param reverse
     *            True if messages need to be in reverse order
     * @param callback
     *            Callback
     */

    public void history(String channel, boolean reverse,
            Callback callback) {
        super.detailedHistory(channel, reverse, callback);
    }

    /**
     *
     * Read DetailedHistory for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param count
     *            Maximum number of messages
     * @param callback
     *            Callback object
     * @deprecated As of version 3.5.2 .
     *            replaced by {@link #history(String channel,
     *            int count, Callback callback)}
     */
    @Deprecated
    public void detailedHistory(String channel, int count, Callback callback) {
        super.detailedHistory(channel, count, callback);
    }

    /**
     *
     * Read History for a channel.
     *
     * @param channel
     *            Channel name for which detailed history is required
     * @param count
     *            Maximum number of messages
     * @param callback
     *            Callback object
     */
    @Override
    public void history(String channel, int count, Callback callback) {
        super.detailedHistory(channel, count, callback);
    }


    /**
     * Unsubscribe/Disconnect from channel.
     *
     * @param args
     *            Hashtable containing channel name.
     * @deprecated As of version 3.5.2 .
     */
    @Deprecated
    public void unsubscribe(Hashtable args) {
        String[] channelList = (String[]) args.get("channels");
        if (channelList == null) {
            channelList = new String[] { (String) args.get("channel") };
        }
        unsubscribe(channelList);
    }

    /**
     *
     * Listen for a message on a channel.
     *
     * @param args
     *            Hashtable containing channel name
     * @param callback
     *            Callback
     * @exception PubnubException
     *                Throws PubnubException if Callback is null
     * @deprecated As of version 3.5.2 .
     */
    @Deprecated
    public void subscribe(Hashtable args, Callback callback)
            throws PubnubException {
        args.put("callback", callback);
        super.subscribe(args);
    }

    /**
     *
     * Listen for a message on a channel.
     *
     * @param args
     *            Hashtable containing channel name, callback
     * @exception PubnubException
     *                Throws PubnubException if Callback is null
     * @deprecated As of version 3.5.2 .
     */
    @Deprecated
    public void subscribe(Hashtable args) throws PubnubException {
        super.subscribe(args);
    }

}
