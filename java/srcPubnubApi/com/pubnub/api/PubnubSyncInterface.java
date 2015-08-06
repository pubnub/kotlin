package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONObject;

interface PubnubSyncInterface {


	 public JSONObject 	channelGroupAddChannel(String group, String[] channels);
   
	 public JSONObject	channelGroupAddChannel(String group, String channel);
	           
	 public JSONObject	channelGroupHereNow(String[] groups, boolean state, boolean uuids); 
	           
	 public JSONObject	channelGroupHereNow(String group, boolean state, boolean uuids); 
	           
	 public JSONObject	channelGroupHereNow(String group); 
	           
	 public JSONObject	channelGroupListChannels(String group); 

	 public JSONObject	channelGroupListGroups();

	 public JSONObject	channelGroupListGroups(String namespace); 

	 public JSONObject	channelGroupListNamespaces();

	 public JSONObject	channelGroupRemoveChannel(String group, String[] channels); 
	           
	 public JSONObject	channelGroupRemoveChannel(String group, String channel); 
	           
	 public JSONObject	channelGroupRemoveGroup(String group); 
	           
	 public JSONObject	channelGroupRemoveNamespace(String namespace); 

	 public JSONObject	getState(String channel, String uuid); 

	 public JSONObject	hereNow(boolean state, boolean uuids); 
	           
	 public JSONObject	hereNow(String[] channels, String[] channelGroups, boolean state, boolean uuids); 

	 public JSONObject	hereNow(String channel, boolean state, boolean uuids); 
	           
	 public JSONObject	hereNow(String channel); 

	 public Object		history(String channel, boolean reverse); 

	 public Object		history(String channel, boolean includeTimetoken, int count); 

	 public Object		history(String channel, int count, boolean reverse); 

	 public Object		history(String channel, int count); 

	 public Object		history(String channel, long start, boolean reverse); 

	 public Object		history(String channel, long start, int count, boolean reverse); 

	 public Object		history(String channel, long start, int count); 

	 public Object		history(String channel, long start, long end, boolean reverse); 

	 public Object		history(String channel, long start, long end); 

	 public Object		history(String channel, long start, long end, int count, boolean reverse, boolean includeTimetoken); 

	 public Object		history(String channel, long start, long end, int count, boolean reverse); 

	 public Object		history(String channel, long start, long end, int count);

	 public Object		publish(String channel, Double message, boolean storeInHistory); 

	 public Object		publish(String channel, Double message); 

	 public Object		publish(String channel, Integer message, boolean storeInHistory); 

	 public Object		publish(String channel, Integer message); 

	 public Object		publish(String channel, JSONArray message, boolean storeInHistory); 

	 public Object		publish(String channel, JSONArray message); 

	 public Object		publish(String channel, JSONObject message, boolean storeInHistory); 

	 public Object		publish(String channel, JSONObject message); 

	 public Object		publish(String channel, String message, boolean storeInHistory); 

	 public Object		publish(String channel, String message); 

	 public JSONObject	setState(String channel, String uuid, JSONObject state); 

	 public JSONArray	time(); 

	 public JSONObject	whereNow(); 
	           
	 public JSONObject	whereNow(String uuid);

}
