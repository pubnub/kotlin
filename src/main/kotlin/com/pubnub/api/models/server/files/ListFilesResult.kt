package com.pubnub.api.models.server.files

import com.pubnub.api.models.consumer.files.PNUploadedFile
import com.pubnub.api.models.consumer.objects.PNPage

data class ListFilesResult(
    val count: Int,
    val next: String?,
    val status: Int,
    val data: Collection<PNUploadedFile>
)
