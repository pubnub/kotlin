package com.pubnub.examples;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;


class SubscribeCallback extends Callback {
	@Override
	public void successCallback(String channel, Object message) {
		System.out.println("Message : " + message + ", on " + channel);
	}
	@Override
	public void connectCallback(String channel, Object message) {
		System.out.println("Connect  on : " + channel);
	}
	@Override
	public void errorCallback(String channel, PubnubError message) {
		System.out.println("Message : " + message + ", on " + channel);
	}
}

class GenericCallback extends Callback {
	@Override
	public void successCallback(String channel, Object message) {
		System.out.println("CALLBACK : " + message );
	}
	@Override
	public void errorCallback(String channel, PubnubError error) {
		System.out.println("ERROR : " + error);
	}
}

public class UnsubscribeCheck {

	public static void main(String[] args) {
		Pubnub pubnub = new Pubnub("ds", "ds");
		
		try {
			pubnub.subscribe("a", new SubscribeCallback());
			
			Thread.sleep(5000);
			
			pubnub.unsubscribeAll(new GenericCallback());
			
			Thread.sleep(5000);
			
			System.out.println();
			
			pubnub.subscribe("a", new SubscribeCallback());
			
			Thread.sleep(5000);
			pubnub.unsubscribe("a", new GenericCallback());
			
			Thread.sleep(5000);
			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.channelGroupUnsubscribe("ab", new GenericCallback());
			
			Thread.sleep(5000);
			
			System.out.println();
			
			
			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.channelGroupUnsubscribe("ab");
			
			
			Thread.sleep(5000);
			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll(new GenericCallback());
			
			
			
			Thread.sleep(5000);
			System.out.println();			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			pubnub.subscribe("b", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll(new GenericCallback());
			
			
			
			Thread.sleep(5000);
			
			System.out.println();
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			pubnub.subscribe("b", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll(new GenericCallback());
			
			
			Thread.sleep(5000);
			
			System.out.println();
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll(new GenericCallback());
			
			Thread.sleep(5000);
			System.out.println();			
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll(new GenericCallback());
			
			Thread.sleep(5000);
			System.out.println();		
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll(new GenericCallback());
			
			Thread.sleep(5000);
			
		
			
			pubnub.subscribe("a", new SubscribeCallback());
			
			Thread.sleep(5000);
			
			pubnub.unsubscribeAll();
			
			Thread.sleep(5000);
			
			System.out.println();
			
			pubnub.subscribe("a", new SubscribeCallback());
			
			Thread.sleep(5000);
			pubnub.unsubscribe("a", new GenericCallback());
			
			Thread.sleep(5000);
			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.channelGroupUnsubscribe("ab");
			
			Thread.sleep(5000);
			
			System.out.println();
			
			
			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.channelGroupUnsubscribe("ab");
			
			
			Thread.sleep(5000);
			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll();
			
			
			
			Thread.sleep(5000);
			System.out.println();			
			pubnub.channelGroupSubscribe("ab", new SubscribeCallback());
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			pubnub.subscribe("b", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll();
			
			
			
			Thread.sleep(5000);
			
			System.out.println();
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			pubnub.subscribe("b", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll();
			
			
			Thread.sleep(5000);
			
			System.out.println();
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			pubnub.subscribe("a", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll();
			
			Thread.sleep(5000);
			System.out.println();			
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll();
			
			Thread.sleep(5000);
			System.out.println();		
			pubnub.channelGroupSubscribe("cd", new SubscribeCallback());
			
			
			Thread.sleep(5000);

			pubnub.unsubscribeAll();
			
			
			
			
			
			Thread.sleep(5000);
			pubnub.shutdown();
			
		} catch (PubnubException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
