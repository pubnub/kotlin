package com.pubnub.api.state

import java.util.concurrent.atomic.AtomicReference

interface ManagedEffectHandler : EffectHandler {
    fun cancel()
}

interface EffectHandler {
    fun start()

    companion object {
        fun create(): EffectHandler {
            return EffectHandlerImplementation(startFn = {})
        }

        fun create(startFn: () -> Unit): EffectHandler {
            return EffectHandlerImplementation(startFn = startFn)
        }

        fun <T> create(startFn: () -> T, cancelFn: T.() -> Unit): ManagedEffectHandler {
            return ManagedEffectHandlerImplementation(startFn = startFn, cancelFn = cancelFn)
        }
    }

    class ManagedEffectHandlerImplementation<T> internal constructor(
        private val startFn: () -> T, private val cancelFn: T.() -> Unit
    ) : ManagedEffectHandler {
        private var cancellable: AtomicReference<T?> = AtomicReference(null)

        override fun start() {
            cancellable.compareAndSet(null, startFn())
        }

        override fun cancel() {
            cancellable.get()?.cancelFn()
        }
    }

    class EffectHandlerImplementation internal constructor(
        private val startFn: () -> Unit
    ) : EffectHandler {

        override fun start() {
            startFn()
        }
    }
}

interface EffectHandlerFactory<EF> {
    fun handler(effect: EF): EffectHandler
}
