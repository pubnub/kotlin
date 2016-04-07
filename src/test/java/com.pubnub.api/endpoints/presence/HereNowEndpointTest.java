package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.HereNow.HereNowData;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HereNowEndpointTest extends EndpointTest {

    private MockWebServer server;
    private HereNow.HereNowBuilder partialHereNow;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialHereNow = this.createPubNubInstance(server).hereNow();
    }

    @Test
    public void testMultipleChannelStateSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}},{\"uuid\":\"user3\",\"state\":{\"age\":30}}]}}},\"service\":\"Presence\"}"));
        HereNowData response =  partialHereNow.channel("ch1").channel("ch2").includeState(true).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 2);
        Assert.assertEquals(response.getTotalOccupancy(), 3);

        Assert.assertEquals(response.getChannels().get("ch1").getChannelName(), "ch1");
        Assert.assertEquals(response.getChannels().get("ch1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().get(0).getUuid(), "user1");
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().get(0).getState().toString(), "{age=10}");

        Assert.assertEquals(response.getChannels().get("ch2").getChannelName(), "ch2");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupancy(), 2);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().size(), 2);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(0).getUuid(), "user1");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(0).getState().toString(), "{age=10}");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(1).getUuid(), "user3");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(1).getState().toString(), "{age=30}");

        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2?state=1", server.takeRequest().getPath());
    }

    @Test
    public void testMultipleChannelSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\"}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\"},{\"uuid\":\"user3\"}]}}},\"service\":\"Presence\"}"));
        HereNowData response =  partialHereNow.channel("ch1").channel("ch2").includeState(true).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 2);
        Assert.assertEquals(response.getTotalOccupancy(), 3);

        Assert.assertEquals(response.getChannels().get("ch1").getChannelName(), "ch1");
        Assert.assertEquals(response.getChannels().get("ch1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().get(0).getUuid(), "user1");
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().get(0).getState(), null);

        Assert.assertEquals(response.getChannels().get("ch2").getChannelName(), "ch2");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupancy(), 2);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().size(), 2);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(0).getUuid(), "user1");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(0).getState(), null);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(1).getUuid(), "user3");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(1).getState(), null);

        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2?state=1", server.takeRequest().getPath());
    }

    @Test
    public void testMultipleChannelWithoutStateSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": {\"game1\": {\"uuids\": [\"a3ffd012-a3b9-478c-8705-64089f24d71e\"], \"occupancy\": 1}}, \"total_channels\": 1, \"total_occupancy\": 1}, \"service\": \"Presence\"}"));
        HereNowData response =  partialHereNow.channel("game1").channel("game2").includeState(false).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);

        Assert.assertEquals(response.getChannels().get("game1").getChannelName(), "game1");
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getUuid(), "a3ffd012-a3b9-478c-8705-64089f24d71e");
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getState(), null);

        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/game1,game2", server.takeRequest().getPath());
    }

    @Test
    public void testMultipleChannelWithoutStateUUIDsSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("\n" +
                "{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": {\"game1\": {\"occupancy\": 1}}, \"total_channels\": 1, \"total_occupancy\": 1}, \"service\": \"Presence\"}"));
        HereNowData response =  partialHereNow.channel("game1").channel("game2").includeState(false).includeUUIDs(false).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);

        Assert.assertEquals(response.getChannels().get("game1").getChannelName(), "game1");
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants(), null);

        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/game1,game2?disable_uuids=1", server.takeRequest().getPath());
    }

    @Test
    public void testSingularChannelWithoutStateUUIDsSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("\n" +
                "{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"occupancy\": 3}"));
        HereNowData response =  partialHereNow.channel("game1").includeState(false).includeUUIDs(false).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 3);
        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/game1?disable_uuids=1", server.takeRequest().getPath());
    }

    @Test
    public void testSingularChannelWithoutStateSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("\n" +
                "{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"uuids\": [\"a3ffd012-a3b9-478c-8705-64089f24d71e\"], \"occupancy\": 1}"));
        HereNowData response =  partialHereNow.channel("game1").includeState(false).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);
        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getUuid(), "a3ffd012-a3b9-478c-8705-64089f24d71e");
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getState(), null);

        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/game1", server.takeRequest().getPath());
    }

    @Test
    public void testSingularChannelSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\":200,\"message\":\"OK\",\"service\":\"Presence\",\"uuids\":[{\"uuid\":\"a3ffd012-a3b9-478c-8705-64089f24d71e\",\"state\":{\"age\":10}}],\"occupancy\":1}"));
        HereNowData response =  partialHereNow.channel("game1").includeState(true).build().sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);
        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getUuid(), "a3ffd012-a3b9-478c-8705-64089f24d71e");
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getState().toString(), "{age=10}");

        assertEquals("/v2/presence/sub_key/mySubscribeKey/channel/game1?state=1", server.takeRequest().getPath());
    }

}
