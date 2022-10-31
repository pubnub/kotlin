package com.pubnub.contract.membership.step

import com.pubnub.contract.loadChannelMembership
import com.pubnub.contract.uuidmetadata.state.MembershipState
import io.cucumber.java.en.Then
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder

class ThenSteps(
    private val membershipState: MembershipState,
) {
    @Then("the response contains list with {string} and {string} memberships")
    fun the_response_contains_list_with_and_memberships(
        firstChannelMembershipName: String,
        secondChannelMembershipName: String
    ) {
        val firstChannelMembership = loadChannelMembership(firstChannelMembershipName)
        val secondChannelMembership = loadChannelMembership(secondChannelMembershipName)

        assertThat(membershipState.memberships, containsInAnyOrder(firstChannelMembership, secondChannelMembership))
    }

    @Then("the response contains list with {string} membership")
    fun the_response_contains_list_with_and_membership(
        firstChannelMembershipName: String,
    ) {
        val firstChannelMembership = loadChannelMembership(firstChannelMembershipName)

        assertThat(membershipState.memberships, containsInAnyOrder(firstChannelMembership))
    }
}
