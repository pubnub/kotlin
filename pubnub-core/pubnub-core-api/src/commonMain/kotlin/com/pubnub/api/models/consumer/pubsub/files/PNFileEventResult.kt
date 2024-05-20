package com.pubnub.api.models.consumer.pubsub.files

import com.pubnub.api.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.models.consumer.files.PNDownloadableFile
import com.pubnub.api.models.consumer.pubsub.PNEvent

class PNFileEventResult(
    override val channel: String,
    // timetoken in every other event model is nullable
    override val timetoken: Long?,
    val publisher: String?,
    val message: Any?,
    val file: PNDownloadableFile,
    val jsonMessage: JsonElement,
    override val subscription: String? = null,
    val error: PubNubError? = null,
) : PNEvent {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (this::class != other::class) {
            return false
        }

        other as PNFileEventResult

        if (channel != other.channel) {
            return false
        }
        if (timetoken != other.timetoken) {
            return false
        }
        if (publisher != other.publisher) {
            return false
        }
        if (message != other.message) {
            return false
        }
        if (file != other.file) {
            return false
        }
        if (jsonMessage != other.jsonMessage) {
            return false
        }
        if (subscription != other.subscription) {
            return false
        }
        if (error != other.error) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = channel.hashCode()
        result = 31 * result + (timetoken?.hashCode() ?: 0)
        result = 31 * result + (publisher?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + file.hashCode()
        result = 31 * result + jsonMessage.hashCode()
        result = 31 * result + (subscription?.hashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}
