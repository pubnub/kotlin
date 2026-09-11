package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncCreateChannelResult
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataSyncChannelIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1
    private val channelId = "channel-" + randomValue()

    /**
     * Wipes every channel on the keyset before the suite runs so leftover rows from earlier runs (or a crashed
     * suite) can't skew list/filter assertions. Uses `server` (holds the secretKey), pages through `getChannels`
     * until exhausted, and best-effort removes each id.
     */
    @BeforeAll
    fun cleanupExistingChannels() {
        /*while (true) {
            val page = server.dataSync.getChannels(limit = 100).sync()
            if (page.data.isEmpty()) {
                break
            }
            page.data.forEach { channel ->
                try {
                    server.dataSync.removeChannel(channel.id).sync()
                } catch (ignored: PubNubException) {
                }
            }
        }*/
    }

    data class TestChannelPayload(
        val username: String? = null,
        val name: String? = null, // this is predefined property in Channel class definition
        val type: String? = null, // this is predefined property in Channel class definition
        val email: String? = null,
        val hobby: String? = null,
        val custom: String? = null,
    )

    @Test
    fun createGetAndDeleteChannel() {
        // create (no className -> server defaults it to "Channel")
        val payload = TestChannelPayload(
            email = "capybara@example.com",
            hobby = "coding",
            custom = "value",
            name = "Capy",
            type = "SDK"
        )
        val createResult: PNDataSyncCreateChannelResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            assertEquals(channelId, createResult.data.id)
            assertEquals(classVersion, createResult.data.classVersion)
            // guards the @SerializedName mapping: wire `entityClass` -> `.className`
            assertEquals("Channel", createResult.data.className)
            assertNotNull(createResult.data.eTag)
            // expiresAt is a required, server-computed field: proves the server always returns it
            assertTrue(createResult.data.expiresAt.isNotBlank())
            assertEquals(payload.username, createResult.data.payload?.get("username"))
            assertEquals(payload.email, createResult.data.payload?.get("email"))

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync.createChannel(
                    classVersion = classVersion,
                    channelId = channelId,
                    status = "active",
                    payload = payload,
                ).sync()
                fail("Expected a 409 when creating a channel with an existing id")
            } catch (e: PubNubException) {
                assertEquals(409, e.statusCode)
            }

            // get
            val getResult = server.dataSync.getChannel(channelId).sync()
            assertEquals(channelId, getResult.data.id)
            assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeChannel(channelId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getChannel(channelId).sync()
                fail("Expected a 404 after deleting the channel")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            // best-effort cleanup: the happy path already deleted the channel, so a 404 here is expected
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createGetAndDeleteUpdatePatchGetAllChannelsWithServerGrantedToken() {
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via setToken.
        // O1 PAM probe: DataSync /channels authorizes via the classic `ChannelGrant` (channels resource type),
        // NOT DataSyncGrant.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific channel id
        grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, create = true))

        val payload = TestChannelPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult = client.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            assertEquals(channelId, createResult.data.id)
            assertEquals(classVersion, createResult.data.classVersion)
            assertNotNull(createResult.data.eTag)
            assertEquals(payload.username, createResult.data.payload?.get("username"))
            assertEquals(payload.email, createResult.data.payload?.get("email"))

            // get -> token scoped to `get` on this specific channel
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, get = true))
            val getResult = client.dataSync.getChannel(channelId).sync()
            assertEquals(channelId, getResult.data.id)
            assertEquals("active", getResult.data.status)

            // getAll -> token scoped to `get` on this specific channel id
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, get = true))
            val getAllResult = client.dataSync.getChannels(
                limit = 100,
            ).sync()
            assertTrue(getAllResult.data.any { it.id == channelId })

            // patch -> token scoped to `update` on this specific channel (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, update = true))
            val patchResult = client.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // update -> token scoped to `update` on this specific channel (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, update = true))
            val newPayload = TestChannelPayload(username = "Bob", email = "bob@example.com")
            val updateResult = client.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("Bob", updateResult.data.payload?.get("username"))

            // delete -> token scoped to `delete` on this specific channel
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, delete = true))
            client.dataSync.removeChannel(channelId).sync()

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = channelId, get = true))
            try {
                client.dataSync.getChannel(channelId).sync()
                fail("Expected a 404 after deleting the channel")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            // best-effort cleanup via `server` (holds the secretKey; the client's token may be scoped
            // to the wrong permission at failure time). The happy path already deleted the channel, so
            // a 404 here is expected.
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: ChannelGrant) {
        val token = server.grantToken(
            ttl = 60,
            authorizedUserId = UserId(authorizedUUID),
            grants = grants.toList(),
        ).sync().token
        client.setToken(token)
    }

    @Test
    fun getChannelsReturnsOnlyChannelsTheTokenCanRead() {
        // Two channels created with `server` (has the secretKey, so no token needed).
        val grantedChannelId = "channel-granted-" + randomValue()
        val ungrantedChannelId = "channel-ungranted-" + randomValue()

        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = grantedChannelId,
            status = "active",
            payload = TestChannelPayload(name = "Granted"),
        ).sync()
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = ungrantedChannelId,
            status = "active",
            payload = TestChannelPayload(name = "Ungranted"),
        ).sync()

        // A client on the same keyset as `server` but without the secretKey; it can only authenticate via setToken.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // Grant the client `get` on ONLY one of the two channels.
        grantAndAuthenticate(client, authorizedUUID, ChannelGrant.name(name = grantedChannelId, get = true))

        try {
            // getChannels with a token that only grants `get` on `grantedChannelId`.
            // The server does NOT reject the whole listing; instead it returns a filtered list containing
            // only the channels the token can read. The ungranted channel is silently omitted rather than
            // leaking to a client that has no permission to read it.
            val getAllResult = client.dataSync.getChannels(limit = 100).sync()

            assertTrue(
                "Expected the granted channel to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedChannelId },
            )
            assertTrue(
                "The ungranted channel must not leak to a token that cannot read it",
                getAllResult.data.none { it.id == ungrantedChannelId },
            )
        } finally {
            // best-effort cleanup with `server` (secretKey), regardless of what the client could see
            try {
                server.dataSync.removeChannel(grantedChannelId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeChannel(ungrantedChannelId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createWithServerGeneratedId() {
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            payload = mapOf("username" to "Bob"),
        ).sync()

        val generatedId = createResult.data.id
        try {
            assertTrue(generatedId.isNotBlank())
        } finally {
            // cleanup
            server.dataSync.removeChannel(generatedId).sync()
        }
    }

    @Test
    fun getBlankChannelIdThrows() {
        try {
            server.dataSync.getChannel("").sync()
            fail("Expected validation to reject a blank channelId")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createGetAllPatchUpdateAndDeleteChannel() {
        // create
        val payload = TestChannelPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
        )
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            // getAll -> the created channel is present
            val getAllResult = server.dataSync.getChannels(
                limit = 100,
            ).sync()
            assertTrue(getAllResult.data.any { it.id == channelId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            assertEquals("inactive", server.dataSync.getChannel(channelId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestChannelPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("Bob", updateResult.data.payload?.get("username"))

            // get reflects the full replacement
            val afterUpdate = server.dataSync.getChannel(channelId).sync()
            assertEquals("archived", afterUpdate.data.status)
            assertEquals("Bob", afterUpdate.data.payload?.get("username"))
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() {
        // create
        val payload = TestChannelPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
                ifMatch = originalETag,
            ).sync()
            assertEquals("inactive", patch1.data.status)
            val newETag = patch1.data.eTag
            assertNotEquals(originalETag, newETag)

            // patch #2 with the now-stale ifMatch -> 412 (optimistic concurrency conflict)
            try {
                server.dataSync.updateChannel(
                    channelId = channelId,
                    operations = listOf(
                        PNJsonPatchOperation(op = "replace", path = "/status", value = "archived"),
                    ),
                    ifMatch = originalETag,
                ).sync()
                fail("Expected a 412 when patching with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                assertEquals(412, e.statusCode)
            }
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateChannel(channelId, emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getAllWithFilterSortLimitAndCursor() {
        // filter/sort operate on the payload properties the entity class marks as filterable. The built-in
        // `Channel` class declares `name` as filterable (like the built-in `User` class) — `username` is a
        // *custom* class property and would be rejected with DS-0005 "Unknown field". Each row is tagged with
        // a run-unique `name` so the assertions stay isolated from any other channels on the keyset, and the
        // names sort a < b < c for deterministic ordering. `getChannels` takes the typed request args:
        // `classLevel = PNDataSyncClassLevel.GLOBAL` (the level the built-in Channel class is defined at, not
        // the raw "SubKey" string) and `sort = listOf(PNDataSyncSortField(...))` (not a "name:desc" string),
        // and returns a non-null `next: PNDataSyncPage` (read `next.cursor` / `next.hasNext`, not flat fields).
        val run = randomValue()
        val nameA = "chan-$run-a"
        val nameB = "chan-$run-b"
        val nameC = "chan-$run-c"
        val namePrefix = "chan-$run-"
        val idA = "channel-$run-a"
        val idB = "channel-$run-b"
        val idC = "channel-$run-c"

        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = idA,
            status = "active",
            payload = TestChannelPayload(name = nameA, email = "alice@example.com"),
        ).sync()
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = idB,
            status = "active",
            payload = TestChannelPayload(name = nameB, email = "bob@example.com"),
        ).sync()
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = idC,
            status = "active",
            payload = TestChannelPayload(name = nameC, email = "carol@example.com"),
        ).sync()

        try {
            // filterFast -> exact name equality (double-quoted string literal, per AppContext QL)
            val filtered = server.dataSync.getChannels(
                filterFast = "name == \"$nameA\"",
            ).sync()
            val filteredIds = filtered.data.map { it.id }
            assertEquals(setOf(idA), filteredIds.toSet())
            assertTrue("Expected the un-matched channel to be filtered out", !filteredIds.contains(idB))

            // classLevel -> the built-in Channel class is defined at the Global level, so scoping the list to
            // it still returns the row
            val scoped = server.dataSync.getChannels(
                classLevel = PNDataSyncClassLevel.GLOBAL,
                filterFast = "name == \"$nameA\"",
            ).sync()
            assertEquals(setOf(idA), scoped.data.map { it.id }.toSet())

            // LIKE prefix match with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
            ).sync()
            assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by name (default direction); this run's rows appear in a-b-c order
            val sortedDefault = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            val sorted = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name", ascending = true)),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sorted.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name", ascending = false)),
            ).sync()
            assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one channel at a time
            val firstPage = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
            ).sync()
            assertEquals(1, firstPage.data.size)
            assertEquals(idA, firstPage.data.first().id)
            assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            assertEquals(1, secondPage.data.size)
            assertEquals(idB, secondPage.data.first().id)
        } finally {
            server.dataSync.removeChannel(idA).sync()
            server.dataSync.removeChannel(idB).sync()
            server.dataSync.removeChannel(idC).sync()
        }
    }

    @Test
    fun createWithClassLevelGlobal() {
        // classLevel is create-only (not accepted by setChannel). The default `Channel` class is defined at
        // the Global level, so creating with `classLevel = PNDataSyncClassLevel.GLOBAL` exercises the typed
        // create-only param end to end. (A create at a level where no `Channel` class is provisioned would
        // fail DS-0100 "Entity class definition not found".)
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            classLevel = PNDataSyncClassLevel.GLOBAL,
            payload = mapOf("name" to "Alice"),
        ).sync()

        try {
            assertEquals(channelId, createResult.data.id)
            assertEquals(classVersion, createResult.data.classVersion)
            // round-trips: the channel is fetchable after a class-level-scoped create
            assertEquals(channelId, server.dataSync.getChannel(channelId).sync().data.id)
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }
}
