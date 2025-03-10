package com.pubnub.matchmaking.internal

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.createEventListener
import com.pubnub.kmp.then
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.entities.FindMatchResult
import com.pubnub.matchmaking.entities.GetUsersResponse
import com.pubnub.matchmaking.entities.MatchmakingStatus
import com.pubnub.matchmaking.internal.common.USER_STATUS_CHANNEL_PREFIX
import com.pubnub.matchmaking.server.MatchmakingRestService

class MatchmakingImpl(override val pubNub: PubNub, private val matchmakingRestService: MatchmakingRestService) :
    Matchmaking {
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
        custom: CustomObject?,
        status: String?,
        type: String?
    ): PNFuture<User> {
        if (!isValidId(id)) {
            throw PubNubException("Id is required") // todo is throwing ok ere
        }

        TODO("Not yet implemented")
    }

    override fun deleteUser(id: String, soft: Boolean): PNFuture<User?> {
        TODO("Not yet implemented")
    }

    // todo how to make sure that userId is unique?
    override fun findMatch(userId: String): PNFuture<String> {
        return matchmakingRestService.findMatch(userId)
    }

    override fun findMatch(userId: String, callback: ((MatchmakingStatus) -> Unit)?): PNFuture<FindMatchResult> {
        return matchmakingRestService.findMatch(userId).then { result: String ->
            val disconnectListener: AutoCloseable? = callback?.let { createListenerAndStartListeningForStatus(userId, it) }
            FindMatchResult(result, disconnectListener)
        }
    }

    override fun getMatchStatus(userId: String): PNFuture<MatchmakingStatus> {
        // last message in channel points to current status
        val userStatusChannelName = USER_STATUS_CHANNEL_PREFIX + userId
        return pubNub.fetchMessages(channels = listOf(userStatusChannelName), page = PNBoundedPage(limit = 1))
            .then { pnFetchMessagesResult ->
                val statusAsString = pnFetchMessagesResult.channels[userStatusChannelName]?.first()?.message.toString()
                MatchmakingStatus.fromString(statusAsString)
            }
    }

    override fun cancelMatchmaking(userId: String): PNFuture<Unit> {
        TODO("Not yet implemented")
    }

    override fun addMissingUserToMatch(userId: String): PNFuture<Unit> {
        TODO("Not yet implemented")
    }

    private fun createListenerAndStartListeningForStatus(userId: String, callback: (MatchmakingStatus) -> Unit): AutoCloseable {
        val userStatusChannelName = USER_STATUS_CHANNEL_PREFIX + userId
        val channelEntity = pubNub.channel(userStatusChannelName)
        val subscription = channelEntity.subscription()
        val listener = createEventListener(
            pubnub = pubNub,
            onMessage = { _, pnMessageResult ->
                try {
                    callback(MatchmakingStatus.fromString(pnMessageResult.message.toString()))
                } catch (e: Exception) {
                    // todo add log
                    println("Error handling onMessage event")
                }
            },
        )
        subscription.addListener(listener)
        subscription.subscribe()
        return subscription
    }
}

internal fun isValidId(id: String): Boolean {
    return id.isNotEmpty()
}
