package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.datasync.entity.PNCreateEntityResult
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class DataSyncEntityIntegrationTest : BaseIntegrationTest() {
    private val entityClass = "TestUser"
    private val entityClassVersion = 1
    private val entityId = "entity-" + randomValue()

    data class TestUserPayload(
        val username: String,
        val email: String,
        val hobby: String? = null,
        val custom: String? = null,
    )

    @Test
    fun createGetAndDeleteEntity() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult: PNCreateEntityResult = server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(entityId, createResult.data.id)
        assertEquals(entityClass, createResult.data.entityClass)
        assertEquals(entityClassVersion, createResult.data.entityClassVersion)
        assertNotNull(createResult.data.eTag)
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // create again with the same id -> 409 (create is create-only)
        try {
            server.dataSync.entity.create(
                entityClass = entityClass,
                entityClassVersion = entityClassVersion,
                entityId = entityId,
                status = "active",
                payload = payload,
            ).sync()
            fail("Expected a 409 when creating an entity with an existing id")
        } catch (e: PubNubException) {
            assertEquals(409, e.statusCode)
        }

        // get
        val getResult = server.dataSync.entity.get(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(entityClass, getResult.data.entityClass)
        assertEquals("active", getResult.data.status)

        // delete
        val pnRemoveEntityResult = server.dataSync.entity.delete(entityId).sync()

        // get after delete -> 404
        try {
            server.dataSync.entity.get(entityId).sync()
            fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
    }

    @Test
    fun createGetDeletePatchUpdateGetAllEntityWithServerGrantedToken() {
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via setToken.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, create = true))
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult = client.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(entityId, createResult.data.id)
        assertEquals(entityClass, createResult.data.entityClass)
        assertEquals(entityClassVersion, createResult.data.entityClassVersion)
        assertNotNull(createResult.data.eTag)
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get -> token scoped to `get` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        val getResult = client.dataSync.entity.get(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(entityClass, getResult.data.entityClass)
        assertEquals("active", getResult.data.status)

        // getAll -> token scoped to `get` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        val getAllResult = client.dataSync.entity.getAll(
            entityClass = entityClass,
            limit = 100,
        ).sync()
        assertTrue(getAllResult.data.any { it.id == entityId })

        // patch -> token scoped to `update` on this specific entity (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, update = true))
        val patchResult = client.dataSync.entity.patch(
            entityId = entityId,
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
            ),
        ).sync()
        assertEquals("inactive", patchResult.data.status)

        // update -> token scoped to `update` on this specific entity (PUT maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, update = true))
        val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
        val updateResult = client.dataSync.entity.update(
            entityId = entityId,
            entityClassVersion = entityClassVersion,
            status = "archived",
            payload = newPayload,
        ).sync()
        assertEquals("archived", updateResult.data.status)
        assertEquals("Bob", updateResult.data.payload?.get("username"))

        // delete -> token scoped to `delete` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, delete = true))
        client.dataSync.entity.delete(entityId).sync()

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        try {
            client.dataSync.entity.get(entityId).sync()
            fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
    }

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: DataSyncGrantType) {
        val token = server.grantToken(
            ttl = 60,
            authorizedUserId = UserId(authorizedUUID),
            dataSync = grants.toList(),
        ).sync().token
        client.setToken(token)
    }

    @Test
    fun createWithServerGeneratedId() {
        val createResult = server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            payload = mapOf("username" to "Bob")
        ).sync()

        val generatedId = createResult.data.id
        assertTrue(generatedId.isNotBlank())

        // cleanup
        server.dataSync.entity.delete(generatedId).sync()
    }

    @Test
    fun getBlankEntityIdThrows() {
        try {
            server.dataSync.entity.get("").sync()
            fail("Expected validation to reject a blank entityId")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createGetAllPatchUpdateAndDeleteEntity() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
        )
        server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            // getAll -> the created entity is present
            val getAllResult = server.dataSync.entity.getAll(
                entityClass = entityClass,
                limit = 100,
            ).sync()
            assertTrue(getAllResult.data.any { it.id == entityId })

            // patch -> replace /status
            val patchResult = server.dataSync.entity.patch(
                entityId = entityId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            assertEquals("inactive", server.dataSync.entity.get(entityId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.entity.update(
                entityId = entityId,
                entityClassVersion = entityClassVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("Bob", updateResult.data.payload?.get("username"))

            // get reflects the full replacement
            val afterUpdate = server.dataSync.entity.get(entityId).sync()
            assertEquals("archived", afterUpdate.data.status)
            assertEquals("Bob", afterUpdate.data.payload?.get("username"))
        } finally {
            server.dataSync.entity.delete(entityId).sync()
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        val createResult = server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.entity.patch(
                entityId = entityId,
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
                server.dataSync.entity.patch(
                    entityId = entityId,
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
            server.dataSync.entity.delete(entityId).sync()
        }
    }

    @Test
    fun getAllBlankEntityClassThrows() {
        try {
            server.dataSync.entity.getAll("").sync()
            fail("Expected validation to reject a blank entityClass")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_CLASS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getAllWithFilterSortLimitAndCursor() {
        // The shared keyset only has a definition for the pre-provisioned `entityClass` (TestUser), so
        // seed within that class. filter/sort operate on the *payload properties* that the class marks
        // as filterable (bare property names, not `payload.` paths and not system fields). TestUser
        // declares `username` (filtering "full", so it supports LIKE) and `email` (filtering "simple").
        // Each row is tagged with a run-unique username so the assertions stay isolated from any other
        // entities on the keyset, and the usernames sort a < b < c for deterministic ordering.
        val run = randomValue()
        val userA = "user-$run-a"
        val userB = "user-$run-b"
        val userC = "user-$run-c"
        val userPrefix = "user-$run-"
        val idA = "entity-$run-a"
        val idB = "entity-$run-b"
        val idC = "entity-$run-c"

        server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = idA,
            status = "active",
            payload = TestUserPayload(username = userA, email = "alice@example.com"),
        ).sync()
        server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = idB,
            status = "active",
            payload = TestUserPayload(username = userB, email = "bob@example.com"),
        ).sync()
        server.dataSync.entity.create(
            entityClass = entityClass,
            entityClassVersion = entityClassVersion,
            entityId = idC,
            status = "active",
            payload = TestUserPayload(username = userC, email = "carol@example.com"),
        ).sync()

        try {
            // filter -> exact username equality (double-quoted string literal, per AppContext QL)
            val filtered = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username == \"$userA\"",
            ).sync()
            val filteredIds = filtered.data.map { it.id }
            assertEquals(setOf(idA), filteredIds.toSet())
            assertTrue("Expected the un-matched entity to be filtered out", !filteredIds.contains(idB))

            // filterAdvanced -> prefix match via LIKE with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username LIKE \"$userPrefix*\"",
            ).sync()
            assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by username (default direction); this run's rows appear in a-b-c order
            val sortedDefault = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username LIKE \"$userPrefix*\"",
                sort = "username",
            ).sync()
            assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            val sorted = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username LIKE \"$userPrefix*\"",
                sort = "username:asc",
            ).sync()
            assertEquals(listOf(idA, idB, idC), sorted.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username LIKE \"$userPrefix*\"",
                sort = "username:desc",
            ).sync()
            assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one entity at a time
            val firstPage = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username LIKE \"$userPrefix*\"",
                sort = "username",
                limit = 1,
            ).sync()
            assertEquals(1, firstPage.data.size)
            assertEquals(idA, firstPage.data.first().id)
            assertTrue("Expected more pages after the first", firstPage.hasNext)
            assertNotNull(firstPage.next)

            val secondPage = server.dataSync.entity.getAll(
                entityClass = entityClass,
                filter = "username LIKE \"$userPrefix*\"",
                sort = "username",
                limit = 1,
                cursor = firstPage.next,
            ).sync()
            assertEquals(1, secondPage.data.size)
            assertEquals(idB, secondPage.data.first().id)
        } finally {
            server.dataSync.entity.delete(idA).sync()
            server.dataSync.entity.delete(idB).sync()
            server.dataSync.entity.delete(idC).sync()
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.entity.patch(entityId, emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }
}
