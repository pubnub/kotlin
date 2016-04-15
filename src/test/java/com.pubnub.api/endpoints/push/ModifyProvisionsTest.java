package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ModifyProvisionsTest extends EndpointTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ModifyProvisions instance;

    @Before
    public void beforeEach() throws IOException {
        instance = this.createPubNubInstance(8080).modifyPushProvisions();
    }

    @Test
    public void testAppleSuccessSync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.APNS)
                .addChannels(Arrays.asList("ch1", "ch2", "ch3"))
                .removeChannels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());

    }

    @Test
    public void testGoogleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.GCM)
                .addChannels(Arrays.asList("ch1","ch2", "ch3"))
                .removeChannels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());

    }

    @Test
    public void testMicrosoftSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.MPNS)
                .addChannels(Arrays.asList("ch1","ch2", "ch3"))
                .removeChannels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());
    }

    @Test
    public void testAppleSuccessSyncRemoveAll() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.APNS).removeAllChannels().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testGoogleSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.GCM).removeAllChannels().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testMicrosoftSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.MPNS).removeAllChannels().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

}
