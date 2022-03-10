package com.pubnub.api.state

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicReference

typealias Transition<S, EV, EF> = (S, EV) -> Pair<S, Collection<EF>>

class EventEngine<S, EV, EF> private constructor(
    private val currentState: AtomicReference<S>,
    private val transition: Transition<S, EV, EF>
) {
    companion object {
        fun <S, EV, EF> create(
            initialState: S,
            initialEvent: EV,
            transition: Transition<S, EV, EF>
        ): Pair<EventEngine<S, EV, EF>, Collection<EF>> {

            val (ns, effects) = transition(initialState, initialEvent)

            return EventEngine(
                currentState = AtomicReference(ns),
                transition = transition
            ) to effects
        }
    }

    fun state(): S = currentState.get()

    fun transition(event: EV): Collection<EF> {
        val (ns, effects) = transition(currentState.get(), event)
        currentState.set(ns)
        return effects
    }
}

class QueuedEngine<IN>(
    private val inputQueue: LinkedBlockingQueue<IN>,
    private val executorService: ExecutorService,
) {
    private lateinit var inputLoop: CompletableFuture<Void>

    private fun <T> LinkedBlockingQueue<T>.waitAndProcess(
        executorService: ExecutorService,
        handle: (T) -> Unit
    ): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            while (!Thread.interrupted()) {
                try {
                    handle(take())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }

        }, executorService)
    }

    fun run(processSingleInput: (IN) -> Unit) {
        inputLoop = inputQueue.waitAndProcess(executorService = executorService) { processSingleInput(it) }
    }

    fun cancel() {
        inputLoop.cancel(true)
    }
}

class QueuedEventEngine<S, EV, EF> private constructor(
    private val eventEngine: EventEngine<S, EV, EF>,
    private val eventQueue: LinkedBlockingQueue<EV>,
    private val effectsQueue: LinkedBlockingQueue<EF>,
    private val queuedEngine: QueuedEngine<EV>
) {
    companion object {
        fun <S, EV, EF> create(
            eventEngine: EventEngine<S, EV, EF>,
            initialEffects: Collection<EF> = listOf(),
            eventQueue: LinkedBlockingQueue<EV>,
            effectQueue: LinkedBlockingQueue<EF>,
            executorService: ExecutorService = Executors.newFixedThreadPool(1)
        ): QueuedEventEngine<S, EV, EF> {

            initialEffects.forEach { effectQueue.put(it) }
            val queuedEngine = QueuedEngine(inputQueue = eventQueue, executorService = executorService)

            return QueuedEventEngine(
                eventEngine = eventEngine,
                eventQueue = eventQueue,
                effectsQueue = effectQueue,
                queuedEngine = queuedEngine
            ).apply { queuedEngine.run(this::processSingleInput) }
        }
    }

    fun state(): S = eventEngine.state()

    fun handle(event: EV) {
        eventQueue.put(event)
    }

    fun processSingleInput(event: EV) {
        val effects = eventEngine.transition(event)
        effects.forEach { effectsQueue.put(it) }
    }

    fun cancel() {
        queuedEngine.cancel()
    }
}
