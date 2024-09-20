package com.pubnub.internal.eventengine

internal interface ManagedEffect : Effect {
    fun cancel()
}
