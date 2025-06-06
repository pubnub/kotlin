package com.pubnub.kmp

internal actual val PLATFORM: String = "JS"

internal actual fun stringToUploadable(content: String, contentType: String?): Uploadable =
    UploadableImpl(content)

internal actual fun readAllBytes(stream: Any?): ByteArray {
    TODO("Not yet implemented")
}
