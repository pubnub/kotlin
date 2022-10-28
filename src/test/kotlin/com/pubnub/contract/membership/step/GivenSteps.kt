package com.pubnub.contract.membership.step

import com.pubnub.contract.loadChannelMembership
import com.pubnub.contract.uuidmetadata.state.MembershipState
import io.cucumber.java.en.Given
import io.cucumber.java.en.Given.Givens

class GivenSteps(
    private val membershipState: MembershipState,
) {

    @Givens(
        Given("the data for {string} membership"),
        Given("the data for {string} membership that we want to add")
    )
    fun the_data_for_membership(channelMembershipName: String) {
        membershipState.memberships.add(loadChannelMembership(channelMembershipName))
    }

    @Given("the data for {string} membership that we want to remove")
    fun the_data_for_membership_that_we_want_to_remove(channelMembershipName: String) {
        membershipState.membershipsToRemove.add(loadChannelMembership(channelMembershipName))
    }
}
