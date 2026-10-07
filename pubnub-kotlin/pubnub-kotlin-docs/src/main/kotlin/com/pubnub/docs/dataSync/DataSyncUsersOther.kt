package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField

class DataSyncUsersOther {
    private fun getUsersFilterFast(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getUsersFilterFast
        pubnub.dataSync.getUsers(
            filterFast = "type == \"shopper\"",
            sort = listOf(PNDataSyncSortField(property = "name")),
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

    private fun getUsersFilter(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getUsersFilter
        pubnub.dataSync.getUsers(
            filter = "name LIKE \"Alice*\"",
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

    private fun getUsersPagination(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#pagination

        // snippet.getUsersPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getUsers(
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
}
