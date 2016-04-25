package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CreatePushNotificationTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private CreatePushNotification instance;
    private Map<String, Object> pushPayload;


    @Before
    public void beforeEach() throws IOException {
        instance = this.createPubNubInstance(8080).createPushNotification();

        pushPayload = new HashMap<>();
        pushPayload.put("a", "b");
        pushPayload.put("c", "d");

    }

    @Test
    public void appleSyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("testChannel")
                .addApplePayload(pushPayload).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void googleSyncTest() throws InterruptedException, PubNubException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));


        instance.channel("testChannel").addGooglePayload(pushPayload).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void microsoftSyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("testChannel")
                .addMicrosoftPayload(pushPayload).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void allPayloadsSyncTest() throws InterruptedException, PubNubException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%2C%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%2C%22pn_gcm%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%2C%22pn_apns%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("testChannel")
                .addApplePayload(pushPayload)
                .addMicrosoftPayload(pushPayload)
                .addGooglePayload(pushPayload)
                .addPubNubPayload(pushPayload)
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void appleAsyncTest() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);
        instance.channel("testChannel")
                .addApplePayload(pushPayload).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                    List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                    assertFalse(status.isError());
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
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));
        final AtomicInteger atomic = new AtomicInteger(0);
        instance.channel("testChannel")
                .addGooglePayload(pushPayload).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                assertFalse(status.isError());
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

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22%3A%7B%22c%22%3A%22d%22%2C%22a%22%3A%22b%22%7D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);
        instance.channel("testChannel")
                .addMicrosoftPayload(pushPayload).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                assertFalse(status.isError());
                assertEquals(1, requests.size());
                assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
                atomic.addAndGet(1);
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }


}
