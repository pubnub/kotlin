package com.pubnub.api

expect abstract class JsonElement
//expect class JsonObject

expect annotation class SerializedName(val value: String, val  alternate: Array<String> = [])