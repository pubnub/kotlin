package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
import com.pubnub.entities.models.consumer.user.RemoveUserResult
import com.pubnub.entities.models.consumer.user.User
import com.pubnub.entities.models.consumer.user.UserKey
import com.pubnub.entities.models.consumer.user.UsersResult
import com.pubnub.entities.models.consumer.user.toRemoveUserResult
import com.pubnub.entities.models.consumer.user.toUser
import com.pubnub.entities.models.consumer.user.toUsersResult

/**
 * Returns a paginated list of User metadata, optionally including the custom data object for each.
 *
 * @param limit Number of objects to return in the response.
 *              Default is 100, which is also the maximum value.
 *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
 * @param page Use for pagination.
 *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
 *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
 *                           Ignored if you also supply the start parameter.
 * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
 *               expression are returned.
 * @param sort List of properties to sort by. Available options are id, name, and updated.
 *             e.g. listOf(ResultSortKey.Asc(key = ResultKey.ID)) , listOf(ResultSortKey.Desc(key = ResultKey.NAME))
 *             @see [Asc], [Desc]
 * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
 *                     Default is `false`.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.fetchUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<ResultSortKey<UserKey>> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): ExtendedRemoteAction<UsersResult?> = firstDo(
    getAllUUIDMetadata(
        limit = limit,
        page = page,
        filter = filter,
        sort = toPNSortKey(sort),
        includeCount = includeCount,
        includeCustom = includeCustom
    )
).then {
    map(
        it, PNOperationType.UserOperation
    ) { pnUuidMetadataArrayResult -> pnUuidMetadataArrayResult.toUsersResult() }
}

private fun toPNSortKey(sort: Collection<ResultSortKey<UserKey>>): Collection<PNSortKey<PNKey>> {
    val pnSortKeyList = sort.map { resultSortKey ->
        when (resultSortKey) {
            is ResultSortKey.Asc -> PNSortKey.PNAsc(key = resultSortKey.key.toPNSortKey())
            is ResultSortKey.Desc -> PNSortKey.PNDesc(key = resultSortKey.key.toPNSortKey())
        }
    }
    return pnSortKeyList
}

/**
 * Returns metadata for the specified User, optionally including the custom data object for each.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.fetchUser(
    userId: String? = configuration.uuid,
    includeCustom: Boolean = false
): ExtendedRemoteAction<User?> = firstDo(
    getUUIDMetadata(
        uuid = userId, includeCustom = includeCustom
    )
).then {
    map(
        it, PNOperationType.UserOperation
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toUser() }
}

/**
 * Create metadata for a User in the database, optionally including the custom data object for each.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param name Display name for the user. Maximum 200 characters.
 * @param externalId User's identifier in an external system
 * @param profileUrl The URL of the user's profile picture
 * @param email The user's email address. Maximum 80 characters.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 * @param type Type of the space.
 * @param status Status of the space.
 */
fun PubNub.createUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false,
    type: String? = null,
    status: String? = null
): ExtendedRemoteAction<User?> = firstDo(
    setUUIDMetadata(
        uuid = userId,
        name = name,
        externalId = externalId,
        profileUrl = profileUrl,
        email = email,
        custom = custom,
        includeCustom = includeCustom,
        type = type,
        status = status
    )
).then {
    map(
        it, PNOperationType.UserOperation
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toUser() }
}

/**
 * Update existing metadata for a User in the database, optionally including the custom data object for each.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 * @param name Display name for the user. Maximum 200 characters.
 * @param externalId User's identifier in an external system
 * @param profileUrl The URL of the user's profile picture
 * @param email The user's email address. Maximum 80 characters.
 * @param custom Object with supported data types.
 * @param includeCustom Include respective additional fields in the response.
 */
fun PubNub.updateUser(
    userId: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false
): ExtendedRemoteAction<User?> = firstDo(
    setUUIDMetadata(
        uuid = userId,
        name = name,
        externalId = externalId,
        profileUrl = profileUrl,
        email = email,
        custom = custom,
        includeCustom = includeCustom
    )
).then {
    map(
        it, PNOperationType.UserOperation
    ) { pnUuidMetadataResult -> pnUuidMetadataResult.toUser() }
}

/**
 * Removes the metadata from a specified User.
 *
 * @param userId Unique user identifier. If not supplied then current user’s userId is used.
 */
fun PubNub.removeUser(
    userId: String? = null
): ExtendedRemoteAction<RemoveUserResult?> = firstDo(
    removeUUIDMetadata(uuid = userId)
).then {
    map(
        it, PNOperationType.UserOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toRemoveUserResult() }
}

sealed class UserEventData {
    abstract val event: String

    data class UserModified(
        val userId: String,
        val name: String?,
        val externalId: String?,
        val profileUrl: String?,
        val email: String?,
        val custom: Map<String, Any>?,
        val status: String?,
        val type: String?
    ) : UserEventData() {
        override val event: String = "modified"
    }

    data class UserRemoved(val userId: String) : UserEventData() {
        override val event: String = "removed"
    }
}

data class Message(val data: UserEventData) {
    val type: String = "user"
    val event: String = data.event
}

data class UserEvent(
    val spaceId: String,
    val timetoken: Long,
    val message: Message
)

fun PNObjectEventResult.toUserEvent(): UserEvent? {
    val message = when (val m = extractedMessage) {
        is PNSetUUIDMetadataEventMessage -> {
            Message(
                UserEventData.UserModified(
                    userId = m.source,
                    name = m.data.name,
                    profileUrl = m.data.profileUrl,
                    email = m.data.email,
                    status = m.data.status,
                    type = m.data.type,
                    custom = m.data.custom as? Map<String, Any>,
                    externalId = m.data.externalId
                )
            )
        }
        is PNDeleteUUIDMetadataEventMessage -> {
            Message(
                UserEventData.UserRemoved(
                    userId = m.source
                )
            )
        }
        else -> {
            return null
        }
    }

    return UserEvent(
        spaceId = channel,
        timetoken = timetoken ?: 0,
        message = message
    )
}


interface Stoppable {
    fun stop()
}

class StoppableListener(
    private val pubNub: PubNub,
    private val listener: Listener
) : Stoppable, Listener by listener {
    override fun stop() {
        pubNub.removeListener(listener)
    }
}

fun PubNub.addUserEventsListener(block: UserEvent.() -> Unit): Stoppable {
    val listener = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
        }

        override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
            objectEvent.toUserEvent()?.let(block)
        }
    }
    addListener(listener)
    return StoppableListener(this, listener)
}

