package com.pubnub.api.endpoints.presence;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class GetStateEndpointTest extends EndpointTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private GetState.GetStateBuilder partialGetState;

    @Before
    public void beforeEach() throws IOException {
        partialGetState = this.createPubNubInstance(8080).getPresenceState();
    }

    @Test
    public void testOneChannelSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));


        Map<String, Object> result = partialGetState.channel("testChannel").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("testChannel");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testFailedPayloadSync() throws PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/testChannel/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": \"age\" : 20, \"status\" : \"online\"}, \"service\": \"Presence\"}")));

        partialGetState.channel("testChannel").uuid("sampleUUID").build().sync();
    }

    @Test
    public void testMultipleChannelSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"ch1\": { \"age\" : 20, \"status\" : \"online\"}, \"ch2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        Map<String, Object> result = partialGetState.channel("ch1").channel("ch2").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("ch1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("ch2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");
    }

    @Test
    public void testOneChannelGroupSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        Map<String, Object> result = partialGetState.channelGroup("cg1").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("chcg1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("chcg2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test
    public void testManyChannelGroupSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        Map<String, Object> result = partialGetState.channelGroup("cg1").channelGroup("cg2").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("chcg1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("chcg2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1,cg2", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test
    public void testCombinationSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/uuid/sampleUUID"))
                .willReturn(aResponse().withBody("{ \"status\": 200, \"message\": \"OK\", \"payload\": { \"chcg1\": { \"age\" : 20, \"status\" : \"online\"}, \"chcg2\": { \"age\": 100, \"status\": \"offline\" } }, \"service\": \"Presence\"}")));

        Map<String, Object> result = partialGetState.channel("ch1").channelGroup("cg1").channelGroup("cg2").uuid("sampleUUID").build().sync();
        Map<String, Object> ch1Data = (Map<String, Object>) result.get("chcg1");
        Assert.assertEquals(ch1Data.get("age"), 20);
        Assert.assertEquals(ch1Data.get("status"), "online");
        Map<String, Object> ch2Data = (Map<String, Object>) result.get("chcg2");
        Assert.assertEquals(ch2Data.get("age"), 100);
        Assert.assertEquals(ch2Data.get("status"), "offline");

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1,cg2", requests.get(0).queryParameter("channel-group").firstValue());

    }

}
