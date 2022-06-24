package com.pubnub.user

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.DisposableListener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.api.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.server.objects_api.ChangeMemberInput
import com.pubnub.api.models.server.objects_api.ChangeMembershipInput
import com.pubnub.api.models.server.objects_api.ChannelMetadataInput
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import com.pubnub.api.models.server.objects_api.UUIDMetadataInput
import com.pubnub.core.UserId
import com.pubnub.user.models.consumer.RemoveUserResult
import com.pubnub.user.models.consumer.User
import com.pubnub.user.models.consumer.UserEvent
import com.pubnub.user.models.consumer.UserKey
import com.pubnub.user.models.consumer.toRemoveUserResult
import com.pubnub.user.models.consumer.toUser
import com.pubnub.user.models.consumer.toUserEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.QueryMap

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
@JvmOverloads
fun PubNub.fetchUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<ResultSortKey<UserKey>> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
) = FetchUsers(
    pubnub = this,
    collectionQueryParameters = CollectionQueryParameters(
        limit = limit,
        page = page,
        filter = filter,
        sort = toPNSortKey(sort),
        includeCount = includeCount
    ),
    withInclude = IncludeQueryParam(includeCustom = includeCustom)
)

data class PNUUIDMetadata(
    val id: String,
    val name: String?,
    val externalId: String?,
    val profileUrl: String?,
    val email: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?,
    val type: String?,
    val status: String?
)

interface ObjectsService {
    @GET("v2/objects/{subKey}/uuids")
    fun getAllUUIDMetadata(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityArrayEnvelope<PNUUIDMetadata>>

    @GET("v2/objects/{subKey}/uuids/{uuid}")
    fun getUUIDMetadata(
        @Path("subKey") subKey: String,
        @Path("uuid") uuid: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityEnvelope<PNUUIDMetadata>>

    @PATCH("v2/objects/{subKey}/uuids/{uuid}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun setUUIDMetadata(
        @Path("subKey") subKey: String,
        @Path("uuid") uuid: String,
        @Body body: UUIDMetadataInput,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityEnvelope<PNUUIDMetadata>>

    @DELETE("v2/objects/{subKey}/uuids/{uuid}")
    fun deleteUUIDMetadata(
        @Path("subKey") subKey: String,
        @Path("uuid") uuid: String
    ): Call<EntityEnvelope<Any?>>

    @GET("v2/objects/{subKey}/channels")
    fun getAllChannelMetadata(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityArrayEnvelope<PNChannelMetadata>>

    @GET("v2/objects/{subKey}/channels/{channel}")
    fun getChannelMetadata(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityEnvelope<PNChannelMetadata>>

    @PATCH("v2/objects/{subKey}/channels/{channel}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun setChannelMetadata(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Body body: ChannelMetadataInput,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityEnvelope<PNChannelMetadata>>

    @DELETE("v2/objects/{subKey}/channels/{channel}")
    fun deleteChannelMetadata(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String
    ): Call<EntityEnvelope<Any?>>

    @GET("/v2/objects/{subKey}/uuids/{uuid}/channels")
    fun getMemberships(
        @Path("subKey") subKey: String,
        @Path("uuid") uuid: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityArrayEnvelope<PNChannelMembership>>

    @PATCH("/v2/objects/{subKey}/uuids/{uuid}/channels")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun patchMemberships(
        @Path("subKey") subKey: String,
        @Path("uuid") uuid: String,
        @Body body: ChangeMembershipInput,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityArrayEnvelope<PNChannelMembership>>

    @GET("/v2/objects/{subKey}/channels/{channel}/uuids")
    fun getChannelMembers(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityArrayEnvelope<PNMember>>

    @PATCH("/v2/objects/{subKey}/channels/{channel}/uuids")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun patchChannelMembers(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Body body: ChangeMemberInput,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf()
    ): Call<EntityArrayEnvelope<PNMember>>
}

val PubNub.aaa: ObjectsService
    get() {
        TODO()
    }

object FetchUsersOperation : com.pubnub.core.OperationType {
    override val queryParam: String? = "obj"
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
    userId: UserId = configuration.userId,
    includeCustom: Boolean = false
): RemoteAction<User?> = firstDo(
    getUUIDMetadata(
        uuid = userId.value, includeCustom = includeCustom
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
    userId: UserId = configuration.userId,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false,
    type: String? = null,
    status: String? = null
): RemoteAction<User?> = firstDo(
    setUUIDMetadata(
        uuid = userId.value,
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
    userId: UserId = configuration.userId,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Map<String, Any>? = null,
    includeCustom: Boolean = false
): RemoteAction<User?> = firstDo(
    setUUIDMetadata(
        uuid = userId.value,
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
    userId: UserId = configuration.userId,
): RemoteAction<RemoveUserResult?> = firstDo(
    removeUUIDMetadata(uuid = userId.value)
).then {
    map(
        it, PNOperationType.UserOperation
    ) { pnRemoveMetadataResult -> pnRemoveMetadataResult.toRemoveUserResult() }
}

/**
 * Add a listener for all user events @see[UserEvent]. To receive any user event it is required to subscribe to
 * a specific userId (passing it as a channel) @see[PubNub.subscribe]
 *
 * @param block Function that will be called for every user event.
 * @return DisposableListener that can be disposed or passed in to PubNub#removeListener method @see[PubNub.removeListener]
 */
fun PubNub.addUserEventsListener(block: (UserEvent) -> Unit): DisposableListener {
    val listener = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
        }

        override fun objects(pubnub: PubNub, objectEvent: PNObjectEventResult) {
            objectEvent.toUserEvent()?.let(block)
        }
    }
    addListener(listener)
    return DisposableListener(this, listener)
}
