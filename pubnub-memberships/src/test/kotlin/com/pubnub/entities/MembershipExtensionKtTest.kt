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
import com.pubnub.entities.models.consumer.membership.FetchMembershipsResult
import com.pubnub.entities.models.consumer.membership.MembershipsResult
import com.pubnub.entities.models.consumer.membership.SpaceIdWithCustom
import com.pubnub.entities.models.consumer.membership.UserIdWithCustom
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MembershipExtensionKtTest {
    private lateinit var pubNub: PubNub

    private val USER_ID = "pn-11111111-d67b-4e20-8b59-03f98990f247";
    private val USER_NAME = "user name";
    private val USER_ID_02 = "pn-22222222-d67b-4e20-8b59-03f98990f247";
    private val USER_NAME_02 = "user name2";

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
    internal fun can_fetchMembershipOfUser() {
        val pnChannelMembershipArrayResult = createPNChannelMembershipArrayResult()
        every { pubNub.getMemberships(any(), any(), any(), any(), any(), any(),any(), any()) } returns getMembershipsEndpoint
        every { getMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val fetchMembershipOfUserEndpoint: ExtendedRemoteAction<FetchMembershipsResult?> =
            pubNub.fetchMembershipsOfUser(userId = USER_ID)
        val fetchMembershipsResult = fetchMembershipOfUserEndpoint.sync()

        assertMembershipResultOfUser(fetchMembershipsResult)
    }

    @Test
    internal fun can_fetchMembershipOfSpace() {
        val pnMemberArrayResult = createPNMemberArrayResult()
        every { pubNub.getChannelMembers(any(), any(), any(), any(), any(), any(), any(), any() ) } returns getChannelMembersEndpoint
        every { getChannelMembersEndpoint.sync() } returns pnMemberArrayResult

        val fetchMembershipOfSpaceEndpoint: ExtendedRemoteAction<FetchMembershipsResult?> =
            pubNub.fetchMembershipsOfSpace(spaceId = SPACE_ID)
        val fetchMembershipsResult = fetchMembershipOfSpaceEndpoint.sync()

        assertMembershipResultOfSpace(fetchMembershipsResult)
    }

    @Test
    internal fun can_addMembershipOfUser() {
        val pnChannelMembershipArrayResult = createPNChannelMembershipArrayResult()
        every { pubNub.setMemberships(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageMembershipsEndpoint
        every { manageMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val spaceIdWithCustomList = listOf(SpaceIdWithCustom(SPACE_ID, SPACE_CUSTOM), SpaceIdWithCustom(SPACE_ID_02, SPACE_CUSTOM))
        val addMembershipOfUserEndpoint: ExtendedRemoteAction<MembershipsResult> =
            pubNub.addMembershipOfUser(spaceIdWithCustomList = spaceIdWithCustomList, userid = USER_ID)
        val membershipsResult = addMembershipOfUserEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }

    @Test
    internal fun can_addMembershipOfSpace() {
        val pnMemberArrayResult = createPNMemberArrayResult()
        every { pubNub.setChannelMembers(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageChannelMembersEndpoint
        every { manageChannelMembersEndpoint.sync() } returns pnMemberArrayResult


        val userIdWithCustomList = listOf(UserIdWithCustom(USER_ID, USER_CUSTOM), UserIdWithCustom(USER_ID_02, USER_CUSTOM))
        val addMembershipOfSpaceEndpoint : ExtendedRemoteAction<MembershipsResult> =
            pubNub.addMembershipOfSpace(spaceId = SPACE_ID, userIdWithCustomList = userIdWithCustomList)
        val membershipsResult = addMembershipOfSpaceEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
    }


    @Test
    internal fun can_removeMembershipOfUser() {
        val pnChannelMembershipArrayResult = PNChannelMembershipArrayResult(status = 200, data = listOf(), totalCount = 0, prev = null, next = null)
        every { pubNub.removeMemberships(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageMembershipsEndpoint
        every { manageMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val spaceIdList = listOf(SPACE_ID, SPACE_ID_02)
        val removeMembershipOfUserEndpoint: ExtendedRemoteAction<MembershipsResult> =
            pubNub.removeMembershipOfUser(spaceIds = spaceIdList, userId = USER_ID)
        val membershipsResult = removeMembershipOfUserEndpoint.sync()

        assertEquals(200, membershipsResult?.status)

    }

    @Test
    internal fun can_removeMembershipOfSpace() {
        val pnMemberArrayResult = PNMemberArrayResult(status = 200, data = listOf(), totalCount = 0, prev = null, next = null)
        every { pubNub.removeChannelMembers(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns manageChannelMembersEndpoint
        every { manageChannelMembersEndpoint.sync() }  returns pnMemberArrayResult

        val userIdList = listOf(USER_ID, USER_ID_02)
        val removeMembershipOfSpaceEndpoint: ExtendedRemoteAction<MembershipsResult> =
            pubNub.removeMembershipOfSpace(spaceId = SPACE_ID, userIdList)
        val membershipsResult = removeMembershipOfSpaceEndpoint.sync()

        assertEquals(200, membershipsResult?.status)
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
        assertEquals(UPDATED, membership01?.user?.updated)
        assertEquals(E_TAG, membership01?.user?.eTag)
        assertEquals(SPACE_ID, membership01?.space?.id)
        assertEquals(MEMBERSHIP_CUSTOM, membership01?.custom)
        assertEquals(UPDATED, membership01?.updated)
        assertEquals(E_TAG, membership01?.eTag)

        val membership02 = fetchMembershipsResult?.data?.elementAt(1)
        assertEquals(USER_ID_02, membership02?.user?.id)
        assertEquals(USER_NAME_02, membership02?.user?.name)
        assertEquals(EXTERNAL_ID, membership02?.user?.externalId)
        assertEquals(PROFILE_URL, membership02?.user?.profileUrl)
        assertEquals(EMAIL, membership02?.user?.email)
        assertEquals(USER_CUSTOM, membership02?.user?.custom)
        assertEquals(UPDATED, membership02?.user?.updated)
        assertEquals(E_TAG, membership02?.user?.eTag)
        assertEquals(SPACE_ID, membership02?.space?.id)
        assertEquals(MEMBERSHIP_CUSTOM, membership02?.custom)
        assertEquals(UPDATED, membership02?.updated)
        assertEquals(E_TAG, membership02?.eTag)

        assertEquals(2, fetchMembershipsResult?.totalCount)
    }

    private fun assertMembershipResultOfUser(fetchMembershipsResult: FetchMembershipsResult?) {
        val membership01 = fetchMembershipsResult?.data?.first()
        assertEquals(200, fetchMembershipsResult?.status)
        assertEquals(USER_ID, membership01?.user?.id)
        assertEquals(SPACE_ID, membership01?.space?.id)
        assertEquals(SPACE_NAME, membership01?.space?.name)
        assertEquals(SPACE_DESCRIPTION, membership01?.space?.description)
        assertEquals(SPACE_CUSTOM, membership01?.space?.custom)
        assertEquals(UPDATED, membership01?.space?.updated)
        assertEquals(E_TAG, membership01?.space?.eTag)
        assertEquals(MEMBERSHIP_CUSTOM, membership01?.custom)
        assertEquals(UPDATED, membership01?.updated)
        assertEquals(E_TAG, membership01?.eTag)

        val membership02 = fetchMembershipsResult?.data?.elementAt(1)
        assertEquals(USER_ID, membership02?.user?.id)
        assertEquals(SPACE_ID_02, membership02?.space?.id)
        assertEquals(SPACE_NAME_02, membership02?.space?.name)
        assertEquals(SPACE_DESCRIPTION, membership02?.space?.description)
        assertEquals(SPACE_CUSTOM, membership02?.space?.custom)
        assertEquals(UPDATED, membership02?.space?.updated)
        assertEquals(E_TAG, membership02?.space?.eTag)
        assertEquals(UPDATED, membership02?.updated)
        assertEquals(E_TAG, membership02?.eTag)

        assertEquals(2, fetchMembershipsResult?.totalCount)
    }


    private fun createPNMemberArrayResult(): PNMemberArrayResult {
        val pnMemberList = listOf(
            createPNMember(uuid = USER_ID, uuidName = USER_NAME),
            createPNMember(uuid = USER_ID_02, uuidName = USER_NAME_02)
        )
        val pnMemberArrayResult =
            PNMemberArrayResult(status = 200, data = pnMemberList, totalCount = 2, next = null, prev = null)
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
            eTag = E_TAG
        )
        return PNMember(uuid = pnUUIDMetadata, custom = MEMBERSHIP_CUSTOM, updated = UPDATED, eTag = E_TAG)
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
            eTag = E_TAG
        )
        return PNChannelMembership(
            channel = pnChannelMetadata,
            custom = membershipCustom,
            updated = UPDATED,
            eTag = E_TAG
        )
    }
}
