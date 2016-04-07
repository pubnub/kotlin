package com.pubnub.api.endpoints;

import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.TimeCallback;
import com.pubnub.api.core.ErrorStatus;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.TimeData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimeEndpointTest extends EndpointTest {
    private MockWebServer server;
    private Time partialTime;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        partialTime = this.createPubNubInstance(server).time().build();
    }


    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("[14593046077243110]"));
        TimeData response = partialTime.sync();
        assertTrue(response.getTimetoken().equals(14593046077243110L));
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithString() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("[abc]"));
        partialTime.sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithoutJSON() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("zimp"));
        partialTime.sync();
    }

    @org.junit.Test(expected=PubnubException.class)
    public void testSyncBrokenWithout200() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("[14593046077243110]").setResponseCode(404));
        TimeData response = partialTime.sync();
        assertEquals(response.getTimetoken(), "14593046077243110");
    }

    @org.junit.Test
    public void testAsyncSuccess() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("[14593046077243110]"));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialTime.async(new TimeCallback(){

            @Override
            public void status(ErrorStatus status) {
            }

            @Override
            public void result(TimeData result) {
                assertTrue(result.getTimetoken().equals(14593046077243110L));
                atomic.incrementAndGet();
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test
    public void testAsyncBrokenWithString() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("[abc]"));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialTime.async(new TimeCallback(){

            @Override
            public void status(ErrorStatus status) {
                atomic.incrementAndGet();
            }

            @Override
            public void result(TimeData result) {
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @org.junit.Test
    public void testAsyncBrokenWithoutJSON() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("zimp"));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialTime.async(new TimeCallback(){

            @Override
            public void status(ErrorStatus status) {
                atomic.incrementAndGet();
            }

            @Override
            public void result(TimeData result) {
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @org.junit.Test
    public void testAsyncBrokenWithout200() throws IOException, PubnubException {
        server.enqueue(new MockResponse().setBody("[14593046077243110]").setResponseCode(404));
        final AtomicInteger atomic = new AtomicInteger(0);
        partialTime.async(new TimeCallback(){

            @Override
            public void status(ErrorStatus status) {
                atomic.incrementAndGet();
            }

            @Override
            public void result(TimeData result) {
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }


}