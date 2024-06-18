package com.pubnub.kmp

import platform.Foundation.NSData
import platform.Foundation.NSURL

actual class CustomObject(val value: Any)

actual abstract class Uploadable

data class DataUploadContent(
    val data: NSData,
    val contentType: String?
): Uploadable()

data class FileUploadContent(
    val url: NSURL
) : Uploadable()

data class StreamUploadContent(
    val url: NSURL,
    val contentType: String?,
    val contentLength: Int
) : Uploadable()
