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

public class AddChannelChannelGroupEndpointTest extends EndpointTest
{
    private MockWebServer server;
    private AddChannelChannelGroup.AddChannelChannelGroupBuilder partialAddChannelChannelGroup;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialAddChannelChannelGroup = this.createPubNubInstance(server).addChannelChannelGroup();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialAddChannelChannelGroup.group("MyGroup").channel("ch1").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?add=ch1&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessMultipleChannel() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialAddChannelChannelGroup.group("MyGroup").channel("ch1").channel("ch2").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?add=ch1,ch2&uuid=myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialAddChannelChannelGroup.group("MyGroup").channel("ch1").uuid("myCustomUUID").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?add=ch1&uuid=myCustomUUID", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedMissedGroupSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        partialAddChannelChannelGroup.channel("ch1").build().sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedMissedChannelSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        partialAddChannelChannelGroup.group("MyGroup").build().sync();
    }
}
