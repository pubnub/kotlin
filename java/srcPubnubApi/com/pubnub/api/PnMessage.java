package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;

public class PnMessage extends JSONObject {
	private Sender sender;
	
	public Sender getSender() {
		return sender;
	}
	public void setSender(Sender sender) {
		this.sender = sender;
	}
	
	public PnMessage(Sender sender) {
		super();
		this.sender = sender;
	}
	
	public PnMessage(Sender sender, PnApnsMessage apnsMsg, PnGcmMessage gcmMsg) {
		super();
		this.sender = sender;
		try {
			if (apnsMsg != null) {
				this.put("pn_apns", apnsMsg);
			}
			if (gcmMsg != null) {
				this.put("pn_gcm", gcmMsg);
			}
		} catch (JSONException e) {

		}
	}
	
	public PnMessage(Sender sender, PnGcmMessage gcmMsg) {
		super();
		this.sender = sender;
		try {
			if (gcmMsg != null) {
				this.put("pn_gcm", gcmMsg);
			}
		} catch (JSONException e) {

		}
	}
	
	public PnMessage(Sender sender, PnApnsMessage apnsMsg) {
		super();
		this.sender = sender;
		try {
			if (apnsMsg != null) {
				this.put("pn_apns", apnsMsg);
			}
		} catch (JSONException e) {

		}
	}
	
	public void publish(Sender sender) throws PubnubException {
		this.sender = sender;
		if (this.sender == null) {
			throw new PubnubException("Sender is null");
		}
		sender.send(this);
	}
	
	public void publish() throws PubnubException {
		if (this.sender == null) {
			throw new PubnubException("Sender is null");
		}
		sender.send(this);
	}
	
}
