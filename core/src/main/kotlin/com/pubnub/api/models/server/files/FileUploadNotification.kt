package com.pubnub.api.models.server.files

import com.pubnub.api.models.consumer.files.PNBaseFile

data class FileUploadNotification(
    val message: Any?,
    val file: PNBaseFile
)
