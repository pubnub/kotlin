package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class SetStateEndpointTest extends EndpointTest {

    private MockWebServer server;
    private SetState.SetStateBuilder partialSetState;
    private Pubnub pubnub;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        pubnub = this.createPubNubInstance(server);

        partialSetState = pubnub.setPresenceState();
    }

    @Test
    public void applyStateForChannelSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"));
        Boolean result = partialSetState.channel("testChannel").state(Arrays.asList("s1", "s2", "s3")).build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data?state=[%22s1%22,%22s2%22,%22s3%22]", server.takeRequest().getPath());
        assertEquals(result, true);
    }

    @Test
    public void applyStateForChannelsSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"));
        Boolean result = partialSetState.channel("testChannel").channel("testChannel2").state(Arrays.asList("s1", "s2", "s3")).build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/testChannel,testChannel2/uuid/myUUID/data?state=[%22s1%22,%22s2%22,%22s3%22]", server.takeRequest().getPath());
        assertEquals(result, true);
    }

    @Test
    public void applyStateForChannelGroupSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"));
        Boolean result = partialSetState.channelGroup("cg1").state(Arrays.asList("s1", "s2", "s3")).build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/myUUID/data?channel-group=cg1&state=[%22s1%22,%22s2%22,%22s3%22]", server.takeRequest().getPath());
        assertEquals(result, true);
    }

    @Test
    public void applyStateForChannelGroupsSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"));
        Boolean result = partialSetState.channelGroup("cg1").channelGroup("cg2").state(Arrays.asList("s1", "s2", "s3")).build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/myUUID/data?channel-group=cg1,cg2&state=[%22s1%22,%22s2%22,%22s3%22]", server.takeRequest().getPath());
        assertEquals(result, true);
    }

    @Test
    public void applyStateForMixSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"));
        Boolean result = partialSetState.channel("ch1").channelGroup("cg1").channelGroup("cg2").state(Arrays.asList("s1", "s2", "s3")).build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data?channel-group=cg1,cg2&state=[%22s1%22,%22s2%22,%22s3%22]", server.takeRequest().getPath());
        assertEquals(result, true);
    }

    @Test
    public void applyNon200Sync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{ \"status\": 400, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}"));
        Boolean result = partialSetState.channel("ch1").channelGroup("cg1").channelGroup("cg2").state(Arrays.asList("s1", "s2", "s3")).build().sync();
        assertEquals("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data?channel-group=cg1,cg2&state=[%22s1%22,%22s2%22,%22s3%22]", server.takeRequest().getPath());
        assertEquals(result, false);
    }

}
