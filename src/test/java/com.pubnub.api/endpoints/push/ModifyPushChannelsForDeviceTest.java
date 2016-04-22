package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.enums.PushType;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ModifyPushChannelsForDeviceTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private Pubnub pubnub;
    private RemoveAllPushChannelsForDevice instance;
    private AddChannelsToPush instanceAdd;
    private RemoveChannelsFromPush instanceRemove;


    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        instance = pubnub.removeAllPushNotificationsFromDeviceWithPushToken();
        instanceAdd = pubnub.addPushNotificationsOnChannels();
        instanceRemove = pubnub.removePushNotificationsFromChannels();
    }

    @Test
    public void testAppleSuccessSyncRemoveAll() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.APNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testGoogleSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.GCM).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testMicrosoftSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PushType.MPNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testAddAppleSuccessSync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PushType.APNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());

    }

    @Test
    public void testAddGoogleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PushType.GCM)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());

    }

    @Test
    public void testAddMicrosoftSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testRemoveAppleSuccessSync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PushType.APNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());

    }

    @Test
    public void testGoogleSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PushType.GCM)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());

    }

    @Test
    public void testMicrosoftSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());
    }

}
