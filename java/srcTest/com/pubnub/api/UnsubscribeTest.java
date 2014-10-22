package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.pubnub.api.matchers.JSONAssert.assertJSONArrayHas;
import static org.junit.Assert.assertEquals;

public class UnsubscribeTest {
    Pubnub pubnub;
    Pubnub pubnub2;

    String channel;
    String group;

    String random;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        pubnub = new Pubnub("demo", "demo");
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);

        pubnub2 = new Pubnub("demo", "demo");
        pubnub2.setOrigin("dara24.devbuild");
        pubnub2.setCacheBusting(false);

        random = UUID.randomUUID().toString().substring(0, 8);
        channel = "ch6-" + random;
        group = "jtest-" + random;
    }

    @Test
    public void testUnsubscribe()
            throws InterruptedException, PubnubException {
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        TestHelper.PresenceCallback presenceCb = new TestHelper.PresenceCallback(latch);

        pubnub.presence(channel, presenceCb);
        pubnub2.subscribe(channel, new TestHelper.SimpleCallback());

        latch.await(10, TimeUnit.SECONDS);

        assertEquals(pubnub2.getUUID(), presenceCb.getUUID());
        assertEquals("join", presenceCb.getAction());

        presenceCb.setLatch(latch2);
        pubnub2.unsubscribe(channel);

        latch2.await(10, TimeUnit.SECONDS);

        assertEquals(pubnub2.getUUID(), presenceCb.getUUID());
        assertEquals("leave", presenceCb.getAction());
    }

    @Test
    public void testUnsubscribeAllForGroup()
            throws PubnubException, InterruptedException, JSONException {
        JSONObject result;

        CountDownLatch latch1 = new CountDownLatch(1);
        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);

        // check online
        connectToGroup();

        // check offline
        pubnub2.unsubscribeAll();
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb1.getResponse();
        assertEquals(0, result.getInt("total_occupancy"));
    }

    @Test
    public void testUnsubscribeAllGroupsForGroup() throws PubnubException, InterruptedException, JSONException {
        JSONObject result;

        CountDownLatch latch1 = new CountDownLatch(1);
        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);

        // check online
        connectToGroup();

        // check offline
        pubnub2.channelGroupUnsubscribeAllGroups();
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb1.getResponse();
        assertEquals(0, result.getInt("total_occupancy"));
    }

    @Test
    public void testUnsubscribeGroupForGroup()
            throws PubnubException, InterruptedException, JSONException {
        JSONObject result;

        CountDownLatch latch1 = new CountDownLatch(1);
        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);

        // check online
        connectToGroup();

        // check offline
        pubnub2.channelGroupUnsubscribe(group);
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb1.getResponse();
        assertEquals(0, result.getInt("total_occupancy"));
    }

    @Test
    public void testUnsubscribeGroupsForGroup()
            throws PubnubException, InterruptedException, JSONException {
        JSONObject result;

        CountDownLatch latch1 = new CountDownLatch(1);
        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);

        // check online
        connectToGroup();

        // check offline
        pubnub2.channelGroupUnsubscribe(new String[]{group});
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb1.getResponse();
        assertEquals(0, result.getInt("total_occupancy"));
    }

    private void connectToGroup()
            throws InterruptedException, PubnubException, JSONException {
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        CountDownLatch latch3 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SubscribeCallback cb2 = new TestHelper.SubscribeCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.channelGroupAddChannel(group, channel, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        // check online
        pubnub2.channelGroupSubscribe(group, cb2);
        latch2.await(10, TimeUnit.SECONDS);
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        JSONObject result = (JSONObject) cb3.getResponse();
        JSONArray uuids = result.getJSONObject("channels")
                .getJSONObject(channel)
                .getJSONArray("uuids");

        assertJSONArrayHas(pubnub2.getUUID(), uuids);
    }
}
