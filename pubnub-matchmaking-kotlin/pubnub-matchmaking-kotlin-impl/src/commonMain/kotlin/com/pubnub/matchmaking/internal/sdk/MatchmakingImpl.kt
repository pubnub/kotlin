package com.pubnub.matchmaking.internal.sdk

import co.touchlab.kermit.Logger
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.asMap
import com.pubnub.api.asString
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.asFuture
import com.pubnub.kmp.catch
import com.pubnub.kmp.createEventListener
import com.pubnub.kmp.then
import com.pubnub.kmp.thenAsync
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User
import com.pubnub.matchmaking.entities.FindMatchResult
import com.pubnub.matchmaking.entities.GetUsersResponse
import com.pubnub.matchmaking.entities.MatchmakingStatus
import com.pubnub.matchmaking.internal.UserImpl
import com.pubnub.matchmaking.internal.common.USER_STATUS_CHANNEL_PREFIX
import com.pubnub.matchmaking.internal.util.channelsUrlDecoded
import com.pubnub.matchmaking.internal.util.logErrorAndReturnException
import com.pubnub.matchmaking.internal.util.nullOn404
import com.pubnub.matchmaking.internal.util.pnError
import com.pubnub.matchmaking.server.MatchmakingRestService

class MatchmakingImpl(override val pubNub: PubNub) : Matchmaking {
    private val matchmakingRestService: MatchmakingRestService = MatchmakingRestService(pubNub)

    companion object {
        private val log = Logger.withTag("MatchmakingImpl")

        private const val ID_IS_REQUIRED = "Id is required"
        private const val USER_ID_ALREADY_EXIST = "User with this ID already exists"
        private const val FAILED_TO_CREATE_UPDATE_USER_DATA = "Failed to create/update user data."
    }

    override fun createUser(
        id: String,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: CustomObject?, // todo change to CustomObject ?
        status: String?,
        type: String?,
    ): PNFuture<User> {
        if (!isValidId(id)) {
            return log.logErrorAndReturnException(ID_IS_REQUIRED).asFuture()
        }

        return getUser(id).thenAsync { user: User? ->
            if (user != null) {
                log.pnError(USER_ID_ALREADY_EXIST)
            } else {
                setUserMetadata(
                    id = id,
                    name = name,
                    externalId = externalId,
                    profileUrl = profileUrl,
                    email = email,
                    custom = custom,
                    type = type,
                    status = status
                )
            }
        }
    }

    override fun getUser(userId: String): PNFuture<User?> {
        if (!isValidId(userId)) {
            return log.logErrorAndReturnException(ID_IS_REQUIRED).asFuture()
        }

        return pubNub.getUUIDMetadata(uuid = userId, includeCustom = true)
            .then { pnUUIDMetadataResult: PNUUIDMetadataResult ->
                UserImpl.fromDTO(this, pnUUIDMetadataResult.data)
            }.nullOn404()
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
        val disconnectListener: AutoCloseable? = callback?.let { createListenerAndStartListeningForStatus(userId, it) }

        return matchmakingRestService.findMatch(userId = userId, withDelay = true).then { result: String ->
            FindMatchResult(result, disconnectListener)
        }
    }

    override fun getStatus(userId: String): PNFuture<MatchmakingStatus> {
        // last message in channel points to current status
        val userStatusChannelId = USER_STATUS_CHANNEL_PREFIX + userId
        return pubNub.fetchMessages(channels = listOf(userStatusChannelId), page = PNBoundedPage(limit = 1))
            .then { pnFetchMessagesResult ->
                val pnFetchMessageItems: List<PNFetchMessageItem> =
                    pnFetchMessagesResult.channelsUrlDecoded[userStatusChannelId] ?: emptyList()
                if (pnFetchMessageItems == emptyList<PNFetchMessageItem>()) {
                    MatchmakingStatus.UNKNOWN
                } else {
                    val statusAsString: String = pnFetchMessageItems.first().message.asMap()?.get("status")?.asString()
                        ?: MatchmakingStatus.UNKNOWN.toString()
                    MatchmakingStatus.fromString(statusAsString)
                }
            }
    }

    override fun cancelMatchmaking(userId: String): PNFuture<Unit> {
        TODO("Not yet implemented")
    }

    override fun addMissingUserToMatch(userId: String): PNFuture<Unit> {
        TODO("Not yet implemented")
    }

    private fun setUserMetadata(
        id: String,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: CustomObject?,
        type: String? = null,
        status: String? = null,
    ): PNFuture<User> {
        return pubNub.setUUIDMetadata(
            uuid = id,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            custom = custom,
            includeCustom = true,
            type = type,
            status = status
        ).then { pnUUIDMetadataResult ->
            UserImpl.fromDTO(this, pnUUIDMetadataResult.data)
        }.catch { exception ->
            Result.failure(PubNubException(FAILED_TO_CREATE_UPDATE_USER_DATA, exception))
        }
    }

    private fun createListenerAndStartListeningForStatus(
        userId: String,
        callback: (MatchmakingStatus) -> Unit
    ): AutoCloseable {
        val userStatusChannelName = USER_STATUS_CHANNEL_PREFIX + userId

        println("-=listening on userStatusChannelName: $userStatusChannelName")
        val channelEntity = pubNub.channel(userStatusChannelName)
        val subscription = channelEntity.subscription()
        val listener = createEventListener(
            pubnub = pubNub,
            onMessage = { _, pnMessageResult ->
                println("-=in on message")
                try {
                    println("-=pnMessageResult: ${pnMessageResult.message}")
                    val statusAsString: String = pnMessageResult.message.asMap()?.get("status")?.asString()
                        ?: MatchmakingStatus.UNKNOWN.toString()
                    callback(MatchmakingStatus.fromString(statusAsString))
                } catch (e: Exception) {
                    // todo add log
                    println("-=Error handling onMessage event")
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
