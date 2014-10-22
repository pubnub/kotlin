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
import static com.pubnub.api.matchers.JSONAssert.assertJSONArrayHasNo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NamespaceTest {
    Pubnub pubnub = new Pubnub("demo", "demo");
    String random;

    @Before
    public void setUp() {
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);

        random = UUID.randomUUID().toString().substring(0, 8);
    }

    @Test
    public void testGetAllNamespacesAndRemoveThem() throws InterruptedException, JSONException, PubnubException {
        JSONObject result;
        JSONArray resultNamespaces;

        final CountDownLatch latch1 = new CountDownLatch(3);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final CountDownLatch latch3 = new CountDownLatch(3);
        final CountDownLatch latch4 = new CountDownLatch(1);

        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);
        final TestHelper.SimpleCallback cb3 = new TestHelper.SimpleCallback(latch3);
        final TestHelper.SimpleCallback cb4 = new TestHelper.SimpleCallback(latch4);

        String[] groups = new String[]{"jtest1" + random, "jtest2" + random, "jtest3" + random};
        String[] namespaces = new String[]{"jspace1" + random, "jspace2" + random, "jspace13" + random};

        // add
        for (int i = 0; i < groups.length; i++) {
            String group = groups[i];
            String namespace = namespaces[i];

            pubnub.channelGroupAddChannel(namespace + ":" + group, "ch1", cb1);
        }

        latch1.await(10, TimeUnit.SECONDS);

        // get
        pubnub.channelGroupListNamespaces(cb2);
        latch2.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb2.getResponse();
        resultNamespaces = result.getJSONArray("namespaces");

        assertFalse("Error is thrown", cb1.responseIsError());
        assertEquals("OK", cb1.getResponse());

        assertJSONArrayHas(namespaces[0], resultNamespaces);
        assertJSONArrayHas(namespaces[1], resultNamespaces);
        assertJSONArrayHas(namespaces[2], resultNamespaces);

        // remove
        pubnub.channelGroupRemoveNamespace(namespaces[0], cb3);
        pubnub.channelGroupRemoveNamespace(namespaces[1], cb3);
        pubnub.channelGroupRemoveNamespace(namespaces[2], cb3);

        latch3.await(10, TimeUnit.SECONDS);

        // get again
        pubnub.channelGroupListNamespaces(cb4);
        latch4.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb4.getResponse();
        resultNamespaces = result.getJSONArray("namespaces");

        assertFalse("Error is thrown", cb3.responseIsError());
        assertEquals("OK", cb3.getResponse());

        assertJSONArrayHasNo(namespaces[0], resultNamespaces);
        assertJSONArrayHasNo(namespaces[1], resultNamespaces);
        assertJSONArrayHasNo(namespaces[2], resultNamespaces);
    }
}
