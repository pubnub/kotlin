package com.pubnub.contract.membership.step

import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.contract.state.World
import com.pubnub.contract.uuidmetadata.state.MembershipState
import io.cucumber.java.en.When

class WhenSteps(
    private val world: World,
    private val membershipState: MembershipState
) {
    @When("I get the memberships")
    fun i_get_the_memberships() {
        world.pubnub.getMemberships(uuid = membershipState.uuid()).sync()?.let {
            membershipState.memberships = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the memberships for current user")
    fun i_get_the_memberships_for_current_user() {
        world.pubnub.getMemberships().sync()?.let {
            membershipState.memberships = it.data
            world.responseStatus = it.status
        }
    }

    @When("I get the memberships including custom and channel custom information")
    fun i_get_the_memberships_including_custom_and_channel_custom_information() {
        world.pubnub.getMemberships(
            uuid = membershipState.uuid(),
            includeCustom = true,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
        ).sync()?.let {
            membershipState.memberships = it.data
            world.responseStatus = it.status
        }
    }

    @When("I set the membership")
    fun i_set_the_membership() {
        val channelMembershipInput = membershipState.membership.let {
            PNChannelMembership.Partial(
                channelId = it.channel!!.id,
                custom = it.custom,
                status = it.status
            )
        }

        world.pubnub.setMemberships(
            uuid = membershipState.uuid(),
            channels = listOf(channelMembershipInput),
        ).sync()?.let {
            world.responseStatus = it.status
            membershipState.memberships = it.data
        }
    }

    @When("I set the membership for current user")
    fun i_set_the_membershipfor_current_user() {
        val channelMembershipInput = membershipState.membership.let {
            PNChannelMembership.Partial(
                channelId = it.channel!!.id,
                custom = it.custom,
                status = it.status
            )
        }

        world.pubnub.setMemberships(
            channels = listOf(channelMembershipInput),
        ).sync()?.let {
            world.responseStatus = it.status
            membershipState.memberships = it.data
        }
    }

    @When("I remove the membership")
    fun i_remove_the_membership() {
        world.pubnub.removeMemberships(
            uuid = membershipState.uuid(),
            channels = listOf(membershipState.membership.channel?.id!!),
        ).sync()?.let {
            world.responseStatus = it.status
            membershipState.memberships = it.data
        }
    }

    @When("I remove the membership for current user")
    fun i_remove_the_membership_for_current_user() {
        world.pubnub.removeMemberships(
            channels = listOf(membershipState.membership.channel?.id!!),
        ).sync()?.let {
            world.responseStatus = it.status
            membershipState.memberships = it.data
        }
    }
}
