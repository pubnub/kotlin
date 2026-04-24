package com.pubnub.api.eventengine

internal interface ManagedEffect : Effect {
    fun cancel()
}
