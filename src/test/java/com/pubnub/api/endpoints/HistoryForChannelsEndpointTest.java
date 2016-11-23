package com.pubnub.api.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.models.server.HistoryForChannelsEnvelope;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class HistoryForChannelsEndpointTest extends TestHarness {

    private HistoryForChannels partialHistory;
    private PubNub pubnub;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        partialHistory = pubnub.historyForChannels();
        wireMockRule.start();
    }


    @Test
    public void testSyncSuccess() throws IOException, PubNubException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", \"channels\": {\"my_channel\":[{\"message\":\"hihi\",\"timetoken\":\"14698320467224036\"},{\"message\":\"hihi\",\"timetoken\":\"14698320468265639\"}],\"mychannel\":[{\"message\":\"sample message\",\"timetoken\":\"14369823849575729\"}]}}")));

        HistoryForChannelsEnvelope response = partialHistory.channels(Arrays.asList("mychannel,my_channel")).maximumPerChannel(25).sync();

        Assert.assertEquals(response.getChannels().size(), 2);
        Assert.assertEquals(response.getChannels().containsKey("mychannel"), true);
        Assert.assertEquals(response.getChannels().containsKey("my_channel"), true);
        Assert.assertEquals(response.getChannels().get("mychannel").size(),1);
        Assert.assertEquals(response.getChannels().get("my_channel").size(),2);
    }

    @Test
    public void testSyncAuthSuccess() throws PubNubException, JsonProcessingException {
        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", \"channels\": {\"my_channel\":[{\"message\":\"hihi\",\"timetoken\":\"14698320467224036\"},{\"message\":\"hihi\",\"timetoken\":\"14698320468265639\"}],\"mychannel\":[{\"message\":\"sample message\",\"timetoken\":\"14369823849575729\"}]}}")));

        pubnub.getConfiguration().setAuthKey("authKey");

        HistoryForChannelsEnvelope response = partialHistory.channels(Arrays.asList("mychannel,my_channel")).maximumPerChannel(25).sync();

        Assert.assertEquals(response.getChannels().size(), 2);
        Assert.assertEquals(response.getChannels().containsKey("mychannel"), true);
        Assert.assertEquals(response.getChannels().containsKey("my_channel"), true);
        Assert.assertEquals(response.getChannels().get("mychannel").size(),1);
        Assert.assertEquals(response.getChannels().get("my_channel").size(),2);

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals("authKey", requests.get(0).queryParameter("auth").firstValue());
        assertEquals(1, requests.size());
    }

    @org.junit.Test
    public void testSyncEncryptedSuccess() throws IOException, PubNubException {
        pubnub.getConfiguration().setCipherKey("testCipher");

        stubFor(get(urlPathEqualTo("/v3/history/sub-key/mySubscribeKey/channel/mychannel,my_channel"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"error\": false, \"error_message\": \"\", \"channels\": {\"my_channel\":[{\"message\":\"jC/yJ2y99BeYFYMQ7c53pg==\",\"timetoken\":\"14797423056306675\"}],\"mychannel\":[{\"message\":\"jC/yJ2y99BeYFYMQ7c53pg==\",\"timetoken\":\"14797423056306675\"}]}}")));

        HistoryForChannelsEnvelope response = partialHistory.channels(Arrays.asList("mychannel,my_channel")).maximumPerChannel(25).sync();

        Assert.assertEquals(response.getChannels().size(), 2);
        Assert.assertEquals(response.getChannels().containsKey("mychannel"), true);
        Assert.assertEquals(response.getChannels().containsKey("my_channel"), true);
        Assert.assertEquals(response.getChannels().get("mychannel").size(),1);
        Assert.assertEquals(response.getChannels().get("my_channel").size(),1);
    }

}
