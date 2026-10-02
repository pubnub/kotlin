package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation

class DataSyncEntitiesOther {
    private fun getEntitiesFilterFast(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getEntitiesFilterFast
        pubnub.dataSync.getEntities(
            className = "product",
            filterFast = "price < 100 && stock > 0",
            sort = listOf(PNDataSyncSortField(property = "price")),
            limit = 20
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

    private fun getEntitiesFilter(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getEntitiesFilter
        pubnub.dataSync.getEntities(
            className = "product",
            filter = "name LIKE \"*Sneaker*\"",
            limit = 20
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

    private fun getEntitiesPagination(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#pagination

        // snippet.getEntitiesPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getEntities(
                className = "product",
                limit = 20,
                cursor = cursor
            ).async { result ->
                result.onFailure { exception ->
                    println("Failed: ${exception.message}")
                }.onSuccess { value ->
                    value.data.forEach { println(it.id) }
                    if (value.next.hasNext) {
                        fetchPage(value.next.cursor)
                    }
                }
            }
        }

        fetchPage(cursor = null)
        // snippet.end
    }

    private fun updateEntityMultipleOperations(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#update-entity

        // snippet.updateEntityMultipleOperations
        pubnub.dataSync.updateEntity(
            entityId = "product-sneaker-42",
            operations = listOf(
                // Fail the whole patch unless the name is still "Retro Sneaker"
                PNJsonPatchOperation(op = "test", path = "/payload/name", value = "Retro Sneaker"),
                // Keep the old price before changing it
                PNJsonPatchOperation(op = "copy", from = "/payload/price", path = "/payload/previousPrice"),
                PNJsonPatchOperation(op = "replace", path = "/payload/price", value = 79.99),
                PNJsonPatchOperation(op = "replace", path = "/payload/stock", value = 8),
                PNJsonPatchOperation(op = "add", path = "/payload/sale", value = true),
                // Rename a field
                PNJsonPatchOperation(op = "move", from = "/payload/sale", path = "/payload/onSale"),
                PNJsonPatchOperation(op = "remove", path = "/payload/legacySku")
            ),
            ifMatch = "a1b2c3d4e5f6"
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Patched ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }
}
