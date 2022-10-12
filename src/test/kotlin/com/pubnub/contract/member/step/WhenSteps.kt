package com.pubnub.contract.member.step

import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.contract.member.state.MemberState
import com.pubnub.contract.state.World
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val memberState: MemberState,
) {

    @When("I get the channel members")
    fun i_get_the_channel_members() {
        world.pubnub.getChannelMembers(channel = memberState.channelId()!!).sync()?.let {
            memberState.members = it.data
            world.responseStatus = it.status
        }
    }

    @When("I set a channel member including custom and UUID with custom")
    fun i_set_a_channel_member_including_custom_and_uuid_with_custom() {
        val memberInput = PNMember.Partial(uuidId = memberState.member?.uuid?.id!!)
        world.pubnub.setChannelMembers(
            channel = memberState.channelId()!!,
            includeCustom = true,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
            uuids = listOf(memberInput)
        ).sync()?.let {
            memberState.members = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the channel members including custom and UUID custom information")
    fun i_get_the_channel_members_including_custom_and_uuid_custom_information() {
        world.pubnub.getChannelMembers(
            channel = memberState.channelId()!!,
            includeCustom = true,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM
        ).sync()?.let {
            memberState.members = it.data
            world.responseStatus = it.status
        }
    }

    @When("I set a channel member")
    fun i_set_a_channel_member() {
        val memberInput = PNMember.Partial(uuidId = memberState.member?.uuid?.id!!)
        world.pubnub.setChannelMembers(
            channel = memberState.channelId()!!,
            uuids = listOf(memberInput)
        ).sync()?.let {
            memberState.members = it.data
            world.responseStatus = it.status
        }
    }

    @When("I remove a channel member")
    fun i_remove_a_channel_member() {
        world.pubnub.removeChannelMembers(
            channel = memberState.channelId()!!,
            uuids = listOf(memberState.member?.uuid?.id!!)
        ).sync()?.let {
            memberState.members = it.data
            world.responseStatus = it.status
        }
    }
}
