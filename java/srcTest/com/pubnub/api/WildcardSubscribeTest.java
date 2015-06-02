package com.pubnub.api;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class WildcardSubscribeTest {
    Pubnub pubnub = new Pubnub("demo-36", "demo-36");

    @Before
    public void setUp() throws PubnubException {
        pubnub.setCacheBusting(false);
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

        String channel = "jtest-foo";

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
    public void testSubscribeToWildcardChannel2() throws PubnubException, InterruptedException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch2) {
            @Override
            public void connectCallback(String channel, Object message) {
                latch1.countDown();
            }
        };

        String channel = "jtest-foo";

        pubnub.subscribe(channel + ".bar.*", cb);

        latch1.await(3, TimeUnit.SECONDS);
        assertEquals(0, latch1.getCount());

        pubnub.publish(channel + ".bar.barbaz", "hi", new Callback() {
        });

        latch2.await(3, TimeUnit.SECONDS);

        assertEquals("hi", cb.getResponse());
        assertEquals(0, latch2.getCount());
    }
}
