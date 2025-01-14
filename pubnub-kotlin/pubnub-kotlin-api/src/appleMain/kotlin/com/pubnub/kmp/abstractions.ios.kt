package com.pubnub.kmp

import platform.Foundation.NSData
import platform.Foundation.NSInputStream
import platform.Foundation.NSURL

actual abstract class Uploadable

data class DataUploadContent(
    val data: NSData,
    val contentType: String?
) : Uploadable()

data class FileUploadContent(
    val url: NSURL
) : Uploadable()

data class StreamUploadContent(
    val stream: NSInputStream,
    val contentType: String?,
    val contentLength: Int
) : Uploadable()
