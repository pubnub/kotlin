package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey

fun PubNub.getAllSpaceMetadata(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
) = getAllChannelMetadata(
    limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom
)

fun PubNub.getSpaceMetadata(
    space: String,
    includeCustom: Boolean = false
) = getChannelMetadata(
    channel = space,
    includeCustom = includeCustom
)

fun PubNub.setSpaceMetadata(
    space: String,
    name: String? = null,
    description: String? = null,
    custom: Any? = null,
    includeCustom: Boolean = false
) = setChannelMetadata(
    channel = space,
    name = name,
    description = description,
    custom = custom,
    includeCustom = includeCustom
)

fun PubNub.removeChannelMetadata(space: String) = removeChannelMetadata(channel = space)