package com.pubnub.api.state

interface EffectHandler {
    fun start()
    fun cancel()
}


interface EffectHandlerFactory<EF> {
    fun handler(effect: EF): EffectHandler
}

