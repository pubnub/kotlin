package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils.randomValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.jupiter.api.TestInstance

/**
 * Integration coverage for the general `/relationships` API. Unlike [DataSyncMembershipIntegrationTest] (which
 * targets the built-in `Membership` class, a fixed Channel↔User MANY_TO_MANY specialization), this suite uses
 * dedicated relationship classes seeded on the test keyset out-of-band — the SDK has no relationship-class
 * metadata API, so the classes are a **precondition**, provisioned via the metadata API / fixture rather than by
 * the test.
 *
 * ### Pre-seeded relationship classes (keyset preconditions)
 *
 * Two entity classes and two relationship classes are assumed to exist on the keyset. The schemas below are the
 * authoritative definitions provisioned by `scripts/datasync/create-classes.sh` (see that script and its README
 * for the metadata-API payloads). Property declarations decide filter/sort behavior, so they are reproduced here.
 *
 * - Entity class **`TestNode`** (version 1) — a generic entity class used for both sides of every relationship
 *   below (`entityAClass` == `entityBClass` == `TestNode`). **No declared properties** — payload fields like
 *   `name` (used by [withTwoEntities]) are stored and returned as-is but are not filterable/sortable.
 * - Relationship class **`TestFriendship`** (version 1) — cardinality **MANY_TO_MANY**, `entityAClass` =
 *   `TestNode`, `entityBClass` = `TestNode`. Exercises the happy path (create/get/getAll/update/set/remove),
 *   filtering, paging and PAM. Declares two properties:
 *
 *   | Property | Path              | valueKind | filtering  | isNullable | Projections     |
 *   |----------|-------------------|-----------|------------|------------|-----------------|
 *   | `status` | `/status`         | string    | **full**   | true       | none            |
 *   | `secret` | `/payload/secret` | string    | **simple** | true       | **admin** only  |
 *
 * - Relationship class **`TestOwnership`** (version 1) — cardinality **ONE_TO_ONE**, `entityAClass` = `TestNode`,
 *   `entityBClass` = `TestNode`, **no declared properties**. Used only to make **DS-0801** (cardinality conflict)
 *   reachable — a second ONE_TO_ONE relationship on the same entity A must be rejected.
 *
 * ### Filtering tiers (why `status` is queryable two ways)
 *
 * A property's `filtering` mode forms a strict capability hierarchy — `full` is a superset of `simple`:
 *
 * - `none` — not filterable or sortable.
 * - `simple` — Postgres-indexed only → queryable via [DataSync.getRelationships] `filterFast` (strongly
 *   consistent; reflects the latest write immediately).
 * - `full` — Postgres-indexed **and** OpenSearch-indexed → queryable via `filterFast` (Postgres, strongly
 *   consistent) **and** `filter` (OpenSearch, **eventually consistent** — a read right after a write may not yet
 *   see the record, so a `filter` assertion must poll; see [awaitRelationshipIds]).
 *
 * The built-in fields `id`, `createdAt`, `updatedAt`, and `status` behave as `full` on every class regardless of
 * declaration, so `status` is always queryable via both params. `TestFriendship` additionally declares `status`
 * `full` (which, for a built-in, only sets its projections — here none). Net effect: `filterFast == "status"`
 * reads the native Postgres column strongly-consistently ([getAllWithFilterSortLimitAndCursor]), while
 * `filter == "status"` exercises the OpenSearch path and must be polled ([getAllWithAdvancedFilterOnStatus]).
 *
 * ### Uncontrolled payload fields
 *
 * `role`/`custom` (the fields in [TestRelationshipPayload]) are **not** declared properties on `TestFriendship`.
 * They round-trip through create/set/get freely but are **not** filterable or sortable.
 *
 * ### Projections
 *
 * A projection is a named, filtered view of a class's fields, carried by the PAM token (not a read parameter). It is
 * strict keep-only: a read exposes ONLY the fields the token's projection declares.
 * `TestFriendship` declares `secret` in the `admin` projection **only**, so it behaves like the entity `TestUser.email`
 * field:
 * - the `admin`-projection **read** exposes `secret` but NOT `status` (which is not in the `admin` projection); the
 *   `__default__`-projection **read** exposes `status` but hides `secret`;
 * - a `__default__`-projection **write** of a payload containing `secret` is rejected with **DS-0202**;
 * - a token-authorized write that includes `secret` must grant the relationship through `projection = "admin"`, and
 *   under that non-default projection the write payload may contain **only** admin-projected fields (no undeclared
 *   fields like `role`/`custom`). See [getRelationshipAppliesProjectionCarriedByTheToken] and
 *   [createRelationshipUnderNonDefaultProjectionEnforcesWriteGuard].
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataSyncRelationshipIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1

    // Seeded entity class used for both ends of every relationship below.
    private val nodeClass = "TestNode"

    // Seeded relationship classes (see the class KDoc for cardinality + entityA/entityB class).
    private val m2mClass = "TestFriendship" // MANY_TO_MANY
    private val oneToOneClass = "TestOwnership" // ONE_TO_ONE

    data class TestRelationshipPayload(
        val role: String? = null,
        val custom: String? = null,
        // Declared on TestFriendship in the `admin` projection ONLY (see class KDoc).
        val secret: String? = null,
    )

    /**
     * Provisions two [nodeClass] entities (the two ends of a relationship) via `server` (holds the secretKey),
     * runs [block] with their ids, and best-effort removes both afterwards. An entity delete soft-cascades any
     * relationships that reference it, so the relationship does not have to be removed first.
     */
    private fun withTwoEntities(block: (entityAId: String, entityBId: String) -> Unit) {
        val run = randomValue()
        val entityAId = "node-a-$run"
        val entityBId = "node-b-$run"
        server.dataSync.createEntity(
            className = nodeClass,
            classVersion = classVersion,
            entityId = entityAId,
            payload = mapOf("name" to "NodeA-$run"),
        ).sync()
        server.dataSync.createEntity(
            className = nodeClass,
            classVersion = classVersion,
            entityId = entityBId,
            payload = mapOf("name" to "NodeB-$run"),
        ).sync()
        try {
            block(entityAId, entityBId)
        } finally {
            try {
                server.dataSync.removeEntity(entityAId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeEntity(entityBId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createGetAndDeleteRelationship() = withTwoEntities { entityAId, entityBId ->
        val relationshipId = "relationship-" + randomValue()
        val payload = TestRelationshipPayload(role = "admin", custom = "value")

        val createResult = server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = relationshipId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            assertEquals(relationshipId, createResult.data.id)
            assertEquals(entityAId, createResult.data.entityAId)
            assertEquals(entityBId, createResult.data.entityBId)
            assertEquals(classVersion, createResult.data.classVersion)
            // guards the @SerializedName mapping: wire `relationshipClass` -> `.className`
            assertEquals(m2mClass, createResult.data.className)
            assertNotNull(createResult.data.eTag)
            assertEquals("admin", createResult.data.payload?.get("role"))

            // DS-0301: create again with the SAME id -> 409 (create is create-only)
            try {
                server.dataSync.createRelationship(
                    entityAId = entityAId,
                    entityBId = entityBId,
                    className = m2mClass,
                    classVersion = classVersion,
                    relationshipId = relationshipId,
                    status = "active",
                    payload = payload,
                ).sync()
                fail("Expected a 409 when creating a relationship with an existing id")
            } catch (e: PubNubException) {
                assertEquals(409, e.statusCode)
            }

            // DS-0301: create with a DIFFERENT id but the SAME (class, entityA, entityB) pair -> 409. On a
            // MANY_TO_MANY class the conflict is on the duplicate pair, not on cardinality (contrast DS-0801).
            try {
                server.dataSync.createRelationship(
                    entityAId = entityAId,
                    entityBId = entityBId,
                    className = m2mClass,
                    classVersion = classVersion,
                    relationshipId = "relationship-" + randomValue(),
                    status = "active",
                    payload = payload,
                ).sync()
                fail("Expected a 409 when creating a relationship for an existing (class, entityA, entityB) pair")
            } catch (e: PubNubException) {
                assertEquals(409, e.statusCode)
            }

            // get
            val getResult = server.dataSync.getRelationship(relationshipId).sync()
            assertEquals(relationshipId, getResult.data.id)
            assertEquals(entityAId, getResult.data.entityAId)
            assertEquals(entityBId, getResult.data.entityBId)
            assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeRelationship(relationshipId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getRelationship(relationshipId).sync()
                fail("Expected a 404 after deleting the relationship")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeRelationship(relationshipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createWithServerGeneratedId() = withTwoEntities { entityAId, entityBId ->
        val createResult = server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
        ).sync()

        val generatedId = createResult.data.id
        try {
            assertTrue(generatedId.isNotBlank())
            assertEquals(entityAId, createResult.data.entityAId)
            assertEquals(entityBId, createResult.data.entityBId)
        } finally {
            server.dataSync.removeRelationship(generatedId).sync()
        }
    }

    @Test
    fun createGetAllPatchUpdateAndDeleteRelationship() = withTwoEntities { entityAId, entityBId ->
        val relationshipId = "relationship-" + randomValue()
        server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = relationshipId,
            status = "active",
            payload = TestRelationshipPayload(role = "admin"),
        ).sync()

        try {
            // getAll (filtered to this entityA + entityB) -> the created relationship is present
            val getAllResult = server.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                entityBId = entityBId,
                limit = 100,
            ).sync()
            assertTrue(getAllResult.data.any { it.id == relationshipId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateRelationship(
                relationshipId = relationshipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)
            assertEquals("inactive", server.dataSync.getRelationship(relationshipId).sync().data.status)

            // set -> full replace of status + payload
            val setResult = server.dataSync.setRelationship(
                relationshipId = relationshipId,
                classVersion = classVersion,
                status = "archived",
                payload = TestRelationshipPayload(role = "member"),
            ).sync()
            assertEquals("archived", setResult.data.status)
            assertEquals("member", setResult.data.payload?.get("role"))

            val afterSet = server.dataSync.getRelationship(relationshipId).sync()
            assertEquals("archived", afterSet.data.status)
            assertEquals("member", afterSet.data.payload?.get("role"))

            // delete
            server.dataSync.removeRelationship(relationshipId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getRelationship(relationshipId).sync()
                fail("Expected a 404 after deleting the relationship")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeRelationship(relationshipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() = withTwoEntities { entityAId, entityBId ->
        val relationshipId = "relationship-" + randomValue()
        val createResult = server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = relationshipId,
            status = "active",
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            assertNotNull(originalETag)

            val patch1 = server.dataSync.updateRelationship(
                relationshipId = relationshipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
                ifMatch = originalETag,
            ).sync()
            assertEquals("inactive", patch1.data.status)
            assertNotEquals(originalETag, patch1.data.eTag)

            try {
                server.dataSync.updateRelationship(
                    relationshipId = relationshipId,
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
            server.dataSync.removeRelationship(relationshipId).sync()
        }
    }

    @Test
    fun createWithWrongEntityClassThrowsDs0800() = withTwoEntities { entityAId, entityBId ->
        // DS-0800: the seeded classes declare entityAClass/entityBClass == TestNode. Point entity A at an entity
        // of a different class (a Channel, class `Channel`) -> the backend's checkEntityClass rejects it.
        // DS-0800 is NOT relationship-only: POST /memberships trips the same code path.
        val wrongClassEntityId = "channel-wrongclass-" + randomValue()
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = wrongClassEntityId,
            payload = mapOf("name" to "WrongClass"),
        ).sync()
        try {
            server.dataSync.createRelationship(
                entityAId = wrongClassEntityId,
                entityBId = entityBId,
                className = m2mClass,
                classVersion = classVersion,
            ).sync()
            fail("Expected DS-0800 (400) when entity A's class does not match the relationship class's entityAClass")
        } catch (e: PubNubException) {
            assertEquals(400, e.statusCode)
        } finally {
            try {
                server.dataSync.removeChannel(wrongClassEntityId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createSecondOneToOneOnSameEntityThrowsDs0801() = withTwoEntities { entityAId, entityBId ->
        // DS-0801 (cardinality): the seeded `TestOwnership` class is ONE_TO_ONE, so entity A may take part in at
        // most one such relationship. A second ONE_TO_ONE anchored on the same entity A (different entity B) must
        // be rejected with a 409. This is effectively relationship-only: /memberships (MANY_TO_MANY) skips the
        // cardinality check, so it is not reachable there.
        val firstId = "relationship-" + randomValue()
        val otherBId = "node-b2-" + randomValue()
        server.dataSync.createEntity(
            className = nodeClass,
            classVersion = classVersion,
            entityId = otherBId,
            payload = mapOf("name" to "NodeB2"),
        ).sync()

        server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = oneToOneClass,
            classVersion = classVersion,
            relationshipId = firstId,
        ).sync()

        try {
            server.dataSync.createRelationship(
                entityAId = entityAId,
                entityBId = otherBId,
                className = oneToOneClass,
                classVersion = classVersion,
                relationshipId = "relationship-" + randomValue(),
            ).sync()
            fail("Expected DS-0801 (409) for a second ONE_TO_ONE relationship on the same entity A")
        } catch (e: PubNubException) {
            assertEquals(409, e.statusCode)
        } finally {
            try {
                server.dataSync.removeRelationship(firstId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeEntity(otherBId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun getBlankRelationshipIdThrows() {
        try {
            server.dataSync.getRelationship("").sync()
            fail("Expected validation to reject a blank relationshipId")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankEntityAIdThrows() {
        try {
            server.dataSync.createRelationship(
                entityAId = "",
                entityBId = "node-" + randomValue(),
                className = m2mClass,
                classVersion = classVersion,
            ).sync()
            fail("Expected validation to reject a blank entityAId on create")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankEntityBIdThrows() {
        try {
            server.dataSync.createRelationship(
                entityAId = "node-" + randomValue(),
                entityBId = "",
                className = m2mClass,
                classVersion = classVersion,
            ).sync()
            fail("Expected validation to reject a blank entityBId on create")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankClassNameThrows() {
        try {
            server.dataSync.createRelationship(
                entityAId = "node-" + randomValue(),
                entityBId = "node-" + randomValue(),
                className = "",
                classVersion = classVersion,
            ).sync()
            fail("Expected validation to reject a blank className on create")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_CLASS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getAllBlankClassNameThrows() {
        try {
            server.dataSync.getRelationships(className = "").sync()
            fail("Expected validation to reject a blank className on list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_CLASS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getRelationshipAppliesProjectionCarriedByTheToken() = withTwoEntities { entityAId, entityBId ->
        // A projection is a named, filtered view of a class's fields, carried by the PAM token (not a read
        // parameter). It is strict keep-only: a read exposes ONLY the fields the token's projection declares.
        // `TestFriendship`'s `admin` projection declares `secret` (and nothing else), while `status` is a built-in
        // field carried by the implicit `__default__` view. So the two views are complementary: the `admin`-projected
        // read exposes `secret` but NOT `status`; the `__default__` read exposes `status` but NOT `secret`.
        // Mirrors DataSyncEntityIntegrationTest.getEntityAppliesProjectionCarriedByTheToken.
        val relationshipId = "relationship-" + randomValue()
        server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = relationshipId,
            status = "active",
            payload = TestRelationshipPayload(secret = "top-secret"),
        ).sync()

        // A client on the same keyset as `server` but without the secretKey, so it only sees what its token allows.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        try {
            // admin-projected `get` token -> the `secret` (admin-only) field is visible; `status`, which is NOT in
            // the `admin` projection, is omitted (keep-only).
            grantAndAuthenticate(
                client,
                authorizedUUID,
                DataSyncGrant.relationship(name = relationshipId, get = true, projection = "admin")
            )
            val adminView = client.dataSync.getRelationship(relationshipId).sync()
            assertEquals(relationshipId, adminView.data.id)
            assertEquals(
                "The admin-projected token must expose the admin-only `secret` field",
                "top-secret",
                adminView.data.payload?.get("secret"),
            )
            assertTrue(
                "`status` is not in the `admin` projection, so it must NOT appear under an admin-projected read",
                adminView.data.status == null,
            )

            // no projection -> implicit `__default__` view: `secret` is omitted, `status` is present.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, get = true))
            val defaultView = client.dataSync.getRelationship(relationshipId).sync()
            assertEquals(relationshipId, defaultView.data.id)
            assertEquals(
                "The __default__ projection must still expose the built-in `status` field",
                "active",
                defaultView.data.status,
            )
            assertTrue(
                "The admin-only `secret` field must NOT leak through the __default__ projection",
                defaultView.data.payload?.get("secret") == null,
            )
        } finally {
            try {
                server.dataSync.removeRelationship(relationshipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createRelationshipUnderNonDefaultProjectionEnforcesWriteGuard() = withTwoEntities { entityAId, entityBId ->
        // `TestFriendship` declares `secret` in the `admin` projection ONLY. The server enforces a projection
        // write-guard on create: a token whose projection does NOT permit a field cannot write it. So writing
        // `secret` requires an `admin`-projected create grant; a `__default__`-projected create of a payload
        // containing `secret` is rejected with DS-0202 (HTTP 403), pre-commit (nothing is persisted).
        //
        // Under a NON-default projection the guard is strict keep-only: the write payload may contain ONLY
        // fields declared in that projection, so the admin-projected payload below carries `secret` and nothing
        // else (no undeclared `role`/`custom`, which would also 403). Asserted as a positive/negative pair so it
        // proves the projection boundary is *enforced*, not merely that some write failed.
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // negative leg: __default__-projected create writing the admin-only `secret` -> 403 (DS-0202).
        val rejectedId = "relationship-" + randomValue()
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = rejectedId, create = true))
        try {
            client.dataSync.createRelationship(
                entityAId = entityAId,
                entityBId = entityBId,
                className = m2mClass,
                classVersion = classVersion,
                relationshipId = rejectedId,
                payload = TestRelationshipPayload(secret = "top-secret"),
            ).sync()
            fail("Expected a 403 when writing an admin-only field under the __default__ projection")
        } catch (e: PubNubException) {
            assertEquals("Writing an admin-only field under __default__ must be rejected by the projection write-guard", 403, e.statusCode)
        }

        // positive leg: admin-projected create of the SAME admin-only field -> succeeds. Payload must contain
        // ONLY admin-projected fields, so it carries `secret` alone.
        val acceptedId = "relationship-" + randomValue()
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = acceptedId, create = true, projection = "admin"))
        try {
            val createResult = client.dataSync.createRelationship(
                entityAId = entityAId,
                entityBId = entityBId,
                className = m2mClass,
                classVersion = classVersion,
                relationshipId = acceptedId,
                payload = TestRelationshipPayload(secret = "top-secret"),
            ).sync()
            assertEquals(acceptedId, createResult.data.id)
            assertEquals("top-secret", createResult.data.payload?.get("secret"))
        } finally {
            try {
                server.dataSync.removeRelationship(acceptedId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateRelationship("relationship-" + randomValue(), emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getRelationshipsReturnsOnlyRelationshipsTheTokenCanRead() = withTwoEntities { entityAId, entityBId ->
        val grantedRelationshipId = "relationship-granted-" + randomValue()
        val ungrantedRelationshipId = "relationship-ungranted-" + randomValue()

        // Two relationships on the same entity A but different entity B, so both can coexist on the M2M class.
        val otherBId = "node-other-" + randomValue()
        server.dataSync.createEntity(
            className = nodeClass,
            classVersion = classVersion,
            entityId = otherBId,
            payload = mapOf("name" to "Other"),
        ).sync()

        server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = grantedRelationshipId,
            status = "active",
        ).sync()
        server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = otherBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = ungrantedRelationshipId,
            status = "active",
        ).sync()

        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // Grant the client `get` on ONLY one of the two relationships (by relationship id).
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = grantedRelationshipId, get = true))

        try {
            val getAllResult = client.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                limit = 100,
            ).sync()

            assertTrue(
                "Expected the granted relationship to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedRelationshipId },
            )
            assertTrue(
                "The ungranted relationship must not leak to a token that cannot read it",
                getAllResult.data.none { it.id == ungrantedRelationshipId },
            )
        } finally {
            try {
                server.dataSync.removeRelationship(grantedRelationshipId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeRelationship(ungrantedRelationshipId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeEntity(otherBId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createGetPatchSetAndDeleteWithServerGrantedToken() = withTwoEntities { entityAId, entityBId ->
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via
        // setToken. DataSync /relationships authorizes via `DataSyncGrant.relationship` (the
        // `datasync:relationships` resource type), and the grant name is the relationship id.
        val relationshipId = "relationship-" + randomValue()
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific relationship id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, create = true))
        val createResult = client.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = relationshipId,
            status = "active",
            payload = TestRelationshipPayload(role = "admin"),
        ).sync()

        try {
            assertEquals(relationshipId, createResult.data.id)
            assertEquals(entityAId, createResult.data.entityAId)
            assertEquals(entityBId, createResult.data.entityBId)

            // get -> `get` on this specific relationship
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, get = true))
            val getResult = client.dataSync.getRelationship(relationshipId).sync()
            assertEquals(relationshipId, getResult.data.id)
            assertEquals("active", getResult.data.status)

            // patch -> `update` on this specific relationship (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, update = true))
            val patchResult = client.dataSync.updateRelationship(
                relationshipId = relationshipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // set -> `update` on this specific relationship (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, update = true))
            val setResult = client.dataSync.setRelationship(
                relationshipId = relationshipId,
                classVersion = classVersion,
                status = "archived",
                payload = TestRelationshipPayload(role = "member"),
            ).sync()
            assertEquals("archived", setResult.data.status)
            assertEquals("member", setResult.data.payload?.get("role"))

            // delete -> `delete` on this specific relationship
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, delete = true))
            client.dataSync.removeRelationship(relationshipId).sync()

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = relationshipId, get = true))
            try {
                client.dataSync.getRelationship(relationshipId).sync()
                fail("Expected a 404 after deleting the relationship")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeRelationship(relationshipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun relationshipRejectsAWrongResourceTypeGrant() = withTwoEntities { entityAId, entityBId ->
        // Reverse-isolation probe (mirror of DataSyncMembershipIntegrationTest.membershipRejectsAWrongResourceTypeGrant):
        // /relationships authorizes against `datasync:relationships`. A grant on the SAME id but under
        // `datasync:memberships` must NOT authorize a relationship op. Namespaces are disjoint (PAM is path-based),
        // even though uniqueness is on a shared, path-agnostic pair table.
        val relationshipId = "relationship-" + randomValue()
        server.dataSync.createRelationship(
            entityAId = entityAId,
            entityBId = entityBId,
            className = m2mClass,
            classVersion = classVersion,
            relationshipId = relationshipId,
            status = "active",
        ).sync()

        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        try {
            // `get` granted on the same id but under `datasync:memberships`, not `datasync:relationships`.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = relationshipId, get = true))
            try {
                client.dataSync.getRelationship(relationshipId).sync()
                fail("Expected a 403: a datasync:memberships grant must not authorize a /relationships op")
            } catch (e: PubNubException) {
                assertEquals(403, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeRelationship(relationshipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun getAllWithFilterSortLimitAndCursor() = withTwoEntities { entityAId, _ ->
        // Built-in fields (id, createdAt, updatedAt, status) are always filterable and sortable. Tag three
        // relationships on the same entity A (distinct entity B) with sortable statuses a < b < c.
        val run = randomValue()
        val statusA = "st-$run-a"
        val statusB = "st-$run-b"
        val statusC = "st-$run-c"
        val idA = "relationship-$run-a"
        val idB = "relationship-$run-b"
        val idC = "relationship-$run-c"
        val bA = "node-$run-a"
        val bB = "node-$run-b"
        val bC = "node-$run-c"

        listOf(bA, bB, bC).forEach {
            server.dataSync.createEntity(
                className = nodeClass,
                classVersion = classVersion,
                entityId = it,
                payload = mapOf("name" to it),
            ).sync()
        }

        server.dataSync.createRelationship(entityAId, bA, m2mClass, classVersion, idA, status = statusA).sync()
        server.dataSync.createRelationship(entityAId, bB, m2mClass, classVersion, idB, status = statusB).sync()
        server.dataSync.createRelationship(entityAId, bC, m2mClass, classVersion, idC, status = statusC).sync()

        try {
            // filterFast -> exact status equality
            val filtered = server.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                filterFast = "status == \"$statusA\"",
            ).sync()
            assertEquals(setOf(idA), filtered.data.map { it.id }.toSet())

            // sort ascending by status -> a-b-c
            val sortedAsc = server.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sortedAsc.data.map { it.id })

            // sort descending -> c-b-a
            val sortedDesc = server.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status", ascending = false)),
            ).sync()
            assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page one at a time
            val firstPage = server.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
                limit = 1,
            ).sync()
            assertEquals(1, firstPage.data.size)
            assertEquals(idA, firstPage.data.first().id)
            assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getRelationships(
                className = m2mClass,
                entityAId = entityAId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            assertEquals(1, secondPage.data.size)
            assertEquals(idB, secondPage.data.first().id)
        } finally {
            listOf(idA, idB, idC).forEach {
                try {
                    server.dataSync.removeRelationship(it).sync()
                } catch (ignored: PubNubException) {
                }
            }
            listOf(bA, bB, bC).forEach {
                try {
                    server.dataSync.removeEntity(it).sync()
                } catch (ignored: PubNubException) {
                }
            }
        }
    }

    @Test
    fun getAllWithAdvancedFilterOnStatus() = withTwoEntities { entityAId, _ ->
        // OpenSearch counterpart to getAllWithFilterSortLimitAndCursor. `TestFriendship.status` is `full`, so it
        // is queryable via BOTH `filterFast` (Postgres, strongly consistent) and `filter` (OpenSearch). This test
        // exercises the `filter` (OpenSearch) path, which is EVENTUALLY consistent: there is a write-to-index
        // delay, so the assertion must poll with a bounded retry ([awaitRelationshipIds]) rather than read once.
        val run = randomValue()
        val statusA = "adv-$run-a"
        val statusB = "adv-$run-b"
        val idA = "relationship-$run-a"
        val idB = "relationship-$run-b"
        val bA = "node-$run-a"
        val bB = "node-$run-b"

        listOf(bA, bB).forEach {
            server.dataSync.createEntity(
                className = nodeClass,
                classVersion = classVersion,
                entityId = it,
                payload = mapOf("name" to it),
            ).sync()
        }

        server.dataSync.createRelationship(entityAId, bA, m2mClass, classVersion, idA, status = statusA).sync()
        server.dataSync.createRelationship(entityAId, bB, m2mClass, classVersion, idB, status = statusB).sync()

        try {
            // filter (advanced / OpenSearch) -> exact status equality, polled until the index catches up.
            val matched = awaitRelationshipIds(setOf(idA)) {
                server.dataSync.getRelationships(
                    className = m2mClass,
                    entityAId = entityAId,
                    filter = "status == \"$statusA\"",
                ).sync().data.map { it.id }.toSet()
            }
            assertEquals(setOf(idA), matched)

            // a prefix LIKE over the same param captures both rows once indexed
            val both = awaitRelationshipIds(setOf(idA, idB)) {
                server.dataSync.getRelationships(
                    className = m2mClass,
                    entityAId = entityAId,
                    filter = "status LIKE \"adv-$run-*\"",
                ).sync().data.map { it.id }.toSet()
            }
            assertEquals(setOf(idA, idB), both)
        } finally {
            listOf(idA, idB).forEach {
                try {
                    server.dataSync.removeRelationship(it).sync()
                } catch (ignored: PubNubException) {
                }
            }
            listOf(bA, bB).forEach {
                try {
                    server.dataSync.removeEntity(it).sync()
                } catch (ignored: PubNubException) {
                }
            }
        }
    }

    /**
     * Polls [query] until it returns exactly [expected] or the timeout elapses, then returns the last result. Used
     * for `filter` (OpenSearch) reads, which are eventually consistent — a record is not visible until the
     * write-to-index step completes, so an immediate read flakes. `filterFast` (Postgres) reads are strongly
     * consistent and never need this.
     */
    private fun awaitRelationshipIds(
        expected: Set<String>,
        timeoutMillis: Long = 15_000,
        pollMillis: Long = 500,
        query: () -> Set<String>,
    ): Set<String> {
        val deadline = System.currentTimeMillis() + timeoutMillis
        var last = query()
        while (last != expected && System.currentTimeMillis() < deadline) {
            Thread.sleep(pollMillis)
            last = query()
        }
        return last
    }

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: DataSyncGrantType) {
        val token = server.grantToken(
            ttl = 60,
            authorizedUserId = UserId(authorizedUUID),
            grants = grants.toList(),
        ).sync().token
        client.setToken(token)
    }
}
