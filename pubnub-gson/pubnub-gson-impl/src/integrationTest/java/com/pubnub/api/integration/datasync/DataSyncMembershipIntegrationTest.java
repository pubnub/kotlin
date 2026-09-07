package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncCreateMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncGetMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncGetMembershipsResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncSetMembershipResult;
import com.pubnub.api.java.models.consumer.datasync.membership.PNDataSyncUpdateMembershipResult;
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

public class DataSyncMembershipIntegrationTest extends BaseIntegrationTest {
    private final static int classVersion = 1;

    @Override
    protected void onBefore() {
        server = getServer();
    }

    private String random() {
        return RandomStringUtils.random(8, "abcdefgh");
    }

    private void createChannel(String channelId) throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Chan-" + channelId);
        server.dataSync().createChannel(classVersion).channelId(channelId).payload(payload).sync();
    }

    private void createUser(String userId) throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("name", "User-" + userId);
        server.dataSync().createUser(classVersion).userId(userId).payload(payload).sync();
    }

    @Test
    public void createGetAndDeleteMembership() throws PubNubException {
        final String run = random();
        final String channelId = "channel-" + run;
        final String userId = "user-" + run;
        final String membershipId = "membership-" + run;
        createChannel(channelId);
        createUser(userId);

        final Map<String, Object> payload = new HashMap<>();
        payload.put("role", "admin");

        try {
            final PNDataSyncCreateMembershipResult createResult = server.dataSync()
                    .createMembership(channelId, userId, classVersion)
                    .membershipId(membershipId)
                    .status("active")
                    .payload(payload)
                    .sync();

            assertNotNull(createResult);
            assertEquals(membershipId, createResult.getData().getId());
            assertEquals(channelId, createResult.getData().getChannelId());
            assertEquals(userId, createResult.getData().getUserId());
            assertEquals(classVersion, createResult.getData().getClassVersion());
            // guards the @SerializedName mapping: wire `relationshipClass` -> `.getClassName()`
            assertEquals("Membership", createResult.getData().getClassName());
            assertNotNull(createResult.getData().getETag());
            assertEquals("admin", createResult.getData().getPayload().get("role"));

            // create again with the SAME id -> 409 (create is create-only)
            try {
                server.dataSync().createMembership(channelId, userId, classVersion)
                        .membershipId(membershipId)
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a membership with an existing id");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // create again with a DIFFERENT id but the SAME (channel, user) pair -> 409 (unique per pair,
            // not just per id). No DS-0801 here: Membership is MANY_TO_MANY, so the conflict is the duplicate
            // pair, not cardinality.
            try {
                server.dataSync().createMembership(channelId, userId, classVersion)
                        .membershipId("membership-" + random())
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a membership for an existing (channel, user) pair");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // get
            final PNDataSyncGetMembershipResult getResult = server.dataSync().getMembership(membershipId).sync();
            assertEquals(membershipId, getResult.getData().getId());
            assertEquals(channelId, getResult.getData().getChannelId());
            assertEquals(userId, getResult.getData().getUserId());
            assertEquals("active", getResult.getData().getStatus());

            // delete
            server.dataSync().removeMembership(membershipId).sync();

            // get after delete -> 404
            try {
                server.dataSync().getMembership(membershipId).sync();
                fail("Expected a 404 after deleting the membership");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveMembership(membershipId);
            bestEffortRemoveChannel(channelId);
            bestEffortRemoveUser(userId);
        }
    }

    @Test
    public void createWithServerGeneratedId() throws PubNubException {
        final String run = random();
        final String channelId = "channel-" + run;
        final String userId = "user-" + run;
        createChannel(channelId);
        createUser(userId);

        String generatedId = null;
        try {
            final PNDataSyncCreateMembershipResult createResult = server.dataSync()
                    .createMembership(channelId, userId, classVersion)
                    .sync();
            generatedId = createResult.getData().getId();
            assertFalse(generatedId.trim().isEmpty());
            assertEquals(channelId, createResult.getData().getChannelId());
            assertEquals(userId, createResult.getData().getUserId());
        } finally {
            bestEffortRemoveMembership(generatedId);
            bestEffortRemoveChannel(channelId);
            bestEffortRemoveUser(userId);
        }
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteMembership() throws PubNubException {
        final String run = random();
        final String channelId = "channel-" + run;
        final String userId = "user-" + run;
        final String membershipId = "membership-" + run;
        createChannel(channelId);
        createUser(userId);

        final Map<String, Object> payload = new HashMap<>();
        payload.put("role", "admin");

        try {
            server.dataSync().createMembership(channelId, userId, classVersion)
                    .membershipId(membershipId)
                    .status("active")
                    .payload(payload)
                    .sync();

            // getAll (filtered to this channel + user) -> the created membership is present
            final PNDataSyncGetMembershipsResult getAllResult = server.dataSync().getMemberships()
                    .channelId(channelId)
                    .userId(userId)
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(m -> membershipId.equals(m.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateMembershipResult patchResult = server.dataSync()
                    .updateMembership(membershipId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());
            assertEquals("inactive", server.dataSync().getMembership(membershipId).sync().getData().getStatus());

            // update -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("role", "member");
            final PNDataSyncSetMembershipResult updateResult = server.dataSync()
                    .setMembership(membershipId, classVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("member", updateResult.getData().getPayload().get("role"));

            final PNDataSyncGetMembershipResult afterUpdate = server.dataSync().getMembership(membershipId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("member", afterUpdate.getData().getPayload().get("role"));
        } finally {
            bestEffortRemoveMembership(membershipId);
            bestEffortRemoveChannel(channelId);
            bestEffortRemoveUser(userId);
        }
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        final String run = random();
        final String channelId = "channel-" + run;
        final String userId = "user-" + run;
        final String membershipId = "membership-" + run;
        createChannel(channelId);
        createUser(userId);

        try {
            final PNDataSyncCreateMembershipResult createResult = server.dataSync()
                    .createMembership(channelId, userId, classVersion)
                    .membershipId(membershipId)
                    .status("active")
                    .sync();

            final String originalETag = createResult.getData().getETag();
            assertNotNull(originalETag);

            final List<PNJsonPatchOperation> inactiveOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateMembershipResult patch1 = server.dataSync()
                    .updateMembership(membershipId, inactiveOps)
                    .ifMatch(originalETag)
                    .sync();
            assertEquals("inactive", patch1.getData().getStatus());
            assertNotEquals(originalETag, patch1.getData().getETag());

            final List<PNJsonPatchOperation> archivedOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("archived").build()
            );
            try {
                server.dataSync().updateMembership(membershipId, archivedOps)
                        .ifMatch(originalETag)
                        .sync();
                fail("Expected a 412 when patching with a stale ifMatch eTag");
            } catch (PubNubException e) {
                assertEquals(412, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveMembership(membershipId);
            bestEffortRemoveChannel(channelId);
            bestEffortRemoveUser(userId);
        }
    }

    @Test
    public void getAllWithFilterSortLimitAndCursor() throws PubNubException {
        // Built-in fields (id, createdAt, updatedAt, status) are always filterable and sortable. Seed three
        // memberships on the same channel (distinct users) tagged with statuses a < b < c.
        final String run = random();
        final String channelId = "channel-" + run;
        createChannel(channelId);

        final String statusA = "st-" + run + "-a";
        final String statusB = "st-" + run + "-b";
        final String statusC = "st-" + run + "-c";
        final String idA = "membership-" + run + "-a";
        final String idB = "membership-" + run + "-b";
        final String idC = "membership-" + run + "-c";
        final String userA = "user-" + run + "-a";
        final String userB = "user-" + run + "-b";
        final String userC = "user-" + run + "-c";

        createUser(userA);
        createUser(userB);
        createUser(userC);
        server.dataSync().createMembership(channelId, userA, classVersion).membershipId(idA).status(statusA).sync();
        server.dataSync().createMembership(channelId, userB, classVersion).membershipId(idB).status(statusB).sync();
        server.dataSync().createMembership(channelId, userC, classVersion).membershipId(idC).status(statusC).sync();

        try {
            // filterFast -> exact status equality
            final PNDataSyncGetMembershipsResult filtered = server.dataSync().getMemberships()
                    .channelId(channelId)
                    .filterFast("status == \"" + statusA + "\"")
                    .sync();
            assertEquals(Collections.singletonList(idA),
                    filtered.getData().stream().map(m -> m.getId()).collect(Collectors.toList()));

            // sort ascending by status -> a-b-c
            final PNDataSyncGetMembershipsResult sortedAsc = server.dataSync().getMemberships()
                    .channelId(channelId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status")))
                    .sync();
            assertEquals(Arrays.asList(idA, idB, idC),
                    sortedAsc.getData().stream().map(m -> m.getId()).collect(Collectors.toList()));

            // sort descending -> c-b-a
            final PNDataSyncGetMembershipsResult sortedDesc = server.dataSync().getMemberships()
                    .channelId(channelId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status", false)))
                    .sync();
            assertEquals(Arrays.asList(idC, idB, idA),
                    sortedDesc.getData().stream().map(m -> m.getId()).collect(Collectors.toList()));

            // limit + cursor -> page one at a time; `next` is non-null
            final PNDataSyncGetMembershipsResult firstPage = server.dataSync().getMemberships()
                    .channelId(channelId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status")))
                    .limit(1)
                    .sync();
            assertEquals(1, firstPage.getData().size());
            assertEquals(idA, firstPage.getData().get(0).getId());
            assertNotNull(firstPage.getNext());
            assertTrue("Expected more pages after the first", firstPage.getNext().isHasNext());
            assertNotNull(firstPage.getNext().getCursor());

            final PNDataSyncGetMembershipsResult secondPage = server.dataSync().getMemberships()
                    .channelId(channelId)
                    .filterFast("status LIKE \"st-" + run + "-*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("status")))
                    .limit(1)
                    .cursor(firstPage.getNext().getCursor())
                    .sync();
            assertEquals(1, secondPage.getData().size());
            assertEquals(idB, secondPage.getData().get(0).getId());
        } finally {
            bestEffortRemoveMembership(idA);
            bestEffortRemoveMembership(idB);
            bestEffortRemoveMembership(idC);
            bestEffortRemoveUser(userA);
            bestEffortRemoveUser(userB);
            bestEffortRemoveUser(userC);
            bestEffortRemoveChannel(channelId);
        }
    }

    /**
     * Same create/get/patch/update/delete flow but the client authenticates with scoped PAM tokens minted by
     * `server`. A DataSync Membership authorizes under the {@code datasync:memberships} PAM resource type, so the
     * grant is a {@link DataSyncGrant#membership(String)} keyed by the membership id.
     */
    @Test
    public void createGetPatchUpdateAndDeleteWithServerGrantedToken() throws PubNubException {
        final String run = random();
        final String channelId = "channel-" + run;
        final String userId = "user-" + run;
        final String membershipId = "membership-" + run;
        createChannel(channelId);
        createUser(userId);

        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        try {
            // create -> `create` on this specific membership id
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(membershipId).create());
            final Map<String, Object> payload = new HashMap<>();
            payload.put("role", "admin");
            final PNDataSyncCreateMembershipResult createResult = client.dataSync()
                    .createMembership(channelId, userId, classVersion)
                    .membershipId(membershipId)
                    .status("active")
                    .payload(payload)
                    .sync();
            assertEquals(membershipId, createResult.getData().getId());
            assertEquals(channelId, createResult.getData().getChannelId());
            assertEquals(userId, createResult.getData().getUserId());

            // get -> `get`
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(membershipId).get());
            final PNDataSyncGetMembershipResult getResult = client.dataSync().getMembership(membershipId).sync();
            assertEquals(membershipId, getResult.getData().getId());
            assertEquals("active", getResult.getData().getStatus());

            // patch -> `update` (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(membershipId).update());
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateMembershipResult patchResult = client.dataSync()
                    .updateMembership(membershipId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // update -> `update` (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(membershipId).update());
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("role", "member");
            final PNDataSyncSetMembershipResult updateResult = client.dataSync()
                    .setMembership(membershipId, classVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("member", updateResult.getData().getPayload().get("role"));

            // delete -> `delete`
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(membershipId).delete());
            client.dataSync().removeMembership(membershipId).sync();

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(membershipId).get());
            try {
                client.dataSync().getMembership(membershipId).sync();
                fail("Expected a 404 after deleting the membership");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            bestEffortRemoveMembership(membershipId);
            bestEffortRemoveChannel(channelId);
            bestEffortRemoveUser(userId);
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

    private void bestEffortRemoveMembership(String membershipId) {
        if (membershipId == null) {
            return;
        }
        try {
            server.dataSync().removeMembership(membershipId).sync();
        } catch (PubNubException ignored) {
            // already deleted / cascaded
        }
    }

    private void bestEffortRemoveChannel(String channelId) {
        try {
            server.dataSync().removeChannel(channelId).sync();
        } catch (PubNubException ignored) {
            // already deleted
        }
    }

    private void bestEffortRemoveUser(String userId) {
        try {
            server.dataSync().removeUser(userId).sync();
        } catch (PubNubException ignored) {
            // already deleted
        }
    }
}