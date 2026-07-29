package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.datasync.entity.PNCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntitiesResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.entity.PNPatchEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNUpdateEntityResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DataSyncEntityIntegrationTest extends BaseIntegrationTest {
    private final static String entityClass = "User";
    private final static int entityClassVersion = 1;
    private final String entityId = "entity-" + RandomStringUtils.random(8, "abcdefgh");

    @Test
    public void createGetAndDeleteEntity() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("custom", "value");

        // create
        final PNCreateEntityResult createResult = pubNub.dataSync().entity()
                .create(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();


        assertNotNull(createResult);
        assertEquals(entityId, createResult.getData().getId());
        assertEquals(entityClass, createResult.getData().getEntityClass());
        assertEquals(entityClassVersion, createResult.getData().getEntityClassVersion());
        assertNotNull(createResult.getData().getETag());

        // get
        final PNGetEntityResult getResult = pubNub.dataSync().entity().get(entityId).sync();
        assertEquals(entityId, getResult.getData().getId());
        assertEquals(entityClass, getResult.getData().getEntityClass());
        assertEquals("active", getResult.getData().getStatus());

        // delete
        pubNub.dataSync().entity().delete(entityId).sync();

        // get after delete -> 404
        try {
            pubNub.dataSync().entity().get(entityId).sync();
            fail("Expected a 404 after deleting the entity");
        } catch (PubNubException e) {
            assertEquals(404, e.getStatusCode());
        }
    }

    @Test
    public void createWithServerGeneratedId() throws PubNubException {
        final PNCreateEntityResult createResult = pubNub.dataSync().entity()
                .create(entityClass, entityClassVersion)
                .sync();

        final String generatedId = createResult.getData().getId();
        assertFalse(generatedId.trim().isEmpty());

        // cleanup
        pubNub.dataSync().entity().delete(generatedId).sync();
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteEntity() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("custom", "value");

        // create
        pubNub.dataSync().entity()
                .create(entityClass, entityClassVersion)
                .entityId(entityId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            // getAll -> the created entity is present
            final PNGetEntitiesResult getAllResult = pubNub.dataSync().entity()
                    .getAll(entityClass)
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(e -> entityId.equals(e.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNPatchEntityResult patchResult = pubNub.dataSync().entity()
                    .patch(entityId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // get reflects the patched status
            assertEquals("inactive", pubNub.dataSync().entity().get(entityId).sync().getData().getStatus());

            // update -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("custom", "updated");
            final PNUpdateEntityResult updateResult = pubNub.dataSync().entity()
                    .update(entityId, entityClassVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("updated", updateResult.getData().getPayload().get("custom"));

            // get reflects the full replacement
            final PNGetEntityResult afterUpdate = pubNub.dataSync().entity().get(entityId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("updated", afterUpdate.getData().getPayload().get("custom"));
        } finally {
            pubNub.dataSync().entity().delete(entityId).sync();
        }
    }
}