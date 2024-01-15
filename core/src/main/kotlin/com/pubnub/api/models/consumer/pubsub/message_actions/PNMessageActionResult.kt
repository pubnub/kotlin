package com.pubnub.api.models.consumer.pubsub.message_actions

import com.pubnub.api.callbacks.SubscribeCallback
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
data class PNMessageActionResult(
    private val result: BasePubSubResult,
    override val event: String,
    override val data: PNMessageAction
) : ObjectResult<PNMessageAction>, PubSubResult by result {
    val messageAction: PNMessageAction = data
}
