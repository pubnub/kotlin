package com.pubnub.api.endpoints.access;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerAuditResult;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static org.junit.Assert.assertEquals;

public class AuditEndpointTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private Audit partialAudit;
    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {

        pubnub = this.createPubNubInstance(8080);
        partialAudit = pubnub.audit();
        pubnub.getConfiguration().setSecretKey("secretKey");
        wireMockRule.start();

    }

    @Test
    public void testSuccessChannelGroupSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("dVle3aE_grdzKypoEegdNSZs_CXpJQJEcnqcM0nMH0Q%3D%0A"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();

        Assert.assertEquals("cg2", pnAccessManagerAuditResult.getChannelGroup());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isManageEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isReadEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isWriteEnabled());
        Assert.assertEquals("channel-group+auth", pnAccessManagerAuditResult.getLevel());
        Assert.assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getSubscribeKey());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v1/auth/audit/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());

    }

    @Test
    public void testSuccessChannelSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("4WIVMIc007EqwYGrqyJuMy5qf-gvtdopDjfaXEa1zOs%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channel("ch1").authKeys(Arrays.asList("key1")).sync();

        Assert.assertEquals("ch1", pnAccessManagerAuditResult.getChannel());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isManageEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isReadEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isWriteEnabled());
        Assert.assertEquals("user", pnAccessManagerAuditResult.getLevel());
        Assert.assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getSubscribeKey());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testSuccessChannelMissingKeySync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("ZlPruaId7jzupmK4LUynpnjvA2CQYyrrT0475wWkbwY%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        partialAudit.channel("ch1").sync();
    }

    @org.junit.Test
    public void testOperationTypeSuccessAsync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        final AtomicInteger atomic = new AtomicInteger(0);

        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).async(new PNCallback<PNAccessManagerAuditResult>() {
            @Override
            public void onResponse(PNAccessManagerAuditResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNAccessManagerAudit) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test
    public void testIsAuthRequiredSuccessSync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("dVle3aE_grdzKypoEegdNSZs_CXpJQJEcnqcM0nMH0Q%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullPayload() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullSecretKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSecretKey(null);
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptySecretKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSecretKey("");
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullSubscribeKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSubscribeKey(null);
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptySubscribeKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSubscribeKey("");
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullPublishKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setPublishKey(null);
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptyPublishKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setPublishKey("");
        partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testChannelAndChanneGroupNull() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        partialAudit.authKeys(Arrays.asList("key1")).channel(null).channelGroup(null).sync();
    }

}
