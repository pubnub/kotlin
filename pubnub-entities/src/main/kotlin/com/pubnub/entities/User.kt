package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey

fun PubNub.getUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
) = getAllUUIDMetadata(
    limit = limit,
    page = page,
    filter = filter,
    sort = sort,
    includeCount = includeCount,
    includeCustom = includeCustom
)

fun PubNub.getUser(
    user: String? = null,
    includeCustom: Boolean = false
) = getUUIDMetadata(
    uuid = user ?: configuration.uuid,
    includeCustom = includeCustom
)

fun PubNub.setUser(
    user: String? = null,
    name: String? = null,
    externalId: String? = null,
    profileUrl: String? = null,
    email: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
) = setUUIDMetadata(
    uuid = user,
    name = name,
    externalId = externalId,
    profileUrl = profileUrl,
    email = email,
    custom = custom,
    includeCustom = includeCustom
)

fun PubNub.deleteUser(user: String? = null) = removeUUIDMetadata(uuid = user)