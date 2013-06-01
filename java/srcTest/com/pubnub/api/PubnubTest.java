package com.pubnub.api;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;


class SubscribeCallback extends Callback {

	private CountDownLatch latch;

	private Object response;

	public SubscribeCallback(CountDownLatch latch) {
		this.latch = latch;
	}

	public SubscribeCallback() {

	}


	public Object getResponse() {
		return response;
	}

	@Override
	public void successCallback(String channel, Object message) {
		response = message;
		if (latch != null) latch.countDown();
	}

	@Override
	public void errorCallback(String channel, Object message) {
		response = message;
		if (latch != null) latch.countDown();
	}
}

class PublishCallback extends Callback {

	private CountDownLatch latch;
	private int result = 0;

	public int getResult() {
		return result;
	}

	public PublishCallback(CountDownLatch latch) {
		this.latch = latch;
	}

	public PublishCallback() {

	}

	public void successCallback(String channel, Object message) {
		JSONArray jsarr;
		try {
			jsarr = (JSONArray)message;
			result = (Integer) jsarr.get(0);
		} catch (JSONException e) {

		}
		if (latch != null) latch.countDown();
	}

	public void errorCallback(String channel, Object message) {
		JSONArray jsarr;
		try {
			jsarr = new JSONArray(message);
			System.out.println(message.toString());
			result = (Integer) jsarr.get(0);
		} catch (JSONException e) {

		}
		if (latch != null) latch.countDown();
	}

}




public class PubnubTest {
	Pubnub pubnub = new Pubnub("demo", "demo");
	String testSuccessMessage = "";


	@Test
	public void testPublishString() {
		String channel = "java-unittest-" + Math.random();
		final String sendMessage = "Test Message " + Math.random();

		final CountDownLatch latch = new CountDownLatch(2); 

		final PublishCallback  pbCb = new PublishCallback(latch);
		SubscribeCallback sbCb = new SubscribeCallback(latch) {
			@Override
			public void connectCallback(String channel, Object message) {
				pubnub.publish(channel, sendMessage, pbCb);
			}
		};

		Hashtable args = new Hashtable();
		args.put("channel", channel);
		args.put("callback", sbCb);

		try {
			pubnub.subscribe(args);
		} catch (PubnubException e1) {

		}

		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		}
		assertEquals(1, pbCb.getResult());
		assertEquals(sendMessage,sbCb.getResponse());
	}

}
