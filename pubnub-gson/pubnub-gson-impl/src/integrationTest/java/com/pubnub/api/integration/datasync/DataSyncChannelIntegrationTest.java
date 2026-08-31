package com.pubnub.api.integration.datasync;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncClassLevel;
import com.pubnub.api.java.models.consumer.datasync.PNDataSyncSortField;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncCreateChannelResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncGetChannelsResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncSetChannelResult;
import com.pubnub.api.java.models.consumer.datasync.channel.PNDataSyncUpdateChannelResult;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
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

public class DataSyncChannelIntegrationTest extends BaseIntegrationTest {
    private final static int classVersion = 1;
    private final String channelId = "channel-" + RandomStringUtils.random(8, "abcdefgh");

    @Override
    protected void onBefore() {
        server = getServer();
    }

    @Test
    public void createGetAndDeleteChannel() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create (no className -> server defaults it to "Channel")
        final PNDataSyncCreateChannelResult createResult = server.dataSync().createChannel(classVersion)
                .channelId(channelId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            assertNotNull(createResult);
            assertEquals(channelId, createResult.getData().getId());
            assertEquals(classVersion, createResult.getData().getClassVersion());
            // guards the @SerializedName mapping: wire `entityClass` -> `.getClassName()`
            assertEquals("Channel", createResult.getData().getClassName());
            assertNotNull(createResult.getData().getETag());
            assertEquals("Alice", createResult.getData().getPayload().get("username"));
            assertEquals("alice@example.com", createResult.getData().getPayload().get("email"));

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync().createChannel(classVersion)
                        .channelId(channelId)
                        .status("active")
                        .payload(payload)
                        .sync();
                fail("Expected a 409 when creating a channel with an existing id");
            } catch (PubNubException e) {
                assertEquals(409, e.getStatusCode());
            }

            // get
            final PNDataSyncGetChannelResult getResult = server.dataSync().getChannel(channelId).sync();
            assertEquals(channelId, getResult.getData().getId());
            assertEquals("active", getResult.getData().getStatus());

            // delete
            server.dataSync().removeChannel(channelId).sync();

            // get after delete -> 404
            try {
                server.dataSync().getChannel(channelId).sync();
                fail("Expected a 404 after deleting the channel");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            // best-effort cleanup: the happy path already deleted the channel, so a 404 here is expected
            try {
                server.dataSync().removeChannel(channelId).sync();
            } catch (PubNubException ignored) {
                // already deleted
            }
        }
    }

    /**
     * Same create/get/getAll/patch/update/delete flow as {@link #createGetAllPatchUpdateAndDeleteChannel()}, but the
     * "server" (the only party holding the secretKey) mints scoped PAM tokens and the client authenticates with them.
     *
     * <p>O1 PAM probe: a DataSync Channel authorizes under the classic {@code channels} PAM resource type, so the
     * grant is a {@link ChannelGrant} keyed by the channelId (NOT a DataSyncGrant).
     */
    @Test
    public void createGetAndDeleteUpdatePatchGetAllChannelsWithServerGrantedToken() throws PubNubException {
        final com.pubnub.api.java.PubNub client = getAuthorizedClient();
        final String authorizedUUID = client.getConfiguration().getUserId().getValue();

        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create -> token scoped to `create` on this specific channel id
        grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).create());
        final PNDataSyncCreateChannelResult createResult = client.dataSync().createChannel(classVersion)
                .channelId(channelId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            assertNotNull(createResult);
            assertEquals(channelId, createResult.getData().getId());
            assertEquals(classVersion, createResult.getData().getClassVersion());
            assertNotNull(createResult.getData().getETag());
            assertEquals("Alice", createResult.getData().getPayload().get("username"));

            // get -> token scoped to `get` on this specific channel
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).get());
            final PNDataSyncGetChannelResult getResult = client.dataSync().getChannel(channelId).sync();
            assertEquals(channelId, getResult.getData().getId());
            assertEquals("active", getResult.getData().getStatus());

            // getAll -> token scoped to `get` on this specific channel id
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).get());
            final PNDataSyncGetChannelsResult getAllResult = client.dataSync().getChannels()
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(c -> channelId.equals(c.getId())));

            // patch -> token scoped to `update` on this specific channel (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).update());
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateChannelResult patchResult = client.dataSync().updateChannel(channelId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // update -> token scoped to `update` on this specific channel (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).update());
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("username", "Bob");
            newPayload.put("email", "bob@example.com");
            final PNDataSyncSetChannelResult updateResult = client.dataSync().setChannel(channelId, classVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("Bob", updateResult.getData().getPayload().get("username"));

            // delete -> token scoped to `delete` on this specific channel
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).delete());
            client.dataSync().removeChannel(channelId).sync();

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(channelId).get());
            try {
                client.dataSync().getChannel(channelId).sync();
                fail("Expected a 404 after deleting the channel");
            } catch (PubNubException e) {
                assertEquals(404, e.getStatusCode());
            }
        } finally {
            // best-effort cleanup via `server` (holds the secretKey; the client's token may be scoped
            // to the wrong permission at failure time). The happy path already deleted the channel, so
            // a 404 here is expected.
            try {
                server.dataSync().removeChannel(channelId).sync();
            } catch (PubNubException ignored) {
                // already deleted
            }
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

        final PNDataSyncCreateChannelResult createResult = server.dataSync().createChannel(classVersion)
                .payload(payload)
                .sync();

        final String generatedId = createResult.getData().getId();
        try {
            assertFalse(generatedId.trim().isEmpty());
        } finally {
            // cleanup
            server.dataSync().removeChannel(generatedId).sync();
        }
    }

    @Test
    public void createGetAllPatchUpdateAndDeleteChannel() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        server.dataSync().createChannel(classVersion)
                .channelId(channelId)
                .status("active")
                .payload(payload)
                .sync();

        try {
            // getAll -> the created channel is present
            final PNDataSyncGetChannelsResult getAllResult = server.dataSync().getChannels()
                    .limit(100)
                    .sync();
            assertNotNull(getAllResult);
            assertTrue(getAllResult.getData().stream().anyMatch(c -> channelId.equals(c.getId())));

            // patch -> replace /status
            final List<PNJsonPatchOperation> operations = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("inactive").build()
            );
            final PNDataSyncUpdateChannelResult patchResult = server.dataSync().updateChannel(channelId, operations)
                    .sync();
            assertEquals("inactive", patchResult.getData().getStatus());

            // get reflects the patched status
            assertEquals("inactive", server.dataSync().getChannel(channelId).sync().getData().getStatus());

            // update -> full replace of status + payload
            final Map<String, Object> newPayload = new HashMap<>();
            newPayload.put("username", "Bob");
            newPayload.put("email", "bob@example.com");
            final PNDataSyncSetChannelResult updateResult = server.dataSync().setChannel(channelId, classVersion)
                    .status("archived")
                    .payload(newPayload)
                    .sync();
            assertEquals("archived", updateResult.getData().getStatus());
            assertEquals("Bob", updateResult.getData().getPayload().get("username"));

            // get reflects the full replacement
            final PNDataSyncGetChannelResult afterUpdate = server.dataSync().getChannel(channelId).sync();
            assertEquals("archived", afterUpdate.getData().getStatus());
            assertEquals("Bob", afterUpdate.getData().getPayload().get("username"));
        } finally {
            server.dataSync().removeChannel(channelId).sync();
        }
    }

    @Test
    public void patchWithIfMatchAndStaleETagThrows412() throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("username", "Alice");
        payload.put("email", "alice@example.com");

        // create
        final PNDataSyncCreateChannelResult createResult = server.dataSync().createChannel(classVersion)
                .channelId(channelId)
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
            final PNDataSyncUpdateChannelResult patch1 = server.dataSync().updateChannel(channelId, inactiveOps)
                    .ifMatch(originalETag)
                    .sync();
            assertEquals("inactive", patch1.getData().getStatus());
            assertNotEquals(originalETag, patch1.getData().getETag());

            // patch #2 with the now-stale ifMatch -> 412 (optimistic concurrency conflict)
            final List<PNJsonPatchOperation> archivedOps = Collections.singletonList(
                    PNJsonPatchOperation.builder().op("replace").path("/status").value("archived").build()
            );
            try {
                server.dataSync().updateChannel(channelId, archivedOps)
                        .ifMatch(originalETag)
                        .sync();
                fail("Expected a 412 when patching with a stale ifMatch eTag");
            } catch (PubNubException e) {
                assertEquals(412, e.getStatusCode());
            }
        } finally {
            server.dataSync().removeChannel(channelId).sync();
        }
    }

    @Test
    public void getAllWithFilterSortLimitAndCursor() throws PubNubException {
        // filter/sort operate on the payload properties the entity class marks as filterable. The built-in
        // `Channel` class declares `name` as filterable (like the built-in `User` class) -- `username` is a
        // *custom* class property and would be rejected with DS-0005 "Unknown field". Seed three channels with
        // a run-unique `name` so assertions stay isolated from any other channels; the names sort a < b < c.
        // `getChannels()` takes the typed builder args: `classLevel(PNDataSyncClassLevel)` (GLOBAL is the level
        // the built-in Channel class is defined at) and `sort(List<PNDataSyncSortField>)`, and returns a
        // non-null `next` (read `getCursor()`/`isHasNext()`).
        final String run = RandomStringUtils.random(8, "abcdefgh");
        final String nameA = "chan-" + run + "-a";
        final String nameB = "chan-" + run + "-b";
        final String nameC = "chan-" + run + "-c";
        final String namePrefix = "chan-" + run + "-";
        final String idA = "channel-" + run + "-a";
        final String idB = "channel-" + run + "-b";
        final String idC = "channel-" + run + "-c";

        createChannelWithNameAndEmail(idA, nameA, "alice@example.com");
        createChannelWithNameAndEmail(idB, nameB, "bob@example.com");
        createChannelWithNameAndEmail(idC, nameC, "carol@example.com");

        try {
            // filter -> exact name equality
            final PNDataSyncGetChannelsResult filtered = server.dataSync().getChannels()
                    .filter("name == \"" + nameA + "\"")
                    .sync();
            final List<String> filteredIds = filtered.getData().stream()
                    .map(c -> c.getId()).collect(Collectors.toList());
            assertEquals(Collections.singletonList(idA), filteredIds);

            // classLevel -> the built-in Channel class is defined at the Global level
            final PNDataSyncGetChannelsResult scoped = server.dataSync().getChannels()
                    .classLevel(PNDataSyncClassLevel.GLOBAL)
                    .filter("name == \"" + nameA + "\"")
                    .sync();
            assertEquals(Collections.singletonList(idA),
                    scoped.getData().stream().map(c -> c.getId()).collect(Collectors.toList()));

            // sort ascending (default direction)
            final PNDataSyncGetChannelsResult sortedAsc = server.dataSync().getChannels()
                    .filter("name LIKE \"" + namePrefix + "*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("name")))
                    .sync();
            assertEquals(Arrays.asList(idA, idB, idC),
                    sortedAsc.getData().stream().map(c -> c.getId()).collect(Collectors.toList()));

            // sort descending
            final PNDataSyncGetChannelsResult sortedDesc = server.dataSync().getChannels()
                    .filter("name LIKE \"" + namePrefix + "*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("name", false)))
                    .sync();
            assertEquals(Arrays.asList(idC, idB, idA),
                    sortedDesc.getData().stream().map(c -> c.getId()).collect(Collectors.toList()));

            // limit + cursor -> page one channel at a time; `next` is non-null
            final PNDataSyncGetChannelsResult firstPage = server.dataSync().getChannels()
                    .filter("name LIKE \"" + namePrefix + "*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("name")))
                    .limit(1)
                    .sync();
            assertEquals(1, firstPage.getData().size());
            assertEquals(idA, firstPage.getData().get(0).getId());
            assertNotNull(firstPage.getNext());
            assertTrue("Expected more pages after the first", firstPage.getNext().isHasNext());
            assertNotNull(firstPage.getNext().getCursor());

            final PNDataSyncGetChannelsResult secondPage = server.dataSync().getChannels()
                    .filter("name LIKE \"" + namePrefix + "*\"")
                    .sort(Collections.singletonList(new PNDataSyncSortField("name")))
                    .limit(1)
                    .cursor(firstPage.getNext().getCursor())
                    .sync();
            assertEquals(1, secondPage.getData().size());
            assertEquals(idB, secondPage.getData().get(0).getId());
        } finally {
            server.dataSync().removeChannel(idA).sync();
            server.dataSync().removeChannel(idB).sync();
            server.dataSync().removeChannel(idC).sync();
        }
    }

    @Test
    public void createWithClassLevelGlobal() throws PubNubException {
        // classLevel is create-only (not accepted by setChannel). The default `Channel` class is defined at
        // the Global level, so creating with `classLevel(PNDataSyncClassLevel.GLOBAL)` exercises the typed
        // create-only builder setter end to end. (A create at a level where no `Channel` class is provisioned
        // would fail DS-0100 "Entity class definition not found".)
        final Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Alice");
        final PNDataSyncCreateChannelResult createResult = server.dataSync().createChannel(classVersion)
                .channelId(channelId)
                .classLevel(PNDataSyncClassLevel.GLOBAL)
                .payload(payload)
                .sync();

        try {
            assertEquals(channelId, createResult.getData().getId());
            assertEquals(classVersion, createResult.getData().getClassVersion());
            // round-trips: the channel is fetchable after a class-level-scoped create
            assertEquals(channelId, server.dataSync().getChannel(channelId).sync().getData().getId());
        } finally {
            server.dataSync().removeChannel(channelId).sync();
        }
    }

    private void createChannelWithNameAndEmail(String id, String name, String email) throws PubNubException {
        final Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("email", email);
        server.dataSync().createChannel(classVersion)
                .channelId(id)
                .status("active")
                .payload(payload)
                .sync();
    }
}
