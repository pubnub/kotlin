package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.pubnub.api.matchers.JSONAssert.assertJSONArrayHas;
import static org.junit.Assert.assertEquals;

public class HereNowTest {
    Pubnub pubnub;
    String group;

    String random;

    @Before
    public void setUp() throws InterruptedException {
        random = UUID.randomUUID().toString().substring(0, 8);

        pubnub = new Pubnub("demo", "demo", "demo");
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);

        group = "jtest-" + random;
    }

    @Test
    public void testHereNowForOneChannel()
            throws InterruptedException, PubnubException, JSONException {

        final String channel = "ch1" + random;

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals(channel)) latch.countDown();
            }
        };

        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);

        pubnub.subscribe(channel, cb1);
        latch1.await(10, TimeUnit.SECONDS);
        Thread.sleep(1000);

        pubnub.hereNow(channel, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject response = (JSONObject) cb2.getResponse();
        JSONArray uuids = response.getJSONArray("uuids");
        assertJSONArrayHas(pubnub.getUUID(), uuids);
    }

    @Test
    public void testHereNowForOneChannelGroup()
            throws InterruptedException, PubnubException, JSONException {

        final String[] channels = new String[]{"ch1" + random, "ch2" + random};

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2) {
            @Override
            public void connectCallback(String item, Object message) {
                latch.countDown();
            }
        };

        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.channelGroupAddChannel(group, channels, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.subscribe(channels[0], cb2);
        latch2.await(10, TimeUnit.SECONDS);
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        JSONObject response = (JSONObject) cb3.getResponse();
        JSONArray uuids = response
                .getJSONObject("channels")
                .getJSONObject(channels[0])
                .getJSONArray("uuids");

        assertEquals(1, response.getInt("total_occupancy"));
        assertEquals(1, response.getInt("total_channels"));

        assertJSONArrayHas(pubnub.getUUID(), uuids);
    }

    @Test
    public void testHereNowForMultipleChannels() throws InterruptedException, PubnubException, JSONException {
        final String[] channels = new String[]{"ch1" + random, "ch2" + random};
        final String[] groups = new String[]{group, "jtest2-" + random};

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3) {
            @Override
            public void connectCallback(String item, Object message) {
                latch.countDown();
            }
        };

        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.channelGroupAddChannel(groups[0], channels, cb1);
        pubnub.channelGroupAddChannel(groups[1], channels, cb2);
        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.subscribe(channels, cb3);
        latch3.await(10, TimeUnit.SECONDS);
        Thread.sleep(1000);

        pubnub.channelGroupHereNow(group, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        JSONObject response = (JSONObject) cb4.getResponse();
        JSONArray uuids1 = response
                .getJSONObject("channels")
                .getJSONObject(channels[0])
                .getJSONArray("uuids");

        JSONArray uuids2 = response
                .getJSONObject("channels")
                .getJSONObject(channels[1])
                .getJSONArray("uuids");

        assertEquals(2, response.getInt("total_occupancy"));
        assertEquals(2, response.getInt("total_channels"));

        assertJSONArrayHas(pubnub.getUUID(), uuids1);
        assertJSONArrayHas(pubnub.getUUID(), uuids2);
    }

    @Test
    public void testHereNowGlobal() throws JSONException, InterruptedException, PubnubException {
        final String[] channels = new String[]{"ch1-" + random, "ch2-" + random};

        Pubnub pubnub2 = new Pubnub("demo", "demo");
        pubnub2.setOrigin("dara24.devbuild");
        pubnub2.setCacheBusting(false);

        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);
        final CountDownLatch latch5 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3) {
            @Override
            public void connectCallback(String item, Object message) {
                latch.countDown();
            }
        };

        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4) {
            @Override
            public void connectCallback(String item, Object message) {
                latch.countDown();
            }
        };

        final TestHelper.SimpleCallback cb5 = new TestHelper.SimpleCallback(latch5);

        pubnub.subscribe(channels[0], cb3);
        latch3.await(10, TimeUnit.SECONDS);
        pubnub2.subscribe(channels[1], cb4);
        latch4.await(10, TimeUnit.SECONDS);
        Thread.sleep(1000);

        pubnub.hereNow(true, true, cb5);
        latch5.await(10, TimeUnit.SECONDS);

        JSONObject response = (JSONObject) cb5.getResponse();

        String uuid1 = response
                .getJSONObject("channels")
                .getJSONObject(channels[0])
                .getJSONArray("uuids")
                .getJSONObject(0)
                .getString("uuid");

        String uuid2 = response
                .getJSONObject("channels")
                .getJSONObject(channels[1])
                .getJSONArray("uuids")
                .getJSONObject(0)
                .getString("uuid");

        assertEquals(pubnub.getUUID(), uuid1);
        assertEquals(pubnub2.getUUID(), uuid2);
    }
}
