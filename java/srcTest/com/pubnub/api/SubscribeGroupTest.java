package com.pubnub.api;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubscribeGroupTest {
    Pubnub pubnub = new Pubnub("demo", "demo");
    String random;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws PubnubException {
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);

        pubnub.channelGroupRemoveGroup("jtest", new TestHelper.SimpleCallback());
        pubnub.channelGroupRemoveGroup("jtest1", new TestHelper.SimpleCallback());
        pubnub.channelGroupRemoveGroup("jtest2", new TestHelper.SimpleCallback());
        pubnub.channelGroupRemoveGroup("jtest3", new TestHelper.SimpleCallback());

        random = UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    public void testSubscribeMultipleChannelsAndMultipleChannelGroups()
            throws InterruptedException, PubnubException {
        final String[] channels = new String[]{"ch1" + random, "ch2" + random, "ch3" + random};
        final String[] groups = new String[]{"jtest1" + random, "jtest2" + random, "jtest3" + random};

        final String testChannel = "ch4" + random;
        final String sendMessage = "Test Message-" + Math.random();
        final String okMessage = "ok-" + random;

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(2);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals(groups[1])) {
                    pubnub.publish(testChannel, sendMessage, cb2);
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                if (message.equals(sendMessage)) {
                    pubnub.publish(channel, okMessage, cb2);
                }
                super.successCallback(channel, message);
            }
        };

        pubnub.channelGroupAddChannel(groups[0], testChannel, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channels, groups, cb3);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        assertEquals(okMessage, cb3.getResponse());
    }

    @Test
    public void testSubscribeMultipleChannelsAndOneChannelGroup()
            throws InterruptedException, PubnubException {
        final String[] channels = new String[]{"ch1", "ch2", "ch3"};
        final String group = "jtest" + random;
        final String ch4 = "ch4-" + random;

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
                if (item.equals(group)) {
                    pubnub.publish(ch4, sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                if (channel.equals(ch4) && message.equals(sendMessage)) {
                    pubnub.publish(channels[1], "ok", cb3);
                }

                super.successCallback(channel, message);
            }
        };

        pubnub.channelGroupAddChannel(group, ch4, cb1);

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
            throws InterruptedException, PubnubException {
        final String channel = "ch1";
        final String[] groups = new String[]{"jtest1" + random, "jtest2" + random, "jtest3" + random};
        final String ch4 = "ch4-" + random;
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
                if (item.equals(groups[0])) {
                    pubnub.publish(ch4, sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, "ok", cb3);
                super.successCallback(channel, message);
            }
        };

        pubnub.channelGroupAddChannel(groups[0], ch4, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channel, groups, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals("ok", cb4.getResponse());
    }

    @Test
    public void testSubscribeOneChannelAndOneChannelGroup()
            throws InterruptedException, PubnubException {
        String channel = "ch1";
        final String group = "jtest" + random;
        final String ch4 = "ch4" + random;

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
                if (item.equals(group)) {
                    pubnub.publish(ch4, sendMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, "ok", cb3);
                super.successCallback(channel, message);
            }
        };

        pubnub.channelGroupAddChannel(group, ch4, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.subscribe(channel, group, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals("ok", cb4.getResponse());
    }

    @Test
    public void testSubscribeOneChannelAndNoChannelGroup()
            throws InterruptedException {
        final String channel = "ch1" + random;

        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(3);

        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4) {
            @Override
            public void connectCallback(String item, Object message) {
                if (item.equals(channel)) {
                    pubnub.publish(channel, "test", cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, sendMessage, cb3);
                super.successCallback(channel, message);
            }
        };

        try {
            pubnub.subscribe(channel, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals(sendMessage, cb4.getResponse());

    }

    @Test
    public void testSubscribeNoChannelAndOneChannelGroup()
            throws InterruptedException, PubnubException {
        final String group = "jtest-" + random;
        final String ch4 = "ch4-" + random;

        final String sendMessage = "Test Message " + Math.random();
        final String testMessage = "test-" + Math.random();

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
                if (item.equals(group)) {
                    pubnub.publish(ch4, testMessage, cb2);
                    latch.countDown();
                }
            }

            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(ch4, sendMessage, cb3);
                super.successCallback(channel, message);
            }
        };

        pubnub.channelGroupAddChannel(group, ch4, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        try {
            pubnub.channelGroupSubscribe(group, cb4);
        } catch (PubnubException e) {
            fail(e.getMessage());
        }

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);
        latch4.await(10, TimeUnit.SECONDS);

        assertEquals(sendMessage, cb4.getResponse());
    }

    @Test
    public void testSubscribeNoChannelAndNoChannelGroup()
            throws InterruptedException, PubnubException {

        final TestHelper.SimpleCallback callback = new TestHelper.SimpleCallback() {
            @Override
            public void connectCallback(String item, Object message) {
            }

            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);
            }
        };

        Hashtable<String, Object> args = new Hashtable<String, Object>();

        args.put("callback", callback);

        thrown.expect(PubnubException.class);

        pubnub.subscribe(args);
    }

    @After
    public void tearDown() throws PubnubException {
        pubnub.channelGroupRemoveGroup("jtest" + random, new TestHelper.SimpleCallback());
        pubnub.channelGroupRemoveGroup("jtest1" + random, new TestHelper.SimpleCallback());
        pubnub.channelGroupRemoveGroup("jtest2" + random, new TestHelper.SimpleCallback());
        pubnub.channelGroupRemoveGroup("jtest3" + random, new TestHelper.SimpleCallback());
    }
}
