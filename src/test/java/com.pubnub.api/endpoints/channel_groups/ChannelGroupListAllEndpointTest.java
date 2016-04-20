package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ChannelGroupListAllEndpointTest extends EndpointTest {
    private MockWebServer server;
    private ChannelGroupListAll.ChannelGroupListAllBuilder partialChannelGroupListAll;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialChannelGroupListAll = this.createPubNubInstance(server).channelGroupListAll();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"groups\": [\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialChannelGroupListAll.build().sync();
        List<String> groups = (List<String>) response.get("groups");
        assertThat(groups, org.hamcrest.Matchers.contains("a", "b"));
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group?uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"groups\": [\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialChannelGroupListAll.uuid("myCustomUUID").build().sync();
        List<String> groups = (List<String>) response.get("groups");
        assertThat(groups, org.hamcrest.Matchers.contains("a", "b"));
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group?uuid=myCustomUUID", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedPayloadSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": \"groups\": [\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialChannelGroupListAll.build().sync();
    }



}
