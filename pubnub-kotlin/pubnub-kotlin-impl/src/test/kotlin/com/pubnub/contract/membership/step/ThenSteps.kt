package com.pubnub.contract.membership.step

import com.pubnub.contract.loadChannelMembership
import com.pubnub.contract.uuidmetadata.state.MembershipState
import io.cucumber.java.en.Then
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.not

class ThenSteps(
    private val membershipState: MembershipState,
) {
    @Then("the response contains list with {string} and {string} memberships")
    fun the_response_contains_list_with_and_memberships(
        firstChannelMembershipName: String,
        secondChannelMembershipName: String,
    ) {
        val firstChannelMembership = loadChannelMembership(firstChannelMembershipName)
        val secondChannelMembership = loadChannelMembership(secondChannelMembershipName)

        assertThat(
            membershipState.returnedMemberships,
            containsInAnyOrder(firstChannelMembership, secondChannelMembership),
        )
    }

    @Then("the response contains list with {string} membership")
    fun the_response_contains_list_with_and_membership(channelMembershipName: String) {
        val channelMembership = loadChannelMembership(channelMembershipName)

        assertThat(membershipState.returnedMemberships, hasItem(channelMembership))
    }

    @Then("the response does not contain list with {string} membership")
    fun the_response_does_not_contain_list_with_patient_membership_membership(channelMembershipName: String) {
        val channelMembership = loadChannelMembership(channelMembershipName)

        assertThat(membershipState.returnedMemberships, not(hasItem(channelMembership)))
    }
}
