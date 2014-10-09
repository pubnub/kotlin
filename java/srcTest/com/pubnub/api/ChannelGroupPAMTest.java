package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ChannelGroupPAMTest {
    Pubnub pubnub;
    String namespace = "blahtest";
    String group = "heygroup";
    String auth_key;

    double random;

    @Before
    public void setUp() throws InterruptedException {
        random = Math.random();
        auth_key = "user-ak";

        pubnub = new Pubnub("demo-36", "demo-36", "demo-36");
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);
    }

    @Test
    public void testGrantAllNonNamespacedChannelGroup()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(group, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(group, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions
                .getJSONObject("channel-groups")
                .getJSONObject(group);

        assertEquals("channel-group", permissions.getString("level"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(group, true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(group, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions
                .getJSONObject("channel-groups")
                .getJSONObject(group);

        assertEquals("channel-group", permissions.getString("level"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }

    @Test
    public void testGrantUserNonNamespacedChannelGroup()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(group, auth_key, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(group, auth_key, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(group, permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(group, auth_key, true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(group, auth_key, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(group, permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }

    @Test
    public void testGrantAllNamespacedChannelGroup()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(namespace + ":" + group, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":" + group, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions
                .getJSONObject("channel-groups")
                .getJSONObject(namespace + ":" + group);

        assertEquals("channel-group", permissions.getString("level"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(namespace + ":" + group, true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":" + group, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions
                .getJSONObject("channel-groups")
                .getJSONObject(namespace + ":" + group);

        assertEquals("channel-group", permissions.getString("level"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }

    @Test
    public void testGrantUserNamespacedChannelGroup()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(namespace + ":" + group, auth_key, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":" + group, auth_key, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(namespace + ":" + group, permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(namespace + ":" + group, auth_key, true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":" + group, auth_key, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(namespace + ":" + group, permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }

    @Test
    public void testGrantAllNamespace()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(namespace + ":", cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":", cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions
                .getJSONObject("channel-groups")
                .getJSONObject(namespace + ":");

        assertEquals("channel-group", permissions.getString("level"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(namespace + ":", true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":", cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions
                .getJSONObject("channel-groups")
                .getJSONObject(namespace + ":");

        assertEquals("channel-group", permissions.getString("level"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }

    @Test
    public void testGrantUserNamespace()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(namespace + ":", auth_key, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":", auth_key, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(namespace + ":", permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(namespace + ":", auth_key, true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(namespace + ":", auth_key, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(namespace + ":", permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }

    @Test
    public void testGrantAllGlobalNamespace()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(":", cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(":", cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse())
                .getJSONObject("channel-groups")
                .getJSONObject(":");

        assertEquals(0, permissions.getInt("w"));
        assertEquals(0, permissions.getInt("r"));
        assertEquals(0, permissions.getInt("m"));

        pubnub.pamGrantChannelGroup(":", true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(":", cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse())
                .getJSONObject("channel-groups")
                .getJSONObject(":");

        assertEquals(0, permissions.getInt("w"));
        assertEquals(1, permissions.getInt("r"));
        assertEquals(1, permissions.getInt("m"));
    }

    @Test
    public void testGrantUserGlobalNamespace()
            throws InterruptedException, PubnubException, JSONException {
        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(1);
        final CountDownLatch latch4 = new CountDownLatch(1);

        TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        pubnub.pamRevokeChannelGroup(":", auth_key, cb1);
        latch1.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(":", auth_key, cb2);
        latch2.await(10, TimeUnit.SECONDS);

        JSONObject permissions = ((JSONObject) cb2.getResponse());
        JSONObject auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(":", permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(0, auths.getInt("r"));
        assertEquals(0, auths.getInt("m"));

        pubnub.pamGrantChannelGroup(":", auth_key, true, true, cb3);
        latch3.await(10, TimeUnit.SECONDS);

        pubnub.pamAuditChannelGroup(":", auth_key, cb4);
        latch4.await(10, TimeUnit.SECONDS);

        permissions = ((JSONObject) cb4.getResponse());
        auths = permissions.getJSONObject("auths").getJSONObject(auth_key);

        assertEquals("channel-group+auth", permissions.getString("level"));
        assertEquals(":", permissions.getString("channel-group"));
        assertEquals(0, auths.getInt("w"));
        assertEquals(1, auths.getInt("r"));
        assertEquals(1, auths.getInt("m"));
    }
}