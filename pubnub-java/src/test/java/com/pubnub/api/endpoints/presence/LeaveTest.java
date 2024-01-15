package com.pubnub.api.endpoints.presence;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.PNStatus;
import org.awaitility.Awaitility;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;


public class LeaveTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(this.PORT), false);

    private Leave instance;
    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException, PubNubException {
        pubnub = this.createPubNubInstance();
        RetrofitManager retrofitManager = new RetrofitManager(pubnub);
        instance = new Leave(pubnub, null, retrofitManager, new TokenManager());
        wireMockRule.start();
    }

    @After
    public void afterEach() {
        pubnub.destroy();
        pubnub = null;
        wireMockRule.stop();
    }

    @Test
    public void subscribeChannelSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.channels(Arrays.asList("coolChannel")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

    }

    @Test
    public void subscribeChannelsSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel,coolChannel2/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.channels(Arrays.asList("coolChannel", "coolChannel2")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
    }


    @Test
    public void subscribeChannelsWithGroupSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel,coolChannel2/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.channels(Arrays.asList("coolChannel", "coolChannel2")).channelGroups(Arrays.asList("cg1")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test
    public void subscribeChannelsWithGroupASync() throws PubNubException {

        final AtomicBoolean statusArrived = new AtomicBoolean();

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel,coolChannel2/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.channels(Arrays.asList("coolChannel", "coolChannel2")).channelGroups(Arrays.asList("cg1")).async(new PNCallback<Boolean>() {
            @Override
            public void onResponse(Boolean result, @NotNull PNStatus status) {
                assertEquals(status.getAffectedChannels().get(0), "coolChannel");
                assertEquals(status.getAffectedChannels().get(1), "coolChannel2");
                assertEquals(status.getAffectedChannelGroups().get(0), "cg1");
                statusArrived.set(true);
            }
        });


        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusArrived,
                org.hamcrest.core.IsEqual.equalTo(true));
    }

    @Test
    public void subscribeGroupsSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.channelGroups(Arrays.asList("cg1", "cg2")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1,cg2", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test
    public void subscribeGroupSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.channelGroups(Arrays.asList("cg1")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("cg1", requests.get(0).queryParameter("channel-group").firstValue());
    }

    @Test(expected = PubNubException.class)
    public void testMissingChannelAndGroupSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        instance.sync();
    }

    @Test(expected = PubNubException.class)
    public void testNullSubKeySync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        pubnub.getConfiguration().setSubscribeKey(null);
        instance.sync();
    }

    @Test(expected = PubNubException.class)
    public void testEmptySubKeySync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        pubnub.getConfiguration().setSubscribeKey("");
        instance.sync();
    }

    @Test
    public void testIsAuthRequiredSuccessSync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/coolChannel/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", " +
                        "\"action\": \"leave\"}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        instance.channels(Arrays.asList("coolChannel")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

}
