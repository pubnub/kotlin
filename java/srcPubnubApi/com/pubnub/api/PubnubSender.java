package com.pubnub.api;

public class PubnubSender implements Sender {
	
	private Callback callback;
	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	private Pubnub pubnub;
	public Pubnub getPubnub() {
		return pubnub;
	}

	public void setPubnub(Pubnub pubnub) {
		this.pubnub = pubnub;
	}

	private String channel;
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public PubnubSender(String channel, Pubnub pubnub, Callback callback) {
		super();
		this.callback = callback;
		this.pubnub = pubnub;
		this.channel = channel;
	}
	
	public PubnubSender(PubnubSender pns) {
		this.callback = pns.getCallback();
		this.channel = pns.getChannel();
		this.pubnub = pns.getPubnub();
	}
	
	public void send(PnMessage message) throws PubnubException {
		if (pubnub == null || channel == null) {
			throw new PubnubException("Pubnub is null");
		}
		pubnub.publish(channel, message, callback);
	}

}
