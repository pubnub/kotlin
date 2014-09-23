package com.pubnub.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class UnsubscribeTest {
    Pubnub pubnub;
    Pubnub pubnub2;

    double random;

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

        random = Math.random();
    }

    @Test
    public void testUnsubscribe()
            throws InterruptedException, PubnubException {
        String channel = "ch6";
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
    public void testUnsubscribeGroups() {

    }
}
