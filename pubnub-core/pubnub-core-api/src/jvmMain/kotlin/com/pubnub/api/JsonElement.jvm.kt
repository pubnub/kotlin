package com.pubnub.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

actual typealias JsonElement = com.google.gson.JsonElement

actual fun JsonElement.asString(): String? {
    return if (this.isJsonPrimitive && this.asJsonPrimitive.isString) this.asString else null
}

actual fun JsonElement.asMap(): Map<String, JsonElement>? {
    return if (this.isJsonObject) this.asJsonObject.asMap() else null
}

actual fun JsonElement.isNull(): Boolean {
    return this.isJsonNull
}

actual fun JsonElement.asList(): List<JsonElement>? {
    return if (this.isJsonArray) this.asJsonArray.asList() else null
}

actual fun JsonElement.asLong(): Long? {
    return if (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) this.asLong else null
}

actual fun JsonElement.asBoolean(): Boolean? {
    return if (this.isJsonPrimitive && this.asJsonPrimitive.isBoolean) this.asBoolean else null
}

actual fun JsonElement.asDouble(): Double? {
    return if (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) this.asDouble else null
}

actual fun JsonElement.asNumber(): Number? {
    return if (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) this.asNumber else null
}

actual fun createJsonElement(any: Any?): JsonElement {
    return GsonBuilder().serializeNulls().create().toJsonTree(any)
}