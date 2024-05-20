@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

actual abstract class JsonValue(val value: Any?)

class JsonValueImpl(value: Any?) : JsonValue(value)

actual fun JsonValue.asString(): String {
    return value.toString() // todo kmp
}