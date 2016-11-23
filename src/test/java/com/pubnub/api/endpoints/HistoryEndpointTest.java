package com.pubnub.api.endpoints;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class HistoryEndpointTest extends TestHarness {

    private History partialHistory;
    private PubNub pubnub;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        partialHistory = pubnub.history();
        wireMockRule.start();
    }


    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(true).sync();

        Assert.assertTrue(response.getStartTimetoken().equals(1234L));
        Assert.assertTrue(response.getEndTimetoken().equals(4321L));

        Assert.assertEquals(response.getMessages().size(), 2);

        Assert.assertTrue(response.getMessages().get(0).getTimetoken().equals(1111L));
        Assert.assertEquals((response.getMessages().get(0).getEntry()).get("a").asInt(), 11);
        Assert.assertEquals((response.getMessages().get(0).getEntry()).get("b").asInt(), 22);

        Assert.assertTrue(response.getMessages().get(1).getTimetoken().equals(2222L));
        Assert.assertEquals((response.getMessages().get(1).getEntry()).get("a").asInt(), 33);
        Assert.assertEquals((response.getMessages().get(1).getEntry()).get("b").asInt(), 44);
    }

    @Test
    public void testSyncAuthSuccess() throws PubNubException {

        pubnub.getConfiguration().setAuthKey("authKey");

        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        partialHistory.channel("niceChannel").includeTimetoken(true).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals("authKey", requests.get(0).queryParameter("auth").firstValue());
        assertEquals(1, requests.size());
    }


    @org.junit.Test
    public void testSyncEncryptedSuccess() throws IOException, PubNubException {
        pubnub.getConfiguration().setCipherKey("testCipher");

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody("[[\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\",\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\",\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\"],14606134331557853,14606134485013970]")));

        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(false).sync();

        Assert.assertTrue(response.getStartTimetoken().equals(14606134331557853L));
        Assert.assertTrue(response.getEndTimetoken().equals(14606134485013970L));

        Assert.assertEquals(response.getMessages().size(), 3);

        Assert.assertEquals(response.getMessages().get(0).getTimetoken(), null);
        Assert.assertEquals("m1", (response.getMessages().get(0).getEntry()).get(0).asText());
        Assert.assertEquals("m2", (response.getMessages().get(0).getEntry()).get(1).asText());
        Assert.assertEquals("m3", (response.getMessages().get(0).getEntry()).get(2).asText());

        Assert.assertEquals("m1", (response.getMessages().get(1).getEntry()).get(0).asText());
        Assert.assertEquals("m2", (response.getMessages().get(1).getEntry()).get(1).asText());
        Assert.assertEquals("m3", (response.getMessages().get(1).getEntry()).get(2).asText());

        Assert.assertEquals("m1", (response.getMessages().get(2).getEntry()).get(0).asText());
        Assert.assertEquals("m2", (response.getMessages().get(2).getEntry()).get(1).asText());
        Assert.assertEquals("m3", (response.getMessages().get(2).getEntry()).get(2).asText());

    }

    @org.junit.Test
    public void testSyncEncryptedWithPNOtherSuccess() throws PubNubException {
        pubnub.getConfiguration().setCipherKey("hello");

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody("[[{\"pn_other\":\"6QoqmS9CnB3W9+I4mhmL7w==\"}],14606134331557852,14606134485013970]")));

        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(false).sync();

        Assert.assertTrue(response.getStartTimetoken().equals(14606134331557852L));
        Assert.assertTrue(response.getEndTimetoken().equals(14606134485013970L));

        Assert.assertEquals(response.getMessages().size(), 1);

        Assert.assertEquals(response.getMessages().get(0).getTimetoken(), null);
        Assert.assertEquals("hey", response.getMessages().get(0).getEntry().get("pn_other").get("text").asText());

    }

    @org.junit.Test
    public void testSyncSuccessWithoutTimeToken() throws PubNubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);

        Map<String, Object> historyItem2 = new HashMap<>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);

        historyItems.add(historyItem1);
        historyItems.add(historyItem2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        PNHistoryResult response = partialHistory.channel("niceChannel").sync();

        Assert.assertTrue(response.getStartTimetoken().equals(1234L));
        Assert.assertTrue(response.getEndTimetoken().equals(4321L));

        Assert.assertEquals(response.getMessages().size(), 2);

        Assert.assertNull(response.getMessages().get(0).getTimetoken());
        Assert.assertEquals(response.getMessages().get(0).getEntry().get("a").asInt(), 11);
        Assert.assertEquals(response.getMessages().get(0).getEntry().get("b").asInt(), 22);

        Assert.assertNull(response.getMessages().get(1).getTimetoken());
        Assert.assertEquals(response.getMessages().get(1).getEntry().get("a").asInt(), 33);
        Assert.assertEquals(response.getMessages().get(1).getEntry().get("b").asInt(), 44);
    }


    @org.junit.Test(expected=PubNubException.class)
    public void testMissinChannel() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        partialHistory.includeTimetoken(true).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testChannelIsEmpty() throws PubNubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        partialHistory.channel("").includeTimetoken(true).sync();
    }

    @org.junit.Test
    public void testOperationTypeSuccessAsync() throws PubNubException {

        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        final AtomicInteger atomic = new AtomicInteger(0);
        partialHistory.channel("niceChannel").includeTimetoken(true).async(new PNCallback<PNHistoryResult>() {
            @Override
            public void onResponse(PNHistoryResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNHistoryOperation) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test
    public void testSyncCountReverseStartEndSuccess() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        PNHistoryResult response = partialHistory.channel("niceChannel").count(5).reverse(true).start(1L).end(2L).includeTimetoken(true).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/history/sub-key/mySubscribeKey/channel/niceChannel.*")));
        Assert.assertTrue(requests.get(0).queryParameter("reverse").firstValue().equals("true"));
        Assert.assertTrue(Integer.valueOf(requests.get(0).queryParameter("count").firstValue()).equals(5));
        Assert.assertTrue(Integer.valueOf(requests.get(0).queryParameter("start").firstValue()).equals(1));
        Assert.assertTrue(Integer.valueOf(requests.get(0).queryParameter("end").firstValue()).equals(2));
        Assert.assertTrue(requests.get(0).queryParameter("include_token").firstValue().equals("true"));


        Assert.assertTrue(response.getStartTimetoken().equals(1234L));
        Assert.assertTrue(response.getEndTimetoken().equals(4321L));

        Assert.assertEquals(response.getMessages().size(), 2);

        Assert.assertTrue(response.getMessages().get(0).getTimetoken().equals(1111L));
        Assert.assertEquals((response.getMessages().get(0).getEntry()).get("a").asInt(), 11);
        Assert.assertEquals((response.getMessages().get(0).getEntry()).get("b").asInt(), 22);

        Assert.assertTrue(response.getMessages().get(1).getTimetoken().equals(2222L));
        Assert.assertEquals((response.getMessages().get(1).getEntry()).get("a").asInt(), 33);
        Assert.assertEquals((response.getMessages().get(1).getEntry()).get("b").asInt(), 44);
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testSyncProcessMessageError() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();

        Map<String, Object> historyEnvelope1 = new HashMap<String, Object>();
        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<String, Object>();
        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);
        historyEnvelope2.put("timetoken", 2222);
        historyEnvelope2.put("message", historyItem2);

        historyItems.add(historyEnvelope1);
        historyItems.add(historyEnvelope2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody(pubnub.getMapper().toJson(testArray))));

        pubnub.getConfiguration().setCipherKey("Test");
        partialHistory.channel("niceChannel").count(5).reverse(true).start(1L).end(2L).includeTimetoken(true).sync();
    }


}
