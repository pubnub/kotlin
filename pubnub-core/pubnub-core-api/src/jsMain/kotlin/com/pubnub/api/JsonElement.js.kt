@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

import com.pubnub.kmp.JsMap
import com.pubnub.kmp.toMap

actual abstract class JsonElement(val value: Any?)

class JsonElementImpl(value: Any?) : JsonElement(value)

actual fun JsonElement.asString(): String {
    return value.toString() // todo kmp
}

actual fun JsonElement.asMap(): Map<String, JsonElement> {
    return value.unsafeCast<JsMap<Any>>().toMap().mapValues { JsonElementImpl(it.value) } // todo kmp
}