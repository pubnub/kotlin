package com.pubnub.api.state.internal

import com.pubnub.api.state.EffectDispatcher
import com.pubnub.api.state.Engine
import com.pubnub.api.state.Transition
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicReference

class EventEngine<S, EV, EF> private constructor(
    private val state: AtomicReference<S>,
    private val transition: Transition<S, EV, EF>
) : Engine<S, EV, EF> {
    companion object {
        fun <S, EV, EF> create(
            initialState: S,
            initialEvent: EV,
            transition: Transition<S, EV, EF>
        ): Pair<EventEngine<S, EV, EF>, List<EF>> {

            val (ns, effects) = transition(initialState, initialEvent)

            return EventEngine(
                state = AtomicReference(ns),
                transition = transition
            ) to effects
        }
    }

    override fun currentState(): S = state.get()

    override fun transition(event: EV): List<EF> {
        val (ns, effects) = transition(state.get(), event)
        state.set(ns)
        return effects
    }
}

class Queued<IN, H> private constructor(
    val syncHandler: H,
    private val inputQueue: LinkedBlockingQueue<IN>,
    private val executorService: ExecutorService,
    private val handle: H.(IN) -> Unit
) {
    companion object {
        fun <IN, H> create(
            syncHandler: H,
            inputQueue: LinkedBlockingQueue<IN>,
            executorService: ExecutorService = Executors.newSingleThreadExecutor(),
            handle: H.(IN) -> Unit
        ): Queued<IN, H> {
            return Queued(
                syncHandler = syncHandler,
                inputQueue = inputQueue,
                executorService = executorService,
                handle = handle
            ).apply { run() }
        }
    }

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

    private fun run() {
        inputLoop = inputQueue.waitAndProcess(executorService = executorService) { syncHandler.handle(it) }
    }

    fun cancel() {
        inputLoop.cancel(true)
    }

}

internal class IntModule<S, EV, EF>(
    private val engine: Engine<S, EV, EF>,
    effectDispatcher: EffectDispatcher<EF>,
    private val eventQueue: LinkedBlockingQueue<EV>,
    private val effectQueue: LinkedBlockingQueue<EF>,
    private val queuedEngine: Queued<EV, Engine<S, EV, EF>> = Queued.create(
        syncHandler = engine,
        inputQueue = eventQueue,
    ) { event ->
        transition(event).forEach {
            effectQueue.put(it)
        }
    },
    private val queuedEffectDispatcher: Queued<EF, EffectDispatcher<EF>> = Queued.create(
        syncHandler = effectDispatcher,
        inputQueue = effectQueue
    ) { effectInvocation ->
        dispatch(effectInvocation)
    }
) {

    fun currentState() = engine.currentState()

    fun cancel() {
        queuedEngine.cancel()
        queuedEffectDispatcher.cancel()
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

    fun state(): S = eventEngine.currentState()

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
