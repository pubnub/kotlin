package com.pubnub.api.state

import java.util.concurrent.atomic.AtomicReference

interface EffectHandler {
    fun start()
    fun cancel()

    companion object {
        fun create(): EffectHandlerImplementation<Unit> {
            return EffectHandlerImplementation(startFn = {}, cancelFn = {})
        }

        fun <T> create(startFn: () -> T?, cancelFn: T.() -> Unit = {}): EffectHandlerImplementation<T> {
            return EffectHandlerImplementation(startFn = startFn, cancelFn = cancelFn)
        }
    }

    class EffectHandlerImplementation<T> internal constructor(
        private val startFn: () -> T?, private val cancelFn: T.() -> Unit
    ) : EffectHandler {
        private var cancellable: AtomicReference<T?> = AtomicReference(null)

        override fun start() {
            cancellable.compareAndSet(null, startFn())
        }

        override fun cancel() {
            cancellable.get()?.cancelFn()
        }
    }

}


interface EffectHandlerFactory<EF> {
    fun handler(effect: EF): EffectHandler
}

