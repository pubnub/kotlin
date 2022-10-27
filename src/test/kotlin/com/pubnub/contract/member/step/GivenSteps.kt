package com.pubnub.contract.member.step

import com.pubnub.contract.loadMember
import com.pubnub.contract.member.state.MemberState
import io.cucumber.java.en.Given

class GivenSteps(
    private val memberState: MemberState,
) {

    @Given("the data for {string} member")
    fun the_data_for_member(memberName: String) {
        memberState.member = loadMember(memberName)
    }
}
