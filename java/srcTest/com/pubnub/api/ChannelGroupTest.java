package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.pubnub.api.matchers.JSONAssert.assertJSONArrayHas;
import static com.pubnub.api.matchers.JSONAssert.assertJSONArrayHasNo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ChannelGroupTest {
    Pubnub pubnub = new Pubnub("demo", "demo");
    String channelGroup;
    String channelNamespace;

    @Before
    public void setUp() {
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);
        channelGroup = "jtest-" + Math.random();
        channelNamespace = "jtest-namespace";
    }

    @Test
    public void testAddChannelToNonNameSpacedGroup() {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.addChannelToGroup(channelGroup, "ch1", cb);

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Error is thrown", cb.responseIsError());
        assertEquals("OK", cb.getResponse());
    }

    @Test
    public void testAddChannelToNameSpacedGroup() {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.addChannelToGroup(channelNamespace, channelGroup, "ch1", cb);

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Error is thrown", cb.responseIsError());
        assertEquals("OK", cb.getResponse());
    }

    @Test
    public void testAddChannelsToNonNameSpacedGroup() {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.addChannelToGroup(channelGroup, new String[]{"ch1", "ch2"}, cb);

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Error is thrown", cb.responseIsError());
        assertEquals("OK", cb.getResponse());
    }

    @Test
    public void testAddChannelsToNameSpacedGroup() {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.addChannelToGroup(channelNamespace, channelGroup, new String[]{"ch1", "ch2"}, cb);

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Error is thrown", cb.responseIsError());
        assertEquals("OK", cb.getResponse());
    }

    @Test
    public void testGetChannelsOnNonNameSpacedGroup() throws InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.addChannelToGroup(channelGroup, "ch1", cb1);
        pubnub.addChannelToGroup(channelGroup, new String[]{"ch2"}, cb2);
        pubnub.addChannelToGroup(channelGroup, new String[]{"ch3", "ch4", "ch5"}, cb3);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.channelGroup(channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}))
                .toString();

        assertEquals(expectedJSONString, cb4.getResponse().toString());
    }

    @Test
    public void testGetChannelsOnNameSpacedGroup() throws InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.addChannelToGroup(channelNamespace, channelGroup, "ch1", cb1);
        pubnub.addChannelToGroup(channelNamespace, channelGroup, new String[]{"ch2"}, cb2);
        pubnub.addChannelToGroup(channelNamespace, channelGroup, new String[]{"ch3", "ch4", "ch5"}, cb3);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.channelGroup(channelNamespace, channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}))
                .toString();

        assertEquals(expectedJSONString, cb4.getResponse().toString());
    }

    @Test
    public void testRemoveChannelsFromNonNameSpacedGroup() throws InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.addChannelToGroup(channelGroup, new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        pubnub.removeChannelFromGroup(channelGroup, "ch1", cb2);
        pubnub.removeChannelFromGroup(channelGroup, new String[]{"ch4", "ch5"}, cb3);

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        assertFalse("Error is thrown", cb2.responseIsError());
        assertFalse("Error is thrown", cb3.responseIsError());
        assertEquals("OK", cb2.getResponse());
        assertEquals("OK", cb3.getResponse());

        pubnub.channelGroup(channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch2", "ch3"}))
                .toString();
        assertEquals(expectedJSONString, cb4.getResponse().toString());
    }

    @Test
    public void testRemoveChannelsFromNameSpacedGroup() throws InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.addChannelToGroup(channelNamespace, channelGroup, new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        pubnub.removeChannelFromGroup(channelNamespace, channelGroup, "ch1", cb2);
        pubnub.removeChannelFromGroup(channelNamespace, channelGroup, new String[]{"ch4", "ch5"}, cb3);

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        assertFalse("Error is thrown", cb2.responseIsError());
        assertFalse("Error is thrown", cb3.responseIsError());
        assertEquals("OK", cb2.getResponse());
        assertEquals("OK", cb3.getResponse());

        pubnub.channelGroup(channelNamespace, channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch2", "ch3"}))
                .toString();
        assertEquals(expectedJSONString, cb4.getResponse().toString());
    }

    @Test
    public void testGetAllChannelGroupNames() throws InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.addChannelToGroup("jtest_group1", "ch1", cb1);
        pubnub.addChannelToGroup("jtest_group2", "ch2", cb2);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.group(cb3);

        latch3.await(10, TimeUnit.SECONDS);

        JSONArray result = (JSONArray) cb3.getResponse();

        assertJSONArrayHas("jtest_group1", result);
        assertJSONArrayHas("jtest_group2", result);
        assertJSONArrayHasNo("jtest_group3", result);
    }

    @Test
    public void testGetAllChannelGroupNamesNamespace() throws InterruptedException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.addChannelToGroup(channelNamespace, "jtest_group1", "ch1", cb1);
        pubnub.addChannelToGroup(channelNamespace, "jtest_group2", "ch2", cb2);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.group(channelNamespace, cb3);

        latch3.await(10, TimeUnit.SECONDS);

        JSONArray result = (JSONArray) cb3.getResponse();

        assertJSONArrayHas("jtest_group1", result);
        assertJSONArrayHas("jtest_group2", result);
        assertJSONArrayHasNo("jtest_group3", result);
    }
}
