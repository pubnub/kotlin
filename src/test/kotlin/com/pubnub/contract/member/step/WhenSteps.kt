package com.pubnub.contract.member.step

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.contract.member.state.MemberState
import io.cucumber.java.en.When

class WhenSteps(
    private val memberState: MemberState,
) {

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
}
