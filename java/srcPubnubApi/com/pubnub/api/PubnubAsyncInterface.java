package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONObject;

interface PubnubAsyncInterface {

    /**
     *
     * @param group
     * @param channels
     * @param callback
     */
    public void channelGroupAddChannel(String group, String[] channels, Callback callback);

    /**
     *
     * @param group
     * @param channel
     * @param callback
     */
    public void channelGroupAddChannel(String group, String channel, Callback callback);

    /**
     *
     * @param groups
     * @param state
     * @param uuids
     * @param callback
     */
    public void channelGroupHereNow(String[] groups, boolean state, boolean uuids, Callback callback);

    /**
     *
     * @param group
     * @param state
     * @param uuids
     * @param callback
     */
    public void channelGroupHereNow(String group, boolean state, boolean uuids, Callback callback);

    /**
     *
     * @param group
     * @param callback
     */
    public void channelGroupHereNow(String group, Callback callback);

    /**
     * Get the list of channels in the namespaced group
     *
     * @param group    name
     * @param callback to invoke
     */
    public void channelGroupListChannels(String group, Callback callback);

    /**
     * Get the list of groups in the global namespace
     *
     * @param callback to invoke
     */
    public void channelGroupListGroups(Callback callback);

    /**
     * Get the list of groups in the namespace
     *
     * @param namespace name
     * @param callback  to invoke
     */
    public void channelGroupListGroups(String namespace, Callback callback);

    /**
     * Get all namespaces
     *
     * @param callback to invoke
     */
    public void channelGroupListNamespaces(Callback callback);

    /**
     *
     * @param group
     * @param channels
     * @param callback
     */
    public void channelGroupRemoveChannel(String group, String[] channels, Callback callback);

    /**
     *
     * @param group
     * @param channel
     * @param callback
     */
    public void channelGroupRemoveChannel(String group, String channel, Callback callback);

    /**
     *
     * @param group
     * @param callback
     */
    public void channelGroupRemoveGroup(String group, Callback callback);

    /**
     * Remove namespace
     *
     * @param namespace to remove
     * @param callback  to invoke
     */
    public void channelGroupRemoveNamespace(String namespace, Callback callback);

    /**
     *
     * @param group
     * @param uuid
     * @param state
     * @param callback
     */
    public void channelGroupSetState(String group, String uuid, JSONObject state, Callback callback);

    /**
     * Listen for a message on multiple channel groups.
     *
     * @param groups   to subscribe
     * @param callback to call
     * @throws PubnubException if Callback is null
     */
    public void channelGroupSubscribe(String[] groups, Callback callback) throws PubnubException;

    /**
     * Listen for a message on multiple channel group.
     *
     * @param groups    to subscribe
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void channelGroupSubscribe(String[] groups, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on multiple channel group.
     *
     * @param groups    to subscribe
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void channelGroupSubscribe(String[] groups, Callback callback, String timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel group.
     *
     * @param group    name to subscribe
     * @param callback to call
     * @throws PubnubException
     */
    public void channelGroupSubscribe(String group, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a channel group.
     *
     * @param group     name to subscribe
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void channelGroupSubscribe(String group, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel group.
     *
     * @param group     name to subscribe
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void channelGroupSubscribe(String group, Callback callback, String timetoken) throws PubnubException;

    /**
     * Unsubscribe from channel group
     *
     * @param group to unsubscribe
     */
    public void channelGroupUnsubscribe(String group);

    /**
     * Unsubscribe from multiple channel groups
     *
     * @param groups to unsubscribe
     */
    public void channelGroupUnsubscribe(String[] groups);

    /**
     * Unsubscribe from all channel groups.
     */
    public void channelGroupUnsubscribeAllGroups();

    /**
     * Disconnect from all channels, and resubscribe
     */
    public void disconnectAndResubscribe();

    /**
     * Disconnect from all channels, and resubscribe
     */
    public void disconnectAndResubscribe(PubnubError error);

    /**
     * Disconnect from all channels, and resubscribe
     */
    public void disconnectAndResubscribeWithTimetoken(String timetoken);

    /**
     * Disconnect from all channels, and resubscribe
     */
    public void disconnectAndResubscribeWithTimetoken(String timetoken, PubnubError error);

    /**
     * Get Cache Busting value
     *
     * @return current cache busting setting
     */
    public boolean getCacheBusting();

    /**
     * This method returns all channel names currently subscribed to in form of
     * a comma separated String
     *
     * @return Comma separated string with all channel names currently
     * subscribed
     */
    public String getCurrentlySubscribedChannelNames();

    /**
     * Returns presence heartbeat value
     *
     * @return Current presence heartbeat value
     */
    public int getHeartbeat();

    public int getHeartbeatInterval();

    /**
     * Returns current max retries for Subscribe
     *
     * @return Current max retries
     */
    public int getMaxRetries();

    /**
     * Returns presence expiry timeout value
     *
     * @return Current presence expiry timeout value
     */
    public int getPnExpires();

    /**
     * Returns Resume on Reconnect current setting
     *
     * @return Resume on Reconnect setting
     */
    public boolean getResumeOnReconnect();

    /**
     * Returns current retry interval for subscribe
     *
     * @return Current Retry Interval in milliseconds
     */
    public int getRetryInterval();

    public void getState(String channel, String uuid, Callback callback);

    /**
     * This method returns array of channel names, currently subscribed to
     *
     * @return Array of channel names
     */
    public String[] getSubscribedChannelsArray();

    /**
     * Returns current window interval for subscribe
     *
     * @return Current Window Interval in milliseconds
     */
    public int getWindowInterval();

    public void hereNow(boolean state, boolean uuids, Callback callback);

    /**
     * Read presence information from a channel or a channel group
     *
     * @param channels      array
     * @param channelGroups array
     * @param state         state enabled ?
     * @param uuids         enable / disable returning uuids in response ?
     * @param callback      object of sub class of Callback class
     */
    public void hereNow(String[] channels, String[] channelGroups, boolean state, boolean uuids, Callback callback);

    public void hereNow(String channel, boolean state, boolean uuids, Callback callback);

    /**
     * Read presence information from a channel
     *
     * @param channel  Channel name
     * @param callback object of sub class of Callback class
     */
    public void hereNow(String channel, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param reverse  True if messages need to be in reverse order
     * @param callback Callback
     */
    public void history(String channel, boolean reverse, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel          Channel name for which history is required
     * @param includeTimetoken True/False whether to include timetokens in response
     * @param count            Maximum number of messages
     * @param callback         Callback object
     */
    public void history(String channel, boolean includeTimetoken, int count, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param count    Upper limit on number of messages to be returned
     * @param reverse  True if messages need to be in reverse order
     * @param callback Callback
     */
    public void history(String channel, int count, boolean reverse, Callback callback);


    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param count    Maximum number of messages
     * @param callback Callback object
     */
    public void history(String channel, int count, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param reverse  True if messages need to be in reverse order
     * @param callback Callback
     */
    public void history(String channel, long start, boolean reverse, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param count    Upper limit on number of messages to be returned
     * @param reverse  True if messages need to be in reverse order
     * @param callback Callback
     */
    public void history(String channel, long start, int count, boolean reverse, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param count    Upper limit on number of messages to be returned
     * @param callback Callback
     */
    public void history(String channel, long start, int count, Callback callback);


    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param end      End time
     * @param reverse  True if messages need to be in reverse order
     * @param callback Callback
     */
    public void history(String channel, long start, long end, boolean reverse, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param end      End time
     * @param callback Callback
     */
    public void history(String channel, long start, long end, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel          Channel name for which history is required
     * @param start            Start time
     * @param end              End time
     * @param count            Upper limit on number of messages to be returned
     * @param reverse          True if messages need to be in reverse order
     * @param includeTimetoken True/False whether to include timetokens in response
     * @param callback         Callback
     */
    public void history(String channel, long start, long end, int count, boolean reverse, boolean includeTimetoken, Callback callback);


    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param end      End time
     * @param count    Upper limit on number of messages to be returned
     * @param reverse  True if messages need to be in reverse order
     * @param callback Callback
     */
    public void history(String channel, long start, long end, int count, boolean reverse, Callback callback);

    /**
     * Read History for a channel.
     *
     * @param channel  Channel name for which history is required
     * @param start    Start time
     * @param end      End time
     * @param count    Upper limit on number of messages to be returned
     * @param callback Callback
     */
    public void history(String channel, long start, long end, int count, Callback callback);

    /**
     * This method returns the state of Resume on Reconnect setting
     *
     * @return Current state of Resume On Reconnect Setting
     */
    public boolean isResumeOnReconnect();

    /**
     * Listen for presence of subscribers on a channel
     *
     * @param channel  Name of the channel on which to listen for join/leave i.e.
     *                 presence events
     * @param callback object of sub class of Callback class
     * @throws PubnubException Throws PubnubException if Callback is null
     */
    public void presence(String channel, Callback callback) throws PubnubException;

    /**
     * Send a message to a channel.
     *
     * @param channel        Channel name
     * @param message        Double to be published
     * @param storeInHistory Store in History ?
     * @param callback       object of sub class of Callback class
     */
    public void publish(String channel, Double message, boolean storeInHistory, Callback callback);


    /**
     * Send a message to a channel.
     *
     * @param channel  Channel name
     * @param message  Double to be published
     * @param callback object of sub class of Callback class
     */
    public void publish(String channel, Double message, Callback callback);


    /**
     * Send a message to a channel.
     *
     * @param channel        Channel name
     * @param message        Integer to be published
     * @param storeInHistory Store in History ?
     * @param callback       object of sub class of Callback class
     */
    public void publish(String channel, Integer message, boolean storeInHistory, Callback callback);

    /**
     * Send a message to a channel.
     *
     * @param channel  Channel name
     * @param message  Integer to be published
     * @param callback object of sub class of Callback class
     */
    public void publish(String channel, Integer message, Callback callback);


    /**
     * Send a message to a channel.
     *
     * @param channel        Channel name
     * @param message        JSONArray to be published
     * @param storeInHistory Store in History ?
     * @param callback       object of sub class of Callback class
     */
    public void publish(String channel, JSONArray message, boolean storeInHistory, Callback callback);


    /**
     * Send a message to a channel.
     *
     * @param channel  Channel name
     * @param message  JSONOArray to be published
     * @param callback object of sub class of Callback class
     */
    public void publish(String channel, JSONArray message, Callback callback);

    /**
     * Send a message to a channel.
     *
     * @param channel        Channel name
     * @param message        JSONObject to be published
     * @param storeInHistory Store in History ?
     * @param callback       object of sub class of Callback class
     */
    public void publish(String channel, JSONObject message, boolean storeInHistory, Callback callback);


    /**
     * Send a message to a channel.
     *
     * @param channel  Channel name
     * @param message  JSONObject to be published
     * @param callback object of sub class of Callback class
     */
    public void publish(String channel, JSONObject message, Callback callback);


    /**
     * Send a message to a channel.
     *
     * @param channel        Channel name
     * @param message        String to be published
     * @param storeInHistory Store in History ?
     * @param callback       object of sub class of Callback class
     */
    public void publish(String channel, String message, boolean storeInHistory, Callback callback);

    /**
     * Send a message to a channel.
     *
     * @param channel  Channel name
     * @param message  String to be published
     * @param callback object of sub class of Callback class
     */
    public void publish(String channel, String message, Callback callback);


    /**
     * Enable/Disable Cache Busting
     *
     * @param cacheBusting
     */
    public void setCacheBusting(boolean cacheBusting);

    public void setHeartbeat(int heartbeat);

    /**
     * This method sets presence expiry timeout.
     *
     * @param heartbeat Presence Heartbeat value in seconds
     */
    public void setHeartbeat(int heartbeat, Callback callback);

    /**
     *
     * @param heartbeatInterval
     */
    public void setHeartbeatInterval(int heartbeatInterval);

    /**
     *
     * @param heartbeatInterval
     * @param callback
     */
    public void setHeartbeatInterval(int heartbeatInterval, Callback callback);

    /**
     * This methods sets maximum number of retries for subscribe. Pubnub API
     * will make maxRetries attempts to connect to pubnub servers before timing
     * out.
     *
     * @param maxRetries Max number of retries
     */
    public void setMaxRetries(int maxRetries);

    /**
     *
     * @param pnexpires
     */
    public void setPnExpires(int pnexpires);

    /**
     * This method sets presence expiry timeout.
     *
     * @param pnexpires Presence Expiry timeout in seconds
     */
    public void setPnExpires(int pnexpires, Callback callback);

    /**
     * If Resume on Reconnect is set to true, then Pubnub catches up on
     * reconnection after disconnection. If false, then messages sent on the
     * channel between disconnection and reconnection are not received.
     *
     * @param resumeOnReconnect True or False setting for Resume on Reconnect
     */
    public void setResumeOnReconnect(boolean resumeOnReconnect);

    /**
     * This method sets retry interval for subscribe. Pubnub API will make
     * maxRetries attempts to connect to pubnub servers. These attemtps will be
     * made at an interval of retryInterval milliseconds.
     *
     * @param retryInterval Retry Interval in milliseconds
     */
    public void setRetryInterval(int retryInterval);

    /**
     *
     * @param channel
     * @param uuid
     * @param state
     * @param callback
     */
    public void setState(String channel, String uuid, JSONObject state, Callback callback);

    /**
     * This method sets window interval for subscribe.
     *
     * @param windowInterval Window Interval in milliseconds
     */
    public void setWindowInterval(int windowInterval);


    /**
     * This method sets timeout value for subscribe/presence. Default value is
     * 310000 milliseconds i.e. 310 seconds
     *
     * @param timeout Timeout value in milliseconds for subscribe/presence
     */
    public void setSubscribeTimeout(int timeout);


    /**
     * This method set timeout value for non subscribe operations like publish,
     * history, hereNow. Default value is 15000 milliseconds i.e. 15 seconds.
     *
     * @param timeout Timeout value in milliseconds for Non subscribe operations
     *                like publish, history, hereNow
     */
    public void setNonSubscribeTimeout(int timeout);

    /**
     * This method when called stops Pubnub threads
     */
    public void shutdown();

    /**
     * Listen for a message on a channel.
     *
     * @param channels array to listen on
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String[] channels, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a channel.
     *
     * @param channels  array to listen on
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String[] channels, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel.
     *
     * @param channels  array to listen on
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException Throws PubnubException if Callback is null
     */
    public void subscribe(String[] channels, Callback callback, String timetoken) throws PubnubException;

    /**
     * Listen for a message on a multiple channels and a multiple channel groups
     *
     * @param channels array to listen on
     * @param groups   array to listen on
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String[] channels, String[] groups, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a multiple channels and a multiple channel groups
     *
     * @param channels  array to listen on
     * @param groups    array to listen on
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String[] channels, String[] groups, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a multiple channels and a multiple channel groups
     *
     * @param channels  array to listen on
     * @param groups    array to listen on
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String[] channels, String[] groups, Callback callback, String timetoken) throws PubnubException;

    /**
     * Listen for a message on a multiple channels and a single channel group.
     *
     * @param channels array to listen on
     * @param group    name
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String[] channels, String group, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a multiple channels and a single channel group.
     *
     * @param channels  array to listen on
     * @param group     name
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String[] channels, String group, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a multiple channels and a single channel group.
     *
     * @param channels  array to listen on
     * @param group     name
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String[] channels, String group, Callback callback, String timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel.
     *
     * @param channel  name
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String channel, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a channel.
     *
     * @param channel  name
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String channel, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel.
     *
     * @param channel   name
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String channel, Callback callback, String timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel and a multiple channel groups.
     *
     * @param channel  name
     * @param groups   array to listen on
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String channel, String[] groups, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a channel and a multiple channel groups.
     *
     * @param channel   name
     * @param groups    array to listen on
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String channel, String[] groups, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel and a multiple channel groups.
     *
     * @param channel   name
     * @param groups    array to listen on
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String channel, String[] groups, Callback callback, String timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel and on a channel group.
     *
     * @param channel  name
     * @param group    name
     * @param callback to call
     * @throws PubnubException
     */
    public void subscribe(String channel, String group, Callback callback) throws PubnubException;

    /**
     * Listen for a message on a channel and on a channel group.
     *
     * @param channel   name
     * @param group     name
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String channel, String group, Callback callback, long timetoken) throws PubnubException;

    /**
     * Listen for a message on a channel and on a channel group.
     *
     * @param channel   name
     * @param group     name
     * @param callback  to call
     * @param timetoken to use for subscribing
     * @throws PubnubException
     */
    public void subscribe(String channel, String group, Callback callback, String timetoken) throws PubnubException;


    /**
     * Read current time from PubNub Cloud.
     *
     * @param callback Callback object
     */
    public void time(Callback callback);

    /**
     * Unsubscribe/Disconnect from channel.
     *
     * @param channel channel name as String.
     */
    public void unsubscribe(String channel);

    /**
     * Unsubscribe from channels.
     *
     * @param channels String array containing channel names
     */
    public void unsubscribe(String[] channels);

    /**
     * Unsubscribe from all channel and channel groups.
     */
    public void unsubscribeAll();

    /**
     * Unsubscribe from all channel.
     */
    public void unsubscribeAllChannels();


    /**
     * Unsubscribe from presence channel.
     *
     * @param channel channel name as String.
     */
    public void unsubscribePresence(String channel);

    /**
     * Read presence information for Pubnub Object uuid
     *
     * @param callback
     */
    public void whereNow(Callback callback);

    /**
     * Read presence information for uuid
     *
     * @param uuid     UUID
     * @param callback object of sub class of Callback class
     */
    public void whereNow(String uuid, Callback callback);

}
