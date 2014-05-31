package com.pubnub.api;

/**
 * Sender interface needs to be implemented by
 * objects that will be attached to PnMessage
 * for publishing data
 * @author Pubnub
 *
 */
public interface Sender {
	public void send(PnMessage message);
}
