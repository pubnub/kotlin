package com.pubnub.api.endpoints.presence;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.Assert.assertEquals;


public class GetStateEndpointTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private PubNub pubnub;
    private GetState partialGetState;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        partialGetState = pubnub.getPresenceState();
        wireMockRule.start();
    }

    @Test
    public void testOneChannelSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));


        PNGetStateResult result = partialGetState.channels(Collections.singletonList("testChannel")).uuid("sampleUUID").sync();
        JsonNode ch1Data = result.getStateByUUID().get("testChannel");
        Assert.assertEquals(ch1Data.get("age").asInt(), 20);
        Assert.assertEquals(ch1Data.get("status").asText(), "online");
    }

    @Test
    public void testOneChannelWithoutUUIDSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/myUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));


        PNGetStateResult result = partialGetState.channels(Collections.singletonList("testChannel")).sync();
        JsonNode ch1Data = result.getStateByUUID().get("testChannel");
        Assert.assertEquals(ch1Data.get("age").asInt(), 20);
        Assert.assertEquals(ch1Data.get("status").asText(), "online");
    }


    @org.junit.Test(expected=PubNubException.class)
    public void testFailedPayloadSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));

        partialGetState.channels(Collections.singletonList("testChannel")).uuid("sampleUUID").sync();
    }

    @Test
    public void testMultipleChannelSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"ch1\": { \"age\" : 20, \"status\" : \"online\"}, \"ch2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        PNGetStateResult result = partialGetState.channels(Arrays.asList("ch1", "ch2")).uuid("sampleUUID").sync();
        JsonNode ch1Data = result.getStateByUUID().get("ch1");
        Assert.assertEquals(ch1Data.get("age").asInt(), 20);
        Assert.assertEquals(ch1Data.get("status").asText(), "online");
        JsonNode ch2Data = result.getStateByUUID().get("ch2");
        Assert.assertEquals(ch2Data.get("age").asInt(), 100);
        Assert.assertEquals(ch2Data.get("status").asText(), "offline");
    }

    @Test
    public void testOneChannelGroupSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        PNGetStateResult result = partialGetState.channelGroups(Collections.singletonList("cg1")).uuid("sampleUUID").sync();
        JsonNode ch1Data = result.getStateByUUID().get("chcg1");
        Assert.assertEquals(ch1Data.get("age").asInt(), 20);
        Assert.assertEquals(ch1Data.get("status").asText(), "online");
        JsonNode ch2Data = result.getStateByUUID().get("chcg2");
        Assert.assertEquals(ch2Data.get("age").asInt(), 100);
        Assert.assertEquals(ch2Data.get("status").asText(), "offline");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test
    public void testManyChannelGroupSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        PNGetStateResult result = partialGetState.channelGroups(Arrays.asList("cg1", "cg2")).uuid("sampleUUID").sync();
        JsonNode ch1Data = result.getStateByUUID().get("chcg1");
        Assert.assertEquals(ch1Data.get("age").asInt(), 20);
        Assert.assertEquals(ch1Data.get("status").asText(), "online");
        JsonNode ch2Data = result.getStateByUUID().get("chcg2");
        Assert.assertEquals(ch2Data.get("age").asInt(), 100);
        Assert.assertEquals(ch2Data.get("status").asText(), "offline");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1,cg2", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test
    public void testCombinationSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        PNGetStateResult result = partialGetState.channels(Collections.singletonList("ch1")).channelGroups(Arrays.asList("cg1", "cg2")).uuid("sampleUUID").sync();
        JsonNode ch1Data = result.getStateByUUID().get("chcg1");
        Assert.assertEquals(ch1Data.get("age").asInt(), 20);
        Assert.assertEquals(ch1Data.get("status").asText(), "online");
        JsonNode ch2Data = result.getStateByUUID().get("chcg2");
        Assert.assertEquals(ch2Data.get("age").asInt(), 100);
        Assert.assertEquals(ch2Data.get("status").asText(), "offline");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1,cg2", requests.get(0).queryParameter("channel-group").firstValue());

    }

    @org.junit.Test(expected=PubNubException.class)
    public void testMissingChannelAndGroupSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));
        partialGetState.uuid("sampleUUID").sync();
    }

    @org.junit.Test
    public void testIsAuthRequiredSuccessSync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        partialGetState.channels(Collections.singletonList("testChannel")).uuid("sampleUUID").sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @org.junit.Test
    public void testOperationTypeSuccessAsync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);

        partialGetState.channels(Collections.singletonList("testChannel")).uuid("sampleUUID").async(new PNCallback<PNGetStateResult>() {
            @Override
            public void onResponse(PNGetStateResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNGetState) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullSubKeySync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));

        pubnub.getConfiguration().setSubscribeKey(null);
        partialGetState.channels(Collections.singletonList("testChannel")).uuid("sampleUUID").sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptySubKeySync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));

        pubnub.getConfiguration().setSubscribeKey("");
        partialGetState.channels(Collections.singletonList("testChannel")).uuid("sampleUUID").sync();
    }
}
