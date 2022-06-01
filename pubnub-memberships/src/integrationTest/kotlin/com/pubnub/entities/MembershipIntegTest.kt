package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.entities.models.consumer.membership.FetchMembershipsResult
import com.pubnub.entities.models.consumer.membership.SpaceDetailsLevel
import com.pubnub.entities.models.consumer.membership.SpaceIdWithCustom
import com.pubnub.entities.models.consumer.membership.UserDetailsLevel
import com.pubnub.entities.models.consumer.membership.UserIdWithCustom
import com.pubnub.entities.models.consumer.space.SpaceResult
import com.pubnub.entities.models.consumer.user.UserResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MembershipIntegTest {
    private lateinit var pubnub: PubNub

    private val USER_ID = "user_id"
    private val USER_NAME = "user name"
    private val USER_ID_02 = "user_id02"
    private val EXTERNAL_ID = "externalId"
    private val PROFILE_URL = "prorileUrl"
    private val EMAIL = "emaiL@mail.com"
    private val USER_CUSTOM = mapOf("user favourite pet" to "mouse")
    private val USER_STATUS = "user_status"
    private val USER_TYPE = "user_type"

    private val SPACE_ID = "space_id"
    private val SPACE_ID_02 = "space_id02"
    private val SPACE_NAME = "space_name"
    private val SPACE_DESCRIPTION = "space_description"
    private val SPACE_CUSTOM = mapOf("space favourite pet" to "mouse")
    private val SPACE_STATUS = "space_status"
    private val SPACE_TYPE = "space_type"
    private val MEMBERSHIP_CUSTOM = mapOf("membership favourite pet" to "mouse")
    private val MEMBERSHIP_STATUS = "membership_status"

    @BeforeEach
    fun setUp() {
        val config = PNConfiguration(USER_ID).apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
            IntegTestConf.origin?.let {
                origin = it
            }
        }
        pubnub = PubNub(config)

        removeEntities()
    }

    @Test
    internal fun can_addMembershipOfUser_and_fetchMembershipOfUser() {
        createUser(USER_ID)
        createSpace(SPACE_ID)
        createSpace(SPACE_ID_02)
        val spaceIdWithCustomList: List<SpaceIdWithCustom> =
            listOf(SpaceIdWithCustom(SPACE_ID, MEMBERSHIP_CUSTOM), SpaceIdWithCustom(SPACE_ID_02, MEMBERSHIP_CUSTOM))

        val addMembershipsResult =
            pubnub.addMembershipsOfUser(spaceIdsWithCustoms = spaceIdWithCustomList, userId = USER_ID).sync()

        assertEquals(200, addMembershipsResult?.status)
        val fetchMembershipsResult = pubnub.fetchMembershipsOfUser(
            userId = USER_ID,
            includeCustom = true,
            includeSpaceDetails = SpaceDetailsLevel.SPACE_WITH_CUSTOM,
            includeCount = true
        ).sync()
        assertMembershipResultOfUser(fetchMembershipsResult)
    }

    @Test
    internal fun can_addMembershipOfSpace_and_fetchMembershipOfSpace() {
        createUser(USER_ID)
        createUser(USER_ID_02)
        createSpace(SPACE_ID)
        val userIdsWithCustoms: List<UserIdWithCustom> =
            listOf(UserIdWithCustom(USER_ID, MEMBERSHIP_CUSTOM), UserIdWithCustom(USER_ID_02, MEMBERSHIP_CUSTOM))

        val addMembershipsResult =
            pubnub.addMembershipsOfSpace(spaceId = SPACE_ID, userIdsWithCustoms = userIdsWithCustoms).sync()

        assertEquals(200, addMembershipsResult?.status)
        val fetchMembershipsResult = pubnub.fetchMembershipsOfSpace(
            spaceId = SPACE_ID,
            includeCustom = true,
            includeUserDetails = UserDetailsLevel.USER_WITH_CUSTOM,
            includeCount = true
        ).sync()
        assertMembershipResultOfSpace(fetchMembershipsResult)
    }

    @Test
    internal fun can_removeMembershipOfUser() {
        val fetchMembershipsResultNoMembership =
            pubnub.fetchMembershipsOfUser(userId = USER_ID, includeCount = true).sync()
        assertEquals(0, fetchMembershipsResultNoMembership?.totalCount)
        createUser(USER_ID)
        createSpace(SPACE_ID)
        createSpace(SPACE_ID_02)
        val spaceIdWithCustomList: List<SpaceIdWithCustom> =
            listOf(SpaceIdWithCustom(SPACE_ID, MEMBERSHIP_CUSTOM), SpaceIdWithCustom(SPACE_ID_02, MEMBERSHIP_CUSTOM))
        pubnub.addMembershipsOfUser(spaceIdsWithCustoms = spaceIdWithCustomList, userId = USER_ID).sync()
        val fetchMembershipsResultAfterAdd = pubnub.fetchMembershipsOfUser(userId = USER_ID, includeCount = true).sync()
        assertEquals(2, fetchMembershipsResultAfterAdd?.totalCount)

        val removeMembershipsResult =
            pubnub.removeMembershipsOfUser(spaceIds = listOf(SPACE_ID, SPACE_ID_02), userId = USER_ID).sync()
        assertEquals(200, removeMembershipsResult?.status)

        val fetchMembershipsResultAfterRemove =
            pubnub.fetchMembershipsOfUser(userId = USER_ID, includeCount = true).sync()
        assertEquals(0, fetchMembershipsResultAfterRemove?.totalCount)
    }

    @Test
    internal fun can_removeMembershipOfSpace() {
        val fetchMembershipsResultNoMembership =
            pubnub.fetchMembershipsOfSpace(spaceId = SPACE_ID, includeCount = true).sync()
        assertEquals(0, fetchMembershipsResultNoMembership?.totalCount)
        createUser(USER_ID)
        createUser(USER_ID_02)
        createSpace(SPACE_ID)
        val userIdsWithCustoms: List<UserIdWithCustom> =
            listOf(UserIdWithCustom(USER_ID, MEMBERSHIP_CUSTOM), UserIdWithCustom(USER_ID_02, MEMBERSHIP_CUSTOM))
        pubnub.addMembershipsOfSpace(spaceId = SPACE_ID, userIdsWithCustoms = userIdsWithCustoms).sync()
        val fetchMembershipsResultAfterAdd =
            pubnub.fetchMembershipsOfSpace(spaceId = SPACE_ID, includeCount = true).sync()
        assertEquals(2, fetchMembershipsResultAfterAdd?.totalCount)

        val removeMembershipsResult =
            pubnub.removeMembershipsOfSpace(spaceId = SPACE_ID, userIds = listOf(USER_ID, USER_ID_02)).sync()
        assertEquals(200, removeMembershipsResult?.status)

        val fetchMembershipsResultAfterRemove =
            pubnub.fetchMembershipsOfSpace(spaceId = SPACE_ID, includeCount = true).sync()
        assertEquals(0, fetchMembershipsResultAfterRemove?.totalCount)
    }

    @Test
    internal fun can_updateMembershipOfUser_by_adding_membership() {
        createUser(USER_ID)
        createSpace(SPACE_ID)
        val spaceIdWithCustomList = listOf(SpaceIdWithCustom(SPACE_ID, MEMBERSHIP_CUSTOM))
        pubnub.addMembershipsOfUser(spaceIdsWithCustoms = spaceIdWithCustomList, userId = USER_ID).sync()
        val fetchMembershipsResult = pubnub.fetchMembershipsOfUser(
            userId = USER_ID,
            includeCustom = true,
            includeSpaceDetails = SpaceDetailsLevel.SPACE_WITH_CUSTOM,
            includeCount = true
        ).sync()
        assertEquals(1, fetchMembershipsResult?.totalCount)

        createSpace(SPACE_ID_02)
        val updateMembershipsResult = pubnub.updateMembershipsOfUser(
            userId = USER_ID,
            spaceIdsWithCustoms = listOf(SpaceIdWithCustom(SPACE_ID_02, MEMBERSHIP_CUSTOM))
        ).sync()
        assertEquals(200, updateMembershipsResult?.status)

        val fetchMembershipsResultAfterUpdate = pubnub.fetchMembershipsOfUser(
            userId = USER_ID,
            includeCount = true,
            includeCustom = true,
            includeSpaceDetails = SpaceDetailsLevel.SPACE_WITH_CUSTOM
        ).sync()
        assertEquals(2, fetchMembershipsResultAfterUpdate?.totalCount)
        assertEquals(MEMBERSHIP_CUSTOM, fetchMembershipsResultAfterUpdate?.data?.first()?.custom)
    }

    @Test
    internal fun can_updateMembershipOfUser_by_adding_membership_custom() {
        createUser(USER_ID)
        createSpace(SPACE_ID)
        val spaceIdWithCustomList = listOf(SpaceIdWithCustom(SPACE_ID))
        pubnub.addMembershipsOfUser(spaceIdsWithCustoms = spaceIdWithCustomList, userId = USER_ID).sync()

        pubnub.updateMembershipsOfUser(
            userId = USER_ID,
            spaceIdsWithCustoms = listOf(SpaceIdWithCustom(SPACE_ID, MEMBERSHIP_CUSTOM)),
            status = MEMBERSHIP_STATUS
        ).sync()

        val fetchMembershipsResultAfterUpdate = pubnub.fetchMembershipsOfUser(
            userId = USER_ID,
            includeCount = true,
            includeCustom = true,
            includeSpaceDetails = SpaceDetailsLevel.SPACE_WITH_CUSTOM
        ).sync()
        assertEquals(1, fetchMembershipsResultAfterUpdate?.totalCount)
        assertEquals(MEMBERSHIP_CUSTOM, fetchMembershipsResultAfterUpdate?.data?.first()?.custom)
    }

    @Test
    internal fun can_updateMembershipOfSpace() {
        createUser(USER_ID)
        createSpace(SPACE_ID)
        val userIdsWithCustoms = listOf(UserIdWithCustom(userId = USER_ID, custom = MEMBERSHIP_CUSTOM))
        pubnub.addMembershipsOfSpace(spaceId = SPACE_ID, userIdsWithCustoms = userIdsWithCustoms).sync()
        val fetchMembershipsResult = pubnub.fetchMembershipsOfSpace(spaceId = SPACE_ID, includeCount = true).sync()
        assertEquals(1, fetchMembershipsResult?.totalCount)

        createUser(USER_ID_02)
        val updateMembershipsResult = pubnub.updateMembershipsOfSpace(
            spaceId = SPACE_ID,
            userIdsWithCustoms = listOf(UserIdWithCustom(USER_ID_02, MEMBERSHIP_CUSTOM)),
            status = MEMBERSHIP_STATUS
        ).sync()
        assertEquals(200, updateMembershipsResult?.status)

        val fetchMembershipsResultAfterUpdate = pubnub.fetchMembershipsOfSpace(
            spaceId = SPACE_ID,
            includeCount = true,
            includeCustom = true,
            includeUserDetails = UserDetailsLevel.USER_WITH_CUSTOM
        ).sync()
        assertEquals(2, fetchMembershipsResultAfterUpdate?.totalCount)
        assertEquals(MEMBERSHIP_CUSTOM, fetchMembershipsResultAfterUpdate?.data?.first()?.custom)
    }

    @Test
    internal fun can_updateMembershipOfSpace_by_adding_membership_custom() {
        createUser(USER_ID)
        createSpace(SPACE_ID)
        val userIdsWithCustoms = listOf(UserIdWithCustom(userId = USER_ID))
        pubnub.addMembershipsOfSpace(spaceId = SPACE_ID, userIdsWithCustoms = userIdsWithCustoms).sync()

        pubnub.updateMembershipsOfSpace(
            spaceId = SPACE_ID,
            userIdsWithCustoms = listOf(UserIdWithCustom(USER_ID, MEMBERSHIP_CUSTOM))
        ).sync()

        val fetchMembershipsResultAfterUpdate = pubnub.fetchMembershipsOfSpace(
            spaceId = SPACE_ID,
            includeCount = true,
            includeCustom = true,
            includeUserDetails = UserDetailsLevel.USER_WITH_CUSTOM
        ).sync()
        assertEquals(1, fetchMembershipsResultAfterUpdate?.totalCount)
        assertEquals(MEMBERSHIP_CUSTOM, fetchMembershipsResultAfterUpdate?.data?.first()?.custom)
    }

    @AfterEach
    internal fun tearDown() {
        removeEntities()
    }

    private fun removeEntities() {
        pubnub.removeSpace(spaceId = SPACE_ID).sync()
        pubnub.removeSpace(spaceId = SPACE_ID_02).sync()
        pubnub.removeUser(userId = USER_ID).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
        pubnub.removeMembershipsOfSpace(spaceId = SPACE_ID, listOf(USER_ID, USER_ID_02)).sync()
        pubnub.removeMembershipsOfSpace(spaceId = SPACE_ID_02, listOf(USER_ID, USER_ID_02)).sync()
    }

    private fun createUser(userId: String): UserResult? {
        return pubnub.createUser(
            userId = userId,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = USER_CUSTOM,
            includeCustom = true,
            status = USER_STATUS,
            type = USER_TYPE
        ).sync()
    }

    private fun createSpace(spaceId: String): SpaceResult? {
        return pubnub.createSpace(
            spaceId = spaceId,
            name = SPACE_NAME,
            description = SPACE_DESCRIPTION,
            custom = SPACE_CUSTOM,
            includeCustom = true,
            status = SPACE_STATUS,
            type = SPACE_TYPE
        ).sync()
    }

    private fun assertMembershipResultOfUser(fetchMembershipsResult: FetchMembershipsResult?) {
        val membership01 = fetchMembershipsResult?.data?.first()
        assertEquals(200, fetchMembershipsResult?.status)
        assertEquals(USER_ID, membership01?.user?.id)
        assertEquals(SPACE_ID, membership01?.space?.id)
        assertEquals(SPACE_NAME, membership01?.space?.name)
        assertEquals(SPACE_DESCRIPTION, membership01?.space?.description)
        assertEquals(SPACE_CUSTOM, membership01?.space?.custom)
        assertTrue(membership01?.space?.updated != null)
        assertTrue(membership01?.space?.eTag != null)
        assertEquals(MEMBERSHIP_CUSTOM, membership01?.custom)
        assertTrue(membership01?.updated != null)
        assertTrue(membership01?.eTag != null)

        val membership02 = fetchMembershipsResult?.data?.elementAt(1)
        assertEquals(USER_ID, membership02?.user?.id)
        assertEquals(SPACE_ID_02, membership02?.space?.id)
        assertEquals(SPACE_NAME, membership02?.space?.name)
        assertEquals(SPACE_DESCRIPTION, membership02?.space?.description)
        assertEquals(SPACE_CUSTOM, membership02?.space?.custom)
        assertTrue(membership02?.space?.updated != null)
        assertTrue(membership02?.space?.eTag != null)
        assertTrue(membership02?.updated != null)
        assertTrue(membership02?.eTag != null)

        assertEquals(2, fetchMembershipsResult?.totalCount)
    }

    private fun assertMembershipResultOfSpace(fetchMembershipsResult: FetchMembershipsResult?) {
        assertEquals(200, fetchMembershipsResult?.status)
        val membership01 = fetchMembershipsResult?.data?.first()
        assertEquals(USER_ID, membership01?.user?.id)
        assertEquals(USER_NAME, membership01?.user?.name)
        assertEquals(EXTERNAL_ID, membership01?.user?.externalId)
        assertEquals(PROFILE_URL, membership01?.user?.profileUrl)
        assertEquals(EMAIL, membership01?.user?.email)
        assertEquals(USER_CUSTOM, membership01?.user?.custom)
        assertTrue(membership01?.user?.updated != null)
        assertTrue(membership01?.user?.eTag != null)
        assertEquals(SPACE_ID, membership01?.space?.id)
        assertEquals(MEMBERSHIP_CUSTOM, membership01?.custom)
        assertTrue(membership01?.updated != null)
        assertTrue(membership01?.eTag != null)

        val membership02 = fetchMembershipsResult?.data?.elementAt(1)
        assertEquals(USER_ID_02, membership02?.user?.id)
        assertEquals(USER_NAME, membership02?.user?.name)
        assertEquals(EXTERNAL_ID, membership02?.user?.externalId)
        assertEquals(PROFILE_URL, membership02?.user?.profileUrl)
        assertEquals(EMAIL, membership02?.user?.email)
        assertEquals(USER_CUSTOM, membership02?.user?.custom)
        assertTrue(membership02?.user?.updated != null)
        assertTrue(membership02?.user?.eTag != null)
        assertEquals(SPACE_ID, membership02?.space?.id)
        assertEquals(MEMBERSHIP_CUSTOM, membership02?.custom)
        assertTrue(membership02?.updated != null)
        assertTrue(membership02?.eTag != null)

        assertEquals(2, fetchMembershipsResult?.totalCount)
    }
}
