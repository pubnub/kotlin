package com.pubnub.api.models.consumer.message_actions

import com.pubnub.internal.PubNub

/**
 * Result for the [PubNub.addMessageAction] API operation.
 *
 * Essentially a wrapper around [PNMessageAction].
 */
class PNAddMessageActionResult internal constructor(action: PNMessageAction) :
    PNMessageAction(action)
