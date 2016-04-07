package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ListProvisionsTest extends EndpointTest {

    private MockWebServer server;
    private ListProvisions.ListProvisionsBuilder instance;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        instance = this.createPubNubInstance(server).listPushProvisions();
    }

    @Test
    public void testAppleSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[\"ch1\", \"ch2\", \"ch3\"]")));
        instance.deviceId("niceDevice").pushType(PushType.APNS).build().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice?type=apns", server.takeRequest().getPath());
    }

    @Test
    public void testGoogleSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[\"ch1\", \"ch2\", \"ch3\"]")));
        instance.deviceId("niceDevice").pushType(PushType.GCM).build().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice?type=gcm", server.takeRequest().getPath());
    }

    @Test
    public void testMicrosoftSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[\"ch1\", \"ch2\", \"ch3\"]")));
        instance.deviceId("niceDevice").pushType(PushType.MPNS).build().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice?type=mpns", server.takeRequest().getPath());
    }

}
