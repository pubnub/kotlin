package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField

class DataSyncRelationshipsOther {
    private fun getRelationshipsFilterFast(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getRelationshipsFilterFast
        // `tier` and `since` must be declared filterable (`simple` or `full`) on the `ProductOwner` class
        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            filterFast = "tier == \"gold\"",
            sort = listOf(PNDataSyncSortField(property = "since", ascending = false)),
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

    private fun getRelationshipsFilter(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getRelationshipsFilter
        // `filter` only accepts properties declared with `full` filtering on the `ProductOwner` class
        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            filter = "tier LIKE \"gold*\"",
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

    private fun getRelationshipsPagination(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#pagination

        // snippet.getRelationshipsPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getRelationships(
                className = "ProductOwner",
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

    private fun getRelationshipsByEntityBId(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-relationships

        // snippet.getRelationshipsByEntityBId
        pubnub.dataSync.getRelationships(
            className = "ProductOwner",
            entityBId = "product-sneaker-42",
            limit = 50
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println("${it.entityAId} owns ${it.entityBId}") }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
    }
}
