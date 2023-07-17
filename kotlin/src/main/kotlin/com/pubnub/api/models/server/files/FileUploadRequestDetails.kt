package com.pubnub.api.models.server.files

import com.pubnub.api.models.consumer.files.PNFile

data class FileUploadRequestDetails(
    val status: Int,
    val data: PNFile,
    val url: String,
    val method: String,
    val expirationDate: String,
    val keyFormField: FormField,
    val formFields: List<FormField>
)
