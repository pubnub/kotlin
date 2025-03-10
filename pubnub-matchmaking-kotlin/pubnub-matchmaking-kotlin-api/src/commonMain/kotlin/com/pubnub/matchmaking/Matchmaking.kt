package com.pubnub.matchmaking

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.matchmaking.entities.GetUsersResponse

interface Matchmaking {
    val pubNub: PubNub

    fun createUser(
        id: String,
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: Any? = null,
        status: String? = null,
        type: String? = null,
    ): PNFuture<User>

    fun getUser(userId: String): PNFuture<User>

    /**
     * Returns a paginated list of all users and their details
     *
     * @param filter Expression used to filter the results. Returns only these users whose properties satisfy the
     * given expression are returned. The filtering language is defined in [documentation](https://www.pubnub.com/docs/general/metadata/filtering).
     * @param sort A collection to specify the sort order. Available options are id, name, and updated. Use asc or desc
     * @param limit Number of objects to return in response. The default (and maximum) value is 100.
     * @param page Object used for pagination to define which previous or next result page you want to fetch.
     *
     * @return [PNFuture] containing a set of users with pagination information (next, prev, total).
     */
    fun getUsers(
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        limit: Int? = null,
        page: PNPage? = null,
    ): GetUsersResponse

    fun updateUser(
        id: String,
        // TODO change nulls to Optionals when there is support. In Kotlin SDK there should be possibility to handle PatchValue
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: CustomObject? = null,
        status: String? = null,
        type: String? = null,
    ): PNFuture<User>

    fun deleteUser(id: String, soft: Boolean = false): PNFuture<User?>

    fun findMatch(userId: String): PNFuture<String>

    fun getMatchStatus(userId: String): com.pubnub.matchmaking.entities.MatchmakingStatus

    fun cancelMatchmaking(userId: String): PNFuture<Unit>

    // todo implement
    fun addMissingUserToMatch(userId: String): PNFuture<Unit>
}
