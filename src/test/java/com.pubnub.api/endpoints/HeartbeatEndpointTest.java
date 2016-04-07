package com.pubnub.api.endpoints;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.presence.Heartbeat;
import com.pubnub.api.managers.StateManager;
import com.pubnub.api.managers.SubscriptionManager;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Max on 4/1/16.
 */
public class HeartbeatEndpointTest extends EndpointTest {

    private MockWebServer server;
    private Heartbeat.HeartbeatBuilder partialHeartbeat;
    private StateManager stateManager;
    private SubscriptionManager subscriptionManager;
    private Pubnub pubnub;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();

        stateManager = new StateManager();
        subscriptionManager = new SubscriptionManager();
        pubnub = this.createPubNubInstance(server);

        partialHeartbeat = Heartbeat.builder().pubnub(pubnub).stateManager(stateManager).subscriptionManager(subscriptionManager);


    }

    @org.junit.Test
    public void testSuccessOneChannel() throws PubnubException, InterruptedException {
        subscriptionManager.addChannel("ch1", false);
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1/heartbeat?heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessManyChannels() throws PubnubException, InterruptedException {
        subscriptionManager.addChannel("ch1", false);
        subscriptionManager.addChannel("ch2", false);
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat?heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessOneChannelGroup() throws PubnubException, InterruptedException {
        subscriptionManager.addChannelGroup("cg1", false);
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat?channel-group=cg1&heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessManyChannelGroups() throws PubnubException, InterruptedException {
        subscriptionManager.addChannelGroup("cg1", false);
        subscriptionManager.addChannelGroup("cg2", false);
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat?channel-group=cg1,cg2&heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessIncludeState() throws PubnubException, InterruptedException {
        Map<String, String> state = new HashMap<String, String>();
        state.put("hi", "CH1");

        Map<String, String> state2 = new HashMap<String, String>();
        state.put("hi", "CH2");

        subscriptionManager.addChannel("ch1", false);
        subscriptionManager.addChannel("ch2", false);
        stateManager.addStateForChannel("ch1", state);
        stateManager.addStateForChannel("ch2", state2);
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat?heartbeat=123&state={%22ch2%22:{},%22ch1%22:{%22hi%22:%22CH2%22}}&uuid=myUUID", server.takeRequest().getPath());
    }

}
