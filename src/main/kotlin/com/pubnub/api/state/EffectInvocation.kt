package com.pubnub.api.state

interface EffectInvocation {
    fun id() = this::class.simpleName!!
}
