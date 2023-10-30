package com.pubnub.contract.member.step

import com.pubnub.contract.loadMember
import com.pubnub.contract.member.state.MemberState
import io.cucumber.java.en.Then
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers

class ThenSteps(
    private val memberState: MemberState,
) {
    @Then("the response contains list with {string} and {string} members")
    fun the_response_contains_list_with_and_members(firstMemberName: String, secondMemberName: String) {
        val firstMember = loadMember(firstMemberName)
        val secondMember = loadMember(secondMemberName)
        assertThat(memberState.returnedMembers, Matchers.containsInAnyOrder(firstMember, secondMember))
    }

    @Then("the response contains list with {string} member")
    fun the_response_contains_list_with_member(memberName: String) {
        val member = loadMember(memberName)
        assertThat(memberState.returnedMembers, Matchers.hasItem(member))
    }

    @Then("the response does not contain list with {string} member")
    fun the_response_does_not_contain_list_with_member(memberName: String) {
        val member = loadMember(memberName)
        assertThat(memberState.returnedMembers, Matchers.not(Matchers.hasItem(member)))
    }
}
