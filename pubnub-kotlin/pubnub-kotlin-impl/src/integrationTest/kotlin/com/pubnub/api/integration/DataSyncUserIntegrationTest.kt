package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.UserGrant
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.datasync.user.PNCreateUserResult
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DataSyncUserIntegrationTest : BaseIntegrationTest() {
    private val entityClassVersion = 1
    private val userId = "user-" + randomValue()

    data class TestUserPayload(
        val username: String,
        val email: String,
        val hobby: String? = null,
        val custom: String? = null,
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
        val createResult: PNCreateUserResult = server.dataSync.createUser(
            entityClassVersion = entityClassVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            assertEquals(userId, createResult.data.id)
            assertEquals(entityClassVersion, createResult.data.entityClassVersion)
            assertNotNull(createResult.data.eTag)
            assertEquals(payload.username, createResult.data.payload?.get("username"))
            assertEquals(payload.email, createResult.data.payload?.get("email"))

            // create again with the same id -> 409 (create is create-only)
            try {
                server.dataSync.createUser(
                    entityClassVersion = entityClassVersion,
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
            entityClassVersion = entityClassVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(userId, createResult.data.id)
        assertEquals(entityClassVersion, createResult.data.entityClassVersion)
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
        val patchResult = client.dataSync.patchUser(
            userId = userId,
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
            ),
        ).sync()
        assertEquals("inactive", patchResult.data.status)

        // update -> token scoped to `update` on this specific user (PUT maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, UserGrant.id(id = userId, update = true))
        val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
        val updateResult = client.dataSync.updateUser(
            userId = userId,
            entityClassVersion = entityClassVersion,
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
    fun createWithServerGeneratedId() {
        val createResult = server.dataSync.createUser(
            entityClassVersion = entityClassVersion,
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
        )
        server.dataSync.createUser(
            entityClassVersion = entityClassVersion,
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
            val patchResult = server.dataSync.patchUser(
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
            val updateResult = server.dataSync.updateUser(
                userId = userId,
                entityClassVersion = entityClassVersion,
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
    fun patchWithIfMatchAndStaleETagThrows412() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        val createResult = server.dataSync.createUser(
            entityClassVersion = entityClassVersion,
            userId = userId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.patchUser(
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
                server.dataSync.patchUser(
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
            server.dataSync.patchUser(userId, emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }
}
