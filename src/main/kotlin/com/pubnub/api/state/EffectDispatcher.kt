package com.pubnub.api.state

internal interface EffectTracker {
    val trackedHandlers: MutableMap<String, ManagedEffectHandler>
    fun startTracking(id: String, handler: ManagedEffectHandler) {
        trackedHandlers[id] = handler
    }

    fun stopTracking(idToCancel: String) {
        trackedHandlers.remove(idToCancel)?.cancel()
    }
}

interface EffectDispatcher<EF : EffectInvocation> {
    fun dispatch(effect: EF)
}
