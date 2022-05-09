package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.entities.objects.endpoint.channel.GetSpace
import com.pubnub.entities.objects.endpoint.internal.ReturningCustom
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.entities.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.entities.objects.PNSortKey

fun PubNub.getSpaces(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
): RemoteAction<PNChannelMetadataArrayResult> = TODO()

fun PubNub.getSpace(
    space: String,
    includeCustom: Boolean = false
) = GetSpace(
    pubnub = this,
    channel = space,
    withCustom = ReturningCustom(includeCustom = includeCustom)
)

fun PubNub.setSpace(
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

fun PubNub.deleteSpace(space: String) = removeChannelMetadata(channel = space)