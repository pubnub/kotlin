package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.managers.MapperManager

class ToJsonMapperImpl(val mapperManager: MapperManager) : ToJsonMapper {
    override fun toJson(input: Any?): String {
        return mapperManager.toJson(input)
    }
}
