package com.pubnub.kmp

internal expect val PLATFORM: String

internal expect fun stringToUploadable(content: String, contentType: String? = null): Uploadable

internal expect fun readAllBytes(stream: Any?): ByteArray
