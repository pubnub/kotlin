@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

import com.pubnub.kmp.JsMap
import com.pubnub.kmp.toMap
import kotlin.js.json

actual abstract class JsonElement(val value: Any?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is JsonElement) {
            return false
        }

        if (asMap() != null && other.asMap() != null) {
            return asMap() == other.asMap()
        } else if (asList() != null && other.asList() != null) {
            return asList() == other.asList()
        }
        if (value != other.value) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}

class JsonElementImpl(value: Any?) : JsonElement(value) {
    override fun toString(): String {
        return "JsonElementImpl($value : ${value?.let { it::class }})"
    }
}

actual fun JsonElement.asString(): String? {
    return value as? String
}

actual fun JsonElement.asMap(): Map<String, JsonElement>? {
    if (value?.isJsObject() != true) {
        return null
    }
    return value.unsafeCast<JsMap<Any>>().toMap().mapValues { JsonElementImpl(it.value) }
}

actual fun JsonElement.isNull(): Boolean {
    return value == null
}

actual fun JsonElement.asList(): List<JsonElement>? {
    return (value as? Array<*>)?.map { JsonElementImpl(it) }
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

private fun Any.isJsObject(): Boolean {
    return this !is Array<*> && jsTypeOf(this) == "object"
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
