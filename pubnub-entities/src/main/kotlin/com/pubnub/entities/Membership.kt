package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.member.PNUUIDWithCustom
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelWithCustom

fun PubNub.getUserSpaces(
    user: String? = null,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: PNChannelDetailsLevel? = null
) = getMemberships(
    uuid = user ?: configuration.uuid,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeChannelDetails = includeSpaceDetails
)

fun PubNub.setUserSpaces(
    spaces: List<PNChannelWithCustom>,
    user: String? = null,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: PNChannelDetailsLevel? = null
) = manageMemberships(
    channelsToSet = spaces,
    channelsToRemove = listOf(),
    uuid = user ?: configuration.uuid,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeChannelDetails = includeSpaceDetails
)

fun PubNub.removeUserSpaces(
    spaces: List<String>,
    user: String? = null,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: PNChannelDetailsLevel? = null
) = manageMemberships(
    channelsToSet = listOf(),
    channelsToRemove = spaces,
    uuid = user ?: configuration.uuid,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeChannelDetails = includeSpaceDetails
)

fun PubNub.manageUserSpaces(
    spacesToSet: List<PNChannelWithCustom>,
    spacesToRemove: List<String>,
    user: String? = null,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeSpaceDetails: PNChannelDetailsLevel? = null
) = manageMemberships(
    channelsToSet = spacesToSet,
    channelsToRemove = spacesToRemove,
    uuid = user ?: configuration.uuid,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeChannelDetails = includeSpaceDetails
)

fun PubNub.getSpaceUsers(
    space: String,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: PNUUIDDetailsLevel? = null
) = getChannelMembers(
    channel = space,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeUUIDDetails = includeUserDetails
)

fun PubNub.setSpaceUsers(
    space: String,
    users: List<PNUUIDWithCustom>,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: PNUUIDDetailsLevel? = null
) = manageChannelMembers(
    channel = space,
    uuidsToSet = users,
    uuidsToRemove = listOf(),
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeUUIDDetails = includeUserDetails
)

fun PubNub.removeSpaceUsers(
    space: String,
    users: List<String>,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: PNUUIDDetailsLevel? = null
) = manageChannelMembers(
    channel = space,
    uuidsToSet = listOf(),
    uuidsToRemove = users,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeUUIDDetails = includeUserDetails
)

fun PubNub.manageSpaceUsers(
    space: String,
    usersToSet: Collection<PNUUIDWithCustom>,
    usersToRemove: Collection<String>,
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false,
    includeUserDetails: PNUUIDDetailsLevel? = null
) = manageChannelMembers(
    channel = space,
    uuidsToSet = usersToSet,
    uuidsToRemove = usersToRemove,
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom,
    includeUUIDDetails = includeUserDetails
)