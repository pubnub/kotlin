package com.pubnub.examples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pubnub.api.Callback;
import com.pubnub.api.PnApnsMessage;
import com.pubnub.api.PnGcmMessage;
import com.pubnub.api.PnMessage;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.pubnub.api.PubnubSender;

public class PubnubPushSampleCode {

	String publish_key = "pub-c-c077418d-f83c-4860-b213-2f6c77bde29a";
	String subscribe_key = "sub-c-e8839098-f568-11e2-a11a-02ee2ddab7fe";
	String secret_key = "sec-c-OGU3Y2Q4ZWUtNDQwMC00NTI1LThjNWYtNWJmY2M4OGIwNjEy";
	
	String channel = "abcd";
	
	String auth_key = "abcd";
	
	
	public void start() {
		Pubnub pubnub = new Pubnub(publish_key, subscribe_key);
		pubnub.setAuthKey("abcd");
		pubnub.setCacheBusting(false);
		pubnub.setOrigin("dara11.devbuild");
		
		// Create APNS message
		/*
		PnApnsMessage apnsMessage = new PnApnsMessage();
		apnsMessage.setApsAlert("Game update 49ers touchdown");
		apnsMessage.setApsBadge(2);
		
		try {
			apnsMessage.put("teams", new JSONArray().put("49ers").put("raiders"));
			apnsMessage.put("score", new JSONArray().put(7).put(0));
		} catch (JSONException e1) {

		}
		
		// Create GCM Message	
		
		PnGcmMessage gcmMessage = new PnGcmMessage();
		
		JSONObject jso = new JSONObject();
		try {
			jso.put("summary", "Game update 49ers touchdown");
			jso.put("lastplay", "5yd run up the middle");
		} catch (JSONException e) {

		}

		gcmMessage.setData(jso);
		*/
		
		Callback callback = new Callback() {
			@Override
			public void successCallback(String channel, Object response) {
				System.out.println(response);
			}
			@Override
			public void errorCallback(String channel, PubnubError error) {
				System.out.println(error);
			}			
		};
		
		PubnubSender sender = new PubnubSender(channel, pubnub, callback);
		
		// Create PnMessage 
		
		//PnMessage message = new PnMessage(sender, apnsMessage, gcmMessage);
		PnMessage message = new PnMessage(sender);

		try {
			message.put("test", "hi");
			message.publish();
		} catch (Exception e) {

		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PubnubPushSampleCode().start();

	}

}
