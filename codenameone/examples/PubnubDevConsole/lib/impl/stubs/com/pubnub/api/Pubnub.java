package com.pubnub.api;


/**
 *  Pubnub object facilitates querying channels for messages and listening on
 *  channels for presence/message events
 * 
 *  @author Pubnub
 */
public class Pubnub extends PubnubCore {

	/**
	 *  Pubnub Constructor
	 * 
	 *  @param publish_key
	 *             Publish Key
	 *  @param subscribe_key
	 *             Subscribe Key
	 *  @param secret_key
	 *             Secret Key
	 *  @param cipher_key
	 *             Cipher Key
	 *  @param ssl_on
	 *             SSL on ?
	 */
	public Pubnub(String publish_key, String subscribe_key, String secret_key, String cipher_key, boolean ssl_on) {
	}

	/**
	 *  Pubnub Constructor
	 * 
	 *  @param publish_key
	 *             Publish key
	 *  @param subscribe_key
	 *             Subscribe Key
	 *  @param secret_key
	 *             Secret Key
	 *  @param ssl_on
	 *             SSL on ?
	 */
	public Pubnub(String publish_key, String subscribe_key, String secret_key, boolean ssl_on) {
	}

	/**
	 *  Pubnub Constructor
	 * 
	 *  @param publish_key
	 *             Publish Key
	 *  @param subscribe_key
	 *             Subscribe Key
	 */
	public Pubnub(String publish_key, String subscribe_key) {
	}

	/**
	 *  @param publish_key
	 *             Publish Key
	 *  @param subscribe_key
	 *             Subscribe Key
	 *  @param ssl
	 */
	public Pubnub(String publish_key, String subscribe_key, boolean ssl) {
	}

	/**
	 *  @param publish_key
	 *  @param subscribe_key
	 *  @param secret_key
	 */
	public Pubnub(String publish_key, String subscribe_key, String secret_key) {
	}

	/**
	 *  Sets value for UUID
	 * 
	 *  @param uuid
	 *             UUID value for Pubnub client
	 */
	public void setUUID(String uuid) {
	}

	protected String uuid() {
	}

	/**
	 *  This method sets timeout value for subscribe/presence. Default value is
	 *  310000 milliseconds i.e. 310 seconds
	 * 
	 *  @param timeout
	 *             Timeout value in milliseconds for subscribe/presence
	 */
	public void setSubscribeTimeout(int timeout) {
	}

	/**
	 *  This method returns timeout value for subscribe/presence.
	 * 
	 *  @return Timeout value in milliseconds for subscribe/presence
	 */
	public int getSubscribeTimeout() {
	}

	/**
	 *  This method set timeout value for non subscribe operations like publish,
	 *  history, hereNow. Default value is 15000 milliseconds i.e. 15 seconds.
	 * 
	 *  @param timeout
	 *             Timeout value in milliseconds for Non subscribe operations
	 *             like publish, history, hereNow
	 */
	public void setNonSubscribeTimeout(int timeout) {
	}

	/**
	 *  This method returns timeout value for non subscribe operations like publish, history, hereNow
	 * 
	 *  @return Timeout value in milliseconds for for Non subscribe operations like publish, history, hereNow
	 */
	public int getNonSubscribeTimeout() {
	}

	protected String getUserAgent() {
	}

	/**
	 *  Send a message to a channel.
	 * 
	 *  @param args
	 *             Hashtable containing channel name, message.
	 *  @param callback
	 *             object of sub class of Callback class
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Methods
	 *              accepting Hashtable as arguments have been deprecated.
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void publish(java.util.Hashtable args, Callback callback) {
	}

	/**
	 *  Send a message to a channel.
	 * 
	 *  @param args
	 *             Hashtable containing channel name, message, callback
	 *  @deprecated As of version 3.5.2 . Will be removed in 3.6.0 . Methods
	 *              accepting Hashtable as arguments have been deprecated.
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void publish(java.util.Hashtable args) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param end
	 *             End time
	 *  @param count
	 *             Upper limit on number of messages to be returned
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, long start, long end, int count, boolean reverse, Callback callback)}
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void detailedHistory(String channel, long start, long end, int count, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read History for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param end
	 *             End time
	 *  @param count
	 *             Upper limit on number of messages to be returned
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 */
	public void history(String channel, long start, long end, int count, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, long start, boolean reverse, Callback callback)}
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void detailedHistory(String channel, long start, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read history for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 */
	public void history(String channel, long start, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param end
	 *             End time
	 *  @param callback
	 *             Callback
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, long start, long end, Callback callback)}
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void detailedHistory(String channel, long start, long end, Callback callback) {
	}

	/**
	 * 
	 *  Read History for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param end
	 *             End time
	 *  @param callback
	 *             Callback
	 */
	public void history(String channel, long start, long end, Callback callback) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param end
	 *             End time
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, long start, long end, boolean reverse, Callback callback)}
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void detailedHistory(String channel, long start, long end, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read History for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param start
	 *             Start time
	 *  @param end
	 *             End time
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 */
	public void history(String channel, long start, long end, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param count
	 *             Upper limit on number of messages to be returned
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, int count, boolean reverse, Callback callback)}
	 */
	@java.lang.Deprecated
	@java.lang.Override
	public void detailedHistory(String channel, int count, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read History for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param count
	 *             Upper limit on number of messages to be returned
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 */
	public void history(String channel, int count, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, boolean reverse, Callback callback)}
	 */
	@java.lang.Deprecated
	public void detailedHistory(String channel, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read History for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param reverse
	 *             True if messages need to be in reverse order
	 *  @param callback
	 *             Callback
	 */
	public void history(String channel, boolean reverse, Callback callback) {
	}

	/**
	 * 
	 *  Read DetailedHistory for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param count
	 *             Maximum number of messages
	 *  @param callback
	 *             Callback object
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Replaced
	 *              by
	 *              {@link #history(String channel, int count, Callback callback)}
	 */
	@java.lang.Deprecated
	public void detailedHistory(String channel, int count, Callback callback) {
	}

	/**
	 * 
	 *  Read History for a channel.
	 * 
	 *  @param channel
	 *             Channel name for which detailed history is required
	 *  @param count
	 *             Maximum number of messages
	 *  @param callback
	 *             Callback object
	 */
	@java.lang.Override
	public void history(String channel, int count, Callback callback) {
	}

	/**
	 *  Unsubscribe/Disconnect from channel.
	 * 
	 *  @param args
	 *             Hashtable containing channel name.
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Methods
	 *              accepting Hashtable as arguments have been deprecated.
	 */
	@java.lang.Deprecated
	public void unsubscribe(java.util.Hashtable args) {
	}

	/**
	 * 
	 *  Listen for a message on a channel.
	 * 
	 *  @param args
	 *             Hashtable containing channel name
	 *  @param callback
	 *             Callback
	 *  @exception PubnubException
	 *                 Throws PubnubException if Callback is null
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 .
	 */
	@java.lang.Deprecated
	public void subscribe(java.util.Hashtable args, Callback callback) {
	}

	/**
	 * 
	 *  Listen for a message on a channel.
	 * 
	 *  @param args
	 *             Hashtable containing channel name, callback
	 *  @exception PubnubException
	 *                 Throws PubnubException if Callback is null
	 *  @deprecated as of version 3.5.2 and will be removed with 3.6.0 . Methods
	 *              accepting Hashtable as arguments have been deprecated.
	 */
	@java.lang.Deprecated
	public void subscribe(java.util.Hashtable args) {
	}
}
