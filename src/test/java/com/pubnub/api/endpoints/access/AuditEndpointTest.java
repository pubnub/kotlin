package com.pubnub.api.endpoints.access;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerAuditResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
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
        pubnub.getConfiguration().setSecretKey("secretKey").setIncludeInstanceIdentifier(true);
        wireMockRule.start();

    }

    @Test
    public void testSuccessChannelGroupSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
        
        assertEquals("cg2", pnAccessManagerAuditResult.getChannelGroup());
        assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isManageEnabled());
        assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isReadEnabled());
        assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isWriteEnabled());
        assertEquals("channel-group+auth", pnAccessManagerAuditResult.getLevel());
        assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getSubscribeKey());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v1/auth/audit/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("rnb_-C8C4twE5IlyMeSlTyF4538WNv4uKCQu6jQwggU=", signature);

    }

    @Test
    public void testSuccessChannelSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channel("ch1").authKeys(Collections.singletonList("key1")).sync();

        assertEquals("ch1", pnAccessManagerAuditResult.getChannel());
        assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isManageEnabled());
        assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isReadEnabled());
        assertEquals(true, pnAccessManagerAuditResult.getAuthKeys().get("key1").isWriteEnabled());
        assertEquals("user", pnAccessManagerAuditResult.getLevel());
        assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getSubscribeKey());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("l0mnete94wADUcKR6THq1L4nhJrJg5q7eot0uRWoT8U=", signature);
    }

    @Test
    public void testSuccessChannelMissingKeySync() {
        try {
            partialAudit.channel("ch1").sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("Auth Keys Missing.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testOperationTypeSuccessAsync() {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("rXy69MNT1vceNs3Ob6HnjShUAzCV5x4OumSG1lSPL6s="))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        final AtomicInteger atomic = new AtomicInteger(0);

        partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).async(new PNCallback<PNAccessManagerAuditResult>() {
            @Override
            public void onResponse(PNAccessManagerAuditResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNAccessManagerAudit) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @Test
    public void testIsAuthRequiredSuccessSync() {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setAuthKey("myKey");

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
            assertEquals(1, requests.size());
            String signature = requests.get(0).queryParameter("signature").firstValue();
            assertEquals("rnb_-C8C4twE5IlyMeSlTyF4538WNv4uKCQu6jQwggU=", signature);
        } catch (PubNubException ex) {
            throw new RuntimeException("should never reach here", ex);
        }
    }

    @Test
    public void testNullPayload() {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("Parsing Error", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testNullSecretKey() {
        pubnub.getConfiguration().setSecretKey(null);

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Secret Key not configured.", e.getPubnubError().getMessage());
        }

    }

    @Test
    public void testEmptySecretKey() {
        pubnub.getConfiguration().setSecretKey("");

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Secret Key not configured.", e.getPubnubError().getMessage());
        }

    }

    @Test
    public void testNullSubscribeKey() {
        pubnub.getConfiguration().setSubscribeKey(null);

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Subscribe Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testEmptySubscribeKey() {
        pubnub.getConfiguration().setSubscribeKey("");

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Subscribe Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testNullPublishKey() {
        pubnub.getConfiguration().setPublishKey(null);

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Publish Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testEmptyPublishKey() {
        pubnub.getConfiguration().setPublishKey("");

        try {
            partialAudit.channelGroup("cg1").authKeys(Collections.singletonList("key1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Publish Key not configured.", e.getPubnubError().getMessage());
        }

    }

    @Test
    public void testChannelAndChanneGroupNull() {
        try {
            partialAudit.authKeys(Collections.singletonList("key1")).channel(null).channelGroup(null).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("Channel and Group Missing.", e.getPubnubError().getMessage());
        }
    }

}
