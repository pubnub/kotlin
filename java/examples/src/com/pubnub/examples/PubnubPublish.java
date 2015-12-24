package com.pubnub.examples;

import com.pubnub.api.*;

public class PubnubPublish {

	public static void main(String[] args) {
		final PubnubSync pubnub = new PubnubSync("demo", "demo");
		
		System.out.println(pubnub.publish("demo", 1));

	}

}
