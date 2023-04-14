package com.pubnub.api.subscribe.eventengine.handler

import com.pubnub.api.subscribe.eventengine.event.Event
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingQueue

interface EffectHandler {
    fun stop()
    fun getCompletableFutureThatHandlesEffect(
        currentlyExecutingEffects: MutableList<CompletableFuture<String>>,
        eventQueue: LinkedBlockingQueue<Event?>?
    ): CompletableFuture<String>
}
