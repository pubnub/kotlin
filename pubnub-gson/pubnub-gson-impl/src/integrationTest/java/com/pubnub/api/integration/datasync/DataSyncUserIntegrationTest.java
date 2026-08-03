package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.user.PNCreateUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNGetUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNGetUsersResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNPatchUserResult;
import com.pubnub.api.java.models.consumer.datasync.user.PNUpdateUserResult;
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

    @Test
    public void createGetAndDeleteUser() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create (no entityClass -> server defaults it to "User")
        final PNCreateUserResult createResult = pubNub.dataSync().user()
                .create(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            assertNotNull(createResult);
            assertEquals(userId, createResult.getData().getId());
            assertEquals(entityClassVersion, createResult.getData().getEntityClassVersion());
            assertNotNull(createResult.getData().getETag());
            assertEquals("Alice", createResult.getData().getPayload().get("username"));
            assertEquals("alice@example.com", createResult.getData().getPayload().get("email"));

            // create again with the same id -> 409 (create is create-only)
            try {
                pubNub.dataSync().user()
                        .create(entityClassVersion)
                        .userId(userId)
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a user with an existing id");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // get
            final PNGetUserResult getResult = pubNub.dataSync().user().get(userId).sync();
            assertEquals(userId, getResult.getData().getId());
            assertEquals("active", getResult.getData().getStatus());

            // delete
            pubNub.dataSync().user().delete(userId).sync();

            // get after delete -> 404
            try {
                pubNub.dataSync().user().get(userId).sync();
                fail("Expected a 404 after deleting the user");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            // best-effort cleanup: the happy path already deleted the user, so a 404 here is expected
            try {
                pubNub.dataSync().user().delete(userId).sync();
            } catch (PubNubException ignored) {
                // already deleted
            }
        }
    }

    /**
     * Same create/get/delete flow as {@link #createGetAndDeleteUser()}, but instead of relying on the client's own
     * secretKey, the "server" (the only party holding the secretKey) mints a scoped PAM token for the client's
     * authorized UUID and the client authenticates with it via {@link PubNub#setToken(String)}. This mirrors the
     * production setup where the client never sees the secretKey. A User is authorized under the classic
     * {@code users} PAM resource type, so the grant is a {@link UUIDGrant} keyed by the userId.
     */
    @Test
    public void createGetAndDeleteUserWithServerGrantedToken() throws PubNubException {
        // server grants a token scoped to this client's authorized UUID, over the `users` resource for this userId
        final String token = server.grantToken(60)
                .authorizedUUID(pubNub.getConfiguration().getUserId().getValue())
                .uuids(Arrays.asList(
                        UUIDGrant.id(userId).get().update().delete()
                ))
                .sync()
                .getToken();

        // client authenticates with the server-issued token
        pubNub.setToken(token);

        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        final PNCreateUserResult createResult = pubNub.dataSync().user()
                .create(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        assertNotNull(createResult);
        assertEquals(userId, createResult.getData().getId());
        assertEquals(entityClassVersion, createResult.getData().getEntityClassVersion());
        assertNotNull(createResult.getData().getETag());
        assertEquals("Alice", createResult.getData().getPayload().get("username"));
        assertEquals("alice@example.com", createResult.getData().getPayload().get("email"));

        // get
        final PNGetUserResult getResult = pubNub.dataSync().user().get(userId).sync();
        assertEquals(userId, getResult.getData().getId());
        assertEquals("active", getResult.getData().getStatus());

        // delete
        pubNub.dataSync().user().delete(userId).sync();

        // get after delete -> 404
        try {
            pubNub.dataSync().user().get(userId).sync();
            fail("Expected a 404 after deleting the user");
        } catch (PubNubException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    @Test
    public void createWithServerGeneratedId() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Bob");

        final PNCreateUserResult createResult = pubNub.dataSync().user()
                .create(entityClassVersion)
                .payload(payload)
                .sync();

        final String generatedId = createResult.getData().getId();
        try {
            assertFalse(generatedId.trim().isEmpty());
        } finally {
            // cleanup
            pubNub.dataSync().user().delete(generatedId).sync();
        }
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteUser() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        pubNub.dataSync().user()
                .create(entityClassVersion)
                .userId(userId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            // getAll -> the created user is present
            final PNGetUsersResult getAllResult = pubNub.dataSync().user()
                    .getAll()
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(u -> userId.equals(u.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNPatchUserResult patchResult = pubNub.dataSync().user()
                    .patch(userId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // get reflects the patched status
            assertEquals("inactive", pubNub.dataSync().user().get(userId).sync().getData().getStatus());

            // update -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("username", "Bob");
            newPayload.put("email", "bob@example.com");
            final PNUpdateUserResult updateResult = pubNub.dataSync().user()
                    .update(userId, entityClassVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("Bob", updateResult.getData().getPayload().get("username"));

            // get reflects the full replacement
            final PNGetUserResult afterUpdate = pubNub.dataSync().user().get(userId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("Bob", afterUpdate.getData().getPayload().get("username"));
        } finally {
            pubNub.dataSync().user().delete(userId).sync();
        }
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        final PNCreateUserResult createResult = pubNub.dataSync().user()
                .create(entityClassVersion)
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
            final PNPatchUserResult patch1 = pubNub.dataSync().user()
                    .patch(userId, inactiveOps)
                    .ifMatch(originalETag)
                    .sync();
            assertEquals("inactive", patch1.getData().getStatus());
            assertNotEquals(originalETag, patch1.getData().getETag());

            // patch #2 with the now-stale ifMatch -> 412 (optimistic concurrency conflict)
            final List<PNJsonPatchOperation> archivedOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("archived").build()
            );
            try {
                pubNub.dataSync().user()
                        .patch(userId, archivedOps)
                        .ifMatch(originalETag)
                        .sync();
                fail("Expected a 412 when patching with a stale ifMatch eTag");
            } catch (PubNubException e) {
                assertEquals(412, e.getStatusCode());
            }
        } finally {
            pubNub.dataSync().user().delete(userId).sync();
        }
    }
}