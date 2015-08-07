package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONObject;

interface PubnubSyncInterface {

	/**
	 *
	 * @param group
     *      Channel Group
	 * @param channels
     *      Array of Channels
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject 	channelGroupAddChannel(String group, String[] channels);

	/**
	 *
	 * @param group
     *      Channel Group
	 * @param channel
     *      Channel
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupAddChannel(String group, String channel);

	/**
	 *
	 * @param groups
     *      Array of Channel Groups
	 * @param state
     *      Return state ?
	 * @param uuids
     *      Return uuids ?
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupHereNow(String[] groups, boolean state, boolean uuids);

	/**
	 *
	 * @param group
     *      Channel Group
	 * @param state
     *      Return state ?
	 * @param uuids
     *      Return uuids ?
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupHereNow(String group, boolean state, boolean uuids);

	/**
	 *
	 * @param group
     *      Channel Group
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupHereNow(String group);

	/**
	 * Get the list of channels in the namespaced group
	 *
	 * @param group
     *      Channel Group
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupListChannels(String group);

	/**
	 * Get the list of groups in the global namespace
	 *
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupListGroups();

	/**
	 * Get the list of groups in the namespace
	 *
	 * @param namespace
     *      Namespace
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupListGroups(String namespace);

	/**
	 * Get all namespaces
	 *
	 * @return
     *      Response of method call. Can also be an error response.
	 */
	 public JSONObject	channelGroupListNamespaces();

	/**
	 *
	 * @param group
	 * @param channels
	 * @return
	 */
	 public JSONObject	channelGroupRemoveChannel(String group, String[] channels);

	/**
	 *
 	 * @param group
	 * @param channel
	 * @return
	 */
	 public JSONObject	channelGroupRemoveChannel(String group, String channel);

	/**
	 *
 	 * @param group
	 * @return
	 */
	 public JSONObject	channelGroupRemoveGroup(String group);

	/**
	 * Remove namespace
	 *
	 * @param namespace to remove
	 * @return
	 */
	 public JSONObject	channelGroupRemoveNamespace(String namespace);

	/**
	 *
	 * @param channel
	 * @param uuid
	 * @return
	 */
	 public JSONObject	getState(String channel, String uuid);

	/**
	 *
	 * @param state
	 * @param uuids
	 * @return
	 */
	 public JSONObject	hereNow(boolean state, boolean uuids);

	/**
	 * Read presence information from a channel or a channel group
	 *
	 * @param channels      array
	 * @param channelGroups array
	 * @param state         state enabled ?
	 * @param uuids         enable / disable returning uuids in response ?
	 * @return
	 */
	 public JSONObject	hereNow(String[] channels, String[] channelGroups, boolean state, boolean uuids);

	/**
	 *
	 * @param channel
	 * @param state
	 * @param uuids
	 * @return
	 */
	 public JSONObject	hereNow(String channel, boolean state, boolean uuids);

	/**
	 * Read presence information from a channel
	 *
	 * @param channel  Channel name
	 * @return
	 */
	 public JSONObject	hereNow(String channel);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param reverse  True if messages need to be in reverse order
	 * @return
	 */
	 public Object		history(String channel, boolean reverse);

	/**
	 * Read History for a channel.
	 *
	 * @param channel          Channel name for which history is required
	 * @param includeTimetoken True/False whether to include timetokens in response
	 * @param count            Maximum number of messages
	 * @return
	 */
	 public Object		history(String channel, boolean includeTimetoken, int count);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param count    Upper limit on number of messages to be returned
	 * @param reverse  True if messages need to be in reverse order
	 * @return
	 */
	 public Object		history(String channel, int count, boolean reverse);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param count    Maximum number of messages
	 * @return
	 */
	 public Object		history(String channel, int count);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param reverse  True if messages need to be in reverse order
	 * @return
	 */
	 public Object		history(String channel, long start, boolean reverse);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param count    Upper limit on number of messages to be returned
	 * @param reverse  True if messages need to be in reverse order
	 * @return
	 */
	 public Object		history(String channel, long start, int count, boolean reverse);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param count    Upper limit on number of messages to be returned
	 * @return
	 */
	 public Object		history(String channel, long start, int count);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param end      End time
	 * @param reverse  True if messages need to be in reverse order
	 * @return
	 */
	 public Object		history(String channel, long start, long end, boolean reverse);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param end      End time
	 * @return
	 */
	 public Object		history(String channel, long start, long end);

	/**
	 * Read History for a channel.
	 *
	 * @param channel          Channel name for which history is required
	 * @param start            Start time
	 * @param end              End time
	 * @param count            Upper limit on number of messages to be returned
	 * @param reverse          True if messages need to be in reverse order
	 * @param includeTimetoken True/False whether to include timetokens in response
	 * @return
	 */
	 public Object		history(String channel, long start, long end, int count, boolean reverse, boolean includeTimetoken);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param end      End time
	 * @param count    Upper limit on number of messages to be returned
	 * @param reverse  True if messages need to be in reverse order
	 * @return
	 */
	 public Object		history(String channel, long start, long end, int count, boolean reverse);

	/**
	 * Read History for a channel.
	 *
	 * @param channel  Channel name for which history is required
	 * @param start    Start time
	 * @param end      End time
	 * @param count    Upper limit on number of messages to be returned
	 * @return
	 */
	 public Object		history(String channel, long start, long end, int count);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel        Channel name
	 * @param message        Double to be published
	 * @param storeInHistory Store in History ?
	 * @return
	 */
	 public Object		publish(String channel, Double message, boolean storeInHistory);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel  Channel name
	 * @param message  Double to be published
	 * @return
	 */
	 public Object		publish(String channel, Double message);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel        Channel name
	 * @param message        Integer to be published
	 * @param storeInHistory Store in History ?
	 * @return
	 */
	 public Object		publish(String channel, Integer message, boolean storeInHistory);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel  Channel name
	 * @param message  Integer to be published
	 * @return
	 */
	 public Object		publish(String channel, Integer message);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel        Channel name
	 * @param message        JSONArray to be published
	 * @param storeInHistory Store in History ?
	 * @return
	 */
	 public Object		publish(String channel, JSONArray message, boolean storeInHistory);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel  Channel name
	 * @param message  JSONOArray to be published
	 * @return
	 */
	 public Object		publish(String channel, JSONArray message);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel        Channel name
	 * @param message        JSONObject to be published
	 * @param storeInHistory Store in History ?
	 * @return
	 */
	 public Object		publish(String channel, JSONObject message, boolean storeInHistory);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel  Channel name
	 * @param message  JSONObject to be published
	 * @return
	 */
	 public Object		publish(String channel, JSONObject message);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel        Channel name
	 * @param message        String to be published
	 * @param storeInHistory Store in History ?
	 * @return
	 */
	 public Object		publish(String channel, String message, boolean storeInHistory);

	/**
	 * Send a message to a channel.
	 *
	 * @param channel  Channel name
	 * @param message  String to be published
	 * @return
	 */
	 public Object		publish(String channel, String message);

	/**
	 *
	 * @param channel
	 * @param uuid
	 * @param state
	 * @return
	 */
	 public JSONObject	setState(String channel, String uuid, JSONObject state);

	/**
	 * Read current time from PubNub Cloud.
	 *
	 * @return
	 */
	 public JSONArray	time();

	/**
	 * Read presence information for Pubnub Object uuid
	 *
	 * @return
	 */
	 public JSONObject	whereNow();

	/**
	 * Read presence information for uuid
	 *
	 * @param uuid     UUID
	 * @return
	 */
	 public JSONObject	whereNow(String uuid);

}
