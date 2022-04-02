package com.pubnub.api.state

import java.util.concurrent.atomic.AtomicReference

interface ManagedEffectHandler : EffectHandler {

}

interface EffectHandler {
    fun start()
    fun cancel()

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

    class ManagedEffectHandlerImplementation internal constructor(
        private val startFn: () -> Unit, private val cancelFn: () -> Unit
    ) : ManagedEffectHandler {

        override fun start() {
            startFn()
        }

        override fun cancel() {
            cancelFn()
        }
    }

    class EffectHandlerImplementation internal constructor(
        private val startFn: () -> Unit
    ) : EffectHandler {

        override fun start() {
            startFn()
        }

        override fun cancel() {

        }
    }
}

interface EffectHandlerFactory<EF> {
    fun handler(effect: EF): EffectHandler
}
