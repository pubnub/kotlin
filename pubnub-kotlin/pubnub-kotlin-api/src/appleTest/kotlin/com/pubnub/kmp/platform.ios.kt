package com.pubnub.kmp

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes

internal actual val PLATFORM: String = "iOS"

@OptIn(ExperimentalForeignApi::class)
internal actual fun stringToUploadable(content: String, contentType: String?): Uploadable {
    val byteArray = content.encodeToByteArray()
    val nsData = byteArray.usePinned {
        NSData.dataWithBytes(it.addressOf(0), byteArray.size.toULong())
    }
    return DataUploadContent(nsData, contentType)
}

internal actual fun readAllBytes(stream: Any?): ByteArray {
    TODO("Not yet implemented")
}
