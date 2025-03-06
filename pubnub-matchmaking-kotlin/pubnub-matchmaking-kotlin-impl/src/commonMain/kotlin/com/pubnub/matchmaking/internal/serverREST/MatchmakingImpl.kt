package com.pubnub.matchmaking.internal.serverREST

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.kmp.PNFuture
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.entities.GetUsersResponse
import com.pubnub.matchmaking.entities.MatchmakingCallback
import com.pubnub.matchmaking.entities.MatchmakingStatus
import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.server.MatchmakingRestService

class MatchmakingImpl(val matchmakingRestService: MatchmakingRestService): Matchmaking {


    override fun createUser(
        id: String,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        status: String?,
        type: String?
    ): PNFuture<User> {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): PNFuture<User> {
        TODO("Not yet implemented")
    }

    override fun getUsers(
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        limit: Int?,
        page: PNPage?
    ): GetUsersResponse {
        TODO("Not yet implemented")
    }

    override fun updateUser(
        id: String,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        status: String?,
        type: String?
    ): PNFuture<User> {
        if (!isValidId(id)) {
            throw PubNubException("Id is required") //todo is throwing ok ere
        }

        TODO("Not yet implemented")
    }


    override fun deleteUser(id: String, soft: Boolean): PNFuture<User?> {
        TODO("Not yet implemented")
    }

    // how to make sure that userId is unique?
    override fun findMatch(userId: String, callback: MatchmakingCallback): PNFuture<String> {
        //simulate call to REST service getStatus
//        ma
        TODO("Not yet implemented")
    }

    override fun getMatchStatus(userId: String): MatchmakingStatus {
        TODO("Not yet implemented")
    }

    override fun cancelMatchmaking(userId: String): PNFuture<Unit> {
        TODO("Not yet implemented")
    }

    override fun addMissingUserToMatch(userId: String): PNFuture<Unit> {
        TODO("Not yet implemented")
    }
}

internal fun isValidId(id: String): Boolean {
    return id.isNotEmpty()
}