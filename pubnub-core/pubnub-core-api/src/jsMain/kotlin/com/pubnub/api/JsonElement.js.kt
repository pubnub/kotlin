@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

import com.pubnub.kmp.JsMap
import com.pubnub.kmp.toMap
import kotlin.js.json

actual abstract class JsonElement(val value: Any?)

class JsonElementImpl(value: Any?) : JsonElement(value) {
    override fun toString(): String {
        return "JsonElementImpl(${value.toString()} : ${value?.let { it::class }})"
    }
}

actual fun JsonElement.asString(): String? {
    return value as? String
}

actual fun JsonElement.asMap(): Map<String, JsonElement>? {
    return value.unsafeCast<JsMap<Any>>().toMap().mapValues { JsonElementImpl(it.value) }
}

actual fun JsonElement.isNull(): Boolean {
    return value == null
}

actual fun JsonElement.asList(): List<JsonElement>? {
    return (value as Array<*>).map { JsonElementImpl(it) }
}

actual fun JsonElement.asLong(): Long? {
    return (value as? Number)?.toLong() ?: (value as? String)?.toLongOrNull()
}

actual fun JsonElement.asBoolean(): Boolean? {
    return value as? Boolean
}

actual fun JsonElement.asDouble(): Double? {
    return (value as? Number)?.toDouble()
}

actual fun JsonElement.asNumber(): Number? {
    return value as? Number
}

actual fun createJsonElement(any: Any?): JsonElement {
    return JsonElementImpl(any.adjustCollectionTypes())
}

internal fun Any?.adjustCollectionTypes(): Any? {
    return when (this) {
        is Map<*, *> -> {
            val json = json()
            entries.forEach {
                val value = it.value.adjustCollectionTypes()
                if (value is JsonElementImpl) {
                    json[it.key.toString()] = value.value
                } else {
                    json[it.key.toString()] = value
                }
            }
            json
        }

        is Collection<*> -> {
            this.map { it.adjustCollectionTypes() }.map {
                    if (it is JsonElementImpl) {
                        it.value
                    } else {
                        it
                    }
                }.toTypedArray()
        }

        is Array<*> -> {
            this.map { it.adjustCollectionTypes() }.map {
                if (it is JsonElementImpl) {
                    it.value
                } else {
                    it
                }
            }.toTypedArray()
        }

        is Long -> {
            return toString()
        }

        else -> this
    }
}