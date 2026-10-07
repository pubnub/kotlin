package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.v2.PNConfiguration

class DataSyncMembershipsMain {
    private fun createMembershipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#create-membership

        // snippet.createMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.createMembership(
            membershipId = "membership-alice-summer-sale",
            channelId = "channel-summer-sale",
            userId = "user-alice",
            classVersion = 1,
            status = "active",
            payload = mapOf("role" to "viewer")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created membership ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun getMembershipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-membership

        // snippet.getMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getMembership("membership-alice-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched membership ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
    }

    private fun getMembershipsBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-memberships

        // snippet.getMembershipsBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getMemberships(
            userId = "user-alice",
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
    }

    private fun setMembershipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#set-membership

        // snippet.setMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.setMembership(
            membershipId = "membership-alice-summer-sale",
            classVersion = 1,
            payload = mapOf("role" to "moderator")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced membership ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun updateMembershipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#update-membership

        // snippet.updateMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.updateMembership(
            membershipId = "membership-alice-summer-sale",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/role", value = "moderator")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated membership ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun removeMembershipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#remove-membership

        // snippet.removeMembershipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.removeMembership("membership-alice-summer-sale").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed membership, HTTP status ${value.status}")
            }
        }
        // snippet.end
    }
}
