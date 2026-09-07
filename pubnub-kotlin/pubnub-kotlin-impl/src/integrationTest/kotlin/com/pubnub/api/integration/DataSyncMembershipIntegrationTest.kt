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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataSyncMembershipIntegrationTest : BaseIntegrationTest() {
    private val classVersion = 1

    data class TestMembershipPayload(
        val role: String? = null,
        val custom: String? = null,
    )

    /**
     * Provisions a Channel and a User (the two sides of a membership) via `server` (holds the secretKey), runs
     * [block] with their ids, and best-effort removes both afterwards. Note the channel/user delete soft-cascades
     * any memberships that reference them, so the membership does not have to be removed first.
     */
    private fun withChannelAndUser(block: (channelId: String, userId: String) -> Unit) {
        val run = randomValue()
        val channelId = "channel-$run"
        val userId = "user-$run"
        server.dataSync.createChannel(
            classVersion = classVersion,
            channelId = channelId,
            payload = mapOf("name" to "Chan-$run"),
        ).sync()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = userId,
            payload = mapOf("name" to "User-$run"),
        ).sync()
        try {
            block(channelId, userId)
        } finally {
            try {
                server.dataSync.removeChannel(channelId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeUser(userId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createGetAndDeleteMembership() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + randomValue()
        val payload = TestMembershipPayload(role = "admin", custom = "value")

        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = payload,
        ).sync()

        try {
            assertEquals(membershipId, createResult.data.id)
            assertEquals(channelId, createResult.data.channelId)
            assertEquals(userId, createResult.data.userId)
            assertEquals(classVersion, createResult.data.classVersion)
            // guards the @SerializedName mapping: wire `relationshipClass` -> `.className`
            assertEquals("Membership", createResult.data.className)
            assertNotNull(createResult.data.eTag)
            assertEquals("admin", createResult.data.payload?.get("role"))

            // create again with the SAME id -> 409 (create is create-only)
            try {
                server.dataSync.createMembership(
                    channelId = channelId,
                    userId = userId,
                    classVersion = classVersion,
                    membershipId = membershipId,
                    status = "active",
                    payload = payload,
                ).sync()
                fail("Expected a 409 when creating a membership with an existing id")
            } catch (e: PubNubException) {
                assertEquals(409, e.statusCode)
            }

            // create again with a DIFFERENT id but the SAME (channel, user) pair -> 409 (a Membership is
            // unique per channel/user pair, not just per id). There is no DS-0801 here: Membership is a
            // MANY_TO_MANY relationship, so the conflict is on the duplicate pair, not on cardinality.
            try {
                server.dataSync.createMembership(
                    channelId = channelId,
                    userId = userId,
                    classVersion = classVersion,
                    membershipId = "membership-" + randomValue(),
                    status = "active",
                    payload = payload,
                ).sync()
                fail("Expected a 409 when creating a membership for an existing (channel, user) pair")
            } catch (e: PubNubException) {
                assertEquals(409, e.statusCode)
            }

            // get
            val getResult = server.dataSync.getMembership(membershipId).sync()
            assertEquals(membershipId, getResult.data.id)
            assertEquals(channelId, getResult.data.channelId)
            assertEquals(userId, getResult.data.userId)
            assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeMembership(membershipId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getMembership(membershipId).sync()
                fail("Expected a 404 after deleting the membership")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeMembership(membershipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createWithServerGeneratedId() = withChannelAndUser { channelId, userId ->
        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
        ).sync()

        val generatedId = createResult.data.id
        try {
            assertTrue(generatedId.isNotBlank())
            assertEquals(channelId, createResult.data.channelId)
            assertEquals(userId, createResult.data.userId)
        } finally {
            server.dataSync.removeMembership(generatedId).sync()
        }
    }

    @Test
    fun createGetAllPatchUpdateAndDeleteMembership() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + randomValue()
        server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = TestMembershipPayload(role = "admin"),
        ).sync()

        try {
            // getAll (filtered to this channel + user) -> the created membership is present
            val getAllResult = server.dataSync.getMemberships(
                channelId = channelId,
                userId = userId,
                limit = 100,
            ).sync()
            assertTrue(getAllResult.data.any { it.id == membershipId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)
            assertEquals("inactive", server.dataSync.getMembership(membershipId).sync().data.status)

            // update -> full replace of status + payload
            val updateResult = server.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "archived",
                payload = TestMembershipPayload(role = "member"),
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("member", updateResult.data.payload?.get("role"))

            val afterUpdate = server.dataSync.getMembership(membershipId).sync()
            assertEquals("archived", afterUpdate.data.status)
            assertEquals("member", afterUpdate.data.payload?.get("role"))
        } finally {
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + randomValue()
        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            assertNotNull(originalETag)

            val patch1 = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
                ifMatch = originalETag,
            ).sync()
            assertEquals("inactive", patch1.data.status)
            assertNotEquals(originalETag, patch1.data.eTag)

            try {
                server.dataSync.updateMembership(
                    membershipId = membershipId,
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
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun getBlankMembershipIdThrows() {
        try {
            server.dataSync.getMembership("").sync()
            fail("Expected validation to reject a blank membershipId")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankChannelIdThrows() {
        try {
            server.dataSync.createMembership(
                channelId = "",
                userId = "user-" + randomValue(),
                classVersion = classVersion,
            ).sync()
            fail("Expected validation to reject a blank channelId on create")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankUserIdThrows() {
        try {
            server.dataSync.createMembership(
                channelId = "channel-" + randomValue(),
                userId = "",
                classVersion = classVersion,
            ).sync()
            fail("Expected validation to reject a blank userId on create")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateMembership("membership-" + randomValue(), emptyList()).sync()
            fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getMembershipsReturnsOnlyMembershipsTheTokenCanRead() = withChannelAndUser { channelId, userId ->
        val grantedMembershipId = "membership-granted-" + randomValue()
        val ungrantedMembershipId = "membership-ungranted-" + randomValue()

        // Two memberships on the same channel but different users, so both can coexist (unique per pair).
        val otherUserId = "user-other-" + randomValue()
        server.dataSync.createUser(
            classVersion = classVersion,
            userId = otherUserId,
            payload = mapOf("name" to "Other"),
        ).sync()

        server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = grantedMembershipId,
            status = "active",
        ).sync()
        server.dataSync.createMembership(
            channelId = channelId,
            userId = otherUserId,
            classVersion = classVersion,
            membershipId = ungrantedMembershipId,
            status = "active",
        ).sync()

        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // Grant the client `get` on ONLY one of the two memberships (by membership id).
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = grantedMembershipId, get = true))

        try {
            val getAllResult = client.dataSync.getMemberships(channelId = channelId, limit = 100).sync()

            assertTrue(
                "Expected the granted membership to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedMembershipId },
            )
            assertTrue(
                "The ungranted membership must not leak to a token that cannot read it",
                getAllResult.data.none { it.id == ungrantedMembershipId },
            )
        } finally {
            try {
                server.dataSync.removeMembership(grantedMembershipId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeMembership(ungrantedMembershipId).sync()
            } catch (ignored: PubNubException) {
            }
            try {
                server.dataSync.removeUser(otherUserId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun createGetPatchUpdateAndDeleteWithServerGrantedToken() = withChannelAndUser { channelId, userId ->
        // A client on the same keyset as `server` but without the secretKey, so it can only authenticate via
        // setToken. DataSync /memberships authorizes via `DataSyncGrant.membership` (the `datasync:memberships`
        // resource type), and the grant name is the membership id.
        val membershipId = "membership-" + randomValue()
        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        // create -> token scoped to `create` on this specific membership id
        grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, create = true))
        val createResult = client.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = TestMembershipPayload(role = "admin"),
        ).sync()

        try {
            assertEquals(membershipId, createResult.data.id)
            assertEquals(channelId, createResult.data.channelId)
            assertEquals(userId, createResult.data.userId)

            // get -> `get` on this specific membership
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, get = true))
            val getResult = client.dataSync.getMembership(membershipId).sync()
            assertEquals(membershipId, getResult.data.id)
            assertEquals("active", getResult.data.status)

            // patch -> `update` on this specific membership (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, update = true))
            val patchResult = client.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            assertEquals("inactive", patchResult.data.status)

            // update -> `update` on this specific membership (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, update = true))
            val updateResult = client.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "archived",
                payload = TestMembershipPayload(role = "member"),
            ).sync()
            assertEquals("archived", updateResult.data.status)
            assertEquals("member", updateResult.data.payload?.get("role"))

            // delete -> `delete` on this specific membership
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, delete = true))
            client.dataSync.removeMembership(membershipId).sync()

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, get = true))
            try {
                client.dataSync.getMembership(membershipId).sync()
                fail("Expected a 404 after deleting the membership")
            } catch (e: PubNubException) {
                assertEquals(404, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeMembership(membershipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun membershipRejectsAWrongResourceTypeGrant() = withChannelAndUser { channelId, userId ->
        // O1 isolation probe: DataSync /memberships authorizes against the `datasync:memberships` resource
        // type — a grant on the SAME id but a DIFFERENT resource type (here `datasync:relationships`, the
        // key divergence from the Channel API) must NOT authorize a membership op. Without this, a wrong
        // resource type in the SDK's PAM wiring (or a backend routing change) would slip past the happy-path
        // grants, which only prove that the matching grant works — never that a mismatching one is rejected.
        val membershipId = "membership-" + randomValue()
        server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
        ).sync()

        val client = createAuthorizedClient()
        val authorizedUUID = client.configuration.userId.value

        try {
            // `get` granted on the same id but under `datasync:relationships`, not `datasync:memberships`.
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.relationship(name = membershipId, get = true))
            try {
                client.dataSync.getMembership(membershipId).sync()
                fail("Expected a 403: a datasync:relationships grant must not authorize a /memberships op")
            } catch (e: PubNubException) {
                assertEquals(403, e.statusCode)
            }
        } finally {
            try {
                server.dataSync.removeMembership(membershipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun getAllWithFilterSortLimitAndCursor() = withChannelAndUser { channelId, _ ->
        // Built-in fields (id, createdAt, updatedAt, status) are always filterable and sortable. Tag three
        // memberships on the same channel (distinct users) with sortable statuses a < b < c.
        val run = randomValue()
        val statusA = "st-$run-a"
        val statusB = "st-$run-b"
        val statusC = "st-$run-c"
        val idA = "membership-$run-a"
        val idB = "membership-$run-b"
        val idC = "membership-$run-c"
        val userA = "user-$run-a"
        val userB = "user-$run-b"
        val userC = "user-$run-c"

        listOf(userA, userB, userC).forEach {
            server.dataSync.createUser(
                classVersion = classVersion,
                userId = it,
                payload = mapOf("name" to it),
            ).sync()
        }

        server.dataSync.createMembership(channelId, userA, classVersion, idA, status = statusA).sync()
        server.dataSync.createMembership(channelId, userB, classVersion, idB, status = statusB).sync()
        server.dataSync.createMembership(channelId, userC, classVersion, idC, status = statusC).sync()

        try {
            // filterFast -> exact status equality
            val filtered = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status == \"$statusA\"",
            ).sync()
            assertEquals(setOf(idA), filtered.data.map { it.id }.toSet())

            // sort ascending by status -> a-b-c
            val sortedAsc = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
            ).sync()
            assertEquals(listOf(idA, idB, idC), sortedAsc.data.map { it.id })

            // sort descending -> c-b-a
            val sortedDesc = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status", ascending = false)),
            ).sync()
            assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page one at a time
            val firstPage = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
                limit = 1,
            ).sync()
            assertEquals(1, firstPage.data.size)
            assertEquals(idA, firstPage.data.first().id)
            assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getMemberships(
                channelId = channelId,
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
                    server.dataSync.removeMembership(it).sync()
                } catch (ignored: PubNubException) {
                }
            }
            listOf(userA, userB, userC).forEach {
                try {
                    server.dataSync.removeUser(it).sync()
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
