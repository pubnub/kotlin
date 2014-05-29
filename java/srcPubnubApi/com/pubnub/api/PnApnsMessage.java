package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;

public class PnApnsMessage extends JSONObject {

	public PnApnsMessage() {
		super();
	}
	
	private JSONObject getAps() {
		JSONObject aps = null;
		try {
			aps = (JSONObject) this.get("aps");
		} catch (JSONException e) {
			
		}
		
		if (aps == null) {
			aps = new JSONObject();
			try {
				this.put("aps", aps);
			} catch (JSONException e) {
				
			}
		}
		return aps;
	}
 	public void setApsAlert(String alert) {

		try {
			JSONObject aps = (JSONObject) getAps();
			aps.put("alert", alert);
		} catch (JSONException e) {
			
		}
		
	}
	public void setApsBadge(int badge) {
		try {
			JSONObject aps = (JSONObject) (JSONObject) getAps();
			aps.put("badge", badge);
		} catch (JSONException e) {

		}
		
	}
}
