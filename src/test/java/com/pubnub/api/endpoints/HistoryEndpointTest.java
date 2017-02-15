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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


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


    @Test
    public void testSyncSuccess() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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

        assertTrue(response.getStartTimetoken().equals(1234L));
        assertTrue(response.getEndTimetoken().equals(4321L));

        assertEquals(response.getMessages().size(), 2);

        assertTrue(response.getMessages().get(0).getTimetoken().equals(1111L));
        assertEquals((response.getMessages().get(0).getEntry()).getAsJsonObject().get("a").getAsInt(), 11);
        assertEquals((response.getMessages().get(0).getEntry()).getAsJsonObject().get("b").getAsInt(), 22);

        assertTrue(response.getMessages().get(1).getTimetoken().equals(2222L));
        assertEquals((response.getMessages().get(1).getEntry()).getAsJsonObject().get("a").getAsInt(), 33);
        assertEquals((response.getMessages().get(1).getEntry()).getAsJsonObject().get("b").getAsInt(), 44);
    }

    @Test
    public void testSyncAuthSuccess() throws PubNubException {

        pubnub.getConfiguration().setAuthKey("authKey");

        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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


    @Test
    public void testSyncEncryptedSuccess() throws IOException, PubNubException {
        pubnub.getConfiguration().setCipherKey("testCipher");

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody("[[\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\",\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\",\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\"],14606134331557853,14606134485013970]")));

        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(false).sync();

        assertTrue(response.getStartTimetoken().equals(14606134331557853L));
        assertTrue(response.getEndTimetoken().equals(14606134485013970L));

        assertEquals(response.getMessages().size(), 3);

        assertEquals(response.getMessages().get(0).getTimetoken(), null);
        assertEquals("m1", (response.getMessages().get(0).getEntry()).getAsJsonArray().get(0).getAsString());
        assertEquals("m2", (response.getMessages().get(0).getEntry()).getAsJsonArray().get(1).getAsString());
        assertEquals("m3", (response.getMessages().get(0).getEntry()).getAsJsonArray().get(2).getAsString());

        assertEquals("m1", (response.getMessages().get(1).getEntry()).getAsJsonArray().get(0).getAsString());
        assertEquals("m2", (response.getMessages().get(1).getEntry()).getAsJsonArray().get(1).getAsString());
        assertEquals("m3", (response.getMessages().get(1).getEntry()).getAsJsonArray().get(2).getAsString());

        assertEquals("m1", (response.getMessages().get(2).getEntry()).getAsJsonArray().get(0).getAsString());
        assertEquals("m2", (response.getMessages().get(2).getEntry()).getAsJsonArray().get(1).getAsString());
        assertEquals("m3", (response.getMessages().get(2).getEntry()).getAsJsonArray().get(2).getAsString());

    }

    @Test
    public void testSyncEncryptedWithPNOtherSuccess() throws PubNubException {
        pubnub.getConfiguration().setCipherKey("hello");

        stubFor(get(urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/niceChannel"))
                .willReturn(aResponse().withBody("[[{\"pn_other\":\"6QoqmS9CnB3W9+I4mhmL7w==\"}],14606134331557852,14606134485013970]")));

        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(false).sync();

        assertTrue(response.getStartTimetoken().equals(14606134331557852L));
        assertTrue(response.getEndTimetoken().equals(14606134485013970L));

        assertEquals(response.getMessages().size(), 1);

        assertEquals(response.getMessages().get(0).getTimetoken(), null);
        assertEquals("hey", response.getMessages().get(0).getEntry().getAsJsonObject().get("pn_other").getAsJsonObject().get("text").getAsString());

    }

    @Test
    public void testSyncSuccessWithoutTimeToken() throws PubNubException {
        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

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

        assertTrue(response.getStartTimetoken().equals(1234L));
        assertTrue(response.getEndTimetoken().equals(4321L));

        assertEquals(response.getMessages().size(), 2);

        assertNull(response.getMessages().get(0).getTimetoken());
        assertEquals(response.getMessages().get(0).getEntry().getAsJsonObject().get("a").getAsInt(), 11);
        assertEquals(response.getMessages().get(0).getEntry().getAsJsonObject().get("b").getAsInt(), 22);

        assertNull(response.getMessages().get(1).getTimetoken());
        assertEquals(response.getMessages().get(1).getEntry().getAsJsonObject().get("a").getAsInt(), 33);
        assertEquals(response.getMessages().get(1).getEntry().getAsJsonObject().get("b").getAsInt(), 44);
    }


    @Test(expected=PubNubException.class)
    public void testMissinChannel() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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

    @Test(expected=PubNubException.class)
    public void testChannelIsEmpty() throws PubNubException {
        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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

    @Test
    public void testOperationTypeSuccessAsync() throws PubNubException {

        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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

    @Test
    public void testSyncCountReverseStartEndSuccess() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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
        assertTrue(requests.get(0).queryParameter("reverse").firstValue().equals("true"));
        assertTrue(Integer.valueOf(requests.get(0).queryParameter("count").firstValue()).equals(5));
        assertTrue(Integer.valueOf(requests.get(0).queryParameter("start").firstValue()).equals(1));
        assertTrue(Integer.valueOf(requests.get(0).queryParameter("end").firstValue()).equals(2));
        assertTrue(requests.get(0).queryParameter("include_token").firstValue().equals("true"));


        assertTrue(response.getStartTimetoken().equals(1234L));
        assertTrue(response.getEndTimetoken().equals(4321L));

        assertEquals(response.getMessages().size(), 2);

        assertTrue(response.getMessages().get(0).getTimetoken().equals(1111L));
        assertEquals((response.getMessages().get(0).getEntry()).getAsJsonObject().get("a").getAsInt(), 11);
        assertEquals((response.getMessages().get(0).getEntry()).getAsJsonObject().get("b").getAsInt(), 22);

        assertTrue(response.getMessages().get(1).getTimetoken().equals(2222L));
        assertEquals((response.getMessages().get(1).getEntry()).getAsJsonObject().get("a").getAsInt(), 33);
        assertEquals((response.getMessages().get(1).getEntry()).getAsJsonObject().get("b").getAsInt(), 44);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSyncProcessMessageError() throws IOException, PubNubException {
        List<Object> testArray = new ArrayList<>();
        List<Object> historyItems = new ArrayList<>();

        Map<String, Object> historyEnvelope1 = new HashMap<>();
        Map<String, Object> historyItem1 = new HashMap<>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);
        historyEnvelope1.put("timetoken", 1111);
        historyEnvelope1.put("message", historyItem1);

        Map<String, Object> historyEnvelope2 = new HashMap<>();
        Map<String, Object> historyItem2 = new HashMap<>();
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
