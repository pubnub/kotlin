package com.pubnub.api.java.models.consumer.objects_api.membership

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MembershipIncludeTest {
    @Test
    fun canCreateMembershipIncludeWithAllParams() {
        val membershipInclude = MembershipInclude.builder()
            .includeCustom(true)
            .includeStatus(true)
            .includeType(true)
            .includeTotalCount(true)
            .includeChannel(true)
            .includeChannelCustom(true)
            .includeChannelType(true)
            .includeChannelStatus(true)
            .build()

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
    fun canCreateMembershipIncludeWithNoParams() {
        val membershipInclude = MembershipInclude.builder().build()

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
