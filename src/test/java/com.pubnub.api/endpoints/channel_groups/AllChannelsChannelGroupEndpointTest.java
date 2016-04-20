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

public class AllChannelsChannelGroupEndpointTest extends EndpointTest
{
    private MockWebServer server;
    private AllChannelsChannelGroup.AllChannelsChannelGroupBuilder partialAllChannelsChannelGroup;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialAllChannelsChannelGroup = this.createPubNubInstance(server).allChannelsChannelGroup();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialAllChannelsChannelGroup.group("MyGroup").build().sync();
        List<String> channels = (List<String>) response.get("channels");
        assertThat(channels, org.hamcrest.Matchers.contains("ch1", "ch2"));
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialAllChannelsChannelGroup.group("MyGroup").uuid("myCustomUUID").build().sync();
        List<String> channels = (List<String>) response.get("channels");
        assertThat(channels, org.hamcrest.Matchers.contains("ch1", "ch2"));
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?uuid=myCustomUUID", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedPayloadSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": \"groups\": [\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialAllChannelsChannelGroup.group("MyGroup").build().sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedGroupMissedSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        Map<String, Object> response = partialAllChannelsChannelGroup.build().sync();
    }



}
