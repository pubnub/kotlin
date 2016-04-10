package com.pubnub.api.endpoints;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.presence.Heartbeat;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatEndpointTest extends EndpointTest {

    private MockWebServer server;
    private Heartbeat.HeartbeatBuilder partialHeartbeat;
    private Pubnub pubnub;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();

        pubnub = this.createPubNubInstance(server);
        partialHeartbeat = Heartbeat.builder().pubnub(pubnub);
    }

    @org.junit.Test
    public void testSuccessOneChannel() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.channels(Arrays.asList("ch1")).build().sync();
        Assert.assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1/heartbeat?heartbeat=123&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessManyChannels() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.channels(Arrays.asList("ch1", "ch2")).build().sync();
        Assert.assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat?heartbeat=123&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessOneChannelGroup() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.channelGroups(Arrays.asList("cg1")).build().sync();
        Assert.assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat?channel-group=cg1&heartbeat=123&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessManyChannelGroups() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.channelGroups(Arrays.asList("cg1", "cg2")).build().sync();
        Assert.assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat?channel-group=cg1,cg2&heartbeat=123&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessIncludeState() throws PubnubException, InterruptedException {
        Map<String, String> state = new HashMap<>();
        state.put("CH1", "this-is-channel1");
        state.put("CH2", "this-is-channel2");

        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.channels(Arrays.asList("ch1", "ch2")).state(state).build().sync();
        Assert.assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat?heartbeat=123&state={CH2%3Dthis-is-channel2,%20CH1%3Dthis-is-channel1}&uuid=myUUID", server.takeRequest().getPath());
    }
}
