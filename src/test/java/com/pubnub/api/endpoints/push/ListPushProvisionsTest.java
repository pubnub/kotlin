package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ListPushProvisionsTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ListPushProvisions instance;
    private PubNub pubnub;


    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        instance = pubnub.auditPushChannelProvisions();
        wireMockRule.start();
    }

    @Test
    public void testAppleSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PNPushType.APNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testGoogleSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PNPushType.GCM).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testMicrosoftSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());

        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testIsAuthRequiredSuccess() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        pubnub.getConfiguration().setAuthKey("myKey");
        instance.deviceId("niceDevice").pushType(PNPushType.APNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test
    public void testOperationTypeSuccess() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);

        instance.deviceId("niceDevice").pushType(PNPushType.APNS).async(new PNCallback<PNPushListProvisionsResult>() {
            @Override
            public void onResponse(PNPushListProvisionsResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNPushNotificationEnabledChannelsOperation) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @Test(expected=PubNubException.class)
    public void testNullSubscribeKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        pubnub.getConfiguration().setSubscribeKey(null);
        instance.deviceId("niceDevice").pushType(PNPushType.APNS).sync();
    }

    @Test(expected=PubNubException.class)
    public void testEmptySubscribeKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        pubnub.getConfiguration().setSubscribeKey("");
        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();
    }

    @Test(expected=PubNubException.class)
    public void testNullPushType() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("niceDevice").sync();
    }

    @Test(expected=PubNubException.class)
    public void testNullDeviceId() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.pushType(PNPushType.MPNS).sync();
    }

    @Test(expected=PubNubException.class)
    public void testEmptyDeviceIdRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[\"ch1\", \"ch2\", \"ch3\"]")));

        instance.deviceId("").pushType(PNPushType.MPNS).sync();
    }

}
