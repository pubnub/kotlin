package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.effect.EventHandler
import com.pubnub.api.subscribe.eventengine.effect.EventSource
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue

class EventEngine(
    intialState: State,
    private val eventSource: EventSource,
    private val effectDispatcher: EffectDispatcher<EffectInvocation>,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {

    private val log = LoggerFactory.getLogger(EventEngine::class.java)
    private var task: Future<*>? = null
    private var currentState: State = intialState

    fun start() {
        task = executorService.submit {
            while (true) {
                try {
                    val event = eventSource.nextEvent()
                    handleEvent(event)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }

    private fun handleEvent(event: Event) {
        log.trace("Handling event: {}", event)
        val (newState, invocations) = currentState.transition(event)
        currentState = newState
        invocations.forEach {
            effectDispatcher.dispatch(it)
        }
    }
}

class QueueEventHandler(private val queue: BlockingQueue<Event> = LinkedBlockingQueue()) :
    EventHandler, EventSource {

    override fun handleEvent(event: Event) {
        queue.put(event)
    }

    override fun nextEvent(): Event = queue.take()
}
