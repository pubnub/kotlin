package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;
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


    @BeforeClass
    public static void cleanup() throws InterruptedException, JSONException {
        TestHelper.cleanup();
    }

    @Before
    public void setUp() {
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);
        channelGroup = UUID.randomUUID().toString().substring(0, 8);
        channelNamespace = "jtest-namespace";
    }

    @Test
    public void testAddChannelToNonNameSpacedGroup() {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.channelGroupAddChannel(channelGroup, "ch1", cb);

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

        pubnub.channelGroupAddChannel(channelNamespace + ":" + channelGroup, "ch1", cb);

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

        pubnub.channelGroupAddChannel(channelGroup, new String[]{"ch1", "ch2"}, cb);

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

        pubnub.channelGroupAddChannel(channelNamespace + ":" + channelGroup, new String[]{"ch1", "ch2"}, cb);

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

        pubnub.channelGroupAddChannel(channelGroup, "ch1", cb1);
        pubnub.channelGroupAddChannel(channelGroup, new String[]{"ch2"}, cb2);
        pubnub.channelGroupAddChannel(channelGroup, new String[]{"ch3", "ch4", "ch5"}, cb3);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListChannels(channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
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

        pubnub.channelGroupAddChannel(channelNamespace + ":" + channelGroup, "ch1", cb1);
        pubnub.channelGroupAddChannel(channelNamespace + ":" + channelGroup, new String[]{"ch2"}, cb2);
        pubnub.channelGroupAddChannel(channelNamespace + ":" + channelGroup, new String[]{"ch3", "ch4", "ch5"}, cb3);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListChannels(channelNamespace + ":" + channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
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

        pubnub.channelGroupAddChannel(channelGroup, new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupRemoveChannel(channelGroup, "ch1", cb2);
        pubnub.channelGroupRemoveChannel(channelGroup, new String[]{"ch4", "ch5"}, cb3);

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        assertFalse("Error is thrown", cb2.responseIsError());
        assertFalse("Error is thrown", cb3.responseIsError());
        assertEquals("OK", cb2.getResponse());
        assertEquals("OK", cb3.getResponse());

        pubnub.channelGroupListChannels(channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch2", "ch3"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
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

        pubnub.channelGroupAddChannel(channelNamespace + ":" + channelGroup, new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupRemoveChannel(channelNamespace + ":" + channelGroup, "ch1", cb2);
        pubnub.channelGroupRemoveChannel(channelNamespace + ":" + channelGroup, new String[]{"ch4", "ch5"}, cb3);

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        assertFalse("Error is thrown", cb2.responseIsError());
        assertFalse("Error is thrown", cb3.responseIsError());
        assertEquals("OK", cb2.getResponse());
        assertEquals("OK", cb3.getResponse());

        pubnub.channelGroupListChannels(channelNamespace + ":" + channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch2", "ch3"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
    }

    @Test
    public void testGetAllChannelGroupNames() throws InterruptedException, JSONException {
        String group1 = "jtest_group1";
        String group2 = "jtest_group2";

        JSONObject result;
        JSONArray groups;

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.channelGroupAddChannel(group1, "ch1", cb1);
        pubnub.channelGroupAddChannel(group2, "ch2", cb2);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListGroups(cb3);

        latch3.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb3.getResponse();
        groups = result.getJSONArray("groups");

        assertJSONArrayHas(group1, groups);
        assertJSONArrayHas(group2, groups);
        assertJSONArrayHasNo("jtest_group3", groups);
    }

    @Test
    public void testGetAllChannelGroupNamesNamespace() throws InterruptedException, JSONException {
        String group1 = "jtest_group1";
        String group2 = "jtest_group2";

        JSONObject result;
        JSONArray groups;

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);

        pubnub.channelGroupAddChannel(channelNamespace + ":" + group1, "ch1", cb1);
        pubnub.channelGroupAddChannel(channelNamespace + ":" + group2, "ch2", cb2);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListGroups(channelNamespace, cb3);

        latch3.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb3.getResponse();
        groups = result.getJSONArray("groups");

        assertJSONArrayHas(group1, groups);
        assertJSONArrayHas(group2, groups);
        assertJSONArrayHasNo("jtest_group3", groups);
        assertEquals(channelNamespace, result.getString("namespace"));
    }

    @Test
    public void testRemoveGroup() throws InterruptedException, JSONException {
        String group = "jtest_group1";
        JSONObject result;
        JSONArray groups;

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.channelGroupAddChannel(group, "ch1", cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListGroups(cb2);
        latch2.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb2.getResponse();
        groups = result.getJSONArray("groups");
        assertJSONArrayHas(group, groups);

        pubnub.channelGroupRemoveGroup(group, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListGroups(cb4);
        latch4.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb4.getResponse();
        groups = result.getJSONArray("groups");

        assertEquals("OK", cb3.getResponse().toString());
        assertJSONArrayHasNo(group, groups);
    }

    @Test
    public void testRemoveNamespacedGroup() throws InterruptedException, JSONException {
        String group = "jtest_group1";
        JSONObject result;
        JSONArray groups;

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.channelGroupAddChannel(channelNamespace + ":" + group, "ch1", cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListGroups(channelNamespace, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb2.getResponse();
        groups = result.getJSONArray("groups");
        assertJSONArrayHas(group, groups);
        assertEquals(channelNamespace, result.getString("namespace"));

        pubnub.channelGroupRemoveGroup(channelNamespace + ":" + group, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.channelGroupListGroups(channelNamespace, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb4.getResponse();
        groups = result.getJSONArray("groups");
        assertJSONArrayHasNo(group, groups);
        assertEquals(channelNamespace, result.getString("namespace"));
    }
}
