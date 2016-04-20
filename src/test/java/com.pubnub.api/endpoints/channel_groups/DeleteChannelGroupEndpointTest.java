package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DeleteChannelGroupEndpointTest extends EndpointTest
{
    private MockWebServer server;
    private DeleteChannelGroup.DeleteChannelGroupBuilder partialDeleteChannelGroup;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialDeleteChannelGroup = this.createPubNubInstance(server).deleteChannelGroup();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialDeleteChannelGroup.group_name("MyGroup").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup/remove?uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialDeleteChannelGroup.group_name("MyGroup").uuid("myCustomUUID").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup/remove?uuid=myCustomUUID", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedMissedGroupSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        partialDeleteChannelGroup.build().sync();
    }
}
