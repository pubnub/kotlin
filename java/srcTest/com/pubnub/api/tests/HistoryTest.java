package com.pubnub.api.tests;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubSync;

public class HistoryTest {

	static PubnubSync pubnub = new PubnubSync("demo", "demo");
	static String unique = "" + System.currentTimeMillis();
	static String channel = "channel-" + unique;
	static String message = "message-" + unique + "-";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		for (int i = 0; i < 10; i++) {
			try {
			JSONArray jsa = (JSONArray) pubnub.publish(channel, message + i);
			} catch (Exception e) {
				fail("Exception " + e.toString());
			}
		}
		Thread.sleep(5000);
	}

	@Test
	public void historyCount10() {
		try {
			JSONArray response = (JSONArray) pubnub.history(channel, 10);
			JSONArray messages = (JSONArray) response.get(0);
			assertEquals(messages.length(), 10);
			assertEquals(messages.get(0), message + "0");
			assertEquals(messages.get(9), message + "9");
		} catch (Exception e) {
			fail("Exception " + e.toString());
		}
	}

	@Test
	public void historyCount10Reverse() {
		try {
			JSONArray response = (JSONArray) pubnub.history(channel, 10, true);
			JSONArray messages = (JSONArray) response.get(0);
			assertEquals(messages.length(), 10);
			assertEquals(messages.get(0), message + "0");
			assertEquals(messages.get(9), message + "9");
		} catch (Exception e) {
			fail("Exception " + e.toString());
		}
	}
	
	@Test
	public void historyCount5() {
		try {
			JSONArray response = (JSONArray) pubnub.history(channel, 5);
			JSONArray messages = (JSONArray) response.get(0);
			assertEquals(messages.length(), 5);
			assertEquals(messages.get(0), message + "5");
			assertEquals(messages.get(4), message + "9");
		} catch (Exception e) {
			fail("Exception " + e.toString());
		}
	}

	@Test
	public void historyCount5Reverse() {
		try {
			JSONArray response = (JSONArray) pubnub.history(channel, 5, true);
			JSONArray messages = (JSONArray) response.get(0);
			assertEquals(messages.length(), 5);
			assertEquals(messages.get(0), message + "0");
			assertEquals(messages.get(4), message + "4");
		} catch (Exception e) {
			fail("Exception " + e.toString());
		}
	}
	
	
}
