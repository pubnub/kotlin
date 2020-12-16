package com.pubnub.api.models.consumer.files

data class PNFileUploadResult(
    val timetoken: Long,
    val status: Int,
    val file: PNFile
)
