package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UserGrant;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncCreateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncGetUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncGetUsersResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncUpdateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.DataSyncSetUserResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataSyncUserIntegrationTest extends BaseIntegrationTest {
    private final static int entityClassVersion = 1;
    private final String userId = "user-" + RandomStringUtils.random(8, "abcdefgh");

    @Override
    protected void onBefore() {
        server = getServer();
    }

    @Test
    public void createGetAndDeleteUser() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create (no entityClass -> server defaults it to "User")
        final DataSyncCreateUserResult createResult = server.dataSync().createUser(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            assertNotNull(createResult);
            assertEquals(userId, createResult.getData().getId());
            assertEquals(entityClassVersion, createResult.getData().getClassVersion());
            assertNotNull(createResult.getData().getETag());
            assertEquals("Alice", createResult.getData().getPayload().get("username"));
            assertEquals("alice@example.com", createResult.getData().getPayload().get("email"));

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync().createUser(entityClassVersion)
                        .userId(userId)
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a user with an existing id");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // get
            final DataSyncGetUserResult getResult = server.dataSync().getUser(userId).sync();
            assertEquals(userId, getResult.getData().getId());
            assertEquals("active", getResult.getData().getStatus());

            // delete
            server.dataSync().removeUser(userId).sync();

            // get after delete -> 404
            try {
                server.dataSync().getUser(userId).sync();
                fail("Expected a 404 after deleting the user");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            // best-effort cleanup: the happy path already deleted the user, so a 404 here is expected
            try {
                server.dataSync().removeUser(userId).sync();
            } catch (PubNubException ignored) {
                // already deleted
            }
        }
    }

    /**
     * Same create/get/getAll/patch/update/delete flow as {@link #createGetAllPatchUpdateAndDeleteUser()}, but instead
     * of relying on the client's own secretKey, the "server" (the only party holding the secretKey) mints a scoped
     * PAM token for the client's authorized UUID and the client authenticates with it via
     * {@link PubNub#setToken(String)}. This mirrors the production setup where the client never sees the secretKey.
     *
     * <p>A User is authorized under the {@code users} PAM resource type, so the grant is a {@link UserGrant} keyed by
     * the userId. Each API call is preceded by a fresh token carrying only the single permission that call requires,
     * verifying the client can operate with least privilege (POST -> {@code create}, GET -> {@code get},
     * PATCH/PUT -> {@code update}, DELETE -> {@code delete}).
     */
    @Test
    public void createGetAndDeleteUpdatePathGetAllUsersWithServerGrantedToken() throws PubNubException {
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via setToken.
        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create -> token scoped to `create` on this specific user id
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).create());
        final DataSyncCreateUserResult createResult = client.dataSync().createUser(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        assertNotNull(createResult);
        assertEquals(userId, createResult.getData().getId());
        assertEquals(entityClassVersion, createResult.getData().getClassVersion());
        assertNotNull(createResult.getData().getETag());
        assertEquals("Alice", createResult.getData().getPayload().get("username"));
        assertEquals("alice@example.com", createResult.getData().getPayload().get("email"));

        // get -> token scoped to `get` on this specific user
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).get());
        final DataSyncGetUserResult getResult = client.dataSync().getUser(userId).sync();
        assertEquals(userId, getResult.getData().getId());
        assertEquals("active", getResult.getData().getStatus());

        // getAll -> token scoped to `get` on this specific user id
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).get());
        final DataSyncGetUsersResult getAllResult = client.dataSync().getUsers()
                .limit(100)
                .sync();
        assertNotNull(getAllResult);
        assertTrue(getAllResult.getData().stream().anyMatch(u -> userId.equals(u.getId())));

        // patch -> token scoped to `update` on this specific user (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).update());
        final List<PNJsonPatchOperation> operations = Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
        );
        final DataSyncUpdateUserResult patchResult = client.dataSync().updateUser(userId, operations)
                .sync();
        assertEquals("inactive", patchResult.getData().getStatus());

        // update -> token scoped to `update` on this specific user (PUT maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).update());
        final Map<String, Object> newPayload = new HashMap<>();
        newPayload.put("username", "Bob");
        newPayload.put("email", "bob@example.com");
        final DataSyncSetUserResult updateResult = client.dataSync().setUser(userId, entityClassVersion)
                .status("archived")
                .payload(newPayload)
                .sync();
        assertEquals("archived", updateResult.getData().getStatus());
        assertEquals("Bob", updateResult.getData().getPayload().get("username"));

        // delete -> token scoped to `delete` on this specific user
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).delete());
        client.dataSync().removeUser(userId).sync();

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(userId).get());
        try {
            client.dataSync().getUser(userId).sync();
            fail("Expected a 404 after deleting the user");
        } catch (PubNubException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    private void grantAndAuthenticate(com.pubnub.api.java.PubNub client, String authorizedUUID, TokenGrant... grants) throws PubNubException {
        final String token = server.grantToken(60)
                .authorizedUserId(new UserId(authorizedUUID))
                .grants(Arrays.asList(grants))
                .sync()
                .getToken();
        client.setToken(token);
    }

    @Test
    public void createWithServerGeneratedId() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Bob");

        final DataSyncCreateUserResult createResult = server.dataSync().createUser(entityClassVersion)
                .payload(payload)
                .sync();

        final String generatedId = createResult.getData().getId();
        try {
            assertFalse(generatedId.trim().isEmpty());
        } finally {
            // cleanup
            server.dataSync().removeUser(generatedId).sync();
        }
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteUser() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        server.dataSync().createUser(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            // getAll -> the created user is present
            final DataSyncGetUsersResult getAllResult = server.dataSync().getUsers()
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(u -> userId.equals(u.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final DataSyncUpdateUserResult patchResult = server.dataSync().updateUser(userId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // get reflects the patched status
            assertEquals("inactive", server.dataSync().getUser(userId).sync().getData().getStatus());

            // update -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("username", "Bob");
            newPayload.put("email", "bob@example.com");
            final DataSyncSetUserResult updateResult = server.dataSync().setUser(userId, entityClassVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("Bob", updateResult.getData().getPayload().get("username"));

            // get reflects the full replacement
            final DataSyncGetUserResult afterUpdate = server.dataSync().getUser(userId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("Bob", afterUpdate.getData().getPayload().get("username"));
        } finally {
            server.dataSync().removeUser(userId).sync();
        }
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        final DataSyncCreateUserResult createResult = server.dataSync().createUser(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            final String originalETag = createResult.getData().getETag();
            assertNotNull(originalETag);

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            final List<PNJsonPatchOperation> inactiveOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final DataSyncUpdateUserResult patch1 = server.dataSync().updateUser(userId, inactiveOps)
                    .ifMatch(originalETag)
                    .sync();
            assertEquals("inactive", patch1.getData().getStatus());
            assertNotEquals(originalETag, patch1.getData().getETag());

            // patch #2 with the now-stale ifMatch -> 412 (optimistic concurrency conflict)
            final List<PNJsonPatchOperation> archivedOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("archived").build()
            );
            try {
                server.dataSync().updateUser(userId, archivedOps)
                        .ifMatch(originalETag)
                        .sync();
                fail("Expected a 412 when patching with a stale ifMatch eTag");
            } catch (PubNubException e) {
                assertEquals(412, e.getStatusCode());
            }
        } finally {
            server.dataSync().removeUser(userId).sync();
        }
    }
}