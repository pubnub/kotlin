package com.pubnub.api.models.consumer.message_actions

import com.pubnub.api.PubNub

/**
 * Result for the [PubNub.addMessageAction] API operation.
 *
 * Essentially a wrapper around [PNMessageAction].
 */
class PNAddMessageActionResult internal constructor(action: PNMessageAction) :
    PNMessageAction(action)
