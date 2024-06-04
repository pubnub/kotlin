@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

import com.pubnub.kmp.JsMap
import com.pubnub.kmp.toMap

actual abstract class JsonElement(val value: Any?)

class JsonElementImpl(value: Any?) : JsonElement(value)

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
    println(value.toString() + " " + value!!::class)
    return (value as? Number)?.toDouble()
}

actual fun JsonElement.asNumber(): Number? {
    return value as? Number
}