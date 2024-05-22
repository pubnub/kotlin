package com.pubnub.api

expect abstract class JsonElement
//expect class JsonObject

expect fun JsonElement.asString(): String
//expect fun JsonElement.asNumber(): Number
expect fun JsonElement.asMap(): Map<String, JsonElement>
//expect fun JsonElement.asList(): List<Any?>
