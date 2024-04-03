package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.endpoints.HasOverridableConfig

interface GetUUIDMetadataInterface : HasOverridableConfig {
    val uuid: String
}
