package com.pubnub.kmp

actual class CustomObject(private val value: Any) : Codable {
    override fun toCodable(): Any = if (value is Codable) {
        value.toCodable()
    } else {
        value
    }
}