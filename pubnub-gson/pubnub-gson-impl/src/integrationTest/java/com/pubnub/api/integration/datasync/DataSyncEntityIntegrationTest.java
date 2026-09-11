package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncGetEntitiesResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncGetEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncUpdateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNDataSyncSetEntityResult;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration tests for the DataSync entity API. They depend on a pre-provisioned {@code TestUser} entity class
 * existing on the keyset (there is no SDK method to create classes — see {@code scripts/datasync/create-classes.sh}).
 *
 * <p>{@code TestUser} (SubKey level, version 1) is defined as:
 * <pre>
 * property     path                    valueKind  filtering  nullable  projections
 * username     /payload/username       string     full       false     __default__, admin
 * email        /payload/email          string     simple     true      admin            (admin-only)
 * status       /status                 string     simple     true      __default__, admin
 * signupDate   /payload/signupDate     date       simple     true      __default__, admin
 * </pre>
 *
 * <p>Consequences the tests rely on:
 * <ul>
 *   <li>{@code username} is non-nullable → every create/PUT must include it (else {@code DS-0650}).</li>
 *   <li>{@code email} is in the {@code admin} projection only → a {@code __default__}-projection read hides it,
 *       and a {@code __default__}-projection write is rejected. Token-authorized writes that include
 *       {@code email} must grant the entity through {@code .projection("admin")}.</li>
 *   <li>Under a non-default projection the write guard rejects EVERY payload field not in that projection, so an
 *       {@code admin}-projected write payload may contain only {@code admin} fields (no undeclared fields like
 *       {@code hobby}/{@code custom}).</li>
 *   <li>{@code signupDate} is {@code date}-kind → date-only {@code YYYY-MM-DD} literals (not RFC-3339 datetime).</li>
 * </ul>
 */
public class DataSyncEntityIntegrationTest extends BaseIntegrationTest {
    private final static String entityClass = "TestUser";
    private final static int entityClassVersion = 1;
    private final String entityId = "entity-" + RandomStringUtils.random(8, "abcdefgh");

    @Override
    protected void onBefore() {
        server = getServer();
    }

    @Test
    public void createGetAndDeleteEntity() throws PubNubException {
        // TestUser declares `username` as non-nullable, so every create must include it (else DS-0650).
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("custom", "value");

        // create
        final PNDataSyncCreateEntityResult createResult = server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();


        assertNotNull(createResult);
        assertEquals(entityId, createResult.getData().getId());
        assertEquals(entityClass, createResult.getData().getClassName());
        assertEquals(entityClassVersion, createResult.getData().getClassVersion());
        assertNotNull(createResult.getData().getETag());
        // expiresAt is a required, server-computed field: proves the server always returns it
        assertFalse(createResult.getData().getExpiresAt().trim().isEmpty());

        // create again with the same id -> 409 (create is create-only)
        try {
            server.dataSync()
                    .createEntity(entityClass, entityClassVersion)
                    .entityId(entityId)
                    .status("active")
                    .payload(payload)
                    .sync();
            fail("Expected a 409 when creating an entity with an existing id");
        } catch (PubNubException e) {
            assertEquals(409, e.getStatusCode());
        }

        // get
        final PNDataSyncGetEntityResult getResult = server.dataSync().getEntity(entityId).sync();
        assertEquals(entityId, getResult.getData().getId());
        assertEquals(entityClass, getResult.getData().getClassName());
        assertEquals("active", getResult.getData().getStatus());

        // delete
        server.dataSync().removeEntity(entityId).sync();

        // get after delete -> 404
        try {
            server.dataSync().getEntity(entityId).sync();
            fail("Expected a 404 after deleting the entity");
        } catch (PubNubException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

     @Test
    public void createGetDeletePatchUpdateGetAllEntityWithServerGrantedToken() throws PubNubException {
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via setToken.
        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).create().projection("admin"));
        final PNDataSyncCreateEntityResult createResult = client.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();

        assertNotNull(createResult);
        assertEquals(entityId, createResult.getData().getId());
        assertEquals(entityClass, createResult.getData().getClassName());
        assertEquals(entityClassVersion, createResult.getData().getClassVersion());
        assertNotNull(createResult.getData().getETag());
        assertEquals("Alice", createResult.getData().getPayload().get("username"));
        assertEquals("alice@example.com", createResult.getData().getPayload().get("email"));

        // get -> token scoped to `get` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get());
        final PNDataSyncGetEntityResult getResult = client.dataSync().getEntity(entityId).sync();
        assertEquals(entityId, getResult.getData().getId());
        assertEquals(entityClass, getResult.getData().getClassName());
        assertEquals("active", getResult.getData().getStatus());

        // getAll -> token scoped to `get` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get());
        final PNDataSyncGetEntitiesResult getAllResult = client.dataSync()
                .getEntities(entityClass)
                .classLevel(PNDataSyncClassLevel.SUBKEY)
                .limit(100)
                .sync();
        assertNotNull(getAllResult);
        assertTrue(getAllResult.getData().stream().anyMatch(e -> entityId.equals(e.getId())));

        // patch -> token scoped to `update` on this specific entity (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).update());
        final List<PNJsonPatchOperation> operations = Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
        );
        final PNDataSyncUpdateEntityResult patchResult = client.dataSync()
                .updateEntity(entityId, operations)
                .sync();
        assertEquals("inactive", patchResult.getData().getStatus());

        // update -> token scoped to `update` on this specific entity (PUT maps to `update`).
        // projection("admin") for the same reason as create: the new payload writes `email`.
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).update().projection("admin"));
        final Map<String, Object> newPayload = new HashMap<>();
        newPayload.put("username", "Bob");
        newPayload.put("email", "bob@example.com");
        final PNDataSyncSetEntityResult updateResult = client.dataSync()
                .setEntity(entityId, entityClassVersion)
                .status("archived")
                .payload(newPayload)
                .sync();
        assertEquals("archived", updateResult.getData().getStatus());
        assertEquals("Bob", updateResult.getData().getPayload().get("username"));

        // delete -> token scoped to `delete` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).delete());
        client.dataSync().removeEntity(entityId).sync();

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get());
        try {
            client.dataSync().getEntity(entityId).sync();
            fail("Expected a 404 after deleting the entity");
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
        // No entityId -> the server generates one. TestUser.username is non-nullable, so a payload
        // carrying it is still required.
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        final PNDataSyncCreateEntityResult createResult = server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .payload(payload)
                .sync();

        final String generatedId = createResult.getData().getId();
        assertFalse(generatedId.trim().isEmpty());

        // cleanup
        server.dataSync().removeEntity(generatedId).sync();
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteEntity() throws PubNubException {
        // TestUser.username is non-nullable -> every create/PUT must include it.
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("custom", "value");

        // create
        server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            // getAll -> the created entity is present
            final PNDataSyncGetEntitiesResult getAllResult = server.dataSync()
                    .getEntities(entityClass)
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(e -> entityId.equals(e.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateEntityResult patchResult = server.dataSync()
                    .updateEntity(entityId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // get reflects the patched status
            assertEquals("inactive", server.dataSync().getEntity(entityId).sync().getData().getStatus());

            // update -> full replace of status + payload (PUT replaces the whole payload, so it must
            // re-send the non-nullable `username`)
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("username", "Alice");
            newPayload.put("custom", "updated");
            final PNDataSyncSetEntityResult updateResult = server.dataSync()
                    .setEntity(entityId, entityClassVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("updated", updateResult.getData().getPayload().get("custom"));

            // get reflects the full replacement
            final PNDataSyncGetEntityResult afterUpdate = server.dataSync().getEntity(entityId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("updated", afterUpdate.getData().getPayload().get("custom"));
        } finally {
            server.dataSync().removeEntity(entityId).sync();
        }
    }

    @Test
    public void getEntityAppliesProjectionCarriedByTheToken() throws PubNubException {
        // A projection is a named, filtered view of a class's fields, carried by the PAM token (not a read
        // parameter). The `TestUser` class declares `email` as belonging to the `admin` projection ONLY, while
        // `username` (and `status`) belong to both `__default__` and `admin`. So the same entity read through an
        // `admin`-projected token exposes `email`, but read through the implicit `__default__` projection hides it.
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();

        // A client on the same keyset as `server` but without the secretKey, so it only sees what its token allows.
        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        try {
            // admin-projected `get` token -> the `email` (admin-only) field is visible.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get().projection("admin"));
            final PNDataSyncGetEntityResult adminView = client.dataSync().getEntity(entityId).sync();
            assertEquals(entityId, adminView.getData().getId());
            assertEquals("Alice", adminView.getData().getPayload().get("username"));
            assertEquals(
                    "The admin-projected token must expose the admin-only `email` field",
                    "alice@example.com",
                    adminView.getData().getPayload().get("email"));

            // no projection -> implicit `__default__` view: `email` is omitted, `username` is still present.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get());
            final PNDataSyncGetEntityResult defaultView = client.dataSync().getEntity(entityId).sync();
            assertEquals(entityId, defaultView.getData().getId());
            assertEquals(
                    "The __default__ projection must still expose `username`",
                    "Alice",
                    defaultView.getData().getPayload().get("username"));
            assertNull(
                    "The admin-only `email` field must NOT leak through the __default__ projection",
                    defaultView.getData().getPayload().get("email"));
        } finally {
            server.dataSync().removeEntity(entityId).sync();
        }
    }

    @Test
    public void getAllRangeFilterAndSortOnDateValueKind() throws PubNubException {
        // `TestUser.signupDate` is a `date`-valueKind property declared `filtering: "simple"` (Postgres,
        // strongly consistent), so these reads resolve immediately after create — no await/poll needed.
        // This exercises the `date` value kind end-to-end: a range `filterFast` (>=) and a non-string sort,
        // as opposed to the string-only `username` coverage in the other filter/sort test.
        //
        // The `date` value kind expects a date-only `YYYY-MM-DD` literal on the wire; a full RFC-3339 datetime is
        // rejected with DS-0008 ("not of expected type 'date'").
        final String run = RandomStringUtils.random(8, "abcdefgh");
        final String userPrefix = "user-" + run + "-";
        final String idOld = "entity-" + run + "-old";
        final String idMid = "entity-" + run + "-mid";
        final String idNew = "entity-" + run + "-new";
        final String dateOld = "2019-01-01";
        final String dateMid = "2021-06-15";
        final String dateNew = "2023-12-31";
        final String cutoff = "2020-01-01"; // excludes idOld, includes idMid and idNew

        createUser(idOld, userPrefix + "old", "old@example.com", dateOld);
        createUser(idMid, userPrefix + "mid", "mid@example.com", dateMid);
        createUser(idNew, userPrefix + "new", "new@example.com", dateNew);

        try {
            // range filterFast -> signupDate >= cutoff keeps idMid and idNew, drops idOld.
            // The username LIKE keeps this run isolated from any other TestUser rows on the shared keyset.
            final PNDataSyncGetEntitiesResult ranged = server.dataSync()
                    .getEntities(entityClass)
                    .filterFast("username LIKE \"" + userPrefix + "*\" && signupDate >= \"" + cutoff + "\"")
                    .sync();
            assertEquals(
                    new java.util.HashSet<>(Arrays.asList(idMid, idNew)),
                    ranged.getData().stream().map(e -> e.getId()).collect(java.util.stream.Collectors.toSet()));

            // sort ascending by the date property -> old, mid, new
            final PNDataSyncGetEntitiesResult sortedAsc = server.dataSync()
                    .getEntities(entityClass)
                    .filterFast("username LIKE \"" + userPrefix + "*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("signupDate", true)))
                    .sync();
            assertEquals(
                    Arrays.asList(idOld, idMid, idNew),
                    sortedAsc.getData().stream().map(e -> e.getId()).collect(java.util.stream.Collectors.toList()));

            // sort descending -> new, mid, old
            final PNDataSyncGetEntitiesResult sortedDesc = server.dataSync()
                    .getEntities(entityClass)
                    .filterFast("username LIKE \"" + userPrefix + "*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("signupDate", false)))
                    .sync();
            assertEquals(
                    Arrays.asList(idNew, idMid, idOld),
                    sortedDesc.getData().stream().map(e -> e.getId()).collect(java.util.stream.Collectors.toList()));
        } finally {
            server.dataSync().removeEntity(idOld).sync();
            server.dataSync().removeEntity(idMid).sync();
            server.dataSync().removeEntity(idNew).sync();
        }
    }

    private void createUser(String id, String username, String email, String signupDate) throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("email", email);
        payload.put("signupDate", signupDate);
        server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(id)
                .status("active")
                .payload(payload)
                .sync();
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        // TestUser.username is non-nullable -> the create must include it.
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("custom", "value");

        // create
        final PNDataSyncCreateEntityResult createResult = server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
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
            final PNDataSyncUpdateEntityResult patch1 = server.dataSync()
                    .updateEntity(entityId, inactiveOps)
                    .ifMatch(originalETag)
                    .sync();
            assertEquals("inactive", patch1.getData().getStatus());
            assertNotEquals(originalETag, patch1.getData().getETag());

            // patch #2 with the now-stale ifMatch -> 412 (optimistic concurrency conflict)
            final List<PNJsonPatchOperation> archivedOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("archived").build()
            );
            try {
                server.dataSync()
                        .updateEntity(entityId, archivedOps)
                        .ifMatch(originalETag)
                        .sync();
                fail("Expected a 412 when patching with a stale ifMatch eTag");
            } catch (PubNubException e) {
                assertEquals(412, e.getStatusCode());
            }
        } finally {
            server.dataSync().removeEntity(entityId).sync();
        }
    }
}