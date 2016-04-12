package com.pubnub.api.endpoints.push;

import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.PublishData;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class CreatePushNotificationTest extends EndpointTest {

    private MockWebServer server;
    private CreatePushNotification.CreatePushNotificationBuilder instance;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        instance = this.createPubNubInstance(server).createPushNotification();
    }

    @Test
    public void appleSyncTest() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.pushType(PushType.APNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().sync();

        assertEquals("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22:[%22a%22,%22b%22]%7D?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void googleSyncTest() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.pushType(PushType.GCM).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().sync();

        assertEquals("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22:[%22a%22,%22b%22]%7D?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void microsoftSyncTest() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.pushType(PushType.MPNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().sync();

        assertEquals("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22:[%22a%22,%22b%22]%7D?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void appleAsyncTest() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.APNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().async(new PNCallback<PublishData>() {
            @Override
            public void onResponse(PublishData result, PNErrorStatus status) {
                try {
                    assertEquals("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_apns%22:[%22a%22,%22b%22]%7D?uuid=myUUID", server.takeRequest().getPath());
                    atomic.addAndGet(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void googleAsyncTest() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.GCM).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().async(new PNCallback<PublishData>() {
            @Override
            public void onResponse(PublishData result, PNErrorStatus status) {
                try {
                    assertEquals("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_gcm%22:[%22a%22,%22b%22]%7D?uuid=myUUID", server.takeRequest().getPath());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                atomic.addAndGet(1);
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void microsoftAsyncTest() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        final AtomicInteger atomic = new AtomicInteger(0);
        instance.pushType(PushType.MPNS).channel("testChannel")
                .pushPayload(Arrays.asList("a", "b")).build().async(new PNCallback<PublishData>() {
            @Override
            public void onResponse(PublishData result, PNErrorStatus status) {
                try {
                    assertEquals("/publish/myPublishKey/mySubscribeKey/0/testChannel/0/%7B%22pn_mpns%22:[%22a%22,%22b%22]%7D?uuid=myUUID", server.takeRequest().getPath());
                    atomic.addAndGet(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }


}
