package com.pubnub.api.integration.pam;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.ITTestConfig;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.java.models.consumer.objects_api.PNObject;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Enumeration ({@code getAllChannelsMetadata} / {@code getAllUUIDMetadata}) enforcement through the
 * {@code getAllChannels} / {@code getAllUUIDs} category grants.
 *
 * <p>Requires the PAM keyset ({@code PAM_PUB_KEY} / {@code PAM_SUB_KEY} / {@code PAM_SEC_KEY}) to have
 * {@code pam_objects_enumeration_getall_mode = 2} (Enforce).
 */
@SuppressWarnings("deprecation")
public class GetAllEnumerationGrantIntegrationTest extends BaseIntegrationTest {
    private final String channelId = "enum-channel-" + RandomStringUtils.random(8, "abcdefgh");
    private final String uuidId = "enum-uuid-" + RandomStringUtils.random(8, "abcdefgh");
    private final String channelFilter = "id == \"" + channelId + "\"";
    private final String uuidFilter = "id == \"" + uuidId + "\"";
    private final List<String> tokens = new ArrayList<>();

    @Override
    protected void onBefore() {
        server = getServer();
        try {
            server.setChannelMetadata().channel(channelId).name("enumeration test channel").sync();
            server.setUUIDMetadata().uuid(uuidId).name("enumeration test uuid").sync();
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onAfter() {
        try {
            server.removeChannelMetadata().channel(channelId).sync();
        } catch (PubNubException ignored) {
        }
        try {
            server.removeUUIDMetadata().uuid(uuidId).sync();
        } catch (PubNubException ignored) {
        }
        // Best effort: revoke may be disabled on the keyset, which must not fail the test.
        for (String token : tokens) {
            try {
                server.revokeToken().token(token).sync();
            } catch (PubNubException ignored) {
            }
        }
    }

    @Test
    public void grantToken_flagsRoundTripThroughParseToken() throws PubNubException {
        final PNToken parsed = server.parseToken(grantToken(true, true));

        assertTrue(parsed.getAllChannels());
        assertTrue(parsed.getAllUUIDs());
    }

    @Test
    public void tokenWithoutCategories_cannotListMetadata() throws PubNubException {
        final PubNub client = tokenClient(grantToken(false, false));

        assertForbidden(() -> client.getAllChannelsMetadata().filter(channelFilter).sync());
        assertForbidden(() -> client.getAllUUIDMetadata().filter(uuidFilter).sync());
    }

    @Test
    public void tokenWithBothCategories_canListMetadata() throws PubNubException {
        final PubNub client = tokenClient(grantToken(true, true));

        assertListsChannel(client);
        assertListsUuid(client);
    }

    @Test
    public void tokenWithGetAllChannelsOnly_canListOnlyChannels() throws PubNubException {
        final PubNub client = tokenClient(grantToken(true, false));

        assertListsChannel(client);
        assertForbidden(() -> client.getAllUUIDMetadata().filter(uuidFilter).sync());
    }

    @Test
    public void tokenWithGetAllUUIDsOnly_canListOnlyUuids() throws PubNubException {
        final PubNub client = tokenClient(grantToken(false, true));

        assertForbidden(() -> client.getAllChannelsMetadata().filter(channelFilter).sync());
        assertListsUuid(client);
    }

    @Test
    public void serverWithSecretKey_canListMetadata() throws PubNubException {
        assertListsChannel(server);
        assertListsUuid(server);
    }

    @Test
    public void clientWithoutTokenOrSecretKey_cannotListMetadata() {
        final PubNub client = getAuthorizedClient();

        assertForbidden(() -> client.getAllChannelsMetadata().filter(channelFilter).sync());
        assertForbidden(() -> client.getAllUUIDMetadata().filter(uuidFilter).sync());
    }

    @Test
    public void legacyGrant_returnsCategories() throws PubNubException {
        final String authKey = "enum-auth-" + UUID.randomUUID();

        final PNAccessManagerGrantResult result = server.grant()
                .authKeys(Collections.singletonList(authKey))
                .getAllChannels(true)
                .getAllUUIDs(true)
                .sync();

        assertEquals(new HashSet<>(Arrays.asList("channels", "uuids")), result.getCategories().keySet());
        assertTrue(result.getCategories().get("channels").get(authKey).isGetEnabled());
        assertTrue(result.getCategories().get("uuids").get(authKey).isGetEnabled());
    }

    @Test
    public void legacyGrant_authKeyWithCategories_canListMetadata() throws PubNubException {
        final String authKey = "enum-auth-" + UUID.randomUUID();
        server.grant()
                .authKeys(Collections.singletonList(authKey))
                .getAllChannels(true)
                .getAllUUIDs(true)
                .sync();

        final PubNub client = authKeyClient(authKey);

        assertListsChannel(client);
        assertListsUuid(client);
    }

    @Test
    public void legacyGrant_authKeyWithoutCategories_cannotListMetadata() {
        final PubNub client = authKeyClient("enum-auth-" + UUID.randomUUID());

        assertForbidden(() -> client.getAllChannelsMetadata().filter(channelFilter).sync());
        assertForbidden(() -> client.getAllUUIDMetadata().filter(uuidFilter).sync());
    }

    // A named-resource `get` is included so the token is valid even with both flags off.
    private String grantToken(boolean getAllChannels, boolean getAllUUIDs) throws PubNubException {
        final String token = server.grantToken(60)
                .channels(Collections.singletonList(ChannelGrant.name(channelId).get()))
                .uuids(Collections.singletonList(UUIDGrant.id(uuidId).get()))
                .getAllChannels(getAllChannels)
                .getAllUUIDs(getAllUUIDs)
                .sync()
                .getToken();
        tokens.add(token);
        return token;
    }

    private PubNub tokenClient(String token) {
        final PubNub client = getAuthorizedClient();
        client.setToken(token);
        return client;
    }

    private PubNub authKeyClient(String authKey) {
        final ITTestConfig config = ConfigFactory.create(ITTestConfig.class, System.getenv());
        return getPubNub(builder -> {
            builder.subscribeKey(config.pamSubKey());
            builder.publishKey(config.pamPubKey());
            builder.authKey(authKey);
        });
    }

    private void assertListsChannel(PubNub client) throws PubNubException {
        final List<String> ids = client.getAllChannelsMetadata().filter(channelFilter).sync()
                .getData().stream().map(PNObject::getId).collect(Collectors.toList());
        assertEquals(Collections.singletonList(channelId), ids);
    }

    private void assertListsUuid(PubNub client) throws PubNubException {
        final List<String> ids = client.getAllUUIDMetadata().filter(uuidFilter).sync()
                .getData().stream().map(PNObject::getId).collect(Collectors.toList());
        assertEquals(Collections.singletonList(uuidId), ids);
    }

    private interface Call {
        void run() throws PubNubException;
    }

    private void assertForbidden(Call call) {
        try {
            call.run();
            fail("Expected HTTP 403");
        } catch (PubNubException e) {
            // The SDK doesn't expose the server error code (2013 / 2014), only the status.
            assertEquals(403, e.getStatusCode());
        }
    }
}