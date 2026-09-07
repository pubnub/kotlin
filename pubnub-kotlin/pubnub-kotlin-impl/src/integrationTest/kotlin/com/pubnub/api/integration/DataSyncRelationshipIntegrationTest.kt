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
 * Two entity classes and two relationship classes are assumed to exist on the keyset:
 *
 * - Entity class **`TestNode`** (version 1) — a generic entity class used for both sides of every relationship
 *   below (`entityAClass` == `entityBClass` == `TestNode`).
 * - Relationship class **`TestFriendship`** (version 1) — cardinality **MANY_TO_MANY**, `entityAClass` =
 *   `TestNode`, `entityBClass` = `TestNode`. Exercises the happy path (create/get/getAll/update/set/remove),
 *   filtering, paging and PAM.
 * - Relationship class **`TestOwnership`** (version 1) — cardinality **ONE_TO_ONE**, `entityAClass` = `TestNode`,
 *   `entityBClass` = `TestNode`. Used only to make **DS-0801** (cardinality conflict) reachable — a second
 *   ONE_TO_ONE relationship on the same entity A must be rejected.
 *
 * Because these are purpose-seeded classes (not the built-in `Membership` class), this suite does not share the
 * `uk_relationship_pair` table with [DataSyncMembershipIntegrationTest]; the cross-suite collision concern is
 * moot. Intra-suite isolation still relies on run-unique entity/relationship ids.
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

    private fun grantAndAuthenticate(client: PubNub, authorizedUUID: String, vararg grants: DataSyncGrantType) {
        val token = server.grantToken(
            ttl = 60,
            authorizedUserId = UserId(authorizedUUID),
            grants = grants.toList(),
        ).sync().token
        client.setToken(token)
    }
}
