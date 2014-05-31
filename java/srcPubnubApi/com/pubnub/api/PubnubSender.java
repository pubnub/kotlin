package com.pubnub.api;

/**
 * @author Pubnub
 *
 */
public class PubnubSender implements Sender {
	
	private Callback callback;
	
	
	/**
	 * Get callback associated with this Sender Object
	 * @return
	 * 		Callback object attached to this sender object
	 */
	public Callback getCallback() {
		return callback;
	}

	/**
	 * Associate callback with this sender object
	 * @param callback
	 * 			Callback object
	 */
	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	private Pubnub pubnub;
	
	
	/**
	 * Get Pubnub Connection Object attached to this sender
	 * @return
	 * 		Pubnub Connection Object
	 */
	public Pubnub getPubnub() {
		return pubnub;
	}

	/**
	 * Attach Pubnub Connection Object to this sender
	 * @param pubnub
	 * 			Pubnub object
	 */
	public void setPubnub(Pubnub pubnub) {
		this.pubnub = pubnub;
	}

	private String channel;
	
	/**
	 * Get channel
	 * @return
	 * 		Channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Set channel
	 * @param channel
	 * 			Channel
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * PubnubSender constructor
	 * @param channel
	 * 			Channel
	 * @param pubnub
	 * 			Pubnub object
	 * @param callback
	 * 			Callback object
	 */
	public PubnubSender(String channel, Pubnub pubnub, Callback callback) {
		super();
		this.callback = callback;
		this.pubnub = pubnub;
		this.channel = channel;
	}
	
	/**
	 * PubnubSender constructor
	 * @param sender
	 * 			Sender to be used as reference to create new sender
	 */
	public PubnubSender(PubnubSender sender) {
		this.callback = sender.getCallback();
		this.channel = sender.getChannel();
		this.pubnub = sender.getPubnub();
	}
	
	/**
	 * Publish the method to pubnub cloud
	 * @param message
	 * 			Message to be sent
	 */
	public void send(PnMessage message) {
		if (pubnub == null ) {
			if (callback != null) callback.errorCallback(channel, PubnubError.PNERROBJ_SENDER_CONNECTION_NOT_SET);
			return;
		}
		if (channel == null ) {
			if (callback != null) callback.errorCallback(channel, PubnubError.PNERROBJ_SENDER_CHANNEL_MISSING);
			return;
		}
		pubnub.publish(channel, message, callback);
	}

}
