package com.pubnub.examples.blackberry;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;

public class Time extends PubnubCommand {

	public Time(Pubnub pubnub) {
		super(pubnub, "Time");
	}

	protected void initScreen() {
	}
	public void handler() {
		_pubnub.time( new Callback() {
			public void successCallback(String channel, Object message) {
				notifyUser(message.toString());
			}

			public void errorCallback(String channel, Object message) {
				notifyUser(message.toString());
			}
		});
	}
}
