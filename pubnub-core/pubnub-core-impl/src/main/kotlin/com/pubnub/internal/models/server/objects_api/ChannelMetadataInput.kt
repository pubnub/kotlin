package com.pubnub.internal.models.server.objects_api

import com.pubnub.api.utils.Optional

internal data class ChannelMetadataInput(
    val name: Optional<String?> = Optional.none(),
    val description: Optional<String?> = Optional.none(),
    val custom: Optional<Any?> = Optional.none(),
    val type: Optional<String?> = Optional.none(),
    val status: Optional<String?> = Optional.none(),
)
