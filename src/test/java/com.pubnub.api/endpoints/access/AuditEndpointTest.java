package com.pubnub.api.endpoints.access;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditResult;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class AuditEndpointTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private Audit partialAudit;
    private Pubnub pubnub;

    @Before
    public void beforeEach() throws IOException {

        pubnub = this.createPubNubInstance(8080);
        partialAudit = pubnub.audit();

        pubnub.getConfiguration().setSecretKey("secretKey");

    }

    @Test
    public void testSuccessChannelGroupSync() throws PubnubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channelGroup("cg1").authKeys(Arrays.asList("key1")).sync();

        Assert.assertEquals("cg2", pnAccessManagerAuditResult.getData().getChannelGroup());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isManageEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isReadEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isWriteEnabled());
        Assert.assertEquals("channel-group+auth", pnAccessManagerAuditResult.getData().getLevel());
        Assert.assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getData().getSubscribeKey());
    }

    @Test
    public void testSuccessChannelSync() throws PubnubException {

        stubFor(get(urlPathEqualTo("/v1/auth/audit/sub-key/mySubscribeKey"))
                .willReturn(aResponse().withBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}")));

        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channel("ch1").authKeys(Arrays.asList("key1")).sync();

        Assert.assertEquals("ch1", pnAccessManagerAuditResult.getData().getChannel());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isManageEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isReadEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isWriteEnabled());
        Assert.assertEquals("user", pnAccessManagerAuditResult.getData().getLevel());
        Assert.assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getData().getSubscribeKey());
    }


    //

}
