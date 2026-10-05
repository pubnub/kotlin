package com.pubnub.api.integration.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.TokenGrant
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncCreateChannelResult
import com.pubnub.api.models.consumer.datasync.dataSyncErrorCode
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class DataSyncChannelIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1
    private val channelId = "channel-" + CommonUtils.randomValue()

    /**
     * On-demand maintenance, not a test: wipes every channel on the keyset, so leftover rows from earlier runs (or a
     * crashed suite) can't skew list/filter assertions. To run it, remove [org.junit.Ignore] and run just this method. Uses
     * `server` (holds the secretKey), pages through `getChannels` and best-effort removes each id; stops when a page
     * is empty or nothing on it could be removed.
     */
    @Ignore("On-demand keyset cleanup; remove @Ignore to run")
    @Test
    fun cleanupExistingChannels() {
        while (true) {
            val page = server.dataSync.getChannels(limit = 100).sync()
            var removed = 0
            page.data.forEach { channel ->
                try {
                    server.dataSync.removeChannel(channel.id).sync()
                    removed++
                } catch (ignored: PubNubException) {
                }
            }
            if (removed == 0) {
                break
            }
        }
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
            Assert.assertEquals(channelId, createResult.data.id)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            // guards the @SerializedName mapping: wire `entityClass` -> `.className`
            Assert.assertEquals("Channel", createResult.data.className)
            Assert.assertNotNull(createResult.data.eTag)
            // expiresAt is a required, server-computed field: proves the server always returns it
            Assert.assertTrue(createResult.data.expiresAt.isNotBlank())
            Assert.assertEquals(payload.username, createResult.data.payload?.get("username"))
            Assert.assertEquals(payload.email, createResult.data.payload?.get("email"))

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync.createChannel(
                    classVersion = classVersion,
                    channelId = channelId,
                    status = "active",
                    payload = payload,
                ).sync()
                Assert.fail("Expected a 409 when creating a channel with an existing id")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_CONFLICT, e.pubnubError)
            }

            // get
            val getResult = server.dataSync.getChannel(channelId).sync()
            Assert.assertEquals(channelId, getResult.data.id)
            Assert.assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeChannel(channelId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getChannel(channelId).sync()
                Assert.fail("Expected a 404 after deleting the channel")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
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
        // PAM check: DataSync /channels is authorized by the plain `channels` bucket; DataSyncGrant.channel writes its
        // bits there.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific channel id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, create = true))

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
            Assert.assertEquals(channelId, createResult.data.id)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            Assert.assertNotNull(createResult.data.eTag)
            Assert.assertEquals(payload.username, createResult.data.payload?.get("username"))
            Assert.assertEquals(payload.email, createResult.data.payload?.get("email"))

            // get -> token scoped to `get` on this specific channel
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, get = true))
            val getResult = client.dataSync.getChannel(channelId).sync()
            Assert.assertEquals(channelId, getResult.data.id)
            Assert.assertEquals("active", getResult.data.status)

            // getAll -> token scoped to `get` on this specific channel id
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, get = true))
            val getAllResult = client.dataSync.getChannels(
                limit = 100,
            ).sync()
            Assert.assertTrue(getAllResult.data.any { it.id == channelId })

            // patch -> token scoped to `update` on this specific channel (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, update = true))
            val patchResult = client.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            Assert.assertEquals("inactive", patchResult.data.status)

            // update -> token scoped to `update` on this specific channel (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, update = true))
            val newPayload = TestChannelPayload(username = "Bob", email = "bob@example.com")
            val updateResult = client.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            Assert.assertEquals("archived", updateResult.data.status)
            Assert.assertEquals("Bob", updateResult.data.payload?.get("username"))

            // delete -> token scoped to `delete` on this specific channel
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, delete = true))
            client.dataSync.removeChannel(channelId).sync()

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = channelId, get = true))
            try {
                client.dataSync.getChannel(channelId).sync()
                Assert.fail("Expected a 404 after deleting the channel")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
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

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: TokenGrant) {
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
        val grantedChannelId = "channel-granted-" + CommonUtils.randomValue()
        val ungrantedChannelId = "channel-ungranted-" + CommonUtils.randomValue()

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
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.channel(name = grantedChannelId, get = true))

        try {
            // getChannels with a token that only grants `get` on `grantedChannelId`.
            // The server does NOT reject the whole listing; instead it returns a filtered list containing
            // only the channels the token can read. The ungranted channel is silently omitted rather than
            // leaking to a client that has no permission to read it.
            val getAllResult = client.dataSync.getChannels(limit = 100).sync()

            Assert.assertTrue(
                "Expected the granted channel to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedChannelId },
            )
            Assert.assertTrue(
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
            Assert.assertTrue(generatedId.isNotBlank())
        } finally {
            // cleanup
            server.dataSync.removeChannel(generatedId).sync()
        }
    }

    @Test
    fun getBlankChannelIdThrows() {
        try {
            server.dataSync.getChannel("").sync()
            Assert.fail("Expected validation to reject a blank channelId")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
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
            Assert.assertTrue(getAllResult.data.any { it.id == channelId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            Assert.assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            Assert.assertEquals("inactive", server.dataSync.getChannel(channelId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestChannelPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            Assert.assertEquals("archived", updateResult.data.status)
            Assert.assertEquals("Bob", updateResult.data.payload?.get("username"))
            Assert.assertNotEquals(patchResult.data.eTag, updateResult.data.eTag)

            // get reflects the full replacement: `hobby` was not re-sent, so it is gone rather than kept
            val afterUpdate = server.dataSync.getChannel(channelId).sync()
            Assert.assertEquals("archived", afterUpdate.data.status)
            Assert.assertEquals("Bob", afterUpdate.data.payload?.get("username"))
            Assert.assertFalse(afterUpdate.data.payload.orEmpty().containsKey("hobby"))
            Assert.assertEquals(updateResult.data.eTag, afterUpdate.data.eTag)
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
            Assert.assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
                ifMatch = originalETag,
            ).sync()
            Assert.assertEquals("inactive", patch1.data.status)
            val newETag = patch1.data.eTag
            Assert.assertNotEquals(originalETag, newETag)

            // patch #2 with the now-stale ifMatch -> 412 (optimistic concurrency conflict)
            try {
                server.dataSync.updateChannel(
                    channelId = channelId,
                    operations = listOf(
                        PNJsonPatchOperation(op = "replace", path = "/status", value = "archived"),
                    ),
                    ifMatch = originalETag,
                ).sync()
                Assert.fail("Expected a 412 when patching with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, e.pubnubError)
            }
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun setWithIfMatchAndStaleETagThrows412() {
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = TestChannelPayload(username = "Alice", email = "alice@example.com"),
        ).sync()

        try {
            // set with the current eTag -> succeeds and bumps the eTag
            val setResult = server.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "inactive",
                payload = TestChannelPayload(username = "Bob", email = "bob@example.com"),
                ifMatch = createResult.data.eTag,
            ).sync()
            Assert.assertEquals("inactive", setResult.data.status)
            Assert.assertNotEquals(createResult.data.eTag, setResult.data.eTag)

            // a patch in between moves the eTag on again
            val patchResult = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "archived")),
                ifMatch = setResult.data.eTag,
            ).sync()
            Assert.assertNotEquals(setResult.data.eTag, patchResult.data.eTag)

            // set with the eTag read before the patch -> 412, and the patched channel is left as it was
            try {
                server.dataSync.setChannel(
                    channelId = channelId,
                    classVersion = classVersion,
                    status = "overwritten",
                    payload = TestChannelPayload(username = "Carol", email = "carol@example.com"),
                    ifMatch = setResult.data.eTag,
                ).sync()
                Assert.fail("Expected a 412 when setting with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, e.pubnubError)
            }
            val current = server.dataSync.getChannel(channelId).sync()
            Assert.assertEquals("archived", current.data.status)
            Assert.assertEquals("Bob", current.data.payload?.get("username"))
            Assert.assertEquals(patchResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun removeWithIfMatchOnlyRemovesTheCurrentVersion() {
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = TestChannelPayload(username = "Alice", email = "alice@example.com"),
        ).sync()

        try {
            val patchResult = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()

            // remove with the pre-patch eTag -> 412, and the channel is still there
            try {
                server.dataSync.removeChannel(channelId, ifMatch = createResult.data.eTag).sync()
                Assert.fail("Expected a 412 when removing with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, e.pubnubError)
            }
            Assert.assertEquals("inactive", server.dataSync.getChannel(channelId).sync().data.status)

            // remove with the current eTag -> removed
            server.dataSync.removeChannel(channelId, ifMatch = patchResult.data.eTag).sync()
            try {
                server.dataSync.getChannel(channelId).sync()
                Assert.fail("Expected a 404 after removing the channel")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
            }
        } finally {
            // best-effort cleanup: the happy path already removed the channel, so a 404 here is expected
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchPayloadOperationsAreApplied() {
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = TestChannelPayload(
                username = "Alice",
                email = "alice@example.com",
                hobby = "poetry",
                custom = "value",
            ),
        ).sync()

        try {
            val patchResult = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    // a passing `test` lets the rest of the patch through
                    PNJsonPatchOperation(op = "test", path = "/payload/username", value = "Alice"),
                    PNJsonPatchOperation(op = "add", path = "/payload/nickname", value = "Ali"),
                    PNJsonPatchOperation(op = "remove", path = "/payload/custom"),
                    PNJsonPatchOperation(op = "copy", from = "/payload/username", path = "/payload/alias"),
                    PNJsonPatchOperation(op = "move", from = "/payload/hobby", path = "/payload/pastime"),
                ),
            ).sync()

            listOf(patchResult.data.payload, server.dataSync.getChannel(channelId).sync().data.payload).forEach { payload ->
                val fields = payload.orEmpty()
                Assert.assertEquals("Ali", fields["nickname"])
                Assert.assertFalse(fields.containsKey("custom"))
                Assert.assertEquals("Alice", fields["alias"])
                Assert.assertEquals("Alice", fields["username"]) // copy leaves the source in place
                Assert.assertEquals("poetry", fields["pastime"])
                Assert.assertFalse(fields.containsKey("hobby")) // move drops the source
                Assert.assertEquals("alice@example.com", fields["email"]) // untouched by any op
            }
            Assert.assertEquals("active", patchResult.data.status)
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun patchWithFailingTestOpIsAtomic() {
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            status = "active",
            payload = TestChannelPayload(username = "Alice", email = "alice@example.com", hobby = "poetry"),
        ).sync()

        try {
            // the `replace` before the failing `test` must be rolled back along with the one after it
            try {
                server.dataSync.updateChannel(
                    channelId = channelId,
                    operations = listOf(
                        PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                        PNJsonPatchOperation(op = "test", path = "/payload/username", value = "Nobody"),
                        PNJsonPatchOperation(op = "replace", path = "/payload/hobby", value = "chess"),
                    ),
                ).sync()
                Assert.fail("Expected DS-0302 (409) when a JSON Patch `test` operation fails")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_CONFLICT, e.pubnubError)
                Assert.assertEquals("DS-0302", e.dataSyncErrorCode())
            }

            val current = server.dataSync.getChannel(channelId).sync()
            Assert.assertEquals("active", current.data.status)
            Assert.assertEquals("poetry", current.data.payload?.get("hobby"))
            Assert.assertEquals(createResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateChannel(channelId, emptyList()).sync()
            Assert.fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
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
        val run = CommonUtils.randomValue()
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
            Assert.assertEquals(setOf(idA), filteredIds.toSet())
            Assert.assertTrue("Expected the un-matched channel to be filtered out", !filteredIds.contains(idB))

            // classLevel -> the built-in Channel class is defined at the Global level, so scoping the list to
            // it still returns the row
            val scoped = server.dataSync.getChannels(
                classLevel = PNDataSyncClassLevel.GLOBAL,
                filterFast = "name == \"$nameA\"",
            ).sync()
            Assert.assertEquals(setOf(idA), scoped.data.map { it.id }.toSet())

            // LIKE prefix match with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
            ).sync()
            Assert.assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by name (default direction); this run's rows appear in a-b-c order
            val sortedDefault = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
            ).sync()
            Assert.assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            val sorted = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name", ascending = true)),
            ).sync()
            Assert.assertEquals(listOf(idA, idB, idC), sorted.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one channel at a time
            val firstPage = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
            ).sync()
            Assert.assertEquals(1, firstPage.data.size)
            Assert.assertEquals(idA, firstPage.data.first().id)
            Assert.assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            Assert.assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getChannels(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            Assert.assertEquals(1, secondPage.data.size)
            Assert.assertEquals(idB, secondPage.data.first().id)
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
            Assert.assertEquals(channelId, createResult.data.id)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            // round-trips: the channel is fetchable after a class-level-scoped create
            Assert.assertEquals(channelId, server.dataSync.getChannel(channelId).sync().data.id)
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    // The tests below use `TestSubChannel` (scripts/datasync/create-classes.sh): a SubKey-level class that
    // `extends` the built-in Global `Channel` v1, inherits its `name`/`type` indexes and adds a `simple` `topic`.

    @Test
    fun createChannelWithSubclassKeepsTheSubclassAcrossReadsAndWrites() {
        val createResult = server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            className = SUBCLASS,
            status = "active",
            payload = mapOf("name" to "Sub", "topic" to "kotlin"),
        ).sync()

        try {
            // a subclass is always registered at the SubKey level, whatever level its parent is defined at
            Assert.assertEquals(SUBCLASS, createResult.data.className)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            Assert.assertEquals(PNDataSyncClassLevel.SUBKEY.value, createResult.data.classLevel)
            Assert.assertEquals("kotlin", createResult.data.payload?.get("topic"))

            // readable both as a Channel and as a plain entity, with the subclass intact
            val getResult = server.dataSync.getChannel(channelId).sync()
            Assert.assertEquals(SUBCLASS, getResult.data.className)
            Assert.assertEquals(PNDataSyncClassLevel.SUBKEY.value, getResult.data.classLevel)
            Assert.assertEquals(SUBCLASS, server.dataSync.getEntity(channelId).sync().data.className)

            // patch and full replace don't take a class, so the instance must stay a TestSubChannel
            val patchResult = server.dataSync.updateChannel(
                channelId = channelId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/payload/topic", value = "swift"),
                ),
            ).sync()
            Assert.assertEquals(SUBCLASS, patchResult.data.className)
            Assert.assertEquals("swift", patchResult.data.payload?.get("topic"))

            val setResult = server.dataSync.setChannel(
                channelId = channelId,
                classVersion = classVersion,
                status = "archived",
                payload = mapOf("name" to "Sub", "topic" to "java"),
            ).sync()
            Assert.assertEquals(SUBCLASS, setResult.data.className)
            Assert.assertEquals(PNDataSyncClassLevel.SUBKEY.value, setResult.data.classLevel)
        } finally {
            server.dataSync.removeChannel(channelId).sync()
        }
    }

    @Test
    fun getChannelsReturnsSubclassInstancesAsPartOfTheChannelFamily() {
        // one plain Channel and one TestSubChannel, tagged with a run-unique `name` (inherited index) so the
        // assertions stay isolated from other channels on the keyset; "plain" sorts before "sub"
        val run = CommonUtils.randomValue()
        val byName = "name LIKE \"family-$run-*\""
        val plainId = "channel-$run-plain"
        val subId = "channel-$run-sub"

        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = plainId,
            payload = mapOf("name" to "family-$run-plain"),
        ).sync()
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = subId,
            className = SUBCLASS,
            payload = mapOf("name" to "family-$run-sub", "topic" to "kotlin"),
        ).sync()

        try {
            val family = setOf(plainId, subId)

            // no className -> the server defaults to Channel, which includes every subclass instance
            Assert.assertEquals(
                family,
                server.dataSync.getChannels(filterFast = byName).sync().data.map { it.id }.toSet()
            )

            // className = Channel, with and without its Global level -> still the whole family
            Assert.assertEquals(
                family,
                server.dataSync.getChannels(className = "Channel", filterFast = byName).sync().data.map { it.id }
                    .toSet(),
            )
            Assert.assertEquals(
                family,
                server.dataSync.getChannels(
                    className = "Channel",
                    classLevel = PNDataSyncClassLevel.GLOBAL,
                    filterFast = byName,
                ).sync().data.map { it.id }.toSet(),
            )

            // each row reports its own class, not the class the list was scoped to
            val rows = server.dataSync.getChannels(className = "Channel", filterFast = byName).sync().data
            Assert.assertEquals("Channel", rows.single { it.id == plainId }.className)
            Assert.assertEquals(SUBCLASS, rows.single { it.id == subId }.className)

            // className = subclass (with and without its SubKey level) -> only the subclass instance
            Assert.assertEquals(
                setOf(subId),
                server.dataSync.getChannels(className = SUBCLASS, filterFast = byName).sync().data.map { it.id }
                    .toSet(),
            )
            Assert.assertEquals(
                setOf(subId),
                server.dataSync.getChannels(
                    className = SUBCLASS,
                    classLevel = PNDataSyncClassLevel.SUBKEY,
                    filterFast = byName,
                ).sync().data.map { it.id }.toSet(),
            )

            // the inherited `name` index sorts across the family
            Assert.assertEquals(
                listOf(plainId, subId),
                server.dataSync.getChannels(
                    filterFast = byName,
                    sort = listOf(PNDataSyncSortField("name")),
                ).sync().data.map { it.id },
            )
        } finally {
            server.dataSync.removeChannel(plainId).sync()
            server.dataSync.removeChannel(subId).sync()
        }
    }

    @Test
    fun subclassPropertyIsFilterableAndSortableOnlyWhenScopedToTheSubclass() {
        val run = CommonUtils.randomValue()
        val byName = "name LIKE \"topic-$run-*\""
        val idA = "channel-$run-a"
        val idB = "channel-$run-b"

        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = idA,
            className = SUBCLASS,
            payload = mapOf("name" to "topic-$run-a", "topic" to "a-$run"),
        ).sync()
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = idB,
            className = SUBCLASS,
            payload = mapOf("name" to "topic-$run-b", "topic" to "b-$run"),
        ).sync()

        try {
            // `topic` is declared by TestSubChannel, so filtering on it works once the list is scoped to it
            val filtered = server.dataSync.getChannels(
                className = SUBCLASS,
                filterFast = "topic == \"a-$run\"",
            ).sync()
            Assert.assertEquals(setOf(idA), filtered.data.map { it.id }.toSet())

            // ...and so does sorting on it (descending -> b before a)
            val sorted = server.dataSync.getChannels(
                className = SUBCLASS,
                filterFast = byName,
                sort = listOf(PNDataSyncSortField("topic", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idB, idA), sorted.data.map { it.id })

            // scoped to Channel (the default), the allowed fields are Channel's own: `topic` is unknown -> 400
            try {
                server.dataSync.getChannels(filterFast = "topic == \"a-$run\"").sync()
                Assert.fail("Expected a 400 when filtering the Channel family on a subclass-only property")
            } catch (e: PubNubException) {
                Assert.assertEquals(400, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_BAD_REQUEST, e.pubnubError)
            }
            try {
                server.dataSync.getChannels(
                    filterFast = byName,
                    sort = listOf(PNDataSyncSortField("topic")),
                ).sync()
                Assert.fail("Expected a 400 when sorting the Channel family on a subclass-only property")
            } catch (e: PubNubException) {
                Assert.assertEquals(400, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_BAD_REQUEST, e.pubnubError)
            }
        } finally {
            server.dataSync.removeChannel(idA).sync()
            server.dataSync.removeChannel(idB).sync()
        }
    }

    @Test
    fun createAndListWithAClassOutsideTheChannelFamilyThrows400() {
        // TestSubUser extends User, not Channel -> DS-0006 "is not a subclass of 'Channel'"
        try {
            server.dataSync.createChannel(
                classVersion = classVersion,
                channelId = channelId,
                className = "TestSubUser",
            ).sync()
            Assert.fail("Expected a 400 when creating a channel with a User subclass")
        } catch (e: PubNubException) {
            Assert.assertEquals(400, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_BAD_REQUEST, e.pubnubError)
        } finally {
            // best-effort: nothing should have been created
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: PubNubException) {
            }
        }

        try {
            server.dataSync.getChannels(className = "TestSubUser").sync()
            Assert.fail("Expected a 400 when listing channels with a User subclass")
        } catch (e: PubNubException) {
            Assert.assertEquals(400, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_BAD_REQUEST, e.pubnubError)
        }
    }

    @Test
    fun subclassAtTheGlobalLevelThrows404() {
        // classLevel is a hard filter and TestSubChannel only exists at SubKey -> DS-0100 "class definition not found"
        try {
            server.dataSync.getChannels(className = SUBCLASS, classLevel = PNDataSyncClassLevel.GLOBAL).sync()
            Assert.fail("Expected a 404 when listing a SubKey subclass at the Global level")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
        }

        try {
            server.dataSync.createChannel(
                classVersion = classVersion,
                channelId = channelId,
                className = SUBCLASS,
                classLevel = PNDataSyncClassLevel.GLOBAL,
            ).sync()
            Assert.fail("Expected a 404 when creating a SubKey subclass at the Global level")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
        } finally {
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    private companion object {
        const val SUBCLASS = "TestSubChannel"
    }
}
