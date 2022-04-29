package com.pubnub.api.state.internal

import com.pubnub.api.state.EffectDispatcher
import com.pubnub.api.state.EffectInvocation
import com.pubnub.api.state.Engine
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

class Queued<IN, H> private constructor(
    val syncHandler: H,
    private val inputQueue: LinkedBlockingQueue<IN>,
    private val executorService: ExecutorService,
    private val handle: H.(IN) -> Unit
) {
    companion object {
        fun <IN, H> createAndRun(
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

    private val logger = LoggerFactory.getLogger(Queued::class.java)

    private lateinit var inputLoop: CompletableFuture<Void>

    private fun <T> LinkedBlockingQueue<T>.waitAndProcess(
        executorService: ExecutorService,
        handle: (T) -> Unit
    ): CompletableFuture<Void> {
        return CompletableFuture.runAsync(
            Runnable {
                while (!Thread.interrupted()) {
                    try {
                        handle(take())
                    } catch (e: InterruptedException) {
                        Thread.currentThread().interrupt()
                    } catch (e: Exception) {
                        logger.error("Uncaught exception when handling message from queue", e)
                        throw e
                    }
                }
            },
            executorService
        )
    }

    private fun run() {
        inputLoop = inputQueue.waitAndProcess(executorService = executorService) { syncHandler.handle(it) }
    }

    fun cancel() {
        inputLoop.cancel(true)
    }
}

internal class IntModule<S, EV, EF : EffectInvocation>(
    private val engine: Engine<S, EV, EF>,
    effectDispatcher: EffectDispatcher<EF>,
    private val eventQueue: LinkedBlockingQueue<EV>,
    private val effectQueue: LinkedBlockingQueue<EF>,
    private val queuedEngine: Queued<EV, Engine<S, EV, EF>> = Queued.createAndRun(
        syncHandler = engine,
        inputQueue = eventQueue
    ) { event ->
        transition(event).forEach {
            effectQueue.put(it)
        }
    },
    private val queuedEffectDispatcher: Queued<EF, EffectDispatcher<EF>> = Queued.createAndRun(
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
