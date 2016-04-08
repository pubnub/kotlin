package com.pubnub.api.endpoints;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.endpoints.presence.Heartbeat;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Created by Max on 4/1/16.
 */
public class HeartbeatEndpointTest extends EndpointTest {

    private MockWebServer server;
    private Heartbeat.HeartbeatBuilder partialHeartbeat;
    private Pubnub pubnub;

    /*
    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();

        pubnub = this.createPubNubInstance(server);
    }

    @org.junit.Test
    public void testSuccessOneChannel() throws PubnubException, InterruptedException {
        pubnub.subscribe().addChannel("ch1").execute();
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        pubnub. .build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1/heartbeat?heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessManyChannels() throws PubnubException, InterruptedException {
        pubnub.subscribe().addChannel("ch1").addChannel("ch2").execute();
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat?heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessOneChannelGroup() throws PubnubException, InterruptedException {
        pubnub.subscribe().addChannelGroup("cg1").execute();
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat?channel-group=cg1&heartbeat=123&state={}&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSuccessManyChannelGroups() throws PubnubException, InterruptedException {
        pubnub.subscribe().addChannelGroup("cg1").addChannelGroup("cg2").execute();
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

        pubnub.subscribe().addChannel("ch1").addChannel("ch2").execute();
        pubnub.setPresenceState().channel("ch1").state(state).build().sync();
        pubnub.setPresenceState().channel("ch2").state(state2).build().sync();
        pubnub.getConfiguration().setPresenceTimeout(123);

        server.enqueue(new MockResponse().setBody(("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\"}")));
        partialHeartbeat.build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat?heartbeat=123&state={%22ch2%22:{},%22ch1%22:{%22hi%22:%22CH2%22}}&uuid=myUUID", server.takeRequest().getPath());
    }
    */
}
