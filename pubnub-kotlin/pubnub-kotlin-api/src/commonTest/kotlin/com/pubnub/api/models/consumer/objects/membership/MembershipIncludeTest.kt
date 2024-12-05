package com.pubnub.api.models.consumer.objects.membership

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MembershipIncludeTest {
    @Test
    fun canCreateMembershipIncludeWithAllParameters() {
        val membershipInclude = MembershipInclude(
            includeCustom = true,
            includeStatus = true,
            includeType = true,
            includeTotalCount = true,
            includeChannel = true,
            includeChannelCustom = true,
            includeChannelType = true,
            includeChannelStatus = true
        )

        assertTrue(membershipInclude.includeCustom)
        assertTrue(membershipInclude.includeStatus)
        assertTrue(membershipInclude.includeType)
        assertTrue(membershipInclude.includeTotalCount)
        assertTrue(membershipInclude.includeChannel)
        assertTrue(membershipInclude.includeChannelCustom)
        assertTrue(membershipInclude.includeChannelType)
        assertTrue(membershipInclude.includeChannelStatus)
    }

    @Test
    fun canCreateMembershipIncludeDefault() {
        val membershipInclude = MembershipInclude()

        assertFalse(membershipInclude.includeCustom)
        assertFalse(membershipInclude.includeStatus)
        assertFalse(membershipInclude.includeType)
        assertFalse(membershipInclude.includeTotalCount)
        assertFalse(membershipInclude.includeChannel)
        assertFalse(membershipInclude.includeChannelCustom)
        assertFalse(membershipInclude.includeChannelType)
        assertFalse(membershipInclude.includeChannelStatus)
    }
}
