package com.pubnub.api.eventengine

interface ManagedEffect : Effect {
    fun cancel()
}
