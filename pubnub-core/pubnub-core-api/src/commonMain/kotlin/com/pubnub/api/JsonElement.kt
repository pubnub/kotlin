package com.pubnub.api

expect abstract class JsonElement

expect fun JsonElement.isNull(): Boolean

expect fun JsonElement.asList(): List<JsonElement>?

expect fun JsonElement.asLong(): Long?

expect fun JsonElement.asDouble(): Double?

expect fun JsonElement.asNumber(): Number?

expect fun JsonElement.asBoolean(): Boolean?

expect fun JsonElement.asString(): String?

expect fun JsonElement.asMap(): Map<String, JsonElement>?

expect fun createJsonElement(any: Any?): JsonElement

fun JsonElement.decode(): Any? {
    if (isNull()) {
        return null
    }
    return asMap()?.mapValues { it.value.decode() }
        ?: asList()?.map { it.decode() }
        ?: asNumber()
        ?: asBoolean()
        ?: asString()
}
