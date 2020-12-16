package com.pubnub.api.models.consumer.pubsub.files

import com.pubnub.api.models.consumer.files.PNDownloadableFile

data class PNFileEventResult(
    val channel: String,
    val timetoken: Long?, // timetoken in every other event model is nullable
    val publisher: String?,
    val message: Any?,
    val file: PNDownloadableFile
)
