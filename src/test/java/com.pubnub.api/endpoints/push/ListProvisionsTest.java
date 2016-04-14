package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ListProvisionsTest extends EndpointTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ListProvisions.ListProvisionsBuilder instance;


    @Before
    public void beforeEach() throws IOException {
        instance = this.createPubNubInstance(8080).listPushProvisions();
    }

    @Test
    public void testAppleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PushType.APNS).build().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testGoogleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PushType.GCM).build().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testMicrosoftSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PushType.MPNS).build().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

}
