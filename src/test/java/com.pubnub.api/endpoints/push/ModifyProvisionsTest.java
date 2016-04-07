package com.pubnub.api.endpoints.push;

import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ModifyProvisionsTest extends EndpointTest {

    private MockWebServer server;
    private ModifyProvisions.ModifyProvisionsBuilder instance;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        instance = this.createPubNubInstance(server).modifyPushProvisions();
    }

    @Test
    public void testAppleSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1, \"Modified Channels\"]")));
        instance.deviceId("niceDevice").pushType(PushType.APNS)
                .addChannel("ch1").addChannels(Arrays.asList("ch2", "ch3"))
                .removeChannel("chr1").removeChannels(Arrays.asList("chr2", "chr3")).build().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice?add=ch1,ch2,ch3&type=apns&remove=chr1,chr2,chr3", server.takeRequest().getPath());
    }

    @Test
    public void testGoogleSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1, \"Modified Channels\"]")));
        instance.deviceId("niceDevice").pushType(PushType.GCM)
                .addChannel("ch1").addChannels(Arrays.asList("ch2", "ch3"))
                .removeChannel("chr1").removeChannels(Arrays.asList("chr2", "chr3")).build().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice?add=ch1,ch2,ch3&type=gcm&remove=chr1,chr2,chr3", server.takeRequest().getPath());
    }

    @Test
    public void testMicrosoftSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1, \"Modified Channels\"]")));
        instance.deviceId("niceDevice").pushType(PushType.MPNS)
                .addChannel("ch1").addChannels(Arrays.asList("ch2", "ch3"))
                .removeChannel("chr1").removeChannels(Arrays.asList("chr2", "chr3")).build().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice?add=ch1,ch2,ch3&type=mpns&remove=chr1,chr2,chr3", server.takeRequest().getPath());
    }

    @Test
    public void testAppleSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1, \"Modified Channels\"]")));
        instance.deviceId("niceDevice").pushType(PushType.APNS).build().removeAllChannels().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove?type=apns", server.takeRequest().getPath());
    }

    @Test
    public void testGoogleSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1, \"Modified Channels\"]")));
        instance.deviceId("niceDevice").pushType(PushType.GCM).build().removeAllChannels().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove?type=gcm", server.takeRequest().getPath());
    }

    @Test
    public void testMicrosoftSuccessSyncRemoveAll() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1, \"Modified Channels\"]")));
        instance.deviceId("niceDevice").pushType(PushType.MPNS).build().removeAllChannels().sync();

        assertEquals("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove?type=mpns", server.takeRequest().getPath());
    }

}
