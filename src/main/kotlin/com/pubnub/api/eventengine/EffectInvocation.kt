package com.pubnub.api.eventengine

interface EffectInvocation {
    val id: String
}

interface CancelEffectInvocation {
    val idToCancel: String
}

interface ManagedEffectInvocation
