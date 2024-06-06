@file:OptIn(ExperimentalForeignApi::class)

package com.pubnub.api

import cocoapods.PubNubSwift.AnyJSONObjC
import kotlinx.cinterop.ExperimentalForeignApi

actual abstract class JsonElement(val value: Any?)

class JsonElementImpl(value: Any?) : JsonElement(value)

actual fun JsonElement.asString(): String? {
    return when (value) {
        is AnyJSONObjC -> value.asString()
        is String -> value
        else -> null
    }
}

actual fun JsonElement.asMap(): Map<String, JsonElement>? {
    return when (value) {
        is AnyJSONObjC -> (value.asMap() as Map<String, Any>).mapValues {
                JsonElementImpl(it.value)
            }
        is Map<*, *> -> (value as Map<String, *>).mapValues { JsonElementImpl(it.value) }
        else -> null
    }
}

actual fun JsonElement.isNull(): Boolean {
    return value == null || (value as? AnyJSONObjC)?.isNull() == true
}

actual fun JsonElement.asList(): List<JsonElement>? {
    return when (value) {
        is AnyJSONObjC -> value.asList()?.map {
                JsonElementImpl(it)
            }
        is List<*> -> value.map { JsonElementImpl(it) }
        else -> null
    }
}

actual fun JsonElement.asLong(): Long? {
    return when (value) {
        is AnyJSONObjC -> value.asInt()?.longValue
        is Long -> value
        is Int -> value.toLong()
        is Boolean -> if (value) 1 else 0
        else -> null
    }
}

actual fun JsonElement.asBoolean(): Boolean? {
    return when (value) {
        is AnyJSONObjC -> value.asBool()?.boolValue
        is Boolean -> value
        else -> null
    }
}

actual fun JsonElement.asDouble(): Double? {
    return when (value) {
        is AnyJSONObjC -> value.asDouble()?.doubleValue
        is Number -> value.toDouble()
        is Boolean -> if (value) 1.0 else 0.0
        else -> null
    }
}

actual fun JsonElement.asNumber(): Number? {
    println(value.toString() + " " + value!!::class)
    return when (value) {
        is AnyJSONObjC -> value.asNumber() as Number
        is Number -> value
        is Boolean -> if (value) 1 else 0
        else -> null
    }
}

actual fun createJsonElement(any: Any?): JsonElement {
    TODO("Not yet implemented")
}