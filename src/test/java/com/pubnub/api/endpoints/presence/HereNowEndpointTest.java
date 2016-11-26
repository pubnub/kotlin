package com.pubnub.api.endpoints.presence;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class HereNowEndpointTest extends TestHarness {
    private PubNub pubnub;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private HereNow partialHereNow;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        partialHereNow = pubnub.hereNow();
        wireMockRule.start();
    }

    @Test
    public void testMultipleChannelStateSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}},{\"uuid\":\"user3\",\"state\":{\"age\":30}}]}}},\"service\":\"Presence\"}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("ch1", "ch2")).includeState(true).sync();

        Assert.assertEquals(response.getTotalChannels(), 2);
        Assert.assertEquals(response.getTotalOccupancy(), 3);

        Assert.assertEquals(response.getChannels().get("ch1").getChannelName(), "ch1");
        Assert.assertEquals(response.getChannels().get("ch1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().get(0).getUuid(), "user1");
        Assert.assertEquals(response.getChannels().get("ch1").getOccupants().get(0).getState().toString(), "{\"age\":10}");

        Assert.assertEquals(response.getChannels().get("ch2").getChannelName(), "ch2");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupancy(), 2);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().size(), 2);
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(0).getUuid(), "user1");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(0).getState().toString(), "{\"age\":10}");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(1).getUuid(), "user3");
        Assert.assertEquals(response.getChannels().get("ch2").getOccupants().get(1).getState().toString(), "{\"age\":30}");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("1", requests.get(0).queryParameter("state").firstValue());
    }

    @Test
    public void testMultipleChannelSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\"}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\"},{\"uuid\":\"user3\"}]}}},\"service\":\"Presence\"}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("ch1", "ch2")).includeState(true).sync();

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

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("1", requests.get(0).queryParameter("state").firstValue());
    }

    @Test
    public void testMultipleChannelWithoutStateSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/game1,game2"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": {\"game1\": {\"uuids\": [\"a3ffd012-a3b9-478c-8705-64089f24d71e\"], \"occupancy\": 1}}, \"total_channels\": 1, \"total_occupancy\": 1}, \"service\": \"Presence\"}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("game1", "game2")).includeState(false).sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);

        Assert.assertEquals(response.getChannels().get("game1").getChannelName(), "game1");
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getUuid(), "a3ffd012-a3b9-478c-8705-64089f24d71e");
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getState(), null);

    }

    @Test
    public void testMultipleChannelWithoutStateUUIDsSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/game1,game2"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": {\"game1\": {\"occupancy\": 1}}, \"total_channels\": 1, \"total_occupancy\": 1}, \"service\": \"Presence\"}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("game1", "game2")).includeState(false).includeUUIDs(false).sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);

        Assert.assertEquals(response.getChannels().get("game1").getChannelName(), "game1");
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants(), null);

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("1", requests.get(0).queryParameter("disable_uuids").firstValue());
    }

    @Test
    public void testSingularChannelWithoutStateUUIDsSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/game1"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"occupancy\": 3}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("game1")).includeState(false).includeUUIDs(false).sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 3);

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("1", requests.get(0).queryParameter("disable_uuids").firstValue());

    }

    @Test
    public void testSingularChannelWithoutStateSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/game1"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"uuids\": [\"a3ffd012-a3b9-478c-8705-64089f24d71e\"], \"occupancy\": 1}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("game1")).includeState(false).sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);
        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getUuid(), "a3ffd012-a3b9-478c-8705-64089f24d71e");
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getState(), null);

    }

    @Test
    public void testSingularChannelSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/game1"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"service\":\"Presence\",\"uuids\":[{\"uuid\":\"a3ffd012-a3b9-478c-8705-64089f24d71e\",\"state\":{\"age\":10}}],\"occupancy\":1}")));

        PNHereNowResult response =  partialHereNow.channels(Arrays.asList("game1")).includeState(true).sync();

        Assert.assertEquals(response.getTotalChannels(), 1);
        Assert.assertEquals(response.getTotalOccupancy(), 1);
        Assert.assertEquals(response.getChannels().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupancy(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().size(), 1);
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getUuid(), "a3ffd012-a3b9-478c-8705-64089f24d71e");
        Assert.assertEquals(response.getChannels().get("game1").getOccupants().get(0).getState().toString(), "{\"age\":10}");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("1", requests.get(0).queryParameter("state").firstValue());
    }

    @Test
    public void testSingularChannelAndGroupSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/game1"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"channels\":{}, \"total_channels\":0, \"total_occupancy\":0},\"service\":\"Presence\"}")));

        PNHereNowResult response =  partialHereNow.channelGroups(Arrays.asList("grp1")).channels(Arrays.asList("game1")).includeState(true).sync();

        Assert.assertEquals(response.getTotalOccupancy(), 0);
    }


    @org.junit.Test
    public void testIsAuthRequiredSuccessSync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}},{\"uuid\":\"user3\",\"state\":{\"age\":30}}]}}},\"service\":\"Presence\"}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        partialHereNow.channels(Arrays.asList("ch1", "ch2")).includeState(true).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @org.junit.Test
    public void testOperationTypeSuccessAsync() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}},{\"uuid\":\"user3\",\"state\":{\"age\":30}}]}}},\"service\":\"Presence\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);

        partialHereNow.async(new PNCallback<PNHereNowResult>() {
            @Override
            public void onResponse(PNHereNowResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNHereNowOperation) {
                    atomic.incrementAndGet();
                }

            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullSubKeySync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}},{\"uuid\":\"user3\",\"state\":{\"age\":30}}]}}},\"service\":\"Presence\"}")));

        pubnub.getConfiguration().setSubscribeKey(null);
        partialHereNow.channels(Arrays.asList("ch1", "ch2")).includeState(true).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptySubKeySync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub_key/mySubscribeKey/channel/ch1,ch2"))
                .willReturn(aResponse().withBody("{\"status\":200,\"message\":\"OK\",\"payload\":{\"total_occupancy\":3,\"total_channels\":2,\"channels\":{\"ch1\":{\"occupancy\":1,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}}]},\"ch2\":{\"occupancy\":2,\"uuids\":[{\"uuid\":\"user1\",\"state\":{\"age\":10}},{\"uuid\":\"user3\",\"state\":{\"age\":30}}]}}},\"service\":\"Presence\"}")));

        pubnub.getConfiguration().setSubscribeKey("");
        partialHereNow.channels(Arrays.asList("ch1", "ch2")).includeState(true).sync();
    }

}
