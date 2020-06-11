package com.pubnub.api.models.server

import com.google.gson.annotations.SerializedName

class DeleteMessagesEnvelope(
    val status: Int?,
    val error: Boolean?,
    @SerializedName("error_message")
    val errorMessage: String?
)