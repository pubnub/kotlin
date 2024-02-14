package com.pubnub.api.models.consumer.message_actions

import com.pubnub.internal.PubNubImpl

/**
 * Result for the [PubNubImpl.addMessageAction] API operation.
 *
 * Essentially a wrapper around [PNMessageAction].
 */
class PNAddMessageActionResult internal constructor(action: PNMessageAction) :
    PNMessageAction(action)
