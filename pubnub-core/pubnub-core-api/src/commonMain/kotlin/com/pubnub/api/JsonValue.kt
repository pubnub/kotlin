package com.pubnub.api

expect abstract class JsonValue
//expect class JsonObject

expect fun JsonValue.asString(): String
//expect fun JsonElement.asNumber(): Number
//expect fun JsonElement.asMap(): Map<String, Any?>
//expect fun JsonElement.asList(): List<Any?>
