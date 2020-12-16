package com.pubnub.api.models.consumer.files

import java.io.InputStream

data class PNDownloadFileResult(
    val fileName: String,
    val byteStream: InputStream?
)
