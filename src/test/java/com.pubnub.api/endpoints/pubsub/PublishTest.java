package com.pubnub.api.endpoints.pubsub;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.endpoints.EndpointTest;
import lombok.Data;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class PublishTest extends EndpointTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private Pubnub pubnub;
    private Publish instance;


    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        instance = pubnub.publish();
    }

    @Test
    public void testSuccessSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message("hi").sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void testSuccessPostSync() throws PubnubException, InterruptedException {
        stubFor(post(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").usePOST(true).message(Arrays.asList("m1", "m2")).sync();

        List<LoggedRequest> requests = findAll(postRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
        assertEquals("[\"m1\",\"m2\"]", new String(requests.get(0).getBody()));
    }

    @Test
    public void testSuccessStoreSync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message("hi").shouldStore(false).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("0", requests.get(0).queryParameter("store").firstValue());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
    }

    @Test
    public void testSuccessMetaSync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message("hi").meta(Arrays.asList("m1", "m2")).shouldStore(false).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("0", requests.get(0).queryParameter("store").firstValue());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
        assertEquals("%5B%22m1%22%2C%22m2%22%5D", requests.get(0).queryParameter("meta").firstValue());
    }

    @Test
    public void testSuccessAuthKeySync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        pubnub.getConfiguration().setAuthKey("authKey");
        instance.channel("coolChannel").message("hi").sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("authKey", requests.get(0).queryParameter("auth").firstValue());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());

    }

    @Test
    public void testSuccessIntSync() throws PubnubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/10"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message(10).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());

    }

    @Test
    public void testSuccessArraySync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%5B%22a%22%2C%22b%22%2C%22c%22%5D?"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message(Arrays.asList("a", "b", "c")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());

    }

    @Test
    public void testSuccessArrayEncryptedSync() throws PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22HFP7V6bDwBLrwc1t8Rnrog%3D%3D%22"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        pubnub.getConfiguration().setCipherKey("testCipher");
        instance.channel("coolChannel").message(Arrays.asList("m1", "m2")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());

    }

    @Test
    public void testSuccessPostEncryptedSync() throws PubnubException, InterruptedException {
        stubFor(post(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        pubnub.getConfiguration().setCipherKey("testCipher");

        instance.channel("coolChannel").usePOST(true).message(Arrays.asList("m1", "m2")).sync();

        List<LoggedRequest> requests = findAll(postRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());
        assertEquals("\"HFP7V6bDwBLrwc1t8Rnrog==\"", new String(requests.get(0).getBody()));
    }

    @Test
    public void testSuccessHashMapSync() throws PubnubException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("a", 10);
        params.put("z", "test");

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%7B%22a%22%3A10%2C%22z%22%3A%22test%22%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message(params).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());

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

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%7B%22field1%22%3A%2210%22%2C%22field2%22%3A%2220%22%7D"))
                .willReturn(aResponse().withBody("[1,\"Sent\",\"14598111595318003\"]")));

        instance.channel("coolChannel").message(testPojo).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myUUID", requests.get(0).queryParameter("uuid").firstValue());

    }

}
