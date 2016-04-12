package com.pubnub.api.endpoints.presence;


import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.WhereNowCallback;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.WhereNowData;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class WhereNowEndpointTest extends EndpointTest {
    private MockWebServer server;
    private WhereNow.WhereNowBuilder partialWhereNow;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialWhereNow = this.createPubNubInstance(server).whereNow();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}"));
        WhereNowData response = partialWhereNow.build().sync();
        assertThat(response.getChannels(), org.hamcrest.Matchers.contains("a", "b"));
        assertEquals("/v2/presence/sub-key/mySubscribeKey/uuid/myUUID", server.takeRequest().getPath());
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}"));
        WhereNowData response = partialWhereNow.uuid("customUUID").build().sync();
        assertThat(response.getChannels(), org.hamcrest.Matchers.contains("a", "b"));
        assertEquals("/v2/presence/sub-key/mySubscribeKey/uuid/customUUID", server.takeRequest().getPath());
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithString() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [zimp]}, \"service\": \"Presence\"}"));
        partialWhereNow.build().sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithoutJSON() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("zimp"));
        partialWhereNow.build().sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithout200() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}").setResponseCode(404));
        partialWhereNow.build().sync();
    }

    @org.junit.Test
    public void testAsyncSuccess() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}"));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.build().async(new WhereNowCallback(){

            @Override
            public void onResponse(WhereNowData result, PNErrorStatus status) {
                assertThat(result.getChannels(), org.hamcrest.Matchers.contains("a", "b"));
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test
    public void testAsyncBrokenWithString() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [zimp]}, \"service\": \"Presence\"}"));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.build().async(new WhereNowCallback(){

            @Override
            public void onResponse(WhereNowData result, PNErrorStatus status) {
                atomic.incrementAndGet();
            }

        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @org.junit.Test
    public void testAsyncBrokenWithoutJSON() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("zimp"));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.build().async(new WhereNowCallback(){

            @Override
            public void onResponse(WhereNowData result, PNErrorStatus status) {
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @org.junit.Test
    public void testAsyncBrokenWithout200() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {\"channels\": [\"a\",\"b\"]}, \"service\": \"Presence\"}").setResponseCode(404));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialWhereNow.build().async(new WhereNowCallback(){

            @Override
            public void onResponse(WhereNowData result, PNErrorStatus status) {
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

}
