package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.v2.PNConfiguration

class DataSyncEntitiesMain {
    private fun createEntityBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#create-entity

        // snippet.createEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.createEntity(
            entityId = "product-sneaker-42",
            className = "product",
            classVersion = 1,
            payload = mapOf("name" to "Retro Sneaker", "price" to 89.99, "stock" to 12)
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created entity ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun getEntityBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-entity

        // snippet.getEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getEntity("product-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched entity ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
    }

    private fun getEntitiesBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-entities

        // snippet.getEntitiesBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getEntities(
            className = "product",
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

    private fun setEntityBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#set-entity

        // snippet.setEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.setEntity(
            entityId = "product-sneaker-42",
            classVersion = 1,
            payload = mapOf("name" to "Retro Sneaker", "price" to 79.99, "stock" to 8)
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced entity ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun updateEntityBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#update-entity

        // snippet.updateEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.updateEntity(
            entityId = "product-sneaker-42",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/price", value = 79.99),
                PNJsonPatchOperation(op = "replace", path = "/payload/stock", value = 8)
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated entity ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun removeEntityBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#remove-entity

        // snippet.removeEntityBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.removeEntity("product-sneaker-42").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed entity, HTTP status ${value.status}")
            }
        }
        // snippet.end
    }
}
