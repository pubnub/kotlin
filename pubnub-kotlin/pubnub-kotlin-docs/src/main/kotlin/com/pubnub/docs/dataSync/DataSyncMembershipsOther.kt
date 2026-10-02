package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField

class DataSyncMembershipsOther {
    private fun getMembershipsFilterFast(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getMembershipsFilterFast
        pubnub.dataSync.getMemberships(
            userId = "user-alice",
            filterFast = "role == \"viewer\"",
            sort = listOf(PNDataSyncSortField(property = "createdAt", ascending = false)),
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

    private fun getMembershipsFilter(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#filtering-and-sorting

        // snippet.getMembershipsFilter
        pubnub.dataSync.getMemberships(
            userId = "user-alice",
            filter = "role LIKE \"view*\"",
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

    private fun getMembershipsPagination(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#pagination

        // snippet.getMembershipsPagination
        fun fetchPage(cursor: String?) {
            pubnub.dataSync.getMemberships(
                userId = "user-alice",
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

    private fun getMembershipsByChannelId(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-memberships

        // snippet.getMembershipsByChannelId
        pubnub.dataSync.getMemberships(
            channelId = "channel-summer-sale",
            limit = 50
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println("${it.userId} is a member of ${it.channelId}") }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
    }
}
