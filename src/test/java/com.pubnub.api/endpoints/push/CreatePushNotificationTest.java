package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PushType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class CreatePushNotificationTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private CreatePushNotification instance;


    @Before
    public void beforeEach() throws IOException {
        instance = this.createPubNubInstance(8080).createPushNotification();
    }

    @Test
    public void appleSyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22%3A%5B%22a%22%2C%22b%22%5D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.pushType(PushType.APNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void googleSyncTest() throws PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22%3A%5B%22a%22%2C%22b%22%5D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));


        instance.pushType(PushType.GCM).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void microsoftSyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22%3A%5B%22a%22%2C%22b%22%5D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.pushType(PushType.MPNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void appleAsyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22:[%22a%22,%22b%22]%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.APNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                    List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                    assertEquals(1, requests.size());
                    assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
                    atomic.addAndGet(1);
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void googleAsyncTest() throws PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22:[%22a%22,%22b%22]%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));
        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.GCM).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                assertEquals(1, requests.size());
                assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
                atomic.addAndGet(1);
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void microsoftAsyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22:[%22a%22,%22b%22]%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.MPNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                assertEquals(1, requests.size());
                assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
                atomic.addAndGet(1);
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }


}
