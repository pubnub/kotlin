package com.pubnub.api.endpoints.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.managers.MapperManager

class JsonMapperManagerForEndpointImpl(val mapperManager: MapperManager) : JsonMapperManagerForEndpoint {
    override fun isJsonObject(element: JsonElement): Boolean {
        return mapperManager.isJsonObject(element)
    }

    override fun hasField(element: JsonElement, field: String): Boolean {
        return mapperManager.hasField(element, field)
    }

    override fun getField(element: JsonElement?, field: String): JsonElement? {
        return mapperManager.getField(element, field)
    }

    override fun elementToString(element: JsonElement?): String? {
        return mapperManager.elementToString(element)
    }

    override fun getArrayIterator(element: JsonElement, field: String): MutableIterator<JsonElement> {
        return mapperManager.getArrayIterator(element, field)
    }

    override fun <T> fromJson(input: String?, clazz: Class<T>): T {
        return mapperManager.fromJson(input, clazz)
    }

    override fun toJson(input: Any?): String {
        return mapperManager.toJson(input)
    }
}
