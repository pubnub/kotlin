package com.pubnub.api.endpoints.access;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import org.awaitility.Awaitility;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

public class GrantEndpointTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().port(this.PORT), false);

    private Grant partialGrant;
    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance();
        partialGrant = pubnub.grant();
        pubnub.getConfiguration().setSecretKey("secretKey").setIncludeInstanceIdentifier(true);
        wireMockRule.start();
    }

    @After
    public void afterEach() {
        pubnub.destroy();
        pubnub = null;
        wireMockRule.stop();
    }

    @Test
    public void noGroupsOneChannelOneKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Collections.singletonList("key1")).channels(
                        Collections.singletonList("ch1")).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.Jk1QUqQGThbRFvLCNuW16YwPa6WmPrHDHJwz61py0nU", signature);

    }

    @Test
    public void noGroupsOneChannelTwoKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0," +
                        "\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Collections.singletonList("ch1")).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.I69785IpOV4l33i-Co3MptUGCbYWH5IlSZ41hfC1xXI", signature);
    }

    @Test
    public void noGroupsTwoChannelOneKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Collections.singletonList("key1")).channels(Arrays.asList("ch1", "ch2")).sync();

        assert result != null;

        assertEquals(2, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(1, result.getChannels().get("ch2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.EBDqRJIIMsiEdX_Rn241b5RFM15E7_hh7GcJF-5Pyxk", signature);
    }

    @Test
    public void noGroupsTwoChannelTwoKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Arrays.asList("ch1", "ch2")).sync();

        assert result != null;

        assertEquals(2, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(2, result.getChannels().get("ch2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.j74xJ4Vm3xLCd23zHp4USuL-a5CtLyeKpue8l-OkwEg", signature);
    }

    @Test
    public void oneGroupNoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList(
                "cg1")).sync();

        assert result != null;

        assertEquals(0, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.JC-H8cdXrPmuvxaO1GS-lQyKcDgN9YeWLoGMUt53uL4", signature);
    }

    @Test
    public void oneGroupNoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(Arrays.asList("cg1")).sync();

        assert result != null;

        assertEquals(0, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.f0CAiVJmacjdFpH1WR1sO77i2GRYcSeGegqju3rT6UE", signature);
    }

    @Test
    public void oneGroupOneChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}," +
                        "\"channel-groups\":\"cg1\"},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).channelGroups(
                        Arrays.asList("cg1")).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.94iSHM2KQGyv96J6YsrjccEVdvtma1Xz1tee7Nzbscs", signature);
    }

    @Test
    public void oneGroupOneChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0," +
                        "\"m\":0}},\"channel-groups\":\"cg1\"},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Arrays.asList("ch1")).channelGroups(
                        Arrays.asList("cg1")).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.g8sVq5n0Lzv4aQCRQi1gNFq2NWPgU1ACOzQ053z8nSY", signature);

    }

    @Test
    public void oneGroupTwoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}},\"channel-groups\":\"cg1\"," +
                        "\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1"
                , "ch2")).channelGroups(Arrays.asList("cg1")).sync();

        assert result != null;

        assertEquals(2, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.OQkQolRdwVGQXEfufd1bSbLN4WJI62VI-xR0cdYrDt0", signature);

    }

    @Test
    public void oneGroupTwoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}}},\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0," +
                        "\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}\n")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(
                        Arrays.asList("ch1", "ch2")).channelGroups(Arrays.asList("cg1")).sync();

        assert result != null;

        assertEquals(2, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.q_pwr2gyi6FV3dy_3ochQPq7Ppf34tOUssjlleFyhoo", signature);
    }


    @Test
    public void twoGroupNoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList(
                "cg1", "cg2")).sync();

        assert result != null;

        assertEquals(0, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(1, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.J_5Lvuq7sdA-vXUQWQzf7qB8Embx7dlAk1XO4RyONYU", signature);
    }

    @Test
    public void twoGroupNoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}," +
                        "\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}," +
                        "\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(Arrays.asList("cg1", "cg2")).sync();

        assert result != null;

        assertEquals(0, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.sW6tFGYkaMCjkZkcWGNW2s8yJZz8LPqTfVGCdi51znI", signature);
    }

    @Test
    public void twoGroupOneChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}," +
                        "\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList(
                "cg1", "cg2")).channels(Arrays.asList("ch1")).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(1, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.4p7-sNppRHYytAKkbombqnLyNOF2PZwZOcCBwcCwk7g", signature);
    }

    @Test
    public void twoGroupOneChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0," +
                        "\"m\":0}},\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}," +
                        "\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}," +
                        "\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(
                        Arrays.asList("cg1", "cg2")).channels(Arrays.asList("ch1")).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(2, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key2").getClass());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.VWNSybYnL6DlXGIRSuuYRC9BUZVMbVTjkllyXxvpTRE", signature);
    }

    @Test
    public void twoGroupTwoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}," +
                        "\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}," +
                        "\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList(
                "cg1", "cg2")).channels(Arrays.asList("ch1", "ch2")).sync();

        assert result != null;

        assertEquals(2, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(1, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(1, result.getChannels().get("ch2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.vRg18jL223FllrGWzODmY_39HYQ0rB9y0AZvPY8JKG8", signature);
    }

    @Test
    public void twoGroupTwoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"channel-group+auth\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0," +
                        "\"w\":0,\"m\":0}}}},\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0," +
                        "\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0," +
                        "\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\"," +
                        "\"status\":200}\n")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(
                        Arrays.asList("cg1", "cg2")).channels(Arrays.asList("ch1", "ch2")).sync();

        assert result != null;

        assertEquals(2, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(2, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key2").getClass());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(2, result.getChannels().get("ch2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key2").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.AFBUh22R8jTC6xbE3uJonhSmQgSGVECtJ0U-tjlEGuA", signature);
    }

    @Test
    public void noGroupsOneChannelOneKeyTTLTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .withQueryParam("ttl", matching("1334"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).ttl(1334).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.cjwNcrqRqzLdYfKYImmznO76CTky1qU0K88kbBvLhOs", signature);
    }

    @Test
    public void noGroupsOneChannelOneReadKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("1"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).read(true).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.tBkN3-YCCxzK8xZ_iY0bpIV2KHhJgHem1jSza_W7EJs", signature);
    }

    @Test
    public void noGroupsOneChannelOneWriteKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("1"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).write(true).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.BcptQPeSMRi7fwyjVmWhNxjHRXU0TL48lpKtrV1U4I8", signature);
    }

    @Test
    public void noGroupsOneChannelOneDeleteKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .withQueryParam("d", matching("1"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).delete(true).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.Fi3Va280ocS40WGPsZ4jBVPfXoXUAXcCTeRAiIuQgWk", signature);
    }

    @Test
    public void noGroupsOneChannelOneKeyManageTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("1"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result =
                partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).manage(true).sync();

        assert result != null;

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.fxKxvKRYoURgJ-UgTBEWlVX3b9Rnh7cAd8Ha7Ht7z4g", signature);
    }

    @Test
    public void testIsAuthRequiredSuccessSync() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        partialGrant.authKeys(Collections.singletonList("key1")).channels(Collections.singletonList("ch1")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/auth/grant/sub-key/mySubscribeKey.*")));
        assertEquals(1, requests.size());
        String signature = requests.get(0).queryParameter("signature").firstValue();
        assertEquals("v2.Jk1QUqQGThbRFvLCNuW16YwPa6WmPrHDHJwz61py0nU", signature);
    }

    @Test
    public void testOperationTypeSuccessAsync() throws IOException, PubNubException, InterruptedException {
        AtomicBoolean atomic = new AtomicBoolean(false);

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("v2.P28cNou37m624BETKNaGaXO_wFR6zVNdBCGD0gQaIu0"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        partialGrant.authKeys(Collections.singletonList("key1")).channels(Collections.singletonList("ch1")).async(
                new PNCallback<PNAccessManagerGrantResult>() {
                    @Override
                    public void onResponse(PNAccessManagerGrantResult result, @NotNull PNStatus status) {
                        if (status != null && status.getOperation() == PNOperationType.PNAccessManagerGrant) {
                            atomic.set(true);
                        }
                    }
                });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomic);
    }

    @Test
    public void testNullSecretKey() {
        pubnub.getConfiguration().setSecretKey(null);

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Secret Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testEmptySecretKey() {
        pubnub.getConfiguration().setSecretKey("");

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Secret Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testNullSubscribeKey() {
        pubnub.getConfiguration().setSubscribeKey(null);

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Subscribe Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testEmptySubscribeKey() {
        pubnub.getConfiguration().setSubscribeKey("");

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Subscribe Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testNullPublishKey() {
        pubnub.getConfiguration().setPublishKey(null);

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Publish Key not configured.", e.getPubnubError().getMessage());
        }

    }

    @Test
    public void testEmptyPublishKey() {
        pubnub.getConfiguration().setPublishKey("");

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
            throw new RuntimeException("should never reach here");
        } catch (PubNubException e) {
            assertEquals("ULS configuration failed. Publish Key not configured.", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testMissingChannelsAndChannelGroup() {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .withQueryParam("signature", matching("v2.aPDDeiJA2CX1QKIIjY3LlgD1p-MjwBozZgMX995E0u8"))
                .willReturn(aResponse().withStatus(200).withBody("{\"message\":\"Success\"," +
                        "\"payload\":{\"level\":\"subkey\",\"subscribe_key\":\"mySubscribeKey\",\"ttl\":1440,\"r\":0," +
                        "\"w\":1,\"m\":0,\"d\":0},\"service\":\"Access Manager\",\"status\":200}")));

        try {
            PNAccessManagerGrantResult grantResult = partialGrant.sync();
            Assert.assertNotNull(grantResult);
            assertEquals("subkey", grantResult.getLevel());
            assertEquals(1440, grantResult.getTtl());
            assertEquals(0, grantResult.getChannels().size());
            assertEquals(0, grantResult.getChannelGroups().size());
        } catch (PubNubException e) {
            e.printStackTrace();
            throw new RuntimeException("should never reach here");
        }
    }

    @Test
    public void testNullPayload() {

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\"," +
                        "\"status\":200}")));

        try {
            partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
        } catch (PubNubException e) {
            assertEquals("Parsing Error", e.getPubnubError().getMessage());
        }
    }

    @Test
    public void testNullAuthKeyAsync() throws PubNubException {

        AtomicBoolean atomic = new AtomicBoolean(false);

        stubFor(get(urlPathEqualTo("/v2/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("signature", matching("v2.gvSzmBMnSxdQnMXWLWGXyLwN6YZdcH9H71froaHgKWc"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\"," +
                        "\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1," +
                        "\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access " +
                        "Manager\",\"status\":200}")));

        partialGrant.channels(Collections.singletonList("ch1")).async(new PNCallback<PNAccessManagerGrantResult>() {
            @Override
            public void onResponse(PNAccessManagerGrantResult result, @NotNull PNStatus status) {
                if (status != null && status.getOperation() == PNOperationType.PNAccessManagerGrant
                        && !status.isError()) {
                    atomic.set(true);
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomic);
    }

}