package com.pubnub.contract.member.step

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.contract.loadMember
import com.pubnub.contract.member.state.MemberState
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers

class MemberSteps(
    private val memberState: MemberState,
) {

    @Given("the data for {string} member")
    fun the_data_for_member(memberName: String) {
        memberState.members.add(loadMember(memberName))
    }

    @Given("the data for {string} member that we want to remove")
    fun the_data_for_member_that_we_want_to_delete(memberName: String) {
        memberState.membersToRemove.add(loadMember(memberName))
    }

    @When("I get the channel members")
    fun i_get_the_channel_members() {
        memberState.pubnub.getChannelMembers(channel = memberState.channelId()).sync()?.let {
            memberState.returnedMembers = it.data
            memberState.responseStatus = it.status
        }
    }

    @When("I set a channel member including custom and UUID with custom")
    fun i_set_a_channel_member_including_custom_and_uuid_with_custom() {
        val uuids = memberState.members.map { PNMember.Partial(uuidId = it.uuid?.id!!) }
        memberState.pubnub.setChannelMembers(
            channel = memberState.channelId(),
            includeCustom = true,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            uuids = uuids
        ).sync()?.let {
            memberState.returnedMembers = it.data
            memberState.responseStatus = it.status
        }
    }

    @When("I get the channel members including custom and UUID custom information")
    fun i_get_the_channel_members_including_custom_and_uuid_custom_information() {
        memberState.pubnub.getChannelMembers(
            channel = memberState.channelId(),
            includeCustom = true,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM
        ).sync()?.let {
            memberState.returnedMembers = it.data
            memberState.responseStatus = it.status
        }
    }

    @When("I set a channel member")
    fun i_set_a_channel_member() {
        val uuids = memberState.members.map { PNMember.Partial(uuidId = it.uuid?.id!!) }
        memberState.pubnub.setChannelMembers(
            channel = memberState.channelId(),
            uuids = uuids
        ).sync()?.let {
            memberState.returnedMembers = it.data
            memberState.responseStatus = it.status
        }
    }

    @When("I remove a channel member")
    fun i_remove_a_channel_member() {
        val uuids = memberState.membersToRemove.map { it.uuid?.id!! }
        memberState.pubnub.removeChannelMembers(
            channel = memberState.channelId(),
            uuids = uuids
        ).sync()?.let {
            memberState.returnedMembers = it.data
            memberState.responseStatus = it.status
        }
    }

    @When("I manage channel members")
    fun i_manage_channel_members() {
        val uuidsToSet = memberState.members.map { PNMember.Partial(uuidId = it.uuid?.id!!) }
        val uuidsToRemove = memberState.membersToRemove.map { it.uuid?.id!! }
        memberState.pubnub.manageChannelMembers(
            channel = memberState.channelId(),
            uuidsToSet = uuidsToSet,
            uuidsToRemove = uuidsToRemove
        ).sync()?.let {
            memberState.returnedMembers = it.data
            memberState.responseStatus = it.status
        }
    }

    @Then("the response contains list with {string} and {string} members")
    fun the_response_contains_list_with_and_members(firstMemberName: String, secondMemberName: String) {
        val firstMember = loadMember(firstMemberName)
        val secondMember = loadMember(secondMemberName)
        MatcherAssert.assertThat(memberState.returnedMembers, Matchers.containsInAnyOrder(firstMember, secondMember))
    }

    @Then("the response contains list with {string} member")
    fun the_response_contains_list_with_member(memberName: String) {
        val member = loadMember(memberName)
        MatcherAssert.assertThat(memberState.returnedMembers, Matchers.hasItem(member))
    }

    @Then("the response does not contain list with {string} member")
    fun the_response_does_not_contain_list_with_member(memberName: String) {
        val member = loadMember(memberName)
        MatcherAssert.assertThat(memberState.returnedMembers, Matchers.not(Matchers.hasItem(member)))
    }
}
