package com.pubnub.api.models.consumer.files

import com.pubnub.api.models.consumer.objects.PNPage

data class PNListFilesResult(
    val count: Int,
    val next: PNPage.PNNext?,
    val status: Int,
    val data: Collection<PNUploadedFile>
)
