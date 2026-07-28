package com.pubnub.api.integration

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
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
        val createResult = server.dataSync.entity.create(
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

    /**
     * Same create/get/delete flow as [createGetAndDeleteEntity], but instead of relying on the client's
     * own secretKey, the "server" (the only party holding the secretKey) mints a scoped PAM token for the
     * client's authorized UUID and the client authenticates with it via [PubNub.setToken]. This mirrors the
     * production setup where the client never sees the secretKey.
     */
    @Test
    fun createGetAndDeleteEntityWithServerGrantedToken() {
        // server grants a DataSync token scoped to this client's authorized UUID
        val token = server.grantToken(
            ttl = 60,
            authorizedUUID = pubnub.configuration.userId.value,
            dataSync = listOf(
                DataSyncGrant.entity(
                    entityId,
                    get = true,
                    delete = true,
                    create = true
                ),
            ),
        ).sync().token

        // client authenticates with the server-issued token
        pubnub.setToken(token)

        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
            hobby = "poetry",
            custom = "value",
        )
        val createResult = pubnub.dataSync.entity.create(
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

        // get
        val getResult = pubnub.dataSync.entity.get(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(entityClass, getResult.data.entityClass)
        assertEquals("active", getResult.data.status)

        // delete
        pubnub.dataSync.entity.delete(entityId).sync()

        // get after delete -> 404
        try {
            pubnub.dataSync.entity.get(entityId).sync()
            fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
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
