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
        pubnub.getConfiguration().setSecretKey("secretKey");
        wireMockRule.start();
    }

    @Test
    public void NoGroupsOneChannelOneKeyTest() throws PubNubException {

        stubFor(get(urlPathEqualTo("/v1/auth/grant/sub-key/mySubscribeKey"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("channel", matching("ch1"))
                .withQueryParam("auth", matching("key1"))
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("Sw4LDrBsxjXYgzw6H2ww_omDFmT5Sozd2Bd0NDRQ7GA%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("LY9uFbCRnA1fHZLd2kBsIdM9LocwnVTIHxwp3BwQ5XQ%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("_z51OsbDp9kvYfKQZUJoiyo82_QuAVZ7JyKi9zoGFAc%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("_TV8Gcpvr6L1W-9cARyGXMj3x3VIJwP1_Cuz3oDDHRE%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("JsJT8pcHUKP3OBpkuNznbmxRejYC_DQBZldbpphM1sk%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("BlTyZ9ysUV8JEU1BMps8Kfjqbu3m0dsUDRHlbnNIX5Y%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("CVKPMHlOKHpnjdM-yitToj3jC2Betd9QTyATN4GCgfU%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("p21l4hlci0VGsQ_cDHfLubFALgtunt6ote7tBx4bpUU%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("ZEp63rTMmTh5N-uqN6UZuDP5GKE-b7SLwOrdi3yiBLM%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("Kcl_Ba5CPDYXlhLcXyzhVHZlWTDaSRV5syEQjq5IGbU%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("SLl749bJMEpxt_o7JeJdSTpLzTaq8P4aCC9jXWnDd58%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("Avb_BjSWm2KsdIXNDoU__q5zfKx7qKZbsEJ6Mo6y_BU%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("wa2h4HJ0b1pWS4R6oNtUKWB5BllrkPzc8GSK44GY2l8%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("P3grHL6O8P185puxlqJZHFUz0_CoirZcaqFlHIhf7E0%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("UuOtPl3i-6nThXYsxuoMcMTo6RkLRriVYXQb9yH4UgY%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("yNi_IK2z0dH3MTwpQZNnUGsba5jyg9nI4PYwMP1WH74%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("uzvZ_RvrHrU0alJGZ5CYhf2i0X5jfjwCXkr4BPFiKPE%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("w-pT2ZJEAp85vvIZCdKwpNG3-QW4_q7wwPMIvh8KBIw%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("me6ZZegEWBqCmjs8dNb4BmB7Y-15FV8KXfecSxm2Y0o%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("VNOI32TCodRd3Uzw0_gRQjbsKVnl6fGkFkXDa08hrkU%3D%0A"))
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
                .withQueryParam("instanceId", matching("PubNubInstanceId"))
                .withQueryParam("requestId", matching("PubNubRequestId"))
                .withQueryParam("signature", matching("Sw4LDrBsxjXYgzw6H2ww_omDFmT5Sozd2Bd0NDRQ7GA%3D%0A"))
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

