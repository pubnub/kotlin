package com.pubnub.api.integration.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.dataSyncErrorCode
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncCreateEntityResult
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

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
class DataSyncEntityIntegrationTest : BaseIntegrationTest() {
    private val className = "TestUser"
    private val classVersion = 1
    private val entityId = "entity-" + CommonUtils.randomValue()

    /**
     * On-demand maintenance, not a test: wipes every entity of [className] on the keyset, so leftover rows from
     * earlier runs (or a crashed suite) can't skew list/filter assertions. To run it, remove [org.junit.Ignore] and run just
     * this method. Uses `server` (holds the secretKey), pages through `getEntities` and best-effort removes each id;
     * stops when a page is empty or nothing on it could be removed.
     */
    @Ignore("On-demand keyset cleanup; remove @Ignore to run")
    @Test
    fun cleanupExistingEntities() {
        while (true) {
            val page = server.dataSync.getEntities(className = className, limit = 100).sync()
            var removed = 0
            page.data.forEach { entity ->
                try {
                    server.dataSync.removeEntity(entity.id).sync()
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

        Assert.assertEquals(entityId, createResult.data.id)
        Assert.assertEquals(className, createResult.data.className)
        Assert.assertEquals(classVersion, createResult.data.classVersion)
        Assert.assertNotNull(createResult.data.eTag)
        // expiresAt is a required, server-computed field: proves the server always returns it
        Assert.assertTrue(createResult.data.expiresAt.isNotBlank())
        Assert.assertEquals(payload.username, createResult.data.payload?.get("username"))
        Assert.assertEquals(payload.email, createResult.data.payload?.get("email"))

        // create again with the same id -> 409 (create is create-only)
        try {
            server.dataSync.createEntity(
                className = className,
                classVersion = classVersion,
                entityId = entityId,
                status = "active",
                payload = payload,
            ).sync()
            Assert.fail("Expected a 409 when creating an entity with an existing id")
        } catch (e: PubNubException) {
            Assert.assertEquals(409, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_CONFLICT, e.pubnubError)
        }

        // get
        val getResult = server.dataSync.getEntity(entityId).sync()
        Assert.assertEquals(entityId, getResult.data.id)
        Assert.assertEquals(className, getResult.data.className)
        Assert.assertEquals("active", getResult.data.status)

        // delete
        val pnRemoveEntityResult = server.dataSync.removeEntity(entityId).sync()

        // get after delete -> 404
        try {
            server.dataSync.getEntity(entityId).sync()
            Assert.fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
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

        Assert.assertEquals(entityId, createResult.data.id)
        Assert.assertEquals(className, createResult.data.className)
        Assert.assertEquals(classVersion, createResult.data.classVersion)
        Assert.assertNotNull(createResult.data.eTag)
        Assert.assertEquals(payload.username, createResult.data.payload?.get("username"))
        Assert.assertEquals(payload.email, createResult.data.payload?.get("email"))

        // get -> token scoped to `get` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        val getResult = client.dataSync.getEntity(entityId).sync()
        Assert.assertEquals(entityId, getResult.data.id)
        Assert.assertEquals(className, getResult.data.className)
        Assert.assertEquals("active", getResult.data.status)

        // getAll -> token scoped to `get` on this specific entity id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        val getAllResult = client.dataSync.getEntities(
            className = className,
            limit = 100,
        ).sync()
        Assert.assertTrue(getAllResult.data.any { it.id == entityId })

        // patch -> token scoped to `update` on this specific entity (PATCH maps to `update`)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, update = true))
        val patchResult = client.dataSync.updateEntity(
            entityId = entityId,
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
            ),
        ).sync()
        Assert.assertEquals("inactive", patchResult.data.status)

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
        Assert.assertEquals("archived", updateResult.data.status)
        Assert.assertEquals("Bob", updateResult.data.payload?.get("username"))

        // delete -> token scoped to `delete` on this specific entity
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, delete = true))
        client.dataSync.removeEntity(entityId).sync()

        // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
        try {
            client.dataSync.getEntity(entityId).sync()
            Assert.fail("Expected a 404 after deleting the entity")
        } catch (e: PubNubException) {
            Assert.assertEquals(404, e.statusCode)
            Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
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
        val grantedEntityId = "entity-granted-" + CommonUtils.randomValue()
        val ungrantedEntityId = "entity-ungranted-" + CommonUtils.randomValue()

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

            Assert.assertTrue(
                "Expected the granted entity to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedEntityId },
            )
            Assert.assertTrue(
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
        Assert.assertTrue(generatedId.isNotBlank())

        // cleanup
        server.dataSync.removeEntity(generatedId).sync()
    }

    @Test
    fun getBlankEntityIdThrows() {
        try {
            server.dataSync.getEntity("").sync()
            Assert.fail("Expected validation to reject a blank entityId")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
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
            Assert.assertTrue(getAllResult.data.any { it.id == entityId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateEntity(
                entityId = entityId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            Assert.assertEquals("inactive", patchResult.data.status)

            // get reflects the patched status
            Assert.assertEquals("inactive", server.dataSync.getEntity(entityId).sync().data.status)

            // update -> full replace of status + payload
            val newPayload = TestUserPayload(username = "Bob", email = "bob@example.com")
            val updateResult = server.dataSync.setEntity(
                entityId = entityId,
                classVersion = classVersion,
                status = "archived",
                payload = newPayload,
            ).sync()
            Assert.assertEquals("archived", updateResult.data.status)
            Assert.assertEquals("Bob", updateResult.data.payload?.get("username"))
            Assert.assertNotEquals(patchResult.data.eTag, updateResult.data.eTag)

            // get reflects the full replacement: `hobby` was not re-sent, so it is gone rather than kept
            val afterUpdate = server.dataSync.getEntity(entityId).sync()
            Assert.assertEquals("archived", afterUpdate.data.status)
            Assert.assertEquals("Bob", afterUpdate.data.payload?.get("username"))
            Assert.assertFalse(afterUpdate.data.payload.orEmpty().containsKey("hobby"))
            Assert.assertEquals(updateResult.data.eTag, afterUpdate.data.eTag)
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
            Assert.assertNotNull(originalETag)

            // patch #1 with a matching ifMatch -> succeeds and bumps the eTag
            val patch1 = server.dataSync.updateEntity(
                entityId = entityId,
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
                server.dataSync.updateEntity(
                    entityId = entityId,
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
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun setWithIfMatchAndStaleETagThrows412() {
        val createResult = server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com"),
        ).sync()

        try {
            // set with the current eTag -> succeeds and bumps the eTag
            val setResult = server.dataSync.setEntity(
                entityId = entityId,
                classVersion = classVersion,
                status = "inactive",
                payload = TestUserPayload(username = "Bob", email = "bob@example.com"),
                ifMatch = createResult.data.eTag,
            ).sync()
            Assert.assertEquals("inactive", setResult.data.status)
            Assert.assertNotEquals(createResult.data.eTag, setResult.data.eTag)

            // a patch in between moves the eTag on again
            val patchResult = server.dataSync.updateEntity(
                entityId = entityId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "archived")),
                ifMatch = setResult.data.eTag,
            ).sync()
            Assert.assertNotEquals(setResult.data.eTag, patchResult.data.eTag)

            // set with the eTag read before the patch -> 412, and the patched entity is left as it was
            try {
                server.dataSync.setEntity(
                    entityId = entityId,
                    classVersion = classVersion,
                    status = "overwritten",
                    payload = TestUserPayload(username = "Carol", email = "carol@example.com"),
                    ifMatch = setResult.data.eTag,
                ).sync()
                Assert.fail("Expected a 412 when setting with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, e.pubnubError)
            }
            val current = server.dataSync.getEntity(entityId).sync()
            Assert.assertEquals("archived", current.data.status)
            Assert.assertEquals("Bob", current.data.payload?.get("username"))
            Assert.assertEquals(patchResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun removeWithIfMatchOnlyRemovesTheCurrentVersion() {
        val createResult = server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com"),
        ).sync()

        try {
            val patchResult = server.dataSync.updateEntity(
                entityId = entityId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()

            // remove with the pre-patch eTag -> 412, and the entity is still there
            try {
                server.dataSync.removeEntity(entityId, ifMatch = createResult.data.eTag).sync()
                Assert.fail("Expected a 412 when removing with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, e.pubnubError)
            }
            Assert.assertEquals("inactive", server.dataSync.getEntity(entityId).sync().data.status)

            // remove with the current eTag -> removed
            server.dataSync.removeEntity(entityId, ifMatch = patchResult.data.eTag).sync()
            try {
                server.dataSync.getEntity(entityId).sync()
                Assert.fail("Expected a 404 after removing the entity")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
                Assert.assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
            }
        } finally {
            // best-effort cleanup: the happy path already removed the entity, so a 404 here is expected
            try {
                server.dataSync.removeEntity(entityId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchPayloadOperationsAreApplied() {
        // `username` is non-nullable on TestUser, so it is only tested and copied, never moved or removed;
        // the ops that drop a field act on the undeclared `hobby`/`custom`.
        server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = TestUserPayload(
                username = "Alice",
                email = "alice@example.com",
                hobby = "poetry",
                custom = "value",
            ),
        ).sync()

        try {
            val patchResult = server.dataSync.updateEntity(
                entityId = entityId,
                operations = listOf(
                    // a passing `test` lets the rest of the patch through
                    PNJsonPatchOperation(op = "test", path = "/payload/username", value = "Alice"),
                    PNJsonPatchOperation(op = "add", path = "/payload/nickname", value = "Ali"),
                    PNJsonPatchOperation(op = "remove", path = "/payload/custom"),
                    PNJsonPatchOperation(op = "copy", from = "/payload/username", path = "/payload/alias"),
                    PNJsonPatchOperation(op = "move", from = "/payload/hobby", path = "/payload/pastime"),
                ),
            ).sync()

            listOf(patchResult.data.payload, server.dataSync.getEntity(entityId).sync().data.payload).forEach { payload ->
                val fields = payload.orEmpty()
                Assert.assertEquals("Ali", fields["nickname"])
                Assert.assertFalse(fields.containsKey("custom"))
                Assert.assertEquals("Alice", fields["alias"])
                Assert.assertEquals("Alice", fields["username"]) // copy leaves the source in place
                Assert.assertEquals("poetry", fields["pastime"])
                Assert.assertFalse(fields.containsKey("hobby")) // move drops the source
            }
            Assert.assertEquals("active", patchResult.data.status)
        } finally {
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun patchWithFailingTestOpIsAtomic() {
        val createResult = server.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = entityId,
            status = "active",
            payload = TestUserPayload(username = "Alice", email = "alice@example.com", hobby = "poetry"),
        ).sync()

        try {
            // the `replace` before the failing `test` must be rolled back along with the one after it
            try {
                server.dataSync.updateEntity(
                    entityId = entityId,
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

            val current = server.dataSync.getEntity(entityId).sync()
            Assert.assertEquals("active", current.data.status)
            Assert.assertEquals("poetry", current.data.payload?.get("hobby"))
            Assert.assertEquals(createResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun getAllBlankEntityClassThrows() {
        try {
            server.dataSync.getEntities("").sync()
            Assert.fail("Expected validation to reject a blank entityClass")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_CLASS_MISSING, e.pubnubError)
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
        val run = CommonUtils.randomValue()
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
            Assert.assertEquals(setOf(idA), filteredIds.toSet())
            Assert.assertTrue("Expected the un-matched entity to be filtered out", !filteredIds.contains(idB))

            // classLevel -> class is defined at the SubKey level, so scoping the list to it still returns the row
            val scoped = server.dataSync.getEntities(
                className = className,
                classLevel = PNDataSyncClassLevel.SUBKEY,
                filterFast = "username == \"$userA\"",
            ).sync()
            Assert.assertEquals(setOf(idA), scoped.data.map { it.id }.toSet())

            // filterFast -> prefix match via LIKE with a `*` wildcard, capturing all three rows
            val advanced = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
            ).sync()
            Assert.assertEquals(setOf(idA, idB, idC), advanced.data.map { it.id }.toSet())

            // sort -> ascending by username (bare property, no direction suffix); a-b-c order
            val sortedDefault = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username")),
            ).sync()
            Assert.assertEquals(listOf(idA, idB, idC), sortedDefault.data.map { it.id })

            val sorted = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username", ascending = true)),
            ).sync()
            Assert.assertEquals(listOf(idA, idB, idC), sorted.data.map { it.id })

            // sort descending -> the same rows in reverse (c-b-a) order
            val sortedDesc = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page through this run's rows one entity at a time
            val firstPage = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username")),
                limit = 1,
            ).sync()
            Assert.assertEquals(1, firstPage.data.size)
            Assert.assertEquals(idA, firstPage.data.first().id)
            Assert.assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            Assert.assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("username")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            Assert.assertEquals(1, secondPage.data.size)
            Assert.assertEquals(idB, secondPage.data.first().id)
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
            Assert.assertEquals(entityId, adminView.data.id)
            Assert.assertEquals("Alice", adminView.data.payload?.get("username"))
            Assert.assertEquals(
                "The admin-projected token must expose the admin-only `email` field",
                "alice@example.com",
                adminView.data.payload?.get("email"),
            )

            // no projection -> implicit `__default__` view: `email` is omitted, `username` is still present.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(entityId, get = true))
            val defaultView = client.dataSync.getEntity(entityId).sync()
            Assert.assertEquals(entityId, defaultView.data.id)
            Assert.assertEquals(
                "The __default__ projection must still expose `username`",
                "Alice",
                defaultView.data.payload?.get("username"),
            )
            Assert.assertTrue(
                "The admin-only `email` field must NOT leak through the __default__ projection",
                defaultView.data.payload?.get("email") == null,
            )
        } finally {
            server.dataSync.removeEntity(entityId).sync()
        }
    }

    @Test
    fun createEntityUnderProjectionEnforcesWriteGuard() {
        // Negative counterpart to getEntityAppliesProjectionCarriedByTheToken (which covers the READ side) and
        // to the projected happy-path writes in createGetDeletePatchUpdateGetAllEntityWithServerGrantedToken.
        // `TestUser` declares `email` in the `admin` projection ONLY. The server enforces a projection write-guard,
        // so which fields a write may carry is decided by the token's projection:
        //   - under `__default__` the guard is a denylist over declared props: a declared field NOT in
        //     `__default__` (here `email`) is rejected;
        //   - under a NON-default projection (`admin`) the guard is strict keep-only: EVERY payload field not
        //     declared in that projection is rejected, including undeclared/uncontrolled ones (`hobby`/`custom`).
        // Both rejections are DS-0202 -> HTTP 403, pre-commit (nothing is persisted). Asserted as pairs so it
        // proves the projection boundary is *enforced*, not merely that some write failed.
        //
        // The write-guard is enforced only on token-authenticated requests (the backend skips it when there is
        // no PAM token). So these run on the token-authorized `client`; the secretKey `server` would bypass the
        // guard and the write would wrongly succeed.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // negative A: __default__-projected create writing the admin-only `email` -> 403 (DS-0202).
        val defaultRejectedId = "entity-" + CommonUtils.randomValue()
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(defaultRejectedId, create = true))
        try {
            client.dataSync.createEntity(
                className = className,
                classVersion = classVersion,
                entityId = defaultRejectedId,
                status = "active",
                payload = TestUserPayload(username = "Alice", email = "alice@example.com"),
            ).sync()
            Assert.fail("Expected a 403 when writing the admin-only email under the __default__ projection")
        } catch (e: PubNubException) {
            Assert.assertEquals(
                "Writing an admin-only field under __default__ must be rejected by the projection write-guard",
                403,
                e.statusCode,
            )
            Assert.assertEquals(PubNubError.DATASYNC_ACCESS_DENIED, e.pubnubError)
            Assert.assertEquals("DS-0202", e.dataSyncErrorCode())
        }

        // negative B: admin-projected create carrying an UNDECLARED field (`hobby`) -> 403 (DS-0202).
        // Isolates a single variable: `username` is admin-declared (and non-nullable, so required) and `email`
        // is deliberately omitted, leaving `hobby` (uncontrolled) as the only field that can trip the guard.
        // A raw map payload is used so `email` can be left out entirely (TestUserPayload.email is non-null).
        val adminRejectedId = "entity-" + CommonUtils.randomValue()
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(adminRejectedId, create = true, projection = "admin"))
        try {
            client.dataSync.createEntity(
                className = className,
                classVersion = classVersion,
                entityId = adminRejectedId,
                status = "active",
                payload = mapOf("username" to "Alice", "hobby" to "poetry"),
            ).sync()
            Assert.fail("Expected a 403 when writing an undeclared field under the non-default admin projection")
        } catch (e: PubNubException) {
            Assert.assertEquals(
                "An undeclared field under a non-default projection must be rejected by the projection write-guard",
                403,
                e.statusCode,
            )
            Assert.assertEquals(PubNubError.DATASYNC_ACCESS_DENIED, e.pubnubError)
            Assert.assertEquals("DS-0202", e.dataSyncErrorCode())
        }

        // positive leg: admin-projected create carrying ONLY admin-declared fields (`username` + `email`,
        // no undeclared fields) -> succeeds, proving the guard rejects on projection membership, not blanket.
        val acceptedId = "entity-" + CommonUtils.randomValue()
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(acceptedId, create = true, projection = "admin"))
        try {
            val createResult = client.dataSync.createEntity(
                className = className,
                classVersion = classVersion,
                entityId = acceptedId,
                status = "active",
                payload = TestUserPayload(username = "Alice", email = "alice@example.com"),
            ).sync()
            Assert.assertEquals(acceptedId, createResult.data.id)
            Assert.assertEquals("Alice", createResult.data.payload?.get("username"))
            Assert.assertEquals("alice@example.com", createResult.data.payload?.get("email"))
        } finally {
            try {
                server.dataSync.removeEntity(acceptedId).sync()
            } catch (ignored: PubNubException) {
            }
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
        val run = CommonUtils.randomValue()
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
            Assert.assertEquals(setOf(idMid, idNew), ranged.data.map { it.id }.toSet())

            // sort ascending by the date property -> old, mid, new
            val sortedAsc = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("signupDate", ascending = true)),
            ).sync()
            Assert.assertEquals(listOf(idOld, idMid, idNew), sortedAsc.data.map { it.id })

            // sort descending -> new, mid, old
            val sortedDesc = server.dataSync.getEntities(
                className = className,
                filterFast = "username LIKE \"$userPrefix*\"",
                sort = listOf(PNDataSyncSortField("signupDate", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idNew, idMid, idOld), sortedDesc.data.map { it.id })
        } finally {
            server.dataSync.removeEntity(idOld).sync()
            server.dataSync.removeEntity(idMid).sync()
            server.dataSync.removeEntity(idNew).sync()
        }
    }

    @Test
    fun createEntityWithNoDeclaredPropertiesRoundTripsArbitraryPayload() {
        // The `TestNode` class declares NO properties (and no projections) — unlike `TestUser`. This verifies
        // that a class without declared properties still accepts arbitrary payload fields on createEntity and
        // returns them verbatim on get — i.e. undeclared fields are uncontrolled (stored as opaque JSON), not
        // rejected. They are simply not filterable/sortable. Driven through a PAM-token client (not the secretKey
        // `server`) to prove the uncontrolled fields survive a token-authorized write: because `TestNode`
        // declares no projections, a default-projection grant is enough and the projection write-guard (DS-0202)
        // does not apply.
        val nodeClass = "TestNode"
        val run = CommonUtils.randomValue()
        val nodeId = "node-arbitrary-$run"

        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific entity id (default projection).
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(nodeId, create = true))
        val createResult = client.dataSync.createEntity(
            className = nodeClass,
            classVersion = classVersion,
            entityId = nodeId,
            status = "active",
            payload = mapOf(
                "name" to "Node-$run",
                "role" to "admin",
                "custom" to "value",
                "nested" to mapOf("k" to "v"),
                "count" to 7,
            ),
        ).sync()

        try {
            Assert.assertEquals(nodeId, createResult.data.id)
            Assert.assertEquals(nodeClass, createResult.data.className)
            // undeclared fields are echoed back on the create response
            Assert.assertEquals("Node-$run", createResult.data.payload?.get("name"))
            Assert.assertEquals("admin", createResult.data.payload?.get("role"))
            Assert.assertEquals("value", createResult.data.payload?.get("custom"))

            // and they round-trip on a subsequent token-authorized get
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(nodeId, get = true))
            val getResult = client.dataSync.getEntity(nodeId).sync()
            Assert.assertEquals(nodeId, getResult.data.id)
            Assert.assertEquals("Node-$run", getResult.data.payload?.get("name"))
            Assert.assertEquals("admin", getResult.data.payload?.get("role"))
            Assert.assertEquals("value", getResult.data.payload?.get("custom"))
            Assert.assertEquals("active", getResult.data.status)
        } finally {
            try {
                server.dataSync.removeEntity(nodeId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createEntityUnderDefaultProjectionRoundTripsUndeclaredPayloadFields() {
        // Companion to createEntityWithNoDeclaredPropertiesRoundTripsArbitraryPayload, but on `TestUser` — a
        // class that DOES declare properties and projections. This verifies that under the implicit `__default__`
        // projection, undeclared fields (`hobby`, `custom`) pass through the write-guard freely (they are
        // uncontrolled — not class properties), alongside a declared `__default__` field (`username`).
        //
        // Contrast with the token-authorized create in
        // createGetDeletePatchUpdateGetAllEntityWithServerGrantedToken: there the payload writes `email` (an
        // `admin`-only field), which forces an `admin` (non-default) projection — and under a NON-default
        // projection the write-guard rejects EVERY field not in that projection, including undeclared ones. Here
        // the payload deliberately omits `email` so the write stays on `__default__`, where undeclared fields are
        // allowed. `username` is non-nullable, so it must be present (else DS-0650).
        val run = CommonUtils.randomValue()
        val userId = "entity-default-proj-$run"

        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> `create` on this specific entity id, NO projection => implicit `__default__`.
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(userId, create = true))
        val createResult = client.dataSync.createEntity(
            className = className,
            classVersion = classVersion,
            entityId = userId,
            status = "active",
            payload = mapOf(
                "username" to "Alice-$run", // declared, in __default__
                "hobby" to "poetry", // undeclared -> uncontrolled
                "custom" to "value", // undeclared -> uncontrolled
            ),
        ).sync()

        try {
            Assert.assertEquals(userId, createResult.data.id)
            Assert.assertEquals(className, createResult.data.className)
            Assert.assertEquals("Alice-$run", createResult.data.payload?.get("username"))
            // undeclared fields are echoed back on the create response
            Assert.assertEquals("poetry", createResult.data.payload?.get("hobby"))
            Assert.assertEquals("value", createResult.data.payload?.get("custom"))

            // and they round-trip on a subsequent __default__-projection get
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.entity(userId, get = true))
            val getResult = client.dataSync.getEntity(userId).sync()
            Assert.assertEquals(userId, getResult.data.id)
            Assert.assertEquals("Alice-$run", getResult.data.payload?.get("username"))
            Assert.assertEquals("poetry", getResult.data.payload?.get("hobby"))
            Assert.assertEquals("value", getResult.data.payload?.get("custom"))
            // sanity: the admin-only `email` was never written and is absent under __default__
            Assert.assertTrue(
                "email was not written and must not appear under the __default__ projection",
                getResult.data.payload?.get("email") == null,
            )
        } finally {
            try {
                server.dataSync.removeEntity(userId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateEntity(entityId, emptyList()).sync()
            Assert.fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }
}
