package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PushType;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ListProvisionsTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ListProvisions instance;


    @Before
    public void beforeEach() throws IOException {
        instance = this.createPubNubInstance(8080).listPushProvisions();
    }

    @Test
    public void testAppleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PushType.APNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testGoogleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PushType.GCM).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testMicrosoftSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PushType.MPNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

}
