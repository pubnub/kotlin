package com.pubnub.api;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class PresenceTest {
    Pubnub pubnub = new Pubnub("demo", "demo");
    Pubnub pubnub2 = new Pubnub("demo", "demo");

    String random;

    @Before
    public void setUp() throws PubnubException {
        pubnub.setCacheBusting(false);
        pubnub2.setCacheBusting(false);

        random = UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    public void testPresenceOnCurrentClient() throws InterruptedException, PubnubException {
        String channel = "java-unittest-" + Math.random();

        CountDownLatch latch = new CountDownLatch(1);

        TestHelper.PresenceCallback presenceCb = new TestHelper.PresenceCallback(latch);

        pubnub.presence(channel, presenceCb);

        pubnub.subscribe(channel, new Callback() {
        });

        latch.await(10, TimeUnit.SECONDS);

        assertEquals(pubnub.getUUID(), presenceCb.getUUID());
    }

    @Test
    public void testPresenceOnOtherClient() throws InterruptedException, PubnubException {
        String channel = "java-unittest-" + Math.random();

        CountDownLatch latch = new CountDownLatch(1);

        TestHelper.PresenceCallback presenceCb = new TestHelper.PresenceCallback(latch);

        pubnub.presence(channel, presenceCb);

        pubnub2.subscribe(channel, new Callback() {
        });

        latch.await(10, TimeUnit.SECONDS);

        assertEquals(pubnub2.getUUID(), presenceCb.getUUID());
    }
}
