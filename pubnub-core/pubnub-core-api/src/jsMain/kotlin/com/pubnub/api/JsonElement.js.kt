@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

actual abstract class JsonElement(val value: Any?)

class JsonElementImpl(value: Any?) : JsonElement(value)

actual fun JsonElement.asString(): String {
    return value.toString() // todo kmp
}