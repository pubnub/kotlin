package com.pubnub.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.HistoryData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryEndpointTest extends EndpointTest {

    private MockWebServer server;
    private History.HistoryBuilder partialHistory;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialHistory = this.createPubNubInstance(server).history();
    }


    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();
        ObjectMapper mapper = new ObjectMapper();


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

        server.enqueue(new MockResponse().setBody(mapper.writeValueAsString(testArray)));
        HistoryData response = partialHistory.channel("niceChannel").includeTimetoken(true).build().sync();

        Assert.assertTrue(response.getStartTimeToken().equals(1234L));
        Assert.assertTrue(response.getEndTimeToken().equals(4321L));

        Assert.assertEquals(response.getItems().size(), 2);

        Assert.assertTrue(response.getItems().get(0).getTimetoken().equals(1111L));
        Assert.assertEquals(((JsonNode) response.getItems().get(0).getEntry()).get("a").asInt(), 11);
        Assert.assertEquals(((JsonNode) response.getItems().get(0).getEntry()).get("b").asInt(), 22);

        Assert.assertTrue(response.getItems().get(1).getTimetoken().equals(2222L));
        Assert.assertEquals(((JsonNode) response.getItems().get(1).getEntry()).get("a").asInt(), 33);
        Assert.assertEquals(((JsonNode) response.getItems().get(1).getEntry()).get("b").asInt(), 44);
    }

    @org.junit.Test
    public void testSyncSuccessWithoutTimeToken() throws IOException, PubnubException {
        List<Object> testArray = new ArrayList<Object>();
        List<Object> historyItems = new ArrayList<Object>();
        ObjectMapper mapper = new ObjectMapper();


        Map<String, Object> historyItem1 = new HashMap<String, Object>();
        historyItem1.put("a", 11);
        historyItem1.put("b", 22);

        Map<String, Object> historyItem2 = new HashMap<String, Object>();
        historyItem2.put("a", 33);
        historyItem2.put("b", 44);

        historyItems.add(historyItem1);
        historyItems.add(historyItem2);

        testArray.add(historyItems);
        testArray.add(1234);
        testArray.add(4321);

        server.enqueue(new MockResponse().setBody(mapper.writeValueAsString(testArray)));
        HistoryData response = partialHistory.channel("niceChannel").build().sync();

        Assert.assertTrue(response.getStartTimeToken().equals(1234L));
        Assert.assertTrue(response.getEndTimeToken().equals(4321L));

        Assert.assertEquals(response.getItems().size(), 2);

        Assert.assertNull(response.getItems().get(0).getTimetoken());
        Assert.assertEquals(((JsonNode) response.getItems().get(0).getEntry()).get("a").asInt(), 11);
        Assert.assertEquals(((JsonNode) response.getItems().get(0).getEntry()).get("b").asInt(), 22);

        Assert.assertNull(response.getItems().get(1).getTimetoken());
        Assert.assertEquals(((JsonNode) response.getItems().get(1).getEntry()).get("a").asInt(), 33);
        Assert.assertEquals(((JsonNode) response.getItems().get(1).getEntry()).get("b").asInt(), 44);
    }

}
