package com.pubnub.api.models.consumer.pubsub.message_actions

import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PubSubResult
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult

/**
 * Wrapper around message actions received in [SubscribeCallback.messageAction].
 *
 * @property event The message action event. Could be `added` or `removed`.
 * @property data The actual message action.
 */
class PNMessageActionResult(
    private val result: BasePubSubResult,
    override val event: String,
    override val data: PNMessageAction,
) : ObjectResult<PNMessageAction>, PubSubResult by result {
    val messageAction: PNMessageAction = data

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

        other as PNMessageActionResult

        if (result != other.result) {
            return false
        }
        if (event != other.event) {
            return false
        }
        if (data != other.data) {
            return false
        }
        if (messageAction != other.messageAction) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result1 = result.hashCode()
        result1 = 31 * result1 + event.hashCode()
        result1 = 31 * result1 + data.hashCode()
        result1 = 31 * result1 + messageAction.hashCode()
        return result1
    }
}
