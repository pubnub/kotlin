package com.pubnub.api.endpoints.presence;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class SetStateEndpointTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private SetState partialSetState;
    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);

        partialSetState = pubnub.setPresenceState();
    }

    @Test
    public void applyStateForChannelSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", matching("%7B%22age%22%3A20%7D"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}")));

        Map<String, Object> myState = new HashMap<>();
        myState.put("age", 20);

        PNSetStateResult result = partialSetState.channels(Arrays.asList("testChannel")).state(myState).sync();
        assertEquals(result.getState().get("age"), 20);
        assertEquals(result.getState().get("status"), "online");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
    }

    @Test
    public void applyStateForChannelsSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel,testChannel2/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", matching("%7B%22age%22%3A20%7D"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}")));

        Map<String, Object> myState = new HashMap<>();
        myState.put("age", 20);

        PNSetStateResult result = partialSetState.channels(Arrays.asList("testChannel", "testChannel2")).state(myState).sync();
        assertEquals(result.getState().get("age"), 20);
        assertEquals(result.getState().get("status"), "online");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

    }

    @Test
    public void applyStateForChannelGroupSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", matching("%7B%22age%22%3A20%7D"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}")));

        Map<String, Object> myState = new HashMap<>();
        myState.put("age", 20);

        PNSetStateResult result = partialSetState.channelGroups(Arrays.asList("cg1")).state(myState).sync();

        assertEquals(result.getState().get("age"), 20);
        assertEquals(result.getState().get("status"), "online");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

    }

    @Test
    public void applyStateForChannelGroupsSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", matching("%7B%22age%22%3A20%7D"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}")));

        Map<String, Object> myState = new HashMap<>();
        myState.put("age", 20);

        PNSetStateResult result = partialSetState.channelGroups(Arrays.asList("cg1", "cg2")).state(myState).sync();

        assertEquals(result.getState().get("age"), 20);
        assertEquals(result.getState().get("status"), "online");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1,cg2", requests.get(0).queryParameter("channel-group").firstValue());

    }

    @Test
    public void applyStateForMixSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", matching("%7B%22age%22%3A20%7D"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}")));

        Map<String, Object> myState = new HashMap<>();
        myState.put("age", 20);

        PNSetStateResult result = partialSetState.channels(Arrays.asList("ch1")).channelGroups(Arrays.asList("cg1", "cg2")).state(myState).sync();

        assertEquals(result.getState().get("age"), 20);
        assertEquals(result.getState().get("status"), "online");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

    }

    @org.junit.Test(expected = PubNubException.class)
    public void applyNon200Sync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("state", matching("%7B%22status%22%3A%22oneline%22%2C%22age%22%3A20%7D"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}").withStatus(400)));


        Map<String, Object> myState = new HashMap<>();
        myState.put("age", 20);

        PNSetStateResult result = partialSetState.channels(Arrays.asList("ch1")).channelGroups(Arrays.asList("cg1", "cg2")).state(myState).sync();

    }

    @org.junit.Test(expected = PubNubException.class)
    public void MissingStateSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID/data"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\" }, \"service\": \"Presence\"}")));

        PNSetStateResult result = partialSetState.channels(Arrays.asList("testChannel")).sync();

    }


}
