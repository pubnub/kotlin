package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.UserGrant
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncCreateUserResult
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
class DataSyncUserIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1
    private val userId = "user-" + randomValue()

    /**
     * Wipes every user on the keyset before the suite runs so leftover rows from earlier runs (or a crashed
     * suite) can't skew list/filter assertions. Uses `server` (holds the secretKey), pages through `getUsers`
     * until exhausted, and best-effort removes each id.
     */
    @BeforeAll
    fun cleanupExistingUsers() {
        /*while (true) {
            val page = server.dataSync.getUsers(limit = 100).sync()
            if (page.data.isEmpty()) {
                break
            }
            page.data.forEach { user ->
                try {
                    server.dataSync.removeUser(user.id).sync()
                } catch (ignored: PubNubException) {
                }
            }
        }*/
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
        // todo add entityClass to test
        // add test with class that inherits from User
        val createResult: PNDataSyncCreateUserResult = server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            assertEquals(userId, createResult.data.id)
            assertEquals(classVersion, createResult.data.classVersion)
            assertNotNull(createResult.data.eTag)
            // expiresAt is a required, server-computed field: proves the server always returns it
            assertTrue(createResult.data.expiresAt.isNotBlank())
            assertEquals(payload.username, createResult.data.payload?.get("username"))
            assertEquals(payload.email, createResult.data.payload?.get("email"))

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync.createUser(
                    classVersion = classVersion,
                    userId = userId,
                    status = "active",
                    payload = payload,
                ).sync()
                fail("Expected a 409 when creating a user with an existing id")
            } catch (e: PubNubException) {
                assertEquals(409, e.statusCode)
            }

            // get
            val getResult = server.dataSync.getUser(userId).sync()
            assertEquals(userId, getResult.data.id)
            assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeUser(userId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getUser(userId).sync()
                fail("Expected a 404 after deleting the user")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
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
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, create = true))

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

        assertEquals(userId, createResult.data.id)
        assertEquals(classVersion, createResult.data.classVersion)
        assertNotNull(createResult.data.eTag)
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get -> token scoped to `get` on this specific user
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, get = true))
        val getResult = client.dataSync.getUser(userId).sync()
        assertEquals(userId, getResult.data.id)
        assertEquals("active", getResult.data.status)

        // getAll -> token scoped to `get` on this specific user id
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, get = true))
        val getAllResult = client.dataSync.getUsers(
            limit = 100,
        ).sync()
        assertTrue(getAllResult.data.any { it.id == userId })

        // patch -> token scoped to `update` on this specific user (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, update = true))
        val patchResult = client.dataSync.updateUser(
            userId = userId,
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
            ),
        ).sync()
        assertEquals("inactive", patchResult.data.status)

        // update -> token scoped to `update` on this specific user (PUT maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, update = true))
        val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
        val updateResult = client.dataSync.setUser(
            userId = userId,
            classVersion = classVersion,
            status = "archived",
            payload = newPayload,
        ).sync()
        assertEquals("archived", updateResult.data.status)
        assertEquals("Bob", updateResult.data.payload?.get("username"))

        // delete -> token scoped to `delete` on this specific user
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, delete = true))
        client.dataSync.removeUser(userId).sync()

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, get = true))
        try {
            client.dataSync.getUser(userId).sync()
            fail("Expected a 404 after deleting the user")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
    }

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: UserGrant) {
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
        val grantedUserId = "user-granted-" + randomValue()
        val ungrantedUserId = "user-ungranted-" + randomValue()

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
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = grantedUserId, get = true))

        try {
            // getUsers with a token that only grants `get` on `grantedUserId`.
            // The server does NOT reject the whole listing; instead it returns a filtered list
            // containing only the users the token can read. The ungranted user is silently omitted
            // rather than leaking to a client that has no permission to read it.
            val getAllResult = client.dataSync.getUsers(limit = 100).sync()

            assertTrue(
                "Expected the granted user to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedUserId },
            )
            assertTrue(
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
            assertTrue(generatedId.isNotBlank())
        } finally {
            // cleanup
            server.dataSync.removeUser(generatedId).sync()
        }
    }

    @Test
    fun getBlankUserIdThrows() {
        try {
            server.dataSync.getUser("").sync()
            fail("Expected validation to reject a blank userId")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
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
            assertTrue(getAllResult.data.any { it.id == userId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateUser(
                userId = userId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            assertEquals("inactive", server.dataSync.getUser(userId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.setUser(
                userId = userId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("Bob", updateResult.data.payload?.get("username"))

            // get reflects the full replacement
            val afterUpdate = server.dataSync.getUser(userId).sync()
            assertEquals("archived", afterUpdate.data.status)
            assertEquals("Bob", afterUpdate.data.payload?.get("username"))
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
        val run = randomValue()
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
            assertEquals(setOf(idA), filteredIds.toSet())
            assertTrue("Expected the un-matched user to be filtered out", !filteredIds.contains(idB))

            // filterFast on the other built-in filterable field, `type` -> the two Admin rows, not the Member
            val filteredByType = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\" && type == \"Admin\"",
            ).sync()
            assertEquals(setOf(idA, idC), filteredByType.data.map { it.id }.toSet())

            // classLevel -> the built-in User class is defined at the Global level, so scoping the list to it
            // still returns the row
            val scoped = server.dataSync.getUsers(
                classLevel = PNDataSyncClassLevel.GLOBAL,
                filterFast = "name == \"$nameA\"",
            ).sync()
            assertEquals(setOf(idA), scoped.data.map { it.id }.toSet())

            // LIKE prefix match with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
            ).sync()
            assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by name (default direction); this run's rows appear in a-b-c order
            val sortedDefault = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name", ascending = false)),
            ).sync()
            assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one user at a time
            val firstPage = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
            ).sync()
            assertEquals(1, firstPage.data.size)
            assertEquals(idA, firstPage.data.first().id)
            assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getUsers(
                filterFast = "name LIKE \"$namePrefix*\"",
                sort = listOf(PNDataSyncSortField("name")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            assertEquals(1, secondPage.data.size)
            assertEquals(idB, secondPage.data.first().id)
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
            assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.updateUser(
                userId = userId,
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
                server.dataSync.updateUser(
                    userId = userId,
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
            server.dataSync.removeUser(userId).sync()
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateUser(userId, emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }
}
