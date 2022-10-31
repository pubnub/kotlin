package com.pubnub.contract.membership.step

import com.pubnub.contract.loadChannelMembership
import com.pubnub.contract.uuidmetadata.state.MembershipState
import io.cucumber.java.en.Given

class GivenSteps(
    private val membershipState: MembershipState,
) {

    @Given("the data for {string} membership")
    fun the_data_for_membership(channelMembershipName: String) {
        membershipState.membership = loadChannelMembership(channelMembershipName)
    }
}
