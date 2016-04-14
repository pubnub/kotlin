package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.PublishData;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.endpoints.EndpointTest;
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

public class CreatePushNotificationTest extends EndpointTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private CreatePushNotification.CreatePushNotificationBuilder instance;


    @Before
    public void beforeEach() throws IOException {
        instance = this.createPubNubInstance(8080).createPushNotification();
    }

    @Test
    public void appleSyncTest() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22%3A%5B%22a%22%2C%22b%22%5D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.pushType(PushType.APNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void googleSyncTest() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22%3A%5B%22a%22%2C%22b%22%5D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));


        instance.pushType(PushType.GCM).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void microsoftSyncTest() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22%3A%5B%22a%22%2C%22b%22%5D%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.pushType(PushType.MPNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void appleAsyncTest() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22:[%22a%22,%22b%22]%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.APNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().async(new PNCallback<PublishData>() {
            @Override
            public void onResponse(PublishData result, PNErrorStatus status) {
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
    public void googleAsyncTest() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22:[%22a%22,%22b%22]%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));
        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.GCM).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().async(new PNCallback<PublishData>() {
            @Override
            public void onResponse(PublishData result, PNErrorStatus status) {
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
    public void microsoftAsyncTest() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22:[%22a%22,%22b%22]%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.MPNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().async(new PNCallback<PublishData>() {
            @Override
            public void onResponse(PublishData result, PNErrorStatus status) {
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
