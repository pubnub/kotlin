package com.pubnub.api.models.consumer.pubsub.files

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.pubsub.PNEvent

data class PNFileEventResult(
    override val channel: String,
    // timetoken in every other event model is nullable
    override val timetoken: Long?,
    val publisher: String?,
    val message: Any?,
    val file: PNDownloadableFile,
    val jsonMessage: JsonElement,
    override val subscription: String? = null,
    val error: PubNubError? = null,
) : PNEvent
