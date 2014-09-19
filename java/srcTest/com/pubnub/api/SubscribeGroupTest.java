package com.pubnub.api;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubscribeGroupTest {
    Pubnub pubnub = new Pubnub("demo", "demo");

    @Before
    public void setUp() {
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);
    }

    @Test
    public void testSubscribeMultipleChannelsAndMultipleChannelGroups()
            throws InterruptedException {
        String[] channels = new String[] {"ch1", "ch2", "ch3"};
        String[] groups = new String[] {"jtest1", "jtest2", "jtest3"};

        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(2);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals("jtest1")) {
                    pubnub.publish("ch4", sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, "ok", cb3);
                super.successCallback(channel, message);
            }
        };

        pubnub.addChannelToGroup("jtest1", "ch4", cb1);

        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channels, groups, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals(sendMessage, cb4.getResponse());
    }

    @Test
    public void testSubscribeMultipleChannelsAndOneChannelGroup()
            throws InterruptedException {
        final String[] channels = new String[] {"ch1", "ch2", "ch3"};
        String group = "jtest";

        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(3);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals("jtest")) {
                    pubnub.publish("ch4", sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                if (channel.equals("jtest") && message.equals(sendMessage)) {
                    pubnub.publish(channels[1], "ok", cb3);
                }

                super.successCallback(channel, message);
            }
        };

        pubnub.addChannelToGroup("jtest1", "ch4", cb1);

        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channels, group, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals("ok", cb4.getResponse());
    }

    @Test
    public void testSubscribeOneChannelAndMultipleChannelGroups()
            throws InterruptedException {
        String channel = "ch1";
        String[] groups = new String[] {"jtest1", "jtest2", "jtest3"};

        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(2);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals("jtest1")) {
                    pubnub.publish("ch4", sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, "ok", cb3);
                super.successCallback(channel, message);
            }
        };

        pubnub.addChannelToGroup("jtest1", "ch4", cb1);

        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channel, groups, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals(sendMessage, cb4.getResponse());
    }

    @Test
    public void testSubscribeOneChannelAndOneChannelGroup()
            throws InterruptedException {
        String channel = "ch1";
        String groups = "jtest";

        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(2);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals("jtest")) {
                    pubnub.publish("ch4", sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, "ok", cb3);
                super.successCallback(channel, message);
            }
        };

        pubnub.addChannelToGroup("jtest", "ch4", cb1);

        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channel, groups, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals(sendMessage, cb4.getResponse());
    }
}
