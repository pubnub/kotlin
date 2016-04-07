package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class GetStateEndpointTest extends EndpointTest {

    private MockWebServer server;
    private GetState.GetStateBuilder partialGetState;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialGetState = this.createPubNubInstance(server).getPresenceState();
    }

    @Test
    public void testOneChannelSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}"));
        Map<String, Object> result = partialGetState.channel("testChannel").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("testChannel");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedPayloadSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}"));
        partialGetState.channel("testChannel").uuid("sampleUUID").build().sync();
    }

    @Test
    public void testMultipleChannelSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"ch1\": { \"age\" : 20, \"status\" : \"online\"}, \"ch2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}"));
        Map<String, Object> result = partialGetState.channel("ch1").channel("ch2").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("ch1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("ch2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/uuid/sampleUUID", server.takeRequest().getPath());
    }

    @Test
    public void testOneChannelGroupSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}"));
        Map<String, Object> result = partialGetState.channelGroup("cg1").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("chcg1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("chcg2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/sampleUUID?channel-group=cg1", server.takeRequest().getPath());
    }

    @Test
    public void testManyChannelGroupSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}"));
        Map<String, Object> result = partialGetState.channelGroup("cg1").channelGroup("cg2").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("chcg1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("chcg2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/sampleUUID?channel-group=cg1,cg2", server.takeRequest().getPath());
    }

    @Test
    public void testCombinationSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}"));
        Map<String, Object> result = partialGetState.channel("ch1").channelGroup("cg1").channelGroup("cg2").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("chcg1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("chcg2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/sampleUUID?channel-group=cg1,cg2", server.takeRequest().getPath());
    }

}
