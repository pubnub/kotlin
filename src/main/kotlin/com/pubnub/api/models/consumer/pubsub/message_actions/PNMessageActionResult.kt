package com.pubnub.api.models.consumer.pubsub.message_actions

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult

/**
 * Wrapper around message actions received in [SubscribeCallback.messageAction].
 *
 * @property event The message action event. Could be `added` or `removed`.
 * @property data The actual message action.
 */
class PNMessageActionResult internal constructor(
    result: BasePubSubResult,
    override val event: String,
    data: PNMessageAction
) : ObjectResult<PNMessageAction>(result, event, data) {
    val messageAction = data
}
