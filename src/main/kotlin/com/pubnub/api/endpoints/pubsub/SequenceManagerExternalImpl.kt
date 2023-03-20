package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.managers.PublishSequenceManager

class SequenceManagerExternalImpl internal constructor(private val publishSequenceManager: PublishSequenceManager) :
    SequenceManagerExternal {
    override fun nextSequence(): Int {
        return publishSequenceManager.nextSequence()
    }
}
