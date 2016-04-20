package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RemoveChannelEndpointTest extends EndpointTest
{
    private MockWebServer server;
    private RemoveChannel.RemoveChannelBuilder partialRemoveChannel;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialRemoveChannel = this.createPubNubInstance(server).removeChannel();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialRemoveChannel.group("MyGroup").channel("ch1").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?uuid=myUUID&remove=ch1", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessMultipleChannel() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialRemoveChannel.group("MyGroup").channel("ch1").channel("ch2").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?uuid=myUUID&remove=ch1,ch2", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"ChannelGroups\"}"));
        partialRemoveChannel.group("MyGroup").channel("ch1").uuid("myCustomUUID").build().sync();
        assertEquals("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/MyGroup?uuid=myCustomUUID&remove=ch1", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedMissedGroupSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        partialRemoveChannel.channel("ch1").uuid("MyCustomID").build().sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedMissedChannelSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": { \"group\": \"myGroup\", \"channels\": [\"ch1\",\"ch2\"]}, \"service\": \"ChannelGroups\"}"));
        partialRemoveChannel.group("MyGroup").uuid("MyCustomID").build().sync();
    }

}
