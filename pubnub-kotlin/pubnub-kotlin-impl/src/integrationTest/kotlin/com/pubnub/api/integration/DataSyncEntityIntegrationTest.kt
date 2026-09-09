package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncCreateEntityResult
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

/**
 * Integration tests for the DataSync entity API. They depend on a pre-provisioned `TestUser` entity class
 * existing on the keyset (there is no SDK method to create classes — see `scripts/datasync/create-classes.sh`).
 *
 * `TestUser` (SubKey level, version 1) is defined as:
 * ```
 * property     path                    valueKind  filtering  nullable  projections
 * username     /payload/username       string     full       false     __default__, admin
 * email        /payload/email          string     simple     true      admin            (admin-only)
 * status       /status                 string     simple     true      __default__, admin
 * signupDate   /payload/signupDate     date       simple     true      __default__, admin
 * ```
 *
 * Consequences the tests rely on:
 * - `username` is non-nullable → every create/PUT must include it (else `DS-0650`).
 * - `email` is in the `admin` projection only → a `__default__`-projection read hides it, and a
 *   `__default__`-projection write is rejected. Token-authorized writes that include `email` must grant the
 *   entity through `projection = "admin"`.
 * - Under a non-default projection the write guard rejects EVERY payload field not in that projection, so an
 *   `admin`-projected write payload may contain only `admin` fields (no undeclared fields like `hobby`/`custom`).
 * - `signupDate` is `date`-kind → date-only `YYYY-MM-DD` literals (not RFC-3339 datetime).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataSyncEntityIntegrationTest : BaseIntegrationTest() {
    private val className = "TestUser"
    private val classVersion = 1
    private val entityId = "entity-" + randomValue()

    /**
     * Wipes every entity of [className] on the keyset before the suite runs so leftover rows from earlier runs
     * (or a crashed suite) can't skew list/filter assertions. Uses `server` (holds the secretKey), pages through
     * `getEntities` until exhausted, and best-effort removes each id.
     */
    @BeforeAll
    fun cleanupExistingEntities() {
        /*while (true) {
            val page = server.dataSync.getEntities(className = className, limit = 100).sync()
            if (page.data.isEmpty()) {
                break
            }
            page.data.forEach { entity ->
                try {
                    server.dataSync.removeEntity(entity.id).sync()
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
        val createResult: PNDataSyncCreateEntityResult = server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(entityId, createResult.data.id)
        assertEquals(className, createResult.data.className)
        assertEquals(classVersion, createResult.data.classVersion)
        assertNotNull(createResult.data.eTag)
        // expiresAt is a required, server-computed field: proves the server always returns it
        assertTrue(createResult.data.expiresAt.isNotBlank())
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // create again with the same id -> 409 (create is create-only)
        try {
            server.dataSync.createEntity(
                className = className,
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = payload,
            ).sync()
            fail("Expected a 409 when creating an entity with an existing id")
        } catch (e: PubNubException) {
            assertEquals(409, e.statusCode)
        }

        // get
        val getResult = server.dataSync.getEntity(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(className, getResult.data.className)
        assertEquals("active", getResult.data.status)

        // delete
        val pnRemoveEntityResult = server.dataSync.removeEntity(entityId).sync()

        // get after delete -> 404
        try {
            server.dataSync.getEntity(entityId).sync()
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

        // create -> token scoped to `create` on this specific entity id.
        // projection = "admin": the payload writes `email`, which TestUser declares in the `admin`
        // projection only. A default-projection token would be rejected (DS-0202) for writing a field
        // outside its projection, so the create grant must resolve this entity through `admin`.
        // Under a non-default projection the write guard rejects EVERY payload field not in the
        // projection (including uncontrolled fields), so the payload must contain only admin fields —
        // no `hobby`/`custom`, which are not declared on the class.
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, create = true, projection = "admin"))
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        val createResult = client.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        assertEquals(entityId, createResult.data.id)
        assertEquals(className, createResult.data.className)
        assertEquals(classVersion, createResult.data.classVersion)
        assertNotNull(createResult.data.eTag)
        assertEquals(payload.username, createResult.data.payload?.get("username"))
        assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get -> token scoped to `get` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        val getResult = client.dataSync.getEntity(entityId).sync()
        assertEquals(entityId, getResult.data.id)
        assertEquals(className, getResult.data.className)
        assertEquals("active", getResult.data.status)

        // getAll -> token scoped to `get` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        val getAllResult = client.dataSync.getEntities(
            className = className,
            limit = 100,
        ).sync()
        assertTrue(getAllResult.data.any { it.id == entityId })

        // patch -> token scoped to `update` on this specific entity (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, update = true))
        val patchResult = client.dataSync.updateEntity(
            entityId = entityId,
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
            ),
        ).sync()
        assertEquals("inactive", patchResult.data.status)

        // update -> token scoped to `update` on this specific entity (PUT maps to `update`).
        // projection = "admin" for the same reason as create: the new payload writes `email`.
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, update = true, projection = "admin"))
        val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
        val updateResult = client.dataSync.setEntity(
            entityId = entityId,
            classVersion = classVersion,
            status = "archived",
            payload = newPayload,
        ).sync()
        assertEquals("archived", updateResult.data.status)
        assertEquals("Bob", updateResult.data.payload?.get("username"))

        // delete -> token scoped to `delete` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, delete = true))
        client.dataSync.removeEntity(entityId).sync()

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        try {
            client.dataSync.getEntity(entityId).sync()
            fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            assertEquals(404, e.statusCode)
        }
    }

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: DataSyncGrantType) {
        val token = server.grantToken(
            ttl = 60,
            authorizedUserId = UserId(authorizedUUID),
            grants = grants.toList(),
        ).sync().token
        client.setToken(token)
    }

    @Test
    fun getEntitiesReturnsOnlyEntitiesTheTokenCanRead() {
        // Two entities created with `server` (has the secretKey, so no token needed).
        val grantedEntityId = "entity-granted-" + randomValue()
        val ungrantedEntityId = "entity-ungranted-" + randomValue()

        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = grantedEntityId,
            status = "active",
            payload = TestUserPayload(username = "Granted", email = "granted@example.com"),
        ).sync()
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = ungrantedEntityId,
            status = "active",
            payload = TestUserPayload(username = "Ungranted", email = "ungranted@example.com"),
        ).sync()

        // A client on the same keyset as `server` but without the secretKey; it can only authenticate via setToken.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // Grant the client `get` on ONLY one of the two entities.
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(grantedEntityId, get = true))

        try {
            // getEntities with a token that only grants `get` on `grantedEntityId`.
            // The server does NOT reject the whole listing; instead it returns a filtered list containing
            // only the entities the token can read. The ungranted entity is silently omitted rather than
            // leaking to a client that has no permission to read it.
            val getAllResult = client.dataSync.getEntities(className = className, limit = 100).sync()

            assertTrue(
                "Expected the granted entity to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedEntityId },
            )
            assertTrue(
                "The ungranted entity must not leak to a token that cannot read it",
                getAllResult.data.none { it.id == ungrantedEntityId },
            )
        } finally {
            // best-effort cleanup with `server` (secretKey), regardless of what the client could see
            try {
                server.dataSync.removeEntity(grantedEntityId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeEntity(ungrantedEntityId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createWithServerGeneratedId() {
        val createResult = server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            payload = mapOf("username" to "Bob")
        ).sync()

        val generatedId = createResult.data.id
        assertTrue(generatedId.isNotBlank())

        // cleanup
        server.dataSync.removeEntity(generatedId).sync()
    }

    @Test
    fun getBlankEntityIdThrows() {
        try {
            server.dataSync.getEntity("").sync()
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
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            // getAll -> the created entity is present
            val getAllResult = server.dataSync.getEntities(
                className = className,
                limit = 100,
            ).sync()
            assertTrue(getAllResult.data.any { it.id == entityId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateEntity(
                entityId = entityId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            assertEquals("inactive", server.dataSync.getEntity(entityId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.setEntity(
                entityId = entityId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("Bob", updateResult.data.payload?.get("username"))

            // get reflects the full replacement
            val afterUpdate = server.dataSync.getEntity(entityId).sync()
            assertEquals("archived", afterUpdate.data.status)
            assertEquals("Bob", afterUpdate.data.payload?.get("username"))
        } finally {
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() {
        // create
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        val createResult = server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.updateEntity(
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
                server.dataSync.updateEntity(
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
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun getAllBlankEntityClassThrows() {
        try {
            server.dataSync.getEntities("").sync()
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

        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = idA,
            status = "active",
            payload = TestUserPayload(username = userA, email = "alice@example.com"),
        ).sync()
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = idB,
            status = "active",
            payload = TestUserPayload(username = userB, email = "bob@example.com"),
        ).sync()
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = idC,
            status = "active",
            payload = TestUserPayload(username = userC, email = "carol@example.com"),
        ).sync()

        try {
            // filterFast -> exact username equality (double-quoted string literal, per AppContext QL)
            val filtered = server.dataSync.getEntities(
                className = className,
                filterFast = "username == \"$userA\"",
            ).sync()
            val filteredIds = filtered.data.map { it.id }
            assertEquals(setOf(idA), filteredIds.toSet())
            assertTrue("Expected the un-matched entity to be filtered out", !filteredIds.contains(idB))

            // classLevel -> class is defined at the SubKey level, so scoping the list to it still returns the row
            val scoped = server.dataSync.getEntities(
                className = className,
                classLevel = PNDataSyncClassLevel.SUBKEY,
                filterFast = "username == \"$userA\"",
            ).sync()
            assertEquals(setOf(idA), scoped.data.map { it.id }.toSet())

            // filterFast -> prefix match via LIKE with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
            ).sync()
            assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by username (bare property, no direction suffix); a-b-c order
            val sortedDefault = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username")),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            val sorted = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username", ascending = true)),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sorted.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username", ascending = false)),
            ).sync()
            assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one entity at a time
            val firstPage = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username")),
                limit = 1,
            ).sync()
            assertEquals(1, firstPage.data.size)
            assertEquals(idA, firstPage.data.first().id)
            assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            assertEquals(1, secondPage.data.size)
            assertEquals(idB, secondPage.data.first().id)
        } finally {
            server.dataSync.removeEntity(idA).sync()
            server.dataSync.removeEntity(idB).sync()
            server.dataSync.removeEntity(idC).sync()
        }
    }

    @Test
    fun getEntityAppliesProjectionCarriedByTheToken() {
        // A projection is a named, filtered view of a class's fields, carried by the PAM token (not a read
        // parameter). The `TestUser` class declares `email` as belonging to the `admin` projection ONLY, while
        // `username` (and `status`) belong to both `__default__` and `admin`. So the same entity read through an
        // `admin`-projected token exposes `email`, but read through the implicit `__default__` projection hides it.
        val payload = TestUserPayload(
            username = "Alice",
            email = "alice@example.com",
        )
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = payload,
        ).sync()

        // A client on the same keyset as `server` but without the secretKey, so it only sees what its token allows.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        try {
            // admin-projected `get` token -> the `email` (admin-only) field is visible.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true, projection = "admin"))
            val adminView = client.dataSync.getEntity(entityId).sync()
            assertEquals(entityId, adminView.data.id)
            assertEquals("Alice", adminView.data.payload?.get("username"))
            assertEquals(
                "The admin-projected token must expose the admin-only `email` field",
                "alice@example.com",
                adminView.data.payload?.get("email"),
            )

            // no projection -> implicit `__default__` view: `email` is omitted, `username` is still present.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
            val defaultView = client.dataSync.getEntity(entityId).sync()
            assertEquals(entityId, defaultView.data.id)
            assertEquals(
                "The __default__ projection must still expose `username`",
                "Alice",
                defaultView.data.payload?.get("username"),
            )
            assertTrue(
                "The admin-only `email` field must NOT leak through the __default__ projection",
                defaultView.data.payload?.get("email") == null,
            )
        } finally {
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun getAllRangeFilterAndSortOnDateValueKind() {
        // `TestUser.signupDate` is a `date`-valueKind property declared `filtering: "simple"` (Postgres,
        // strongly consistent), so these reads resolve immediately after create — no await/poll needed.
        // This exercises the `date` value kind end-to-end: a range `filterFast` (>=) and a non-string sort,
        // as opposed to the string-only `username` coverage in getAllWithFilterSortLimitAndCursor.
        //
        // The `date` value kind expects a date-only `YYYY-MM-DD` literal on the wire; a full RFC-3339 datetime is
        // rejected with DS-0008 ("not of expected type 'date'").
        val run = randomValue()
        val userPrefix = "user-$run-"
        val idOld = "entity-$run-old"
        val idMid = "entity-$run-mid"
        val idNew = "entity-$run-new"
        val dateOld = "2019-01-01"
        val dateMid = "2021-06-15"
        val dateNew = "2023-12-31"
        val cutoff = "2020-01-01" // excludes idOld, includes idMid and idNew

        // Each row carries a run-unique username (so a LIKE keeps the assertions isolated) and a distinct signupDate.
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = idOld,
            status = "active",
            payload = mapOf("username" to "${userPrefix}old", "email" to "old@example.com", "signupDate" to dateOld),
        ).sync()
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = idMid,
            status = "active",
            payload = mapOf("username" to "${userPrefix}mid", "email" to "mid@example.com", "signupDate" to dateMid),
        ).sync()
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = idNew,
            status = "active",
            payload = mapOf("username" to "${userPrefix}new", "email" to "new@example.com", "signupDate" to dateNew),
        ).sync()

        try {
            // range filterFast -> signupDate >= cutoff keeps idMid and idNew, drops idOld.
            // The username LIKE keeps this run isolated from any other TestUser rows on the shared keyset.
            val ranged = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\" && signupDate >= \"$cutoff\"",
            ).sync()
            assertEquals(setOf(idMid, idNew), ranged.data.map { it.id }.toSet())

            // sort ascending by the date property -> old, mid, new
            val sortedAsc = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("signupDate", ascending = true)),
            ).sync()
            assertEquals(listOf(idOld, idMid, idNew), sortedAsc.data.map { it.id })

            // sort descending -> new, mid, old
            val sortedDesc = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("signupDate", ascending = false)),
            ).sync()
            assertEquals(listOf(idNew, idMid, idOld), sortedDesc.data.map { it.id })
        } finally {
            server.dataSync.removeEntity(idOld).sync()
            server.dataSync.removeEntity(idMid).sync()
            server.dataSync.removeEntity(idNew).sync()
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateEntity(entityId, emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }
}
