@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.pubnub.api

actual abstract class JsonElement

actual fun JsonElement.asString(): String {
    return this.toString() // todo ?
}