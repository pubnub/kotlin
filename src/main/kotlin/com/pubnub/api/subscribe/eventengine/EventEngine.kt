package com.pubnub.api.subscribe.eventengine

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EffectInvocation
import com.pubnub.api.eventengine.EffectSink
import com.pubnub.api.eventengine.EffectSource
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.EventSource
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.eventengine.Source
import com.pubnub.api.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.SubscribeState
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue

class EventEngine(
    intialState: SubscribeState = SubscribeState.Unsubscribed,
    private val eventSource: EventSource,
    private val effectSink: EffectSink,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {

    private val log = LoggerFactory.getLogger(EventEngine::class.java)
    private var task: Future<*>? = null
    private var currentState: SubscribeState = intialState

    fun start() {
        task = executorService.submit {
            while (true) {
                try {
                    val event = eventSource.next()
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
            effectSink.add(it)
        }
    }
}

interface EventBus : Bus<Event>, EventSink, EventSource

interface EffectBus<T : EffectInvocation> : Bus<T>, EffectSink<T>, EffectSource<T>

interface Bus<T> : Sink<T>, Source<T>

interface QueueBus<T> : Bus<T>, Source<T>, Sink<T> {
    val queue: BlockingQueue<T>

    override fun add(el: T) {
        queue.put(el)
    }

    override fun next(): T = queue.take()
}

interface QueueEventBus : QueueBus<Event>
interface QueueEffectBus : QueueBus<SubscribeEffectInvocation>, EffectBus<SubscribeEffectInvocation>