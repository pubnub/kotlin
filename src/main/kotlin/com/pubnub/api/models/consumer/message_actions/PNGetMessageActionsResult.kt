package com.pubnub.api.models.consumer.message_actions

import com.pubnub.api.PubNub

/**
 * Result for the [PubNub.getMessageActions] API operation.
 *
 * @property actions List of message actions for a certain message in a certain channel.
 */
class PNGetMessageActionsResult internal constructor(
    val actions: List<PNMessageAction>
)
