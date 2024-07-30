package com.pubnub.api.models.consumer.files

data class PNFileUploadResult(
    val timetoken: Long,
    @Deprecated("This field's value is always `200` and so it will be removed in a future release.")
    val status: Int,
    val file: PNBaseFile,
)
