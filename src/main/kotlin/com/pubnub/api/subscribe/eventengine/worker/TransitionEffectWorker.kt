package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.handler.EffectHandler
import java.util.concurrent.LinkedBlockingQueue

class TransitionEffectWorker : Runnable {

    private val eventQueue: LinkedBlockingQueue<Event>
    private val effectQueue: LinkedBlockingQueue<EffectInvocation>
    private val effectToEffectHandlerMap: Map<EffectInvocation, EffectHandler> = getEffectToEffectHandlerMap()

    constructor(eventQueue: LinkedBlockingQueue<Event>, effectQueue: LinkedBlockingQueue<EffectInvocation>) {
        this.eventQueue = eventQueue
        this.effectQueue = effectQueue
    }


    override fun run() {
        TODO("Not yet implemented")
    }


    private fun getEffectToEffectHandlerMap(): Map<EffectInvocation, EffectHandler> {
        val effectToEffectHandlerMap: MutableMap<EffectInvocation, EffectHandler> = HashMap()
//        effectToEffectHandlerMap[Effect.HANDSHAKE_REQUEST] = HandshakeRequestEffectHandler()
        //... add more
        return effectToEffectHandlerMap
    }

}