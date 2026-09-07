package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncGetRelationshipsResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult;
import com.pubnub.api.java.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Java mirror of {@code DataSyncRelationshipIntegrationTest.kt} — integration coverage for the general
 * {@code /relationships} API. It relies on the same pre-seeded keyset preconditions:
 *
 * <ul>
 *   <li>Entity class {@code TestNode} (v1) — generic entity class used for both ends of every relationship.</li>
 *   <li>Relationship class {@code TestFriendship} (v1) — MANY_TO_MANY, entityAClass = entityBClass =
 *       {@code TestNode}; the happy-path / filtering / paging / PAM class.</li>
 *   <li>Relationship class {@code TestOwnership} (v1) — ONE_TO_ONE, entityAClass = entityBClass =
 *       {@code TestNode}; used only to make DS-0801 (cardinality) reachable.</li>
 * </ul>
 *
 * The SDK has no relationship-class metadata API, so these classes are provisioned out-of-band; the test treats
 * them as preconditions and uses run-unique ids for intra-suite isolation.
 */
public class DataSyncRelationshipIntegrationTest extends BaseIntegrationTest {
    private final static int classVersion = 1;
    private final static String NODE_CLASS = "TestNode";
    private final static String M2M_CLASS = "TestFriendship";
    private final static String ONE_TO_ONE_CLASS = "TestOwnership";

    @Override
    protected void onBefore() {
        server = getServer();
    }

    private String random() {
        return RandomStringUtils.random(8, "abcdefgh");
    }

    private void createNode(String entityId) throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Node-" + entityId);
        server.dataSync().createEntity(NODE_CLASS, classVersion).entityId(entityId).payload(payload).sync();
    }

    @Test
    public void createGetAndDeleteRelationship() throws PubNubException {
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        createNode(entityAId);
        createNode(entityBId);

        final Map<String, Object> payload = new HashMap<>();
        payload.put("role", "admin");

        try {
            final PNDataSyncCreateRelationshipResult createResult = server.dataSync()
                    .createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                    .relationshipId(relationshipId)
                    .status("active")
                    .payload(payload)
                    .sync();

            assertNotNull(createResult);
            assertEquals(relationshipId, createResult.getData().getId());
            assertEquals(entityAId, createResult.getData().getEntityAId());
            assertEquals(entityBId, createResult.getData().getEntityBId());
            assertEquals(classVersion, createResult.getData().getClassVersion());
            // guards the @SerializedName mapping: wire `relationshipClass` -> `.getClassName()`
            assertEquals(M2M_CLASS, createResult.getData().getClassName());
            assertNotNull(createResult.getData().getETag());
            assertEquals("admin", createResult.getData().getPayload().get("role"));

            // DS-0301: create again with the SAME id -> 409 (create is create-only)
            try {
                server.dataSync().createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                        .relationshipId(relationshipId)
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a relationship with an existing id");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // DS-0301: DIFFERENT id, SAME (class, entityA, entityB) pair -> 409. No DS-0801 on a MANY_TO_MANY
            // class: the conflict is the duplicate pair, not cardinality.
            try {
                server.dataSync().createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                        .relationshipId("relationship-" + random())
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a relationship for an existing (class, entityA, entityB) pair");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // get
            final PNDataSyncGetRelationshipResult getResult = server.dataSync().getRelationship(relationshipId).sync();
            assertEquals(relationshipId, getResult.getData().getId());
            assertEquals(entityAId, getResult.getData().getEntityAId());
            assertEquals(entityBId, getResult.getData().getEntityBId());
            assertEquals("active", getResult.getData().getStatus());

            // delete
            server.dataSync().removeRelationship(relationshipId).sync();

            // get after delete -> 404
            try {
                server.dataSync().getRelationship(relationshipId).sync();
                fail("Expected a 404 after deleting the relationship");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveRelationship(relationshipId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
        }
    }

    @Test
    public void createWithServerGeneratedId() throws PubNubException {
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        createNode(entityAId);
        createNode(entityBId);

        String generatedId = null;
        try {
            final PNDataSyncCreateRelationshipResult createResult = server.dataSync()
                    .createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                    .sync();
            generatedId = createResult.getData().getId();
            assertFalse(generatedId.trim().isEmpty());
            assertEquals(entityAId, createResult.getData().getEntityAId());
            assertEquals(entityBId, createResult.getData().getEntityBId());
        } finally {
            bestEffortRemoveRelationship(generatedId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
        }
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteRelationship() throws PubNubException {
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        createNode(entityAId);
        createNode(entityBId);

        final Map<String, Object> payload = new HashMap<>();
        payload.put("role", "admin");

        try {
            server.dataSync().createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                    .relationshipId(relationshipId)
                    .status("active")
                    .payload(payload)
                    .sync();

            // getAll (filtered to this entityA + entityB) -> the created relationship is present
            final PNDataSyncGetRelationshipsResult getAllResult = server.dataSync().getRelationships(M2M_CLASS)
                    .entityAId(entityAId)
                    .entityBId(entityBId)
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(r -> relationshipId.equals(r.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateRelationshipResult patchResult = server.dataSync()
                    .updateRelationship(relationshipId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());
            assertEquals("inactive", server.dataSync().getRelationship(relationshipId).sync().getData().getStatus());

            // set -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("role", "member");
            final PNDataSyncSetRelationshipResult setResult = server.dataSync()
                    .setRelationship(relationshipId, classVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", setResult.getData().getStatus());
            assertEquals("member", setResult.getData().getPayload().get("role"));

            final PNDataSyncGetRelationshipResult afterSet = server.dataSync().getRelationship(relationshipId).sync();
            assertEquals("archived", afterSet.getData().getStatus());
            assertEquals("member", afterSet.getData().getPayload().get("role"));

            // delete
            server.dataSync().removeRelationship(relationshipId).sync();

            // get after delete -> 404
            try {
                server.dataSync().getRelationship(relationshipId).sync();
                fail("Expected a 404 after deleting the relationship");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveRelationship(relationshipId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
        }
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        createNode(entityAId);
        createNode(entityBId);

        try {
            final PNDataSyncCreateRelationshipResult createResult = server.dataSync()
                    .createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                    .relationshipId(relationshipId)
                    .status("active")
                    .sync();

            final String originalETag = createResult.getData().getETag();
            assertNotNull(originalETag);

            final List<PNJsonPatchOperation> inactiveOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateRelationshipResult patch1 = server.dataSync()
                    .updateRelationship(relationshipId, inactiveOps)
                    .ifMatch(originalETag)
                    .sync();
            assertEquals("inactive", patch1.getData().getStatus());
            assertNotEquals(originalETag, patch1.getData().getETag());

            final List<PNJsonPatchOperation> archivedOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("archived").build()
            );
            try {
                server.dataSync().updateRelationship(relationshipId, archivedOps)
                        .ifMatch(originalETag)
                        .sync();
                fail("Expected a 412 when patching with a stale ifMatch eTag");
            } catch (PubNubException e) {
                assertEquals(412, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveRelationship(relationshipId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
        }
    }

    @Test
    public void createWithWrongEntityClassThrowsDs0800() throws PubNubException {
        // DS-0800: entity A's class must match the seeded class's declared entityAClass (TestNode). Point it at a
        // Channel (class `Channel`) -> the backend's checkEntityClass rejects it. Not relationship-only.
        final String run = random();
        final String entityBId = "node-b-" + run;
        final String wrongClassEntityId = "channel-wrongclass-" + run;
        createNode(entityBId);
        final Map<String, Object> chanPayload = new HashMap<>();
        chanPayload.put("name", "WrongClass");
        server.dataSync().createChannel(classVersion).channelId(wrongClassEntityId).payload(chanPayload).sync();

        try {
            server.dataSync().createRelationship(wrongClassEntityId, entityBId, M2M_CLASS, classVersion).sync();
            fail("Expected DS-0800 (400) when entity A's class does not match the relationship class's entityAClass");
        } catch (PubNubException e) {
            assertEquals(400, e.getStatusCode());
        } finally {
            try {
                server.dataSync().removeChannel(wrongClassEntityId).sync();
            } catch (PubNubException ignored) {
                // already deleted
            }
            bestEffortRemoveEntity(entityBId);
        }
    }

    @Test
    public void createSecondOneToOneOnSameEntityThrowsDs0801() throws PubNubException {
        // DS-0801 (cardinality): TestOwnership is ONE_TO_ONE, so entity A may take part in at most one such
        // relationship. A second ONE_TO_ONE on the same entity A must be rejected (409). Effectively
        // relationship-only: /memberships (MANY_TO_MANY) skips the cardinality check.
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String otherBId = "node-b2-" + run;
        final String firstId = "relationship-" + run;
        createNode(entityAId);
        createNode(entityBId);
        createNode(otherBId);

        server.dataSync().createRelationship(entityAId, entityBId, ONE_TO_ONE_CLASS, classVersion)
                .relationshipId(firstId)
                .sync();

        try {
            server.dataSync().createRelationship(entityAId, otherBId, ONE_TO_ONE_CLASS, classVersion)
                    .relationshipId("relationship-" + random())
                    .sync();
            fail("Expected DS-0801 (409) for a second ONE_TO_ONE relationship on the same entity A");
        } catch (PubNubException e) {
            assertEquals(409, e.getStatusCode());
        } finally {
            bestEffortRemoveRelationship(firstId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
            bestEffortRemoveEntity(otherBId);
        }
    }

    @Test
    public void getAllWithFilterSortLimitAndCursor() throws PubNubException {
        // Built-in fields (id, createdAt, updatedAt, status) are always filterable and sortable. Seed three
        // relationships on the same entity A (distinct entity B) tagged with statuses a < b < c.
        final String run = random();
        final String entityAId = "node-a-" + run;
        createNode(entityAId);

        final String statusA = "st-" + run + "-a";
        final String statusB = "st-" + run + "-b";
        final String statusC = "st-" + run + "-c";
        final String idA = "relationship-" + run + "-a";
        final String idB = "relationship-" + run + "-b";
        final String idC = "relationship-" + run + "-c";
        final String bA = "node-" + run + "-a";
        final String bB = "node-" + run + "-b";
        final String bC = "node-" + run + "-c";

        createNode(bA);
        createNode(bB);
        createNode(bC);
        server.dataSync().createRelationship(entityAId, bA, M2M_CLASS, classVersion).relationshipId(idA).status(statusA).sync();
        server.dataSync().createRelationship(entityAId, bB, M2M_CLASS, classVersion).relationshipId(idB).status(statusB).sync();
        server.dataSync().createRelationship(entityAId, bC, M2M_CLASS, classVersion).relationshipId(idC).status(statusC).sync();

        try {
            // filterFast -> exact status equality
            final PNDataSyncGetRelationshipsResult filtered = server.dataSync().getRelationships(M2M_CLASS)
                    .entityAId(entityAId)
                    .filterFast("status == \"" + statusA + "\"")
                    .sync();
            assertEquals(Collections.singletonList(idA),
                    filtered.getData().stream().map(r -> r.getId()).collect(Collectors.toList()));

            // sort ascending by status -> a-b-c
            final PNDataSyncGetRelationshipsResult sortedAsc = server.dataSync().getRelationships(M2M_CLASS)
                    .entityAId(entityAId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status")))
                    .sync();
            assertEquals(Arrays.asList(idA, idB, idC),
                    sortedAsc.getData().stream().map(r -> r.getId()).collect(Collectors.toList()));

            // sort descending -> c-b-a
            final PNDataSyncGetRelationshipsResult sortedDesc = server.dataSync().getRelationships(M2M_CLASS)
                    .entityAId(entityAId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status", false)))
                    .sync();
            assertEquals(Arrays.asList(idC, idB, idA),
                    sortedDesc.getData().stream().map(r -> r.getId()).collect(Collectors.toList()));

            // limit + cursor -> page one at a time; `next` is non-null
            final PNDataSyncGetRelationshipsResult firstPage = server.dataSync().getRelationships(M2M_CLASS)
                    .entityAId(entityAId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status")))
                    .limit(1)
                    .sync();
            assertEquals(1, firstPage.getData().size());
            assertEquals(idA, firstPage.getData().get(0).getId());
            assertNotNull(firstPage.getNext());
            assertTrue("Expected more pages after the first", firstPage.getNext().isHasNext());
            assertNotNull(firstPage.getNext().getCursor());

            final PNDataSyncGetRelationshipsResult secondPage = server.dataSync().getRelationships(M2M_CLASS)
                    .entityAId(entityAId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status")))
                    .limit(1)
                    .cursor(firstPage.getNext().getCursor())
                    .sync();
            assertEquals(1, secondPage.getData().size());
            assertEquals(idB, secondPage.getData().get(0).getId());
        } finally {
            bestEffortRemoveRelationship(idA);
            bestEffortRemoveRelationship(idB);
            bestEffortRemoveRelationship(idC);
            bestEffortRemoveEntity(bA);
            bestEffortRemoveEntity(bB);
            bestEffortRemoveEntity(bC);
            bestEffortRemoveEntity(entityAId);
        }
    }

    /**
     * Same create/get/patch/set/delete flow but the client authenticates with scoped PAM tokens minted by
     * `server`. A DataSync Relationship authorizes under the {@code datasync:relationships} PAM resource type, so
     * the grant is a {@link DataSyncGrant#relationship(String)} keyed by the relationship id.
     */
    @Test
    public void createGetPatchSetAndDeleteWithServerGrantedToken() throws PubNubException {
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        createNode(entityAId);
        createNode(entityBId);

        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        try {
            // create -> `create` on this specific relationship id
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(relationshipId).create());
            final Map<String, Object> payload = new HashMap<>();
            payload.put("role", "admin");
            final PNDataSyncCreateRelationshipResult createResult = client.dataSync()
                    .createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                    .relationshipId(relationshipId)
                    .status("active")
                    .payload(payload)
                    .sync();
            assertEquals(relationshipId, createResult.getData().getId());
            assertEquals(entityAId, createResult.getData().getEntityAId());
            assertEquals(entityBId, createResult.getData().getEntityBId());

            // get -> `get`
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(relationshipId).get());
            final PNDataSyncGetRelationshipResult getResult = client.dataSync().getRelationship(relationshipId).sync();
            assertEquals(relationshipId, getResult.getData().getId());
            assertEquals("active", getResult.getData().getStatus());

            // patch -> `update` (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(relationshipId).update());
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateRelationshipResult patchResult = client.dataSync()
                    .updateRelationship(relationshipId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // set -> `update` (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(relationshipId).update());
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("role", "member");
            final PNDataSyncSetRelationshipResult setResult = client.dataSync()
                    .setRelationship(relationshipId, classVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", setResult.getData().getStatus());
            assertEquals("member", setResult.getData().getPayload().get("role"));

            // delete -> `delete`
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(relationshipId).delete());
            client.dataSync().removeRelationship(relationshipId).sync();

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(relationshipId).get());
            try {
                client.dataSync().getRelationship(relationshipId).sync();
                fail("Expected a 404 after deleting the relationship");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveRelationship(relationshipId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
        }
    }

    /**
     * Reverse-isolation probe: /relationships authorizes against {@code datasync:relationships}. A grant on the
     * SAME id but under {@code datasync:memberships} must NOT authorize a relationship op (namespaces are
     * disjoint). Mirrors the /memberships direction covered in {@code DataSyncMembershipIntegrationTest}.
     */
    @Test
    public void relationshipRejectsAWrongResourceTypeGrant() throws PubNubException {
        final String run = random();
        final String entityAId = "node-a-" + run;
        final String entityBId = "node-b-" + run;
        final String relationshipId = "relationship-" + run;
        createNode(entityAId);
        createNode(entityBId);

        server.dataSync().createRelationship(entityAId, entityBId, M2M_CLASS, classVersion)
                .relationshipId(relationshipId)
                .status("active")
                .sync();

        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        try {
            // `get` granted on the same id but under `datasync:memberships`, not `datasync:relationships`.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(relationshipId).get());
            try {
                client.dataSync().getRelationship(relationshipId).sync();
                fail("Expected a 403: a datasync:memberships grant must not authorize a /relationships op");
            } catch (PubNubException e) {
                assertEquals(403, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveRelationship(relationshipId);
            bestEffortRemoveEntity(entityAId);
            bestEffortRemoveEntity(entityBId);
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

    private void bestEffortRemoveRelationship(String relationshipId) {
        if (relationshipId == null) {
            return;
        }
        try {
            server.dataSync().removeRelationship(relationshipId).sync();
        } catch (PubNubException ignored) {
            // already deleted / cascaded
        }
    }

    private void bestEffortRemoveEntity(String entityId) {
        try {
            server.dataSync().removeEntity(entityId).sync();
        } catch (PubNubException ignored) {
            // already deleted
        }
    }
}