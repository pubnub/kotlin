package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;

public class PnGcmMessage extends JSONObject {

	public PnGcmMessage() {
		super();
	}
	public void setData(JSONObject jso) {
		try {
			this.put("data", jso);
		} catch (JSONException e) {

		}
	}
}
