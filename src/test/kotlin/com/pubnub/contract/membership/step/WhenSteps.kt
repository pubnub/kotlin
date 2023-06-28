package com.pubnub.contract.membership.step

import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.contract.uuidmetadata.state.MembershipState
import io.cucumber.java.en.When

class WhenSteps(
    private val membershipState: MembershipState
) {
    @When("I get the memberships")
    fun i_get_the_memberships() {
        membershipState.pubnub.getMemberships(uuid = membershipState.uuid()).sync()?.let {
            membershipState.returnedMemberships = it.data
            membershipState.responseStatus = it.status
        }
    }

    @When("I get the memberships for current user")
    fun i_get_the_memberships_for_current_user() {
        membershipState.pubnub.getMemberships().sync()?.let {
            membershipState.returnedMemberships = it.data
            membershipState.responseStatus = it.status
        }
    }

    @When("I get the memberships including custom and channel custom information")
    fun i_get_the_memberships_including_custom_and_channel_custom_information() {
        membershipState.pubnub.getMemberships(
            uuid = membershipState.uuid(),
            includeCustom = true,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
        ).sync()?.let {
            membershipState.returnedMemberships = it.data
            membershipState.responseStatus = it.status
        }
    }

    @When("I set the membership")
    fun i_set_the_membership() {
        val channels = membershipState.memberships.map {
            PNChannelMembership.Partial(
                channelId = it.channel!!.id,
                custom = it.custom,
                status = it.status
            )
        }

        membershipState.pubnub.setMemberships(
            uuid = membershipState.uuid(),
            channels = channels,
        ).sync()?.let {
            membershipState.responseStatus = it.status
            membershipState.returnedMemberships = it.data
        }
    }

    @When("I set the membership for current user")
    fun i_set_the_membership_for_current_user() {
        val channels = membershipState.memberships.map {
            PNChannelMembership.Partial(
                channelId = it.channel!!.id,
                custom = it.custom,
                status = it.status
            )
        }

        membershipState.pubnub.setMemberships(
            channels = channels,
        ).sync()?.let {
            membershipState.responseStatus = it.status
            membershipState.returnedMemberships = it.data
        }
    }

    @When("I remove the membership")
    fun i_remove_the_membership() {
        val channels = membershipState.memberships.map { it.channel!!.id }
        membershipState.pubnub.removeMemberships(
            uuid = membershipState.uuid(),
            channels = channels,
        ).sync()?.let {
            membershipState.responseStatus = it.status
            membershipState.returnedMemberships = it.data
        }
    }

    @When("I remove the membership for current user")
    fun i_remove_the_membership_for_current_user() {
        val channels = membershipState.memberships.map { it.channel!!.id }
        membershipState.pubnub.removeMemberships(
            channels = channels
        ).sync()?.let {
            membershipState.responseStatus = it.status
            membershipState.returnedMemberships = it.data
        }
    }

    @When("I manage memberships")
    fun i_manage_memberships() {
        val channelsToSet = membershipState.memberships.map {
            PNChannelMembership.Partial(
                channelId = it.channel!!.id,
                custom = it.custom,
                status = it.status
            )
        }
        val channelsToRemove = membershipState.membershipsToRemove.map { it.channel!!.id }
        membershipState.pubnub.manageMemberships(
            channelsToSet = channelsToSet,
            channelsToRemove = channelsToRemove,
            uuid = membershipState.uuid()
        ).sync()?.let {
            membershipState.responseStatus = it.status
            membershipState.returnedMemberships = it.data
        }
    }
}
