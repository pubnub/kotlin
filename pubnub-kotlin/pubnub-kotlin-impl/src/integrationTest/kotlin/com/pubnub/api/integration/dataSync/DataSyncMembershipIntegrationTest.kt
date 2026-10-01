package com.pubnub.api.integration.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.test.CommonUtils
import org.junit.Assert
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
        val run = CommonUtils.randomValue()
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
        val membershipId = "membership-" + CommonUtils.randomValue()
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
            Assert.assertEquals(membershipId, createResult.data.id)
            Assert.assertEquals(channelId, createResult.data.channelId)
            Assert.assertEquals(userId, createResult.data.userId)
            Assert.assertEquals(classVersion, createResult.data.classVersion)
            // guards the @SerializedName mapping: wire `relationshipClass` -> `.className`
            Assert.assertEquals("Membership", createResult.data.className)
            Assert.assertNotNull(createResult.data.eTag)
            Assert.assertEquals("admin", createResult.data.payload?.get("role"))

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
                Assert.fail("Expected a 409 when creating a membership with an existing id")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
            }

            // create again with a DIFFERENT id but the SAME (channel, user) pair -> 409 (a Membership is
            // unique per channel/user pair, not just per id). There is no DS-0801 here: Membership is a
            // MANY_TO_MANY relationship, so the conflict is on the duplicate pair, not on cardinality.
            try {
                server.dataSync.createMembership(
                    channelId = channelId,
                    userId = userId,
                    classVersion = classVersion,
                    membershipId = "membership-" + CommonUtils.randomValue(),
                    status = "active",
                    payload = payload,
                ).sync()
                Assert.fail("Expected a 409 when creating a membership for an existing (channel, user) pair")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
            }

            // get
            val getResult = server.dataSync.getMembership(membershipId).sync()
            Assert.assertEquals(membershipId, getResult.data.id)
            Assert.assertEquals(channelId, getResult.data.channelId)
            Assert.assertEquals(userId, getResult.data.userId)
            Assert.assertEquals("active", getResult.data.status)

            // delete
            server.dataSync.removeMembership(membershipId).sync()

            // get after delete -> 404
            try {
                server.dataSync.getMembership(membershipId).sync()
                Assert.fail("Expected a 404 after deleting the membership")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
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
            Assert.assertTrue(generatedId.isNotBlank())
            Assert.assertEquals(channelId, createResult.data.channelId)
            Assert.assertEquals(userId, createResult.data.userId)
        } finally {
            server.dataSync.removeMembership(generatedId).sync()
        }
    }

    @Test
    fun createGetAllPatchUpdateAndDeleteMembership() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + CommonUtils.randomValue()
        server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = TestMembershipPayload(role = "admin", custom = "value"),
        ).sync()

        try {
            // getAll (filtered to this channel + user) -> the created membership is present
            val getAllResult = server.dataSync.getMemberships(
                channelId = channelId,
                userId = userId,
                limit = 100,
            ).sync()
            Assert.assertTrue(getAllResult.data.any { it.id == membershipId })

            // patch -> replace /status
            val patchResult = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            Assert.assertEquals("inactive", patchResult.data.status)
            Assert.assertEquals("inactive", server.dataSync.getMembership(membershipId).sync().data.status)

            // update -> full replace of status + payload
            val updateResult = server.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "archived",
                payload = TestMembershipPayload(role = "member"),
            ).sync()
            Assert.assertEquals("archived", updateResult.data.status)
            Assert.assertEquals("member", updateResult.data.payload?.get("role"))
            Assert.assertNotEquals(patchResult.data.eTag, updateResult.data.eTag)

            // get reflects the full replacement: `custom` was not re-sent, so it is gone rather than kept
            val afterUpdate = server.dataSync.getMembership(membershipId).sync()
            Assert.assertEquals("archived", afterUpdate.data.status)
            Assert.assertEquals("member", afterUpdate.data.payload?.get("role"))
            Assert.assertFalse(afterUpdate.data.payload.orEmpty().containsKey("custom"))
            Assert.assertEquals(updateResult.data.eTag, afterUpdate.data.eTag)
        } finally {
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun patchWithIfMatchAndStaleETagThrows412() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + CommonUtils.randomValue()
        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
        ).sync()

        try {
            val originalETag = createResult.data.eTag
            Assert.assertNotNull(originalETag)

            val patch1 = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
                ifMatch = originalETag,
            ).sync()
            Assert.assertEquals("inactive", patch1.data.status)
            Assert.assertNotEquals(originalETag, patch1.data.eTag)

            try {
                server.dataSync.updateMembership(
                    membershipId = membershipId,
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
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun setWithIfMatchAndStaleETagThrows412() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + CommonUtils.randomValue()
        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = TestMembershipPayload(role = "admin"),
        ).sync()

        try {
            // set with the current eTag -> succeeds and bumps the eTag
            val setResult = server.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "inactive",
                payload = TestMembershipPayload(role = "member"),
                ifMatch = createResult.data.eTag,
            ).sync()
            Assert.assertEquals("inactive", setResult.data.status)
            Assert.assertNotEquals(createResult.data.eTag, setResult.data.eTag)

            // a patch in between moves the eTag on again
            val patchResult = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "archived")),
                ifMatch = setResult.data.eTag,
            ).sync()
            Assert.assertNotEquals(setResult.data.eTag, patchResult.data.eTag)

            // set with the eTag read before the patch -> 412, and the patched membership is left as it was
            try {
                server.dataSync.setMembership(
                    membershipId = membershipId,
                    classVersion = classVersion,
                    status = "overwritten",
                    payload = TestMembershipPayload(role = "guest"),
                    ifMatch = setResult.data.eTag,
                ).sync()
                Assert.fail("Expected a 412 when setting with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
            }
            val current = server.dataSync.getMembership(membershipId).sync()
            Assert.assertEquals("archived", current.data.status)
            Assert.assertEquals("member", current.data.payload?.get("role"))
            Assert.assertEquals(patchResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun removeWithIfMatchOnlyRemovesTheCurrentVersion() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + CommonUtils.randomValue()
        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
        ).sync()

        try {
            val patchResult = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ).sync()

            // remove with the pre-patch eTag -> 412, and the membership is still there
            try {
                server.dataSync.removeMembership(membershipId, ifMatch = createResult.data.eTag).sync()
                Assert.fail("Expected a 412 when removing with a stale ifMatch eTag")
            } catch (e: PubNubException) {
                Assert.assertEquals(412, e.statusCode)
            }
            Assert.assertEquals("inactive", server.dataSync.getMembership(membershipId).sync().data.status)

            // remove with the current eTag -> removed
            server.dataSync.removeMembership(membershipId, ifMatch = patchResult.data.eTag).sync()
            try {
                server.dataSync.getMembership(membershipId).sync()
                Assert.fail("Expected a 404 after removing the membership")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
            }
        } finally {
            // best-effort cleanup: the happy path already removed the membership, so a 404 here is expected
            try {
                server.dataSync.removeMembership(membershipId).sync()
            } catch (ignored: PubNubException) {
            }
        }
    }

    @Test
    fun patchPayloadOperationsAreApplied() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + CommonUtils.randomValue()
        server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = mapOf("role" to "admin", "custom" to "value", "team" to "red"),
        ).sync()

        try {
            val patchResult = server.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    // a passing `test` lets the rest of the patch through
                    PNJsonPatchOperation(op = "test", path = "/payload/role", value = "admin"),
                    PNJsonPatchOperation(op = "add", path = "/payload/nickname", value = "Ali"),
                    PNJsonPatchOperation(op = "remove", path = "/payload/custom"),
                    PNJsonPatchOperation(op = "copy", from = "/payload/role", path = "/payload/previousRole"),
                    PNJsonPatchOperation(op = "move", from = "/payload/team", path = "/payload/squad"),
                ),
            ).sync()

            listOf(patchResult.data.payload, server.dataSync.getMembership(membershipId).sync().data.payload).forEach { payload ->
                val fields = payload.orEmpty()
                Assert.assertEquals("Ali", fields["nickname"])
                Assert.assertFalse(fields.containsKey("custom"))
                Assert.assertEquals("admin", fields["previousRole"])
                Assert.assertEquals("admin", fields["role"]) // copy leaves the source in place
                Assert.assertEquals("red", fields["squad"])
                Assert.assertFalse(fields.containsKey("team")) // move drops the source
            }
            Assert.assertEquals("active", patchResult.data.status)
        } finally {
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun patchWithFailingTestOpIsAtomic() = withChannelAndUser { channelId, userId ->
        val membershipId = "membership-" + CommonUtils.randomValue()
        val createResult = server.dataSync.createMembership(
            channelId = channelId,
            userId = userId,
            classVersion = classVersion,
            membershipId = membershipId,
            status = "active",
            payload = TestMembershipPayload(role = "admin", custom = "value"),
        ).sync()

        try {
            // the `replace` before the failing `test` must be rolled back along with the one after it
            try {
                server.dataSync.updateMembership(
                    membershipId = membershipId,
                    operations = listOf(
                        PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                        PNJsonPatchOperation(op = "test", path = "/payload/role", value = "nobody"),
                        PNJsonPatchOperation(op = "replace", path = "/payload/custom", value = "changed"),
                    ),
                ).sync()
                Assert.fail("Expected DS-0302 (409) when a JSON Patch `test` operation fails")
            } catch (e: PubNubException) {
                Assert.assertEquals(409, e.statusCode)
            }

            val current = server.dataSync.getMembership(membershipId).sync()
            Assert.assertEquals("active", current.data.status)
            Assert.assertEquals("value", current.data.payload?.get("custom"))
            Assert.assertEquals(createResult.data.eTag, current.data.eTag)
        } finally {
            server.dataSync.removeMembership(membershipId).sync()
        }
    }

    @Test
    fun getBlankMembershipIdThrows() {
        try {
            server.dataSync.getMembership("").sync()
            Assert.fail("Expected validation to reject a blank membershipId")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankChannelIdThrows() {
        try {
            server.dataSync.createMembership(
                channelId = "",
                userId = "user-" + CommonUtils.randomValue(),
                classVersion = classVersion,
            ).sync()
            Assert.fail("Expected validation to reject a blank channelId on create")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun createBlankUserIdThrows() {
        try {
            server.dataSync.createMembership(
                channelId = "channel-" + CommonUtils.randomValue(),
                userId = "",
                classVersion = classVersion,
            ).sync()
            Assert.fail("Expected validation to reject a blank userId on create")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.ENTITY_ID_MISSING, e.pubnubError)
        }
    }

    @Test
    fun patchEmptyOperationsThrows() {
        try {
            server.dataSync.updateMembership("membership-" + CommonUtils.randomValue(), emptyList()).sync()
            Assert.fail("Expected validation to reject an empty patch operations list")
        } catch (e: PubNubException) {
            Assert.assertEquals(PubNubError.JSON_PATCH_OPERATIONS_MISSING, e.pubnubError)
        }
    }

    @Test
    fun getMembershipsReturnsOnlyMembershipsTheTokenCanRead() = withChannelAndUser { channelId, userId ->
        val grantedMembershipId = "membership-granted-" + CommonUtils.randomValue()
        val ungrantedMembershipId = "membership-ungranted-" + CommonUtils.randomValue()

        // Two memberships on the same channel but different users, so both can coexist (unique per pair).
        val otherUserId = "user-other-" + CommonUtils.randomValue()
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

            Assert.assertTrue(
                "Expected the granted membership to be present in the filtered listing",
                getAllResult.data.any { it.id == grantedMembershipId },
            )
            Assert.assertTrue(
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
        val membershipId = "membership-" + CommonUtils.randomValue()
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
            Assert.assertEquals(membershipId, createResult.data.id)
            Assert.assertEquals(channelId, createResult.data.channelId)
            Assert.assertEquals(userId, createResult.data.userId)

            // get -> `get` on this specific membership
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, get = true))
            val getResult = client.dataSync.getMembership(membershipId).sync()
            Assert.assertEquals(membershipId, getResult.data.id)
            Assert.assertEquals("active", getResult.data.status)

            // patch -> `update` on this specific membership (PATCH maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, update = true))
            val patchResult = client.dataSync.updateMembership(
                membershipId = membershipId,
                operations = listOf(
                    PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"),
                ),
            ).sync()
            Assert.assertEquals("inactive", patchResult.data.status)

            // update -> `update` on this specific membership (PUT maps to `update`)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, update = true))
            val updateResult = client.dataSync.setMembership(
                membershipId = membershipId,
                classVersion = classVersion,
                status = "archived",
                payload = TestMembershipPayload(role = "member"),
            ).sync()
            Assert.assertEquals("archived", updateResult.data.status)
            Assert.assertEquals("member", updateResult.data.payload?.get("role"))

            // delete -> `delete` on this specific membership
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, delete = true))
            client.dataSync.removeMembership(membershipId).sync()

            // get after delete -> 404 (re-grant `get` so we hit a 404 rather than a permission error)
            grantAndAuthenticate(client, authorizedUUID, DataSyncGrant.membership(name = membershipId, get = true))
            try {
                client.dataSync.getMembership(membershipId).sync()
                Assert.fail("Expected a 404 after deleting the membership")
            } catch (e: PubNubException) {
                Assert.assertEquals(404, e.statusCode)
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
        val membershipId = "membership-" + CommonUtils.randomValue()
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
                Assert.fail("Expected a 403: a datasync:relationships grant must not authorize a /memberships op")
            } catch (e: PubNubException) {
                Assert.assertEquals(403, e.statusCode)
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
        val run = CommonUtils.randomValue()
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
            Assert.assertEquals(setOf(idA), filtered.data.map { it.id }.toSet())

            // sort ascending by status -> a-b-c
            val sortedAsc = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
            ).sync()
            Assert.assertEquals(listOf(idA, idB, idC), sortedAsc.data.map { it.id })

            // sort descending -> c-b-a
            val sortedDesc = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status", ascending = false)),
            ).sync()
            Assert.assertEquals(listOf(idC, idB, idA), sortedDesc.data.map { it.id })

            // limit + cursor -> page one at a time
            val firstPage = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
                limit = 1,
            ).sync()
            Assert.assertEquals(1, firstPage.data.size)
            Assert.assertEquals(idA, firstPage.data.first().id)
            Assert.assertTrue("Expected more pages after the first", firstPage.next.hasNext)
            Assert.assertNotNull(firstPage.next.cursor)

            val secondPage = server.dataSync.getMemberships(
                channelId = channelId,
                filterFast = "status LIKE \"st-$run-*\"",
                sort = listOf(PNDataSyncSortField("status")),
                limit = 1,
                cursor = firstPage.next.cursor,
            ).sync()
            Assert.assertEquals(1, secondPage.data.size)
            Assert.assertEquals(idB, secondPage.data.first().id)
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
