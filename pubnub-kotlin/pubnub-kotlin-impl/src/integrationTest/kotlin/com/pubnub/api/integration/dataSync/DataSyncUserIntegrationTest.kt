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
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncCreateUserResult
import com.pubnub.test.CommonUtils
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class DataSyncUserIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1
    private val userId = "user-" + CommonUtils.randomValue()

    /**
     * On-demand maintenance, not a test: wipes every user on the keyset, so leftover rows from earlier runs (or a
     * crashed suite) can't skew list/filter assertions. To run it, remove [org.junit.Ignore] and run just this method. Uses
     * `server` (holds the secretKey), pages through `getUsers` and best-effort removes each id; stops when a page is
     * empty or nothing on it could be removed.
     */
    @Ignore("On-demand keyset cleanup; remove @Ignore to run")
    @Test
    fun cleanupExistingUsers() {
        while (true) {
            val page = server.dataSync.getUsers(limit = 100).sync()
            var removed = 0
            page.data.forEach { user ->
                try {
                    server.dataSync.removeUser(user.id).sync()
                    removed++
                } catch (ignored: PubNubException) {
                }
            }
            if (removed == 0) {
                break
            }
        }
    }

    data class TestUserPayload(
        val username: String,
        val email: String,
        val hobby: String? = null,
        val custom: String? = null,
        val name: String? = null, // this is predefined property in User class definition
        val type: String? = null // this is predefined property in User class definition
    )

    @Test
    fun createGetAndDeleteUser() {
        // create (no entityClass -> server defaults it to "User")
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult: PNDataSyncCreateUserResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            Assert.assertEquals(userId, createResult.data.id)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            Assert.assertNotNull(createResult.data.eTag)
            // expiresAt is a required, server-computed field: proves the server always returns it
            Assert.assertTrue(createResult.data.expiresAt.isNotBlank())
            Assert.assertEquals(payload.username, createResult.data.payload?.get("username"))
            Assert.assertEquals(payload.email, createResult.data.payload?.get("email"))

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync.createUser(
                    classVersion = classVersion,
                    userId = userId,
                    status = "active",
                    payload = payload,
                ).sync()
                Assert.fail("Expected a 409 when creating a user with an existing id")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
            }

            // get
            val getResult = server.dataSync.getUser(userId).sync()
            Assert.assertEquals(userId, getResult.data.id)
            Assert.assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeUser(userId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getUser(userId).sync()
                Assert.fail("Expected a 404 after deleting the user")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
            }
        } finally {
            // best-effort cleanup: the happy path already deleted the user, so a 404 here is expected
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createGetAndDeleteUpdatePatchGetAllUsersWithServerGrantedToken() {
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via setToken.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific user id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, create = true))

        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult = client.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        Assert.assertEquals(userId, createResult.data.id)
        Assert.assertEquals(classVersion, createResult.data.classVersion)
        Assert.assertNotNull(createResult.data.eTag)
        Assert.assertEquals(payload.username, createResult.data.payload?.get("username"))
        Assert.assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get -> token scoped to `get` on this specific user
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, get = true))
        val getResult = client.dataSync.getUser(userId).sync()
        Assert.assertEquals(userId, getResult.data.id)
        Assert.assertEquals("active", getResult.data.status)

        // getAll -> token scoped to `get` on this specific user id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, get = true))
        val getAllResult = client.dataSync.getUsers(
            limit = 100,
        ).sync()
        Assert.assertTrue(getAllResult.data.any { it.id == userId })

        // patch -> token scoped to `update` on this specific user (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, update = true))
        val patchResult = client.dataSync.updateUser(
            userId = userId,
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
            ),
        ).sync()
        Assert.assertEquals("inactive", patchResult.data.status)

        // update -> token scoped to `update` on this specific user (PUT maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, update = true))
        val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
        val updateResult = client.dataSync.setUser(
            userId = userId,
            classVersion = classVersion,
            status = "archived",
            payload = newPayload,
        ).sync()
        Assert.assertEquals("archived", updateResult.data.status)
        Assert.assertEquals("Bob", updateResult.data.payload?.get("username"))

        // delete -> token scoped to `delete` on this specific user
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, delete = true))
        client.dataSync.removeUser(userId).sync()

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = userId, get = true))
        try {
            client.dataSync.getUser(userId).sync()
            Assert.fail("Expected a 404 after deleting the user")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
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
    fun getUsersReturnsOnlyUsersTheTokenCanRead() {
        // Two users created with `server` (has the secretKey, so no token needed).
        val grantedUserId = "user-granted-" + CommonUtils.randomValue()
        val ungrantedUserId = "user-ungranted-" + CommonUtils.randomValue()

        server.dataSync.createUser(
            classVersion = classVersion,
            userId = grantedUserId,
            status = "active",
            payload = TestUserPayload(username = "Granted", email = "granted@example.com"),
        ).sync()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = ungrantedUserId,
            status = "active",
            payload = TestUserPayload(username = "Ungranted", email = "ungranted@example.com"),
        ).sync()

        // A client on the same keyset as `server` but without the secretKey; it can only authenticate via setToken.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // Grant the client `get` on ONLY one of the two users.
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.user(name = grantedUserId, get = true))

        try {
            // getUsers with a token that only grants `get` on `grantedUserId`.
            // The server does NOT reject the whole listing; instead it returns a filtered list
            // containing only the users the token can read. The ungranted user is silently omitted
            // rather than leaking to a client that has no permission to read it.
            val getAllResult = client.dataSync.getUsers(limit = 100).sync()

            Assert.assertTrue(
                "Expected the granted user to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedUserId },
            )
            Assert.assertTrue(
                "The ungranted user must not leak to a token that cannot read it",
                getAllResult.data.none { it.id == ungrantedUserId },
            )
        } finally {
            // best-effort cleanup with `server` (secretKey), regardless of what the client could see
            try {
                server.dataSync.removeUser(grantedUserId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeUser(ungrantedUserId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createWithServerGeneratedId() {
        val createResult = server.dataSync.createUser(
            classVersion = classVersion,
            payload = mapOf("username" to "Bob"),
        ).sync()

        val generatedId = createResult.data.id
        try {
            Assert.assertTrue(generatedId.isNotBlank())
        } finally {
            // cleanup
            server.dataSync.removeUser(generatedId).sync()
        }
    }

    @Test
    fun getBlankUserIdThrows() {
        try {
            server.dataSync.getUser("").sync()
            Assert.fail("Expected validation to reject a blank userId")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createGetAllPatchUpdateAndDeleteUser() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            name = "Doe",
            type = "Admin"
        )
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            // getAll -> the created user is present
            val getAllResult = server.dataSync.getUsers(
                limit = 100,
            ).sync()
            Assert.assertTrue(getAllResult.data.any { it.id == userId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateUser(
                userId = userId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            Assert.assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            Assert.assertEquals("inactive", server.dataSync.getUser(userId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.setUser(
                userId = userId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            Assert.assertEquals("archived", updateResult.data.status)
            Assert.assertEquals("Bob", updateResult.data.payload?.get("username"))
            Assert.assertNotEquals(patchResult.data.eTag, updateResult.data.eTag)

            // get reflects the full replacement: `hobby` was not re-sent, so it is gone rather than kept
            val afterUpdate = server.dataSync.getUser(userId).sync()
            Assert.assertEquals("archived", afterUpdate.data.status)
            Assert.assertEquals("Bob", afterUpdate.data.payload?.get("username"))
            Assert.assertFalse(afterUpdate.data.payload.orEmpty().containsKey("hobby"))
            Assert.assertEquals(updateResult.data.eTag, afterUpdate.data.eTag)
        } finally {
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun getUsersWithFilterSortLimitAndCursor() {
        // filter/sort operate on the payload properties the entity class marks as filterable. The built-in
        // `User` class declares `name` and `type` as filterable/sortable — `username`/`email` are *custom*
        // class properties and would be rejected with DS-0005 "Unknown field" on the built-in User class. Each
        // row is tagged with a run-unique `name` so the assertions stay isolated from any other users on the
        // keyset, and the names sort a < b < c for deterministic ordering. `getUsers` takes the typed request
        // args: `classLevel = PNDataSyncClassLevel.GLOBAL` (the level the built-in User class is defined at) and
        // `sort = listOf(PNDataSyncSortField(...))`, and returns a non-null `next: PNDataSyncPage`.
        val run = CommonUtils.randomValue()
        val nameA = "user-$run-a"
        val nameB = "user-$run-b"
        val nameC = "user-$run-c"
        val namePrefix = "user-$run-"
        val idA = "user-$run-id-a"
        val idB = "user-$run-id-b"
        val idC = "user-$run-id-c"

        server.dataSync.createUser(
            classVersion = classVersion,
            userId = idA,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com", name = nameA, type = "Admin"),
        ).sync()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = idB,
            status = "active",
            payload = TestUserPayload(username = "Bob", email = "bob@example.com", name = nameB, type = "Member"),
        ).sync()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = idC,
            status = "active",
            payload = TestUserPayload(username = "Carol", email = "carol@example.com", name = nameC, type = "Admin"),
        ).sync()

        try {
            // filterFast -> exact name equality (double-quoted string literal, per AppContext QL)
            val filtered = server.dataSync.getUsers(
                filterFast = "name == \"$nameA\"",
            ).sync()
            val filteredIds = filtered.data.map { it.id }
            Assert.assertEquals(setOf(idA), filteredIds.toSet())
            Assert.assertTrue("Expected the un-matched user to be filtered out", !filteredIds.contains(idB))

            // filterFast on the other built-in filterable field, `type` -> the two Admin rows, not the Member
            val filteredByType = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\" && type == \"Admin\"",
            ).sync()
            Assert.assertEquals(setOf(idA, idC), filteredByType.data.map { it.id }.toSet())

            // classLevel -> the built-in User class is defined at the Global level, so scoping the list to it
            // still returns the row
            val scoped = server.dataSync.getUsers(
                classLevel = PNDataSyncClassLevel.GLOBAL,
                filterFast = "name == \"$nameA\"",
            ).sync()
            Assert.assertEquals(setOf(idA), scoped.data.map { it.id }.toSet())

            // LIKE prefix match with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
            ).sync()
            Assert.assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by name (default direction); this run's rows appear in a-b-c order
            val sortedDefault = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
            ).sync()
            Assert.assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one user at a time
            val firstPage = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
            ).sync()
            Assert.assertEquals(1, firstPage.data.size)
            Assert.assertEquals(idA, firstPage.data.first().id)
            Assert.assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            Assert.assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            Assert.assertEquals(1, secondPage.data.size)
            Assert.assertEquals(idB, secondPage.data.first().id)
        } finally {
            server.dataSync.removeUser(idA).sync()
            server.dataSync.removeUser(idB).sync()
            server.dataSync.removeUser(idC).sync()
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        val createResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            Assert.assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.updateUser(
                userId = userId,
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
                server.dataSync.updateUser(
                    userId = userId,
                    operations = listOf(
                        PNJsonPatchOperation(op = "replace", path = "/status", value = "archived"),
                    ),
                    ifMatch = originalETag,
                ).sync()
                Assert.fail("Expected a 412 when patching with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
            }
        } finally {
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun setWithIfMatchAndStaleETagThrows412() {
        val createResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com"),
        ).sync()

        try {
            // set with the current eTag -> succeeds and bumps the eTag
            val setResult = server.dataSync.setUser(
                userId = userId,
                classVersion = classVersion,
                status = "inactive",
                payload = TestUserPayload(username = "Bob", email = "bob@example.com"),
                ifMatch = createResult.data.eTag,
            ).sync()
            Assert.assertEquals("inactive", setResult.data.status)
            Assert.assertNotEquals(createResult.data.eTag, setResult.data.eTag)

            // a patch in between moves the eTag on again
            val patchResult = server.dataSync.updateUser(
                userId = userId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "archived")),
                ifMatch = setResult.data.eTag,
            ).sync()
            Assert.assertNotEquals(setResult.data.eTag, patchResult.data.eTag)

            // set with the eTag read before the patch -> 412, and the patched user is left as it was
            try {
                server.dataSync.setUser(
                    userId = userId,
                    classVersion = classVersion,
                    status = "overwritten",
                    payload = TestUserPayload(username = "Carol", email = "carol@example.com"),
                    ifMatch = setResult.data.eTag,
                ).sync()
                Assert.fail("Expected a 412 when setting with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
            }
            val current = server.dataSync.getUser(userId).sync()
            Assert.assertEquals("archived", current.data.status)
            Assert.assertEquals("Bob", current.data.payload?.get("username"))
            Assert.assertEquals(patchResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun removeWithIfMatchOnlyRemovesTheCurrentVersion() {
        val createResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com"),
        ).sync()

        try {
            val patchResult = server.dataSync.updateUser(
                userId = userId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()

            // remove with the pre-patch eTag -> 412, and the user is still there
            try {
                server.dataSync.removeUser(userId, ifMatch = createResult.data.eTag).sync()
                Assert.fail("Expected a 412 when removing with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
            }
            Assert.assertEquals("inactive", server.dataSync.getUser(userId).sync().data.status)

            // remove with the current eTag -> removed
            server.dataSync.removeUser(userId, ifMatch = patchResult.data.eTag).sync()
            try {
                server.dataSync.getUser(userId).sync()
                Assert.fail("Expected a 404 after removing the user")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
            }
        } finally {
            // best-effort cleanup: the happy path already removed the user, so a 404 here is expected
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchPayloadOperationsAreApplied() {
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = TestUserPayload(
                username = "Alice",
                email = "alice@example.com",
                hobby = "poetry",
                custom = "value",
            ),
        ).sync()

        try {
            val patchResult = server.dataSync.updateUser(
                userId = userId,
                operations = listOf(
                    // a passing `test` lets the rest of the patch through
                    PNJsonPatchOperation(op = "test", path = "/payload/username", value = "Alice"),
                    PNJsonPatchOperation(op = "add", path = "/payload/nickname", value = "Ali"),
                    PNJsonPatchOperation(op = "remove", path = "/payload/custom"),
                    PNJsonPatchOperation(op = "copy", from = "/payload/username", path = "/payload/alias"),
                    PNJsonPatchOperation(op = "move", from = "/payload/hobby", path = "/payload/pastime"),
                ),
            ).sync()

            listOf(patchResult.data.payload, server.dataSync.getUser(userId).sync().data.payload).forEach { payload ->
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
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun patchWithFailingTestOpIsAtomic() {
        val createResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com", hobby = "poetry"),
        ).sync()

        try {
            // the `replace` before the failing `test` must be rolled back along with the one after it
            try {
                server.dataSync.updateUser(
                    userId = userId,
                    operations = listOf(
                        PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                        PNJsonPatchOperation(op = "test", path = "/payload/username", value = "Nobody"),
                        PNJsonPatchOperation(op = "replace", path = "/payload/hobby", value = "chess"),
                    ),
                ).sync()
                Assert.fail("Expected DS-0302 (409) when a JSON Patch `test` operation fails")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
            }

            val current = server.dataSync.getUser(userId).sync()
            Assert.assertEquals("active", current.data.status)
            Assert.assertEquals("poetry", current.data.payload?.get("hobby"))
            Assert.assertEquals(createResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateUser(userId, emptyList()).sync()
            Assert.fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }

    // The tests below use `TestSubUser` (scripts/datasync/create-classes.sh): a SubKey-level class that
    // `extends` the built-in Global `User` v1, inherits its `name`/`type` indexes and adds a `simple` `email`.

    @Test
    fun createUserWithSubclassKeepsTheSubclassAcrossReadsAndWrites() {
        val createResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            className = SUBCLASS,
            status = "active",
            payload = mapOf("name" to "Sub", "email" to "sub@example.com"),
        ).sync()

        try {
            // a subclass is always registered at the SubKey level, whatever level its parent is defined at
            Assert.assertEquals(SUBCLASS, createResult.data.className)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            Assert.assertEquals(PNDataSyncClassLevel.SUBKEY.value, createResult.data.classLevel)
            Assert.assertEquals("sub@example.com", createResult.data.payload?.get("email"))

            // readable both as a User and as a plain entity, with the subclass intact
            val getResult = server.dataSync.getUser(userId).sync()
            Assert.assertEquals(SUBCLASS, getResult.data.className)
            Assert.assertEquals(PNDataSyncClassLevel.SUBKEY.value, getResult.data.classLevel)
            Assert.assertEquals(SUBCLASS, server.dataSync.getEntity(userId).sync().data.className)

            // patch and full replace don't take a class, so the instance must stay a TestSubUser
            val patchResult = server.dataSync.updateUser(
                userId = userId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/payload/email", value = "patched@example.com"),
                ),
            ).sync()
            Assert.assertEquals(SUBCLASS, patchResult.data.className)
            Assert.assertEquals("patched@example.com", patchResult.data.payload?.get("email"))

            val setResult = server.dataSync.setUser(
                userId = userId,
                classVersion = classVersion,
                status = "archived",
                payload = mapOf("name" to "Sub", "email" to "set@example.com"),
            ).sync()
            Assert.assertEquals(SUBCLASS, setResult.data.className)
            Assert.assertEquals(PNDataSyncClassLevel.SUBKEY.value, setResult.data.classLevel)
        } finally {
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun getUsersReturnsSubclassInstancesAsPartOfTheUserFamily() {
        // one plain User and one TestSubUser, tagged with a run-unique `name` (inherited index) so the
        // assertions stay isolated from other users on the keyset; "plain" sorts before "sub"
        val run = CommonUtils.randomValue()
        val byName = "name LIKE \"family-$run-*\""
        val plainId = "user-$run-plain"
        val subId = "user-$run-sub"

        server.dataSync.createUser(
            classVersion = classVersion,
            userId = plainId,
            payload = mapOf("name" to "family-$run-plain"),
        ).sync()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = subId,
            className = SUBCLASS,
            payload = mapOf("name" to "family-$run-sub", "email" to "sub@example.com"),
        ).sync()

        try {
            val family = setOf(plainId, subId)

            // no className -> the server defaults to User, which includes every subclass instance
            Assert.assertEquals(family, server.dataSync.getUsers(filterFast = byName).sync().data.map { it.id }.toSet())

            // className = User, with and without its Global level -> still the whole family
            Assert.assertEquals(
                family,
                server.dataSync.getUsers(className = "User", filterFast = byName).sync().data.map { it.id }.toSet(),
            )
            Assert.assertEquals(
                family,
                server.dataSync.getUsers(
                    className = "User",
                    classLevel = PNDataSyncClassLevel.GLOBAL,
                    filterFast = byName,
                ).sync().data.map { it.id }.toSet(),
            )

            // each row reports its own class, not the class the list was scoped to
            val rows = server.dataSync.getUsers(className = "User", filterFast = byName).sync().data
            Assert.assertEquals("User", rows.single { it.id == plainId }.className)
            Assert.assertEquals(SUBCLASS, rows.single { it.id == subId }.className)

            // className = subclass (with and without its SubKey level) -> only the subclass instance
            Assert.assertEquals(
                setOf(subId),
                server.dataSync.getUsers(className = SUBCLASS, filterFast = byName).sync().data.map { it.id }.toSet(),
            )
            Assert.assertEquals(
                setOf(subId),
                server.dataSync.getUsers(
                    className = SUBCLASS,
                    classLevel = PNDataSyncClassLevel.SUBKEY,
                    filterFast = byName,
                ).sync().data.map { it.id }.toSet(),
            )

            // the inherited `name` index sorts across the family
            Assert.assertEquals(
                listOf(plainId, subId),
                server.dataSync.getUsers(
                    filterFast = byName,
                    sort = listOf(PNDataSyncSortField("name")),
                ).sync().data.map { it.id },
            )
        } finally {
            server.dataSync.removeUser(plainId).sync()
            server.dataSync.removeUser(subId).sync()
        }
    }

    @Test
    fun subclassPropertyIsFilterableAndSortableOnlyWhenScopedToTheSubclass() {
        val run = CommonUtils.randomValue()
        val byName = "name LIKE \"email-$run-*\""
        val idA = "user-$run-a"
        val idB = "user-$run-b"

        server.dataSync.createUser(
            classVersion = classVersion,
            userId = idA,
            className = SUBCLASS,
            payload = mapOf("name" to "email-$run-a", "email" to "a-$run@example.com"),
        ).sync()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = idB,
            className = SUBCLASS,
            payload = mapOf("name" to "email-$run-b", "email" to "b-$run@example.com"),
        ).sync()

        try {
            // `email` is declared by TestSubUser, so filtering on it works once the list is scoped to it
            val filtered = server.dataSync.getUsers(
                className = SUBCLASS,
                filterFast = "email == \"a-$run@example.com\"",
            ).sync()
            Assert.assertEquals(setOf(idA), filtered.data.map { it.id }.toSet())

            // ...and so does sorting on it (descending -> b before a)
            val sorted = server.dataSync.getUsers(
                className = SUBCLASS,
                filterFast = byName,
                sort = listOf(PNDataSyncSortField("email", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idB, idA), sorted.data.map { it.id })

            // scoped to User (the default), the allowed fields are User's own: `email` is an unknown field -> 400
            try {
                server.dataSync.getUsers(filterFast = "email == \"a-$run@example.com\"").sync()
                Assert.fail("Expected a 400 when filtering the User family on a subclass-only property")
            } catch (e: PubNubException) {
                Assert.assertEquals(400, e.statusCode)
            }
            try {
                server.dataSync.getUsers(
                    filterFast = byName,
                    sort = listOf(PNDataSyncSortField("email")),
                ).sync()
                Assert.fail("Expected a 400 when sorting the User family on a subclass-only property")
            } catch (e: PubNubException) {
                Assert.assertEquals(400, e.statusCode)
            }
        } finally {
            server.dataSync.removeUser(idA).sync()
            server.dataSync.removeUser(idB).sync()
        }
    }

    @Test
    fun createAndListWithAClassOutsideTheUserFamilyThrows400() {
        // TestNode is a root entity class, not a User subclass -> DS-0006 "is not a subclass of 'User'"
        try {
            server.dataSync.createUser(classVersion = classVersion, userId = userId, className = "TestNode").sync()
            Assert.fail("Expected a 400 when creating a user with a non-User class")
        } catch (e: PubNubException) {
            Assert.assertEquals(400, e.statusCode)
        } finally {
            // best-effort: nothing should have been created
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: PubNubException) {
            }
        }

        try {
            server.dataSync.getUsers(className = "TestNode").sync()
            Assert.fail("Expected a 400 when listing users with a non-User class")
        } catch (e: PubNubException) {
            Assert.assertEquals(400, e.statusCode)
        }
    }

    @Test
    fun subclassAtTheGlobalLevelThrows404() {
        // classLevel is a hard filter and TestSubUser only exists at SubKey -> DS-0100 "class definition not found"
        try {
            server.dataSync.getUsers(className = SUBCLASS, classLevel = PNDataSyncClassLevel.GLOBAL).sync()
            Assert.fail("Expected a 404 when listing a SubKey subclass at the Global level")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
        }

        try {
            server.dataSync.createUser(
                classVersion = classVersion,
                userId = userId,
                className = SUBCLASS,
                classLevel = PNDataSyncClassLevel.GLOBAL,
            ).sync()
            Assert.fail("Expected a 404 when creating a SubKey subclass at the Global level")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
        } finally {
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    private companion object {
        const val SUBCLASS = "TestSubUser"
    }
}
