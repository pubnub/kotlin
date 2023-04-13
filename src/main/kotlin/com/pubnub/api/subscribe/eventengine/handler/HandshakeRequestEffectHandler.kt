package com.pubnub.api.subscribe.eventengine.handler

import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class HandshakeRequestEffectHandler : EffectHandler {
    override fun stop() {
        println("Stopping handling HandshakeRequestEffectHandler")
    }

    override fun getCompletableFutureThatHandlesEffect(
        currentlyExecutingEffects: MutableList<CompletableFuture<String>>,
        eventQueue: LinkedBlockingQueue<Event?>?
    ): CompletableFuture<String> {
        val completableFuture = CompletableFuture<String>()
        currentlyExecutingEffects.add(completableFuture)
        Executors.newCachedThreadPool().submit<Any?> {
            var result = ""
            try {
                Thread.sleep(10000) // simulate 10s execution
                result = "SUCCESS"
                //jesli jest sukcess to dodaj do kolejki Eventów "RECONNECTION_SUCCESS"(Handshake Success)
                eventQueue?.add(Event.Success())
            } catch (e: InterruptedException) {
                result = "ERROR"
                //jeśli jest error to co dodaj do kolejki Eventów "RECONNECTION_FAILURE"(Handshake Success)
                eventQueue?.add(Event.Fail())
            }
            completableFuture.complete(result)
            currentlyExecutingEffects.remove(completableFuture)
            null
        }
        return completableFuture
    }
}
