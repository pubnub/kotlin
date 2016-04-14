package com.pubnub.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.consumer_facing.PNHistoryResult;
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
    private Pubnub pubnub;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();

        pubnub = this.createPubNubInstance(server);
        partialHistory = pubnub.history();
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
        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(true).build().sync();

        Assert.assertTrue(response.getStartTimeToken().equals(1234L));
        Assert.assertTrue(response.getEndTimeToken().equals(4321L));

        Assert.assertEquals(response.getMessages().size(), 2);

        Assert.assertTrue(response.getMessages().get(0).getTimetoken().equals(1111L));
        Assert.assertEquals(((JsonNode) response.getMessages().get(0).getEntry()).get("a").asInt(), 11);
        Assert.assertEquals(((JsonNode) response.getMessages().get(0).getEntry()).get("b").asInt(), 22);

        Assert.assertTrue(response.getMessages().get(1).getTimetoken().equals(2222L));
        Assert.assertEquals(((JsonNode) response.getMessages().get(1).getEntry()).get("a").asInt(), 33);
        Assert.assertEquals(((JsonNode) response.getMessages().get(1).getEntry()).get("b").asInt(), 44);
    }


    @org.junit.Test
    public void testSyncEncryptedSuccess() throws IOException, PubnubException {
        pubnub.getConfiguration().setCipherKey("testCipher");


        server.enqueue(new MockResponse().setBody("[[\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\",\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\",\"EGwV+Ti43wh2TprPIq7o0KMuW5j6B3yWy352ucWIOmU=\\n\"],14606134331557853,14606134485013970]"));
        PNHistoryResult response = partialHistory.channel("niceChannel").includeTimetoken(false).build().sync();

        Assert.assertTrue(response.getStartTimeToken().equals(14606134331557853L));
        Assert.assertTrue(response.getEndTimeToken().equals(14606134485013970L));

        Assert.assertEquals(response.getMessages().size(), 3);

        Assert.assertEquals(response.getMessages().get(0).getTimetoken(), null);
        Assert.assertEquals("m1", ((JsonNode) response.getMessages().get(0).getEntry()).get(0).asText());
        Assert.assertEquals("m2", ((JsonNode) response.getMessages().get(0).getEntry()).get(1).asText());
        Assert.assertEquals("m3", ((JsonNode) response.getMessages().get(0).getEntry()).get(2).asText());

        Assert.assertEquals("m1", ((JsonNode) response.getMessages().get(1).getEntry()).get(0).asText());
        Assert.assertEquals("m2", ((JsonNode) response.getMessages().get(1).getEntry()).get(1).asText());
        Assert.assertEquals("m3", ((JsonNode) response.getMessages().get(1).getEntry()).get(2).asText());

        Assert.assertEquals("m1", ((JsonNode) response.getMessages().get(2).getEntry()).get(0).asText());
        Assert.assertEquals("m2", ((JsonNode) response.getMessages().get(2).getEntry()).get(1).asText());
        Assert.assertEquals("m3", ((JsonNode) response.getMessages().get(2).getEntry()).get(2).asText());

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
        PNHistoryResult response = partialHistory.channel("niceChannel").build().sync();

        Assert.assertTrue(response.getStartTimeToken().equals(1234L));
        Assert.assertTrue(response.getEndTimeToken().equals(4321L));

        Assert.assertEquals(response.getMessages().size(), 2);

        Assert.assertNull(response.getMessages().get(0).getTimetoken());
        Assert.assertEquals(((JsonNode) response.getMessages().get(0).getEntry()).get("a").asInt(), 11);
        Assert.assertEquals(((JsonNode) response.getMessages().get(0).getEntry()).get("b").asInt(), 22);

        Assert.assertNull(response.getMessages().get(1).getTimetoken());
        Assert.assertEquals(((JsonNode) response.getMessages().get(1).getEntry()).get("a").asInt(), 33);
        Assert.assertEquals(((JsonNode) response.getMessages().get(1).getEntry()).get("b").asInt(), 44);
    }

}
