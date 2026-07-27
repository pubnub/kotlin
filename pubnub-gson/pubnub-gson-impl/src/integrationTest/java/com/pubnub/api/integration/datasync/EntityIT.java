package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.datasync.entity.PNCreateEntityResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNGetEntityResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class EntityIT extends BaseIntegrationTest {
    private final String entityClass = "User";
    private final int entityClassVersion = 1;
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
}