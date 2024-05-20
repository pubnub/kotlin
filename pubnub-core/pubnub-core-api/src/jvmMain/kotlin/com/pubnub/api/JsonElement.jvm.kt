package com.pubnub.api

actual typealias JsonValue = com.google.gson.JsonElement

actual fun JsonValue.asString(): String {
    return this.asString
}