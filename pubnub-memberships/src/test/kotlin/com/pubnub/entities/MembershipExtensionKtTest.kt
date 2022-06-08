package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.member.GetChannelMembers
import com.pubnub.api.endpoints.objects.member.ManageChannelMembers
import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.endpoints.objects.membership.ManageMemberships
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.entities.models.consumer.membership.Membership
import com.pubnub.entities.models.consumer.membership.MembershipsResult
import com.pubnub.entities.models.consumer.membership.MembershipsStatusResult
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.user.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MembershipExtensionKtTest {
    private lateinit var pubNub: PubNub

    private val USER_ID = "pn-11111111-d67b-4e20-8b59-03f98990f247"
    private val USER_NAME = "user name"
    private val USER_ID_02 = "pn-22222222-d67b-4e20-8b59-03f98990f247"
    private val USER_NAME_02 = "user name2"

    private val SPACE_ID = "spaceId"
    private val SPACE_ID_02 = "spaceId02"
    private val SPACE_NAME = "spaceName"
    private val SPACE_NAME_02 = "spaceName02"
    private val SPACE_DESCRIPTION = "spaceDescription"
    private val SPACE_CUSTOM = mapOf("favouriteColour" to "green")
    private val MEMBERSHIP_CUSTOM = mapOf("membershipCustom" to "green")
    private val MEMBERSHIP_CUSTOM_02 = mapOf("membershipCustom02" to "green")
    private val USER_CUSTOM = mapOf("favouriteColour" to "green")
    private val UPDATED = "updated"
    private val E_TAG = "eTag"
    private val EXTERNAL_ID = "externalId"
    private val EMAIL = "email@mail.com"
    private val PROFILE_URL = "profileUrl"
    private val TYPE = "type"
    private val STATUS = "status"

    @MockK
    private lateinit var getMembershipsEndpoint: GetMemberships

    @MockK
    private lateinit var getChannelMembersEndpoint: GetChannelMembers

    @MockK
    private lateinit var manageMembershipsEndpoint: ManageMemberships

    @MockK
    private lateinit var manageChannelMembersEndpoint: ManageChannelMembers

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration = PNConfiguration(USER_ID)
        pubNub = spyk(PubNub(configuration = pnConfiguration))
    }

    @Test
    internal fun can_fetchMembershipsOfUser() {
        val pnChannelMembershipArrayResult = createPNChannelMembershipArrayResult()
        every { pubNub.getMemberships(any(), any(), any(), any(), any(), any(), any(), any()) } returns getMembershipsEndpoint
        every { getMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val fetchMembershipOfUserEndpoint: ExtendedRemoteAction<MembershipsResult?> =
            pubNub.fetchMembershipsOfUser(userId = USER_ID)
        val fetchMembershipsResult = fetchMembershipOfUserEndpoint.sync()

        assertMembershipResultOfUser(fetchMembershipsResult)
    }

    @Test
    internal fun can_fetchMembershipsOfSpace() {
        val pnMemberArrayResult = createPNMemberArrayResult()
        every { pubNub.getChannelMembers(any(), any(), any(), any(), any(), any(), any(), any()) } returns getChannelMembersEndpoint
        every { getChannelMembersEndpoint.sync() } returns pnMemberArrayResult

        val fetchMembershipOfSpaceEndpoint: ExtendedRemoteAction<MembershipsResult?> =
            pubNub.fetchMembershipsOfSpace(spaceId = SPACE_ID)
        val fetchMembershipsResult = fetchMembershipOfSpaceEndpoint.sync()

        assertMembershipResultOfSpace(fetchMembershipsResult)
    }

    @Test
    internal fun can_addMembershipsOfUser() {
        val pnChannelMembershipArrayResult = createPNChannelMembershipArrayResult()
        every { pubNub.setMemberships(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageMembershipsEndpoint
        every { manageMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val spaceIdWithCustomList = listOf(Membership.PartialWithSpace(SPACE_ID, SPACE_CUSTOM), Membership.PartialWithSpace(SPACE_ID_02, SPACE_CUSTOM))
        val addMembershipOfUserEndpoint: ExtendedRemoteAction<MembershipsStatusResult> =
            pubNub.addMembershipsOfUser(spaceIdsWithCustoms = spaceIdWithCustomList, userId = USER_ID)
        val membershipsResult = addMembershipOfUserEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    @Test
    internal fun can_addMembershipsOfSpace() {
        val pnMemberArrayResult = createPNMemberArrayResult()
        every { pubNub.setChannelMembers(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageChannelMembersEndpoint
        every { manageChannelMembersEndpoint.sync() } returns pnMemberArrayResult

        val userIdsWithCustoms = listOf(Membership.PartialWithUser(USER_ID, USER_CUSTOM), Membership.PartialWithUser(USER_ID_02, USER_CUSTOM))
        val addMembershipOfSpaceEndpoint: ExtendedRemoteAction<MembershipsStatusResult> =
            pubNub.addMembershipsOfSpace(spaceId = SPACE_ID, userIdsWithCustoms = userIdsWithCustoms)
        val membershipsResult = addMembershipOfSpaceEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    @Test
    internal fun can_removeMembershipsOfUser() {
        val pnChannelMembershipArrayResult = PNChannelMembershipArrayResult(status = 200, data = listOf(), totalCount = 0, prev = null, next = null)
        every { pubNub.removeMemberships(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageMembershipsEndpoint
        every { manageMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val spaceIdList = listOf(SPACE_ID, SPACE_ID_02)
        val removeMembershipOfUserEndpoint: ExtendedRemoteAction<MembershipsStatusResult> =
            pubNub.removeMembershipsOfUser(spaceIds = spaceIdList, userId = USER_ID)
        val membershipsResult = removeMembershipOfUserEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    @Test
    internal fun can_removeMembershipsOfSpace() {
        val pnMemberArrayResult = PNMemberArrayResult(status = 200, data = listOf(), totalCount = 0, prev = null, next = null)
        every { pubNub.removeChannelMembers(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageChannelMembersEndpoint
        every { manageChannelMembersEndpoint.sync() } returns pnMemberArrayResult

        val userIdList = listOf(USER_ID, USER_ID_02)
        val removeMembershipOfSpaceEndpoint: ExtendedRemoteAction<MembershipsStatusResult> =
            pubNub.removeMembershipsOfSpace(spaceId = SPACE_ID, userIdList)
        val membershipsResult = removeMembershipOfSpaceEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    @Test
    internal fun can_updateMembershipsOfUser() {
        val pnChannelMembershipArrayResult = createPNChannelMembershipArrayResult()
        every { pubNub.setMemberships(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageMembershipsEndpoint
        every { manageMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val spaceIdWithCustomListToBeUpserted = listOf(Membership.PartialWithSpace(SPACE_ID_02, SPACE_CUSTOM))
        val updateMembershipOfUserEndpoint = pubNub.updateMembershipsOfUser(spaceIdsWithCustoms = spaceIdWithCustomListToBeUpserted, userId = USER_ID)
        val membershipsResult = updateMembershipOfUserEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    @Test
    internal fun can_updateMembershipsOfSpace() {
        val pnMemberArrayResult = createPNMemberArrayResult()
        every { pubNub.setChannelMembers(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageChannelMembersEndpoint
        every { manageChannelMembersEndpoint.sync() } returns pnMemberArrayResult

        val userIdsWithCustomsToBeAdded = listOf(Membership.PartialWithUser(USER_ID, USER_CUSTOM), Membership.PartialWithUser(USER_ID_02, USER_CUSTOM))
        val updateMembershipOfSpaceEndpoint = pubNub.updateMembershipsOfSpace(spaceId = SPACE_ID, userIdsWithCustoms = userIdsWithCustomsToBeAdded)
        val membershipsResult = updateMembershipOfSpaceEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    private fun assertMembershipResultOfSpace(fetchMembershipsResult: MembershipsResult?) {
        val user01 = User(id = USER_ID, name = USER_NAME, externalId = EXTERNAL_ID, profileUrl = PROFILE_URL, email = EMAIL, custom = USER_CUSTOM, updated = UPDATED, eTag = E_TAG, type = TYPE, status = STATUS)
        val space01 = Space(id = SPACE_ID)
        val user02 = User(id = USER_ID_02, name = USER_NAME_02, externalId = EXTERNAL_ID, profileUrl = PROFILE_URL, email = EMAIL, custom = USER_CUSTOM, updated = UPDATED, eTag = E_TAG, type = TYPE, status = STATUS)
        val space02 = Space(id = SPACE_ID)
        val membership01 = Membership(user = user01, space = space01, custom = MEMBERSHIP_CUSTOM, updated = UPDATED, eTag = E_TAG, status = STATUS)
        val membership02 = Membership(user = user02, space = space02, custom = MEMBERSHIP_CUSTOM, updated = UPDATED, eTag = E_TAG, status = STATUS)
        val memberships = listOf(membership01, membership02)
        val expectedFetchMembershipsResult = MembershipsResult(status = 200, data = memberships, totalCount = 2, next = null, prev = null)

        assertEquals(expectedFetchMembershipsResult, fetchMembershipsResult)
    }

    private fun assertMembershipResultOfUser(fetchMembershipsResult: MembershipsResult?) {
        val user01 = User(id = USER_ID)
        val space01 = Space(id = SPACE_ID, name = SPACE_NAME, description = SPACE_DESCRIPTION, custom = SPACE_CUSTOM, updated = UPDATED, eTag = E_TAG, type = TYPE, status = STATUS)
        val user02 = User(id = USER_ID)
        val space02 = Space(id = SPACE_ID_02, name = SPACE_NAME_02, description = SPACE_DESCRIPTION, custom = SPACE_CUSTOM, updated = UPDATED, eTag = E_TAG, type = TYPE, status = STATUS)
        val membership01 = Membership(user = user01, space = space01, custom = MEMBERSHIP_CUSTOM, updated = UPDATED, eTag = E_TAG, status = STATUS)
        val membership02 = Membership(user = user02, space = space02, custom = MEMBERSHIP_CUSTOM_02, updated = UPDATED, eTag = E_TAG, status = STATUS)
        val memberships = listOf(membership01, membership02)
        val expectedMembershipsResult = MembershipsResult(status = 200, data = memberships, totalCount = 2, next = null, prev = null)

        assertEquals(expectedMembershipsResult, fetchMembershipsResult)
    }

    private fun createPNMemberArrayResult(): PNMemberArrayResult {
        val pnMemberList = listOf(
            createPNMember(uuid = USER_ID, uuidName = USER_NAME),
            createPNMember(uuid = USER_ID_02, uuidName = USER_NAME_02)
        )
        val pnMemberArrayResult = PNMemberArrayResult(status = 200, data = pnMemberList, totalCount = 2, next = null, prev = null)
        return pnMemberArrayResult
    }

    private fun createPNMember(uuid: String, uuidName: String): PNMember {
        val pnUUIDMetadata = PNUUIDMetadata(
            id = uuid,
            name = uuidName,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = USER_CUSTOM,
            updated = UPDATED,
            eTag = E_TAG,
            type = TYPE,
            status = STATUS
        )
        return PNMember(uuid = pnUUIDMetadata, custom = MEMBERSHIP_CUSTOM, updated = UPDATED, eTag = E_TAG, status = STATUS)
    }

    private fun createPNChannelMembershipArrayResult(): PNChannelMembershipArrayResult {
        val pnChannelMembershipList = listOf(
            createPNChannelMembership(SPACE_ID, SPACE_NAME, MEMBERSHIP_CUSTOM),
            createPNChannelMembership(SPACE_ID_02, SPACE_NAME_02, MEMBERSHIP_CUSTOM_02)
        )
        return PNChannelMembershipArrayResult(
            status = 200,
            data = pnChannelMembershipList,
            totalCount = 2,
            next = null,
            prev = null
        )
    }

    private fun createPNChannelMembership(
        channelId: String,
        channelName: String,
        membershipCustom: Any
    ): PNChannelMembership {
        val pnChannelMetadata = PNChannelMetadata(
            id = channelId,
            name = channelName,
            description = SPACE_DESCRIPTION,
            custom = SPACE_CUSTOM,
            updated = UPDATED,
            eTag = E_TAG,
            type = TYPE,
            status = STATUS
        )
        return PNChannelMembership(
            channel = pnChannelMetadata,
            custom = membershipCustom,
            updated = UPDATED,
            eTag = E_TAG,
            status = STATUS
        )
    }
}
