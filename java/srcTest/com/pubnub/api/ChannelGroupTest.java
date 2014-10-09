package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
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
    public void testAddChannelToNonNameSpacedGroup() throws PubnubException {
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
    public void testAddChannelToNameSpacedGroup() throws PubnubException {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.addChannelToGroup(channelNamespace + ":" + channelGroup, "ch1", cb);

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Error is thrown", cb.responseIsError());
        assertEquals("OK", cb.getResponse());
    }

    @Test
    public void testAddChannelsToNonNameSpacedGroup() throws PubnubException {
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
    public void testAddChannelsToNameSpacedGroup() throws PubnubException {
        final CountDownLatch latch = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb = new TestHelper.SimpleCallback(latch) {
        };

        pubnub.addChannelToGroup(channelNamespace + ":" + channelGroup, new String[]{"ch1", "ch2"}, cb);

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Error is thrown", cb.responseIsError());
        assertEquals("OK", cb.getResponse());
    }

    @Test
    public void testGetChannelsOnNonNameSpacedGroup() throws InterruptedException, JSONException, PubnubException {
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

        pubnub.groupChannels(channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
    }

    @Test
    public void testGetChannelsOnNameSpacedGroup() throws InterruptedException, JSONException, PubnubException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.addChannelToGroup(channelNamespace + ":" + channelGroup, "ch1", cb1);
        pubnub.addChannelToGroup(channelNamespace + ":" + channelGroup, new String[]{"ch2"}, cb2);
        pubnub.addChannelToGroup(channelNamespace + ":" + channelGroup, new String[]{"ch3", "ch4", "ch5"}, cb3);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.groupChannels(channelNamespace + ":" + channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
    }

    @Test
    public void testRemoveChannelsFromNonNameSpacedGroup() throws InterruptedException, JSONException, PubnubException {
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

        pubnub.groupChannels(channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch2", "ch3"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
    }

    @Test
    public void testRemoveChannelsFromNameSpacedGroup() throws InterruptedException, JSONException, PubnubException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.addChannelToGroup(channelNamespace + ":" + channelGroup, new String[]{"ch1", "ch2", "ch3", "ch4", "ch5"}, cb1);

        latch1.await(10, TimeUnit.SECONDS);

        pubnub.removeChannelFromGroup(channelNamespace + ":" + channelGroup, "ch1", cb2);
        pubnub.removeChannelFromGroup(channelNamespace + ":" + channelGroup, new String[]{"ch4", "ch5"}, cb3);

        latch2.await(10, TimeUnit.SECONDS);
        latch3.await(10, TimeUnit.SECONDS);

        assertFalse("Error is thrown", cb2.responseIsError());
        assertFalse("Error is thrown", cb3.responseIsError());
        assertEquals("OK", cb2.getResponse());
        assertEquals("OK", cb3.getResponse());

        pubnub.groupChannels(channelNamespace + ":" + channelGroup, cb4);

        latch4.await(10, TimeUnit.SECONDS);

        String expectedJSONString = (new JSONArray(new String[]{"ch2", "ch3"}))
                .toString();

        JSONObject result = (JSONObject) cb4.getResponse();
        JSONArray channels = result.getJSONArray("channels");

        assertEquals(expectedJSONString, channels.toString());
        assertEquals(channelGroup, result.getString("group"));
    }

    @Test
    public void testGetAllChannelGroupNames() throws InterruptedException, JSONException, PubnubException {
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

        pubnub.addChannelToGroup(group1, "ch1", cb1);
        pubnub.addChannelToGroup(group2, "ch2", cb2);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.namespaceGroups(cb3);

        latch3.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb3.getResponse();
        groups = result.getJSONArray("groups");

        assertJSONArrayHas(group1, groups);
        assertJSONArrayHas(group2, groups);
        assertJSONArrayHasNo("jtest_group3", groups);
    }

    @Test
    public void testGetAllChannelGroupNamesNamespace() throws InterruptedException, JSONException, PubnubException {
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

        pubnub.addChannelToGroup(channelNamespace + ":" + group1, "ch1", cb1);
        pubnub.addChannelToGroup(channelNamespace + ":" + group2, "ch2", cb2);

        latch1.await(10, TimeUnit.SECONDS);
        latch2.await(10, TimeUnit.SECONDS);

        pubnub.namespaceGroups(channelNamespace, cb3);

        latch3.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb3.getResponse();
        groups = result.getJSONArray("groups");

        assertJSONArrayHas(group1, groups);
        assertJSONArrayHas(group2, groups);
        assertJSONArrayHasNo("jtest_group3", groups);
        assertEquals(channelNamespace, result.getString("namespace"));
    }

    @Test
    public void testRemoveGroup() throws InterruptedException, JSONException, PubnubException {
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

        pubnub.addChannelToGroup(group, "ch1", cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.namespaceGroups(cb2);
        latch2.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb2.getResponse();
        groups = result.getJSONArray("groups");
        assertJSONArrayHas(group, groups);

        pubnub.removeGroup(group, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.namespaceGroups(cb4);
        latch4.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb4.getResponse();
        groups = result.getJSONArray("groups");

        assertEquals("OK", cb3.getResponse().toString());
        assertJSONArrayHasNo(group, groups);
    }

    @Test
    public void testRemoveNamespacedGroup() throws InterruptedException, JSONException, PubnubException {
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

        pubnub.addChannelToGroup(channelNamespace + ":" + group, "ch1", cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.namespaceGroups(channelNamespace, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb2.getResponse();
        groups = result.getJSONArray("groups");
        assertJSONArrayHas(group, groups);
        assertEquals(channelNamespace, result.getString("namespace"));

        pubnub.removeGroup(channelNamespace + ":" + group, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.namespaceGroups(channelNamespace, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb4.getResponse();
        groups = result.getJSONArray("groups");
        assertJSONArrayHasNo(group, groups);
        assertEquals(channelNamespace, result.getString("namespace"));
    }

    @AfterClass
    public static void tearDown() throws InterruptedException, JSONException, PubnubException {
        Pubnub pubnub = new Pubnub("demo", "demo");
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);

        final CountDownLatch latch1 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);

        pubnub.namespaceGroups(cb1);
        latch1.await(10, TimeUnit.SECONDS);

        JSONObject result = (JSONObject) cb1.getResponse();
        JSONArray groups = result.getJSONArray("groups");

        for (int i = 0; i < groups.length(); i++) {
            final String group = groups.getString(i);

            if (group.startsWith("jtest")) {
                CountDownLatch latch = new CountDownLatch(1);
                pubnub.removeGroup(group, new TestHelper.SimpleCallback(latch) {
                    @Override
                    public void successCallback(String channel, Object message) {
                        System.out.println("Successfully removed group " + group);
                        super.successCallback(channel, message);
                    }
                });
                latch.await(10, TimeUnit.SECONDS);
            }

        }
    }
}
