package com.pubnub.api;

public interface Sender {
	public void send(PnMessage message) throws PubnubException;
}
