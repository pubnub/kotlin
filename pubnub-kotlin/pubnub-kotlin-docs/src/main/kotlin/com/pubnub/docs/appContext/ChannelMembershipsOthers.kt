package com.pubnub.docs.appContext

import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.PubNub

class ChannelMembershipsOthers {
    private fun getMembershipsBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-8

        // snippet.getMembershipsBasic
        pubnub.getMemberships(
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeTotalCount = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true
            ),
            sort = listOf(PNSortKey.PNAsc(PNMembershipKey.CHANNEL_ID))
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun setMembershipsBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-9

        // snippet.setMembershipsBasic
        pubnub.setMemberships(
            channels = listOf(
                PNChannelMembership.Partial(
                    channelId = "myChannel",
                    custom = mapOf("owner" to "PubNub"),
                    status = "active",
                    type = "regular_membership"
                )
            ),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true,
                includeTotalCount = true
            ),
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun removeMembershipsBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-10

        // snippet.removeMembershipsBasic
        pubnub.removeMemberships(listOf("myChannel"))
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun manageMembershipsBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-11

        // snippet.manageMembershipsBasic
        pubnub.manageMemberships(
            channelsToSet = listOf(PNChannelMembership.Partial(channelId = "myChannelToSet")),
            channelsToRemove = listOf("myChannelToRemove"),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true,
                includeTotalCount = true
            )
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }
}
