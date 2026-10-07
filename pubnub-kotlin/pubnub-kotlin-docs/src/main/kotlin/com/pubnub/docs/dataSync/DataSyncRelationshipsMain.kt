package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.v2.PNConfiguration

class DataSyncRelationshipsMain {
    private fun createRelationshipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#create-relationship

        // snippet.createRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.createRelationship(
            relationshipId = "rel-bob-owns-sneaker-42",
            entityAId = "seller-bob",
            entityBId = "product-sneaker-42",
            className = "ProductOwner",
            classVersion = 1,
            payload = mapOf("since" to "2026-07-13")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created relationship ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun getRelationshipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-relationship

        // snippet.getRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getRelationship("rel-bob-owns-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched relationship ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
    }

    private fun getRelationshipsBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-relationships

        // snippet.getRelationshipsBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
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

    private fun setRelationshipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#set-relationship

        // snippet.setRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.setRelationship(
            relationshipId = "rel-bob-owns-sneaker-42",
            classVersion = 1,
            payload = mapOf("since" to "2026-07-13", "tier" to "gold")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced relationship ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun updateRelationshipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#update-relationship

        // snippet.updateRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.updateRelationship(
            relationshipId = "rel-bob-owns-sneaker-42",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/tier", value = "platinum")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated relationship ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun removeRelationshipBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#remove-relationship

        // snippet.removeRelationshipBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.removeRelationship("rel-bob-owns-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed relationship, HTTP status ${value.status}")
            }
        }
        // snippet.end
    }
}
