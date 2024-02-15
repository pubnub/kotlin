package com.pubnub.api.models.consumer.message_actions

/**
 * Result for the AddMessageAction API operation.
 *
 * Essentially a wrapper around [PNMessageAction].
 */
class PNAddMessageActionResult internal constructor(action: PNMessageAction) :
    PNMessageAction(action)
