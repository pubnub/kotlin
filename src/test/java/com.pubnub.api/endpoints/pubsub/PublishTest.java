package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import lombok.Data;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PublishTest extends EndpointTest {

    private MockWebServer server;
    private Pubnub pubnub;
    private Publish.PublishBuilder instance;


    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        pubnub = this.createPubNubInstance(server);
        instance = pubnub.publish();
    }

    @Test
    public void testSuccessSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message("hi").build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessPostSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").usePOST(true).message(Arrays.asList("m1", "m2")).build().sync();
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0?uuid=myUUID", recordedRequest.getPath());
        assertEquals("[\"m1\",\"m2\"]", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void testSuccessStoreSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message("hi").shouldStore(true).build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22?store=true&uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessMetaSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message("hi").meta(Arrays.asList("m1", "m2")).shouldStore(true).build().sync();

        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22?meta=[%22m1%22,%22m2%22]&store=true&uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessAuthKeySync() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setAuthKey("authKey");
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message("hi").build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22?auth=authKey&uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessIntSync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message(10).build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/10?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessArraySync() throws PubnubException, InterruptedException {
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message(Arrays.asList("a", "b", "c")).build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/[%22a%22,%22b%22,%22c%22]?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessArrayEncryptedSync() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setCipherKey("testCipher");
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message(Arrays.asList("m1", "m2")).build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22HFP7V6bDwBLrwc1t8Rnrog==%5Cn%22?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessPostEncryptedSync() throws PubnubException, InterruptedException {
        pubnub.getConfiguration().setCipherKey("testCipher");
        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").usePOST(true).message(Arrays.asList("m1", "m2")).build().sync();
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0?uuid=myUUID", recordedRequest.getPath());
        assertEquals("\"HFP7V6bDwBLrwc1t8Rnrog==\\n\"", recordedRequest.getBody().readUtf8().trim());
    }

    @Test
    public void testSuccessHashMapSync() throws PubnubException, InterruptedException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("a", 10);
        params.put("z", "test");

        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message(params).build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%7B%22a%22:10,%22z%22:%22test%22%7D?uuid=myUUID", server.takeRequest().getPath());
    }

    @Test
    public void testSuccessPOJOSync() throws PubnubException, InterruptedException {

        @Data
        class TestPojo {
            String field1;
            String field2;
        }

        TestPojo testPojo = new TestPojo();
        testPojo.setField1("10");
        testPojo.setField2("20");

        server.enqueue(new MockResponse().setBody(("[1,\"Sent\",\"14598111595318003\"]")));
        instance.channel("coolChannel").message(testPojo).build().sync();
        assertEquals("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%7B%22field1%22:%2210%22,%22field2%22:%2220%22%7D?uuid=myUUID", server.takeRequest().getPath());
    }

}
