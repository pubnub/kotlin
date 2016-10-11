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
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class GrantEndpointTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private Grant partialGrant;
    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        partialGrant = pubnub.grant();
        pubnub.getConfiguration().setSecretKey("secretKey").setIncludeInstanceIdentifier(true);
        wireMockRule.start();
    }

    @Test
    public void NoGroupsOneChannelOneKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("nUZe6LEDe2RGPeh4qoe-IpjAYzBf8RJMtSXI7ktaah8%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

    }

    @Test
    public void NoGroupsOneChannelTwoKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("ml__GSdczD-5CLqz8Vappaux1bZj6IycOP4O6FyS7ek%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Arrays.asList("ch1")).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());
    }

    @Test
    public void NoGroupsTwoChannelOneKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("J6g30_aZUtbXOLCQ3kkYx0-K6Rm0F-pFUPa3kZ2647Q%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1", "ch2")).sync();

        assertEquals(2, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(1, result.getChannels().get("ch2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());
    }

    @Test
    public void NoGroupsTwoChannelTwoKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("-3DlrA39lnyhbjTtuJC6s13Gc5Owa_SdRxAx3zJFUd4%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Arrays.asList("ch1", "ch2")).sync();

        assertEquals(2, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(2, result.getChannels().get("ch2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key2").getClass());
    }

    @Test
    public void OneGroupNoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("JfuIHr4BMGVbaAVp1QdDvvhcX2aaCEqINIcIsHJMGCs%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList("cg1")).sync();

        assertEquals(0, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());

    }

    @Test
    public void OneGroupNoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("m47WC6N_zx9EhnX3XcO-bNS0wO5Fw4sj8154NoyyVKs%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(Arrays.asList("cg1")).sync();

        assertEquals(0, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());

    }

    @Test
    public void OneGroupOneChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("TXuXp8mvpziVIeuyd8b0dgNXRYEJ9oBIhktnhilt7Xs%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}},\"channel-groups\":\"cg1\"},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).channelGroups(Arrays.asList("cg1")).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

    }

    @Test
    public void OneGroupOneChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("KbQ7nkSPXZO_iTfn7nNfCO_Ut7OsEg4Hzb321vGgYUc%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}},\"channel-groups\":\"cg1\"},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Arrays.asList("ch1")).channelGroups(Arrays.asList("cg1")).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());

        assertEquals(2, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key2").getClass());

    }

    @Test
    public void OneGroupTwoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("gstN6_LP_z9LVMcBY4nAgXZQu_AcAta6Cuq2nWglbmw%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}},\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1", "ch2")).channelGroups(Arrays.asList("cg1")).sync();

        assertEquals(2, result.getChannels().size());
        assertEquals(1, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch2").get("key1").getClass());

    }

    @Test
    public void OneGroupTwoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("channel-group", matching("cg1"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("p6eQF50jjcKvWQ1rulCE41WHKlJyp11byW989x4FEFc%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}},\"channel-groups\":\"cg1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channels(Arrays.asList("ch1", "ch2")).channelGroups(Arrays.asList("cg1")).sync();

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
    }

    //

    @Test
    public void TwoGroupNoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("rBRwhq15luvJ5IOFMgQqICHP_K10h0lm1m4TbKTgdWE%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList("cg1", "cg2")).sync();

        assertEquals(0, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(1, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());

    }

    @Test
    public void TwoGroupNoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("_78G4e79CvlnpKJfQTvORBGzimTFZfzxY5tcEVLW4Xs%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(Arrays.asList("cg1", "cg2")).sync();

        assertEquals(0, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(2, result.getChannelGroups().get("cg1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key2").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key2").getClass());

    }

    @Test
    public void TwoGroupOneChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("8y9GXwhZFO1m9saIe1FVkAu5jIPMu-U9eHVSy0ybyio%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}},\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList("cg1", "cg2")).channels(Arrays.asList("ch1")).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(2, result.getChannelGroups().size());

        assertEquals(1, result.getChannelGroups().get("cg1").size());
        assertEquals(1, result.getChannelGroups().get("cg2").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg1").get("key1").getClass());
        assertEquals(PNAccessManagerKeyData.class, result.getChannelGroups().get("cg2").get("key1").getClass());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());
    }

    @Test
    public void TwoGroupOneChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("GIgfQnwun0NgZNYD_rDtS3DkIuK7xMyWN8p_7kG4y5Q%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}},\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(Arrays.asList("cg1", "cg2")).channels(Arrays.asList("ch1")).sync();

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

    }

    @Test
    public void TwoGroupTwoChannelOneKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("Kvm2uRwgJ60iGYyvI60KC_WIdU3cxIKyYaX1LK0PL3c%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}},\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channelGroups(Arrays.asList("cg1", "cg2")).channels(Arrays.asList("ch1", "ch2")).sync();

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
    }

    @Test
    public void TwoGroupTwoChannelTwoKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1,ch2"))
                .withQueryParam("channel-group", matching("cg1,cg2"))
                .withQueryParam("auth", matching("key1,key2"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("uptOF3nuKzGHSJ-swKQFj4XSgzKetO5uHl9iRkPeYGY%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channels\":{\"ch1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"ch2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}},\"channel-groups\":{\"cg1\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}},\"cg2\":{\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0},\"key2\":{\"r\":0,\"w\":0,\"m\":0}}}}},\"service\":\"Access Manager\",\"status\":200}\n")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1", "key2")).channelGroups(Arrays.asList("cg1", "cg2")).channels(Arrays.asList("ch1", "ch2")).sync();

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

    }

    @Test
    public void NoGroupsOneChannelOneKeyTTLTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("VcJTCcpMugw4Stm409oUeaLWR0-ZewRJNKKetI6A1Jk%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .withQueryParam("ttl", matching("1334"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).ttl(1334).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

    }

    @Test
    public void NoGroupsOneChannelOneReadKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("9ngAxIIhZUq6qqCAPXwcpoqPlSRXDokohXmgZ9jEZsM%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("1"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).read(true).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

    }

    @Test
    public void NoGroupsOneChannelOneWriteKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("ZNoFdFsoGjNmKLoOFPW7Xn9es3c41ZG1gHZ3Szz9BbA%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("1"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).write(true).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

    }

    @Test
    public void NoGroupsOneChannelOneKeyManageTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("lMTGADViQb_pQPkwOypoiRhiJGdN2prTlWlHCfZrgx0%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("1"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerGrantResult result = partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).manage(true).sync();

        assertEquals(1, result.getChannels().size());
        assertEquals(0, result.getChannelGroups().size());

        assertEquals(1, result.getChannels().get("ch1").size());
        assertEquals(PNAccessManagerKeyData.class, result.getChannels().get("ch1").get("key1").getClass());

    }


    @org.junit.Test(expected=PubNubException.class)
    public void NoGroupsOneChannelMissingKey() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("_znR67zw5cdCq3Cmn1QHUHtFolkquYARh_JYCeMb8ig%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("1"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        partialGrant.channels(Arrays.asList("ch1")).manage(true).sync();
    }

    @org.junit.Test
    public void testIsAuthRequiredSuccessSync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceid", matching("PubNubInstanceId"))
                .withQueryParam("requestid", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("nUZe6LEDe2RGPeh4qoe-IpjAYzBf8RJMtSXI7ktaah8%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setAuthKey("myKey");
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
    }

    @org.junit.Test
    public void testOperationTypeSuccessAsync() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        final AtomicInteger atomic = new AtomicInteger(0);

        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).async(new PNCallback<PNAccessManagerGrantResult>() {
            @Override
            public void onResponse(PNAccessManagerGrantResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNAccessManagerGrant) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullSecretKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSecretKey(null);
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptySecretKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSecretKey("");
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullSubscribeKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSubscribeKey(null);
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptySubscribeKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setSubscribeKey("");
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullPublishKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setPublishKey(null);
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testEmptyPublishKey() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        pubnub.getConfiguration().setPublishKey("");
        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testMissingChannelsAndChannelGroup() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"ttl\":1,\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":0,\"w\":0,\"m\":0}}},\"service\":\"Access Manager\",\"status\":200}")));

        partialGrant.authKeys(Arrays.asList("key1")).sync();
    }

    @org.junit.Test(expected=PubNubException.class)
    public void testNullPayload() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("signature", matching("HlyfXDFhdgNhKfBzGaouxh2T2SRimm4bVq_JVKLRPQI%3D%0A"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("timestamp", matching("1337"))
                .withQueryParam("r", matching("0"))
                .withQueryParam("w", matching("0"))
                .withQueryParam("m", matching("0"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"service\":\"Access Manager\",\"status\":200}")));

        partialGrant.authKeys(Arrays.asList("key1")).channels(Arrays.asList("ch1")).sync();
    }
}

