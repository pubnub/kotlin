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
import com.pubnub.api.PubnubSenderMissingException;

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
		pubnub.setOrigin("dara20.devbuild");
		
		// Create APNS message
		
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
		
		// Create message1 with apns, gcm and normal data  
		
		PnMessage message1 = new PnMessage(sender, apnsMessage, gcmMessage);
		try {
			message1.put("test","hi");
		} catch (JSONException e1) {

		}
		
		
		// Create message2 with only apns payload
		
		PnMessage message2 = new PnMessage(sender, apnsMessage);

		
		
		// Create message3 with only gcm payload
		
		PnMessage message3 = new PnMessage(sender, gcmMessage);

		
		// Create message4 with only no gcm or apns data
		
		PnMessage message4 = new PnMessage(sender);
		try {
			message4.put("data","mydata");
		} catch (JSONException e1) {

		}
		
		
		// Create message4 with only gcm and apns data
		
		PnMessage message5 = new PnMessage(sender, apnsMessage, gcmMessage);

		
		try {
			message1.publish();
			message2.publish();
			message3.publish();
			message4.publish();
			message5.publish();
		} catch (PubnubSenderMissingException e) {
			System.out.println("Set Sender");
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PubnubPushSampleCode().start();

	}

}
