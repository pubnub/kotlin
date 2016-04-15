package com.pubnub.api.endpoints.presence;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.WhereNowCallback;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.core.models.consumer_facing.PNPresenceWhereNowResult;
import com.pubnub.api.endpoints.EndpointTest;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThat;

public class WhereNowEndpointTest extends EndpointTest {
    private WhereNow partialWhereNow;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void beforeEach() throws IOException {
        partialWhereNow = this.createPubNubInstance(8080).whereNow();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}")));

        PNPresenceWhereNowResult response = partialWhereNow.sync();
        assertThat(response.getChannels(), org.hamcrest.Matchers.contains("a", "b"));
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/customUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}")));

        PNPresenceWhereNowResult response = partialWhereNow.uuid("customUUID").sync();
        assertThat(response.getChannels(), org.hamcrest.Matchers.contains("a", "b"));
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithString() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [zimp]}, \"service\": \"Presence\"}")));

        partialWhereNow.sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithoutJSON() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": zimp}, \"service\": \"Presence\"}")));

        partialWhereNow.sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithout200() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}")));

        partialWhereNow.sync();
    }

    @org.junit.Test
    public void testAsyncSuccess() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.async(new WhereNowCallback(){

            @Override
            public void onResponse(PNPresenceWhereNowResult result, PNErrorStatus status) {
                assertThat(result.getChannels(), org.hamcrest.Matchers.contains("a", "b"));
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test
    public void testAsyncBrokenWithString() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [zimp]}, \"service\": \"Presence\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.async(new WhereNowCallback(){

            @Override
            public void onResponse(PNPresenceWhereNowResult result, PNErrorStatus status) {
                atomic.incrementAndGet();
            }

        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @org.junit.Test
    public void testAsyncBrokenWithoutJSON() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": zimp}, \"service\": \"Presence\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.async(new WhereNowCallback(){

            @Override
            public void onResponse(PNPresenceWhereNowResult result, PNErrorStatus status) {
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @org.junit.Test
    public void testAsyncBrokenWithout200() throws IOException, PubnubException {

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}")));

        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.async(new WhereNowCallback(){

            @Override
            public void onResponse(PNPresenceWhereNowResult result, PNErrorStatus status) {
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

}
