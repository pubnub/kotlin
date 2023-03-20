package com.pubnub.api.endpoints.pubsub

interface SequenceManagerExternal {
    fun nextSequence(): Int
}
