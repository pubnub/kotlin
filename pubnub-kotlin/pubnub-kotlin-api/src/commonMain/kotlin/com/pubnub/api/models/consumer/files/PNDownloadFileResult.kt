package com.pubnub.api.models.consumer.files

import com.pubnub.kmp.Downloadable

data class PNDownloadFileResult(
    val fileName: String,
    val byteStream: Downloadable?,
)
