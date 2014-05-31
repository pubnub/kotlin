package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;




/**
 * Pubnub Message Object
 * @author Pubnub
 *
 */
public class PnMessage extends JSONObject {
	private Sender sender;
	
	/**
	 * Get Sender Object Associated with this Pubnub Message
	 * @return
	 * 		Sender
	 */
	public Sender getSender() {
		return sender;
	}
	/**
	 * Associate Sender Object with this Pubnub Message
	 * @param sender
	 * 		Sender object
	 */
	public void setSender(Sender sender) {
		this.sender = sender;
	}
	
	/**
	 * Constructor for Pubnub Message Class
	 * @param sender
	 * 		Sender object
	 */
	public PnMessage(Sender sender) {
		super();
		this.sender = sender;
	}
	
	/**
	 * Constructor for Pubnub Message Class
	 * @param sender
	 * 		Sender object
	 * @param apnsMsg
	 * 		Pubnub APNS message object
	 * @param gcmMsg
	 * 		Pubnub GCM message object
	 */
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
	
	/**
	 * Constructor for Pubnub Message Class
	 * @param sender
	 * 		Sender object
	 * @param gcmMsg
	 * 		Pubnub GCM message object
	 */
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
	
	/**
	 * Constructor for Pubnub Message Class
	 * @param sender
	 * 		Sender object
	 * @param apnsMsg
	 * 		Pubnub APNS message object
	 */
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
	
	/**
	 * Publish Message
	 * @param sender
	 * 		Sender object
	 * @throws PubnubSenderMissingException
	 * 		Exception since Sender object null
	 */
	public void publish(Sender sender) throws PubnubSenderMissingException {
		this.sender = sender;
		if (this.sender == null) {
			throw new PubnubSenderMissingException("Sender is null");
		}
		sender.send(this);
	}
	
	/**
	 * Publish Message
	 * @throws PubnubSenderMissingException
	 * 		Exception since sender object not set
	 */
	public void publish() throws PubnubSenderMissingException {
		if (this.sender == null) {
			throw new PubnubSenderMissingException("Sender is null");
		}
		sender.send(this);
	}
	
}
