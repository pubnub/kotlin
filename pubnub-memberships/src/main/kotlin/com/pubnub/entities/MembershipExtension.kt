package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ComposableRemoteAction.Companion.firstDo
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction.Companion.map
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.entities.models.consumer.membership.MembershipsResult
import com.pubnub.entities.models.consumer.membership.SpaceDetailsLevel
import com.pubnub.entities.models.consumer.membership.UserDetailsLevel
import com.pubnub.entities.models.consumer.membership.toPNChannelDetailsLevel
import com.pubnub.entities.models.consumer.membership.toPNUUIDDetailsLevel
import com.pubnub.entities.models.consumer.membership.toSpaceMembershipResult
import com.pubnub.entities.models.consumer.membership.toUserMembershipsResult
import com.pubnub.entities.models.consumer.space.Space

//fun PubNub.addMembershipOfUser(
//    spaces: List<PNChannelWithCustom>, //zmienić PNChannelWithCustom na SpaceIdWithCustom? po co jest to "custom" przecież ten space już istnieje?
//    uuid: String? = null,
//    limit: Int? = null,   //usunąć
//    page: PNPage? = null, //usunąć
//    filter: String? = null,
//    sort: Collection<PNSortKey> = listOf(),
//    includeCount: Boolean = false,
//    includeCustom: Boolean = false,
//    includeChannelDetails: PNChannelDetailsLevel? = null
//): ExtendedRemoteAction<MembershipsResult> = firstDo(
//    setMemberships(
//
//    )
//).then{
//    map(
//        it,
//        PNOperationType.MembershipOperation
//    ) {}
//}

fun PubNub.fetchMembershipsOfUser(
    userId: String = configuration.uuid,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: SpaceDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult?> = firstDo(
    getMemberships(
        uuid = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeSpaceDetails?.toPNChannelDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult(userId) }
}

fun PubNub.fetchMembershipsOfSpace(
    spaceId: String,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: UserDetailsLevel? = null
): ExtendedRemoteAction<MembershipsResult?> = firstDo(
    getChannelMembers(
        channel = spaceId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeUUIDDetails = includeUserDetails?.toPNUUIDDetailsLevel()
    )
).then {
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnMemberArrayResult -> pnMemberArrayResult.toSpaceMembershipResult(spaceId) }
}

fun PubNub.removeMembershipOfUser(
    spaces: List<Space>,  // List<String> czy na pewno to mam być lista Space a nie spaceId. W fetchMembershipOfUser jest też user a ja zaimplementowałęm userId
    userId: String = configuration.uuid,  //zmieniłem z String? Pytanie czy userId może być null co w removeMemberships może być null?
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: SpaceDetailsLevel? = null  //jak to działa? jaki jest rezultat jak to jest włączone/wyłączone
): ExtendedRemoteAction<MembershipsResult> = firstDo(
//    val spaceIdList: List<String> = spaces.map { space -> space.id }
    removeMemberships(
        channels = listOf(), // spaces   <----tu trzeba przekonwertować List<Space> -> List<spaceId>
        uuid = userId,
        limit = limit,
        page = page,
        filter = filter,
        sort = sort,
        includeCount = includeCount,
        includeCustom = includeCustom,
        includeChannelDetails = includeSpaceDetails?.toPNChannelDetailsLevel()
    )
).then{
    map(
        it,
        PNOperationType.MembershipOperation
    ) { pnChannelMembershipArrayResult -> pnChannelMembershipArrayResult.toUserMembershipsResult(userId)}
}

/*
In the task https://pubnub.atlassian.net/browse/CLEN-435  there is requirement about removeMembership:
"removeMemberships(users: [User], spaceId: String) returns List of Memberships possibly containing User Data"
Is "users" in method signature collection of User objects or collection of userId-s ?
In "removeMemberships" expect collection of channelId not channels objects (channels: List<String>)
 */


/*
setMemberships takes channels: List<PNChannelWithCustom>,

data class PNChannelWithCustom(
    val channel: String,  //<--this is rather channelId
    val custom: Any? = null
)

on the other site
data class PNChannelMetadata(
    val id: String,
    val name: String?,
    val description: String?,
    val custom: Any?,
    val updated: String?,
    val eTag: String?
)


 */

//pubnub.setMemberships()
//.channelMemberships(Collections.singletonList(PNChannelMembership.channel("channelId")))
//.async
//});