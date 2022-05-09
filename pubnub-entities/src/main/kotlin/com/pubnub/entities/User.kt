package com.pubnub.entities

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.entities.objects.PNPage
import com.pubnub.entities.objects.PNSortKey

fun PubNub.getUsers(
    limit: Int? = null,
    page: PNPage? = null,
    filter: String? = null,
    sort: Collection<PNSortKey> = listOf(),
    includeCount: Boolean = false,
    includeCustom: Boolean = false
) : RemoteAction<GetAllUUIDMetadata> = TODO()

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