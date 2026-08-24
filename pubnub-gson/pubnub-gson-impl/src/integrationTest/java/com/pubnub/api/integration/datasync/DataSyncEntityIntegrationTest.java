package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.datasync.entity.PNCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntitiesResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.entity.PNPatchEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNUpdateEntityResult;
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
        final Map<String, Object> payload = new HashMap<>();
        payload.put("custom", "value");

        // create
        final PNCreateEntityResult createResult = server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();


        assertNotNull(createResult);
        assertEquals(entityId, createResult.getData().getId());
        assertEquals(entityClass, createResult.getData().getEntityClass());
        assertEquals(entityClassVersion, createResult.getData().getEntityClassVersion());
        assertNotNull(createResult.getData().getETag());

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
        final PNGetEntityResult getResult = server.dataSync().getEntity(entityId).sync();
        assertEquals(entityId, getResult.getData().getId());
        assertEquals(entityClass, getResult.getData().getEntityClass());
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
        payload.put("custom", "value");

        // create -> token scoped to `create` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).create());
        final PNCreateEntityResult createResult = client.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();

        assertNotNull(createResult);
        assertEquals(entityId, createResult.getData().getId());
        assertEquals(entityClass, createResult.getData().getEntityClass());
        assertEquals(entityClassVersion, createResult.getData().getEntityClassVersion());
        assertNotNull(createResult.getData().getETag());

        // get -> token scoped to `get` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get());
        final PNGetEntityResult getResult = client.dataSync().getEntity(entityId).sync();
        assertEquals(entityId, getResult.getData().getId());
        assertEquals(entityClass, getResult.getData().getEntityClass());
        assertEquals("active", getResult.getData().getStatus());

        // getAll -> token scoped to `get` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).get());
        final PNGetEntitiesResult getAllResult = client.dataSync()
                .getEntities(entityClass)
                .entityClassLevel("SubKey")
                .limit(100)
                .sync();
        assertNotNull(getAllResult);
        assertTrue(getAllResult.getData().stream().anyMatch(e -> entityId.equals(e.getId())));

        // patch -> token scoped to `update` on this specific entity (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).update());
        final List<PNJsonPatchOperation> operations = Collections.singletonList(
                PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
        );
        final PNPatchEntityResult patchResult = client.dataSync()
                .patchEntity(entityId, operations)
                .sync();
        assertEquals("inactive", patchResult.getData().getStatus());

        // update -> token scoped to `update` on this specific entity (PUT maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId).update());
        final Map<String, Object> newPayload = new HashMap<>();
        newPayload.put("custom", "updated");
        final PNUpdateEntityResult updateResult = client.dataSync()
                .updateEntity(entityId, entityClassVersion)
                .status("archived")
                .payload(newPayload)
                .sync();
        assertEquals("archived", updateResult.getData().getStatus());
        assertEquals("updated", updateResult.getData().getPayload().get("custom"));

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

    private void grantAndAuthenticate(com.pubnub.api.java.PubNub client, String authorizedUUID, DataSyncGrant... grants) throws PubNubException {
        final String token = server.grantToken(60)
                .authorizedUserId(new UserId(authorizedUUID))
                .dataSync(Arrays.asList(grants))
                .sync()
                .getToken();
        client.setToken(token);
    }

    @Test
    public void createWithServerGeneratedId() throws PubNubException {
        final PNCreateEntityResult createResult = server.dataSync()
                .createEntity(entityClass, entityClassVersion)
                .sync();

        final String generatedId = createResult.getData().getId();
        assertFalse(generatedId.trim().isEmpty());

        // cleanup
        server.dataSync().removeEntity(generatedId).sync();
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteEntity() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
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
            final PNGetEntitiesResult getAllResult = server.dataSync()
                    .getEntities(entityClass)
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(e -> entityId.equals(e.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNPatchEntityResult patchResult = server.dataSync()
                    .patchEntity(entityId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // get reflects the patched status
            assertEquals("inactive", server.dataSync().getEntity(entityId).sync().getData().getStatus());

            // update -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("custom", "updated");
            final PNUpdateEntityResult updateResult = server.dataSync()
                    .updateEntity(entityId, entityClassVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("updated", updateResult.getData().getPayload().get("custom"));

            // get reflects the full replacement
            final PNGetEntityResult afterUpdate = server.dataSync().getEntity(entityId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("updated", afterUpdate.getData().getPayload().get("custom"));
        } finally {
            server.dataSync().removeEntity(entityId).sync();
        }
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("custom", "value");

        // create
        final PNCreateEntityResult createResult = server.dataSync()
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
            final PNPatchEntityResult patch1 = server.dataSync()
                    .patchEntity(entityId, inactiveOps)
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
                        .patchEntity(entityId, archivedOps)
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