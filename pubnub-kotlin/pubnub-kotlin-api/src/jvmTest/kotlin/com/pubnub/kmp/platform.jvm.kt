package com.pubnub.kmp

import java.io.ByteArrayInputStream
import java.io.InputStream

internal actual val PLATFORM: String = "JVM"

internal actual fun stringToUploadable(content: String, contentType: String?): Uploadable =
    ByteArrayInputStream(content.toByteArray())

internal actual fun readAllBytes(stream: Any?): ByteArray {
    return (stream as InputStream).readBytes()
}
