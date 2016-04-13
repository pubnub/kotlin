package com.pubnub.api.endpoints.access;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditResult;
import com.pubnub.api.endpoints.EndpointTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AuditEndpointTest extends EndpointTest {

    private MockWebServer server;
    private Audit.AuditBuilder partialAudit;
    private Pubnub pubnub;

    @Before
    public void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();

        pubnub = this.createPubNubInstance(server);
        partialAudit = Audit.builder().pubnub(pubnub);

        pubnub.getConfiguration().setSecretKey("secretKey");

    }

    @Test
    public void testSuccessChannelGroupSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"message\":\"Success\",\"payload\":{\"level\":\"channel-group+auth\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel-group\":\"cg2\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}"));
        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channelGroup("cg1").authKey("key1").build().sync();

        Assert.assertEquals("cg2", pnAccessManagerAuditResult.getData().getChannelGroup());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isManageEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isReadEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isWriteEnabled());
        Assert.assertEquals("channel-group+auth", pnAccessManagerAuditResult.getData().getLevel());
        Assert.assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getData().getSubscribeKey());
    }

    @Test
    public void testSuccessChannelSync() throws PubnubException {
        server.enqueue(new MockResponse().setBody("{\"message\":\"Success\",\"payload\":{\"level\":\"user\",\"subscribe_key\":\"sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f\",\"channel\":\"ch1\",\"auths\":{\"key1\":{\"r\":1,\"m\":1,\"w\":1}}},\"service\":\"Access Manager\",\"status\":200}"));
        PNAccessManagerAuditResult pnAccessManagerAuditResult = partialAudit.channel("ch1").authKey("key1").build().sync();

        Assert.assertEquals("ch1", pnAccessManagerAuditResult.getData().getChannel());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isManageEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isReadEnabled());
        Assert.assertEquals(true, pnAccessManagerAuditResult.getData().getAuthKeys().get("key1").isWriteEnabled());
        Assert.assertEquals("user", pnAccessManagerAuditResult.getData().getLevel());
        Assert.assertEquals("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f", pnAccessManagerAuditResult.getData().getSubscribeKey());
    }


    //

}
