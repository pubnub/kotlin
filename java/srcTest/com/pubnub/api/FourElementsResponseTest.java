package com.pubnub.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Hashtable;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Pubnub.class)
public class FourElementsResponseTest {
    Subscriptions channelSubscriptions;
    Subscriptions channelGroupSubscriptions;

    Callback channelCallback;
    Callback channelCallback2;
    Callback channelGroupCallback;

    String third; // cg position
    String fourth; // channel position
    String channel;
    String timetoken;

    Object message;

    HttpRequest hreq;
    Pubnub pubnub;

    @Before
    public void setUp() throws InterruptedException {
        channelSubscriptions = new Subscriptions();
        channelGroupSubscriptions = new Subscriptions();

        channelCallback = new Callback() {
        };
        channelCallback2 = new Callback() {
        };
        channelGroupCallback = new Callback() {
        };

        timetoken = "123";

        hreq = new HttpRequest(new String[] { "a", "b", "c" }, new Hashtable(), new ResponseHandler() {
            @Override
            public void handleResponse(HttpRequest hreq, String response) {

            }

            @Override
            public void handleError(HttpRequest hreq, PubnubError error) {

            }
        });

        pubnub = PowerMockito.spy(new Pubnub("demo", "demo"));

        Whitebox.setInternalState(pubnub, "channelSubscriptions", channelSubscriptions);
        Whitebox.setInternalState(pubnub, "channelGroupSubscriptions", channelGroupSubscriptions);
    }

    @Test
    public void testChannelMessage() throws Exception {
        third = "foo";
        fourth = "foo";
        message = "hello";

        SubscriptionItem thirdSubscriptionItem = new SubscriptionItem(third, channelCallback);

        channelSubscriptions.addItem(thirdSubscriptionItem);

        Whitebox.invokeMethod(pubnub, "handleFourElementsSubscribeResponse", third, fourth, message, timetoken, hreq);

        PowerMockito.verifyPrivate(pubnub, times(1)).invoke("invokeSubscribeCallback", eq(fourth), eq(channelCallback),
                eq(message), Mockito.anyString(), eq(hreq));
    }

    @Test
    public void testChannelGroupMessage() throws Exception {
        third = "bar";
        fourth = "foo";
        message = "hello";

        SubscriptionItem fourthSubscriptionItem = new SubscriptionItem(fourth, channelCallback);
        SubscriptionItem channelGroupSubscriptionItem = new SubscriptionItem(third, channelGroupCallback);

        channelSubscriptions.addItem(fourthSubscriptionItem);
        channelGroupSubscriptions.addItem(channelGroupSubscriptionItem);

        Whitebox.invokeMethod(pubnub, "handleFourElementsSubscribeResponse", third, fourth, message, timetoken, hreq);

        PowerMockito.verifyPrivate(pubnub, times(1)).invoke("invokeSubscribeCallback", eq(fourth),
                eq(channelGroupCallback), eq(message), Mockito.anyString(), eq(hreq));
    }

    @Test
    public void testWildcardMessage() throws Exception {
        String wildcardChannel = "foo.*";
        String channel = "foo.bar";
        message = "hello";

        SubscriptionItem thirdSubscriptionItem = new SubscriptionItem(wildcardChannel, channelCallback);

        channelSubscriptions.addItem(thirdSubscriptionItem);

        Whitebox.invokeMethod(pubnub, "handleFourElementsSubscribeResponse", wildcardChannel, channel, message,
                timetoken, hreq);

        PowerMockito.verifyPrivate(pubnub, times(1)).invoke("invokeSubscribeCallback", eq(channel),
                eq(channelCallback), eq(message), Mockito.anyString(), eq(hreq));
    }

    @Test
    public void testWildcardPresenceSameLevelWithSubscribe() throws Exception {
        String presenceChannel = "foo.*-pnpres";
        String messagesChannel = "foo.*";
        String channel = "foo.*-pnpres";
        message = "hello";

        SubscriptionItem presenceSubscriptionItem = new SubscriptionItem(presenceChannel, channelCallback);
        SubscriptionItem messagesSubscriptionItem = new SubscriptionItem(messagesChannel, channelCallback2);

        channelSubscriptions.addItem(presenceSubscriptionItem);
        channelSubscriptions.addItem(messagesSubscriptionItem);

        Whitebox.invokeMethod(pubnub, "handleFourElementsSubscribeResponse", messagesChannel, channel, message,
                timetoken, hreq);

        PowerMockito.verifyPrivate(pubnub, times(1)).invoke("invokeSubscribeCallback", eq(channel),
                eq(channelCallback), eq(message), Mockito.anyString(), eq(hreq));
    }

    @Test
    public void testWildcardPresenceDifferentLevelWithSubscribe() throws Exception {
        String presenceChannel = "foo.*-pnpres";
        String messagesChannel = "foo.*";
        String channel = "foo.bar.baz.*-pnpres";
        message = "hello";

        SubscriptionItem presenceSubscriptionItem = new SubscriptionItem(presenceChannel, channelCallback);
        SubscriptionItem messagesSubscriptionItem = new SubscriptionItem(messagesChannel, channelCallback2);

        channelSubscriptions.addItem(presenceSubscriptionItem);
        channelSubscriptions.addItem(messagesSubscriptionItem);

        Whitebox.invokeMethod(pubnub, "handleFourElementsSubscribeResponse", messagesChannel, channel, message,
                timetoken, hreq);

        PowerMockito.verifyPrivate(pubnub, times(1)).invoke("invokeSubscribeCallback", eq(channel),
                eq(channelCallback2), eq(message), Mockito.anyString(), eq(hreq));
    }
}
