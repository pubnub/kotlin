package com.pubnub.api.models.consumer.pubsub.message_actions

import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult

class PNMessageActionResult(
    result: BasePubSubResult,
    event: String,
    data: PNMessageAction
) : ObjectResult<PNMessageAction>(result, event, data) {

    val messageAction = data
}