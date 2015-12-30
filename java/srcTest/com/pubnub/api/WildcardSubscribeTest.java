package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WildcardSubscribeTest {
    Pubnub pubnub = new Pubnub("demo-36", "demo-36");
    Pubnub pubnub2 = new Pubnub("demo-36", "demo-36");
    String channel = "ytest-foo";

    @Before
    public void setUp() throws PubnubException {
        pubnub.setCacheBusting(false);
        pubnub2.setCacheBusting(false);
        pubnub.setOrigin("ps9");
        pubnub2.setOrigin("ps9");
    }

    @Test
    public void testSubscribeToWildcardChannel() throws PubnubException, InterruptedException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch2) {
            @Override
            public void connectCallback(String channel, Object message) {
                latch1.countDown();
            }
        };

        pubnub.subscribe(channel + ".*", cb);

        latch1.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch1.getCount());

        pubnub.publish(channel + ".bar", "hi", new Callback() {
        });

        latch2.await(3, TimeUnit.SECONDS);

        assertEquals("hi", cb.getResponse());
        assertEquals(0, latch2.getCount());
    }

    @Test
    public void testSubscribeToWildcardChannelAtSecondLevel() throws PubnubException, InterruptedException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch2) {
            @Override
            public void connectCallback(String channel, Object message) {
                latch1.countDown();
            }
        };

        pubnub.subscribe(channel + ".bar.*", cb);

        latch1.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch1.getCount());

        pubnub.publish(channel + ".bar.barbaz", "hi", new Callback() {
        });

        latch2.await(3, TimeUnit.SECONDS);

        assertEquals("hi", cb.getResponse());
        assertEquals(0, latch2.getCount());
    }

    @Test
    public void testPresenceToWildcardChannel() throws PubnubException, InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(2);

        TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch2) {
            @Override
            public void connectCallback(String channel, Object message) {
                latch1.countDown();
            }
        };

        pubnub.presence(channel + ".*", cb);

        latch1.await(5, TimeUnit.SECONDS);
        assertEquals(0, latch1.getCount());

        Thread.sleep(1000);

        pubnub2.subscribe(channel + ".bar", new Callback() {
        });

        latch2.await(5, TimeUnit.SECONDS);

        JSONObject response = (JSONObject) cb.getResponse();

        assertNotNull(response);
        assertEquals("join", response.getString("action"));
        assertEquals(pubnub2.getUUID(), response.getString("uuid"));
        assertEquals(0, latch2.getCount());
    }

    @Test
    public void testSubscribeAndPresenceToWildcardChannel() throws PubnubException, InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch2) {
            @Override
            public void connectCallback(String channel, Object message) {
                latch1.countDown();
            }
        };

        // subscribe#connect event will not be triggered, because presence event
        // already implicitly added it to the list
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch4) {
        };

        Thread.sleep(1000);

        pubnub.subscribe(channel + ".*", cb2);
        pubnub.presence(channel + ".*", cb);

        latch1.await(5, TimeUnit.SECONDS);
        assertEquals(0, latch1.getCount());

        pubnub.publish(channel + ".baz", "hello", new Callback() {
        });

        latch4.await(5, TimeUnit.SECONDS);
        assertEquals(0, latch4.getCount());

        pubnub.subscribe(channel + ".bar.baz", new Callback() {
        });

        latch2.await(5, TimeUnit.SECONDS);
        assertEquals(0, latch2.getCount());

        JSONObject response = (JSONObject) cb.getResponse();

        assertNotNull(response);
        assertEquals("join", response.getString("action"));
        assertEquals(pubnub.getUUID(), response.getString("uuid"));
        assertEquals(0, latch2.getCount());
    }

    @Test
    public void testSubscribeToChannelChannelGroupWildcardChannel() throws PubnubException, InterruptedException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch10 = new CountDownLatch(1);
        final CountDownLatch latch20 = new CountDownLatch(1);
        final CountDownLatch latch21 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback() {
            @Override
            public void connectCallback(String channel, Object message) {
                System.out.println(channel);
                latch1.countDown();
            }

            @Override
            public void successCallback(String channel, Object message) {
                this.response = message;

                if (channel.equals("stream") && message.equals("hey")) {
                    latch2.countDown();
                } else if (message.equals("hey2")) {
                    latch10.countDown();
                }
            }
        };

        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch20) {
            @Override
            public void connectCallback(String channel, Object message) {
                latch21.countDown();
            }
        };

        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.subscribe(new String[] { "stream", "news.*" }, cb);
        pubnub.channelGroupSubscribe("blog", cb2);
        pubnub.channelGroupAddChannel("blog", "user1", cb3);

        // stream,news.* connected
        latch1.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch1.getCount());
        // blog connected
        latch21.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch21.getCount());
        // add channel to channel group
        latch3.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch3.getCount());

        // publish #1
        pubnub.publish("stream", "hey", new Callback() {
        });
        latch2.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch2.getCount());

        String result = (String) cb.getResponse();

        assertNotNull(result);
        assertEquals("hey", result);

        // publish #2
        pubnub.publish("news.music", "hey2", new Callback() {
        });
        latch10.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch10.getCount());

        String result2 = (String) cb.getResponse();

        assertNotNull(result2);
        assertEquals("hey2", result2);

        // publish #3
        pubnub.publish("user1", "hey3", new Callback() {
        });
        latch20.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch20.getCount());

        String result3 = (String) cb2.getResponse();

        assertNotNull(result3);
        assertEquals("hey3", result3);
    }
}
