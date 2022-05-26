package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.member.GetChannelMembers
import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.entities.models.consumer.membership.MembershipsResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MembershipExtensionKtTest {
    private var pubNub: PubNub? = null

    private val USER_ID = "pn-11111111-d67b-4e20-8b59-03f98990f247";
    private val USER_NAME = "user name";
    private val USER_ID_02 = "pn-22222222-d67b-4e20-8b59-03f98990f247";
    private val USER_NAME_02 = "user name2";

    private val SPACE_ID = "spaceId"
    private val SPACE_ID_02 = "spaceId02"
    private val SPACE_NAME = "spaceName"
    private val SPACE_NAME_02 = "spaceName02"
    private val SPACE_DESCRIPTION = "spaceDescription"
    private val SPACE_CUSTOM = "spaceCustom"
    private val MEMBERSHIP_CUSTOM = "membershipCustom"
    private val MEMBERSHIP_CUSTOM_02 = "membershipCustom02"
    private val USER_CUSTOM = "userCustom"
    private val UPDATED = "updated"
    private val E_TAG = "eTag"
    private val EXTERNAL_ID = "externalId"
    private val EMAIL = "email@mail.com"
    private val PROFILE_URL = "profileUrl"

    @MockK
    private lateinit var getMembershipsEndpoint: GetMemberships

    @MockK
    private lateinit var getChannelMembersEndpoint: GetChannelMembers

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration = PNConfiguration(USER_ID)
        pubNub = spyk(PubNub(configuration = pnConfiguration))
    }

    @Test
    internal fun can_fetchMembershipOfUser() {
        val pnChannelMembershipList = listOf(
            createPNChannelMembership(SPACE_ID, SPACE_NAME, MEMBERSHIP_CUSTOM),
            createPNChannelMembership(SPACE_ID_02, SPACE_NAME_02, MEMBERSHIP_CUSTOM_02)
        )
        val pnChannelMembershipArrayResult =
            PNChannelMembershipArrayResult(
                status = 200,
                data = pnChannelMembershipList,
                totalCount = 2,
                next = null,
                prev = null
            )
        every {
            pubNub?.getMemberships(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns getMembershipsEndpoint
        every { getMembershipsEndpoint.sync() } returns pnChannelMembershipArrayResult

        val fetchMembershipOfUserEndpoint: ExtendedRemoteAction<MembershipsResult?>? =
            pubNub?.fetchMembershipsOfUser(userId = USER_ID)
        val membershipsResult = fetchMembershipOfUserEndpoint?.sync()

        val membership01 = membershipsResult?.data?.first()
        assertEquals(200, membershipsResult?.status)
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

        val membership02 = membershipsResult?.data?.elementAt(1)
        assertEquals(USER_ID, membership02?.user?.id)
        assertEquals(SPACE_ID_02, membership02?.space?.id)
        assertEquals(SPACE_NAME_02, membership02?.space?.name)
        assertEquals(SPACE_DESCRIPTION, membership02?.space?.description)
        assertEquals(SPACE_CUSTOM, membership02?.space?.custom)
        assertEquals(UPDATED, membership02?.space?.updated)
        assertEquals(E_TAG, membership02?.space?.eTag)
        assertEquals(UPDATED, membership02?.updated)
        assertEquals(E_TAG, membership02?.eTag)

        assertEquals(2, membershipsResult?.totalCount)
    }

    @Test
    internal fun can_fetchMembershipOfSpace() {
        val pnMemberList = listOf(createPNMember(uuid = USER_ID, uuidName = USER_NAME ), createPNMember(uuid = USER_ID_02 , uuidName = USER_NAME_02))
        val pnMemberArrayResult = PNMemberArrayResult(status = 200, data = pnMemberList, totalCount = 2, next = null, prev = null)
        every {
            pubNub?.getChannelMembers(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns getChannelMembersEndpoint
        every { getChannelMembersEndpoint.sync() } returns pnMemberArrayResult

        val fetchMembershipOfSpaceEndpoint: ExtendedRemoteAction<MembershipsResult?>? =
            pubNub?.fetchMembershipsOfSpace(spaceId = SPACE_ID)
        val membershipsResult = fetchMembershipOfSpaceEndpoint?.sync()

        assertEquals(200, membershipsResult?.status)
        val membership01 = membershipsResult?.data?.first()
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

        val membership02 = membershipsResult?.data?.elementAt(1)
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

        assertEquals(2, membershipsResult?.totalCount)
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

}