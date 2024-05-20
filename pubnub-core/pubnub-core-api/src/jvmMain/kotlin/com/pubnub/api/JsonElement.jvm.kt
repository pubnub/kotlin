package com.pubnub.api

actual typealias JsonElement = com.google.gson.JsonElement

actual fun JsonElement.asString(): String {
    return this.asString
}