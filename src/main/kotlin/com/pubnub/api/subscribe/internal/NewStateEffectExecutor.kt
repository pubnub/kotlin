package com.pubnub.api.subscribe.internal

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.state.CancelFn
import com.pubnub.api.state.EffectExecutor
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass


internal class NewStateEffectExecutor(private val listenerManager: ListenerManager) : EffectExecutor<NewState> {
    private var previousStateRef: AtomicReference<NewState> = AtomicReference()

    override fun execute(effect: NewState, longRunningEffectDone: (String) -> Unit): CancelFn {
        val previousState = previousStateRef.getAndSet(effect)

        when {
            transition(
                previousState,
                effect,
                Handshaking::class,
                Receiving::class
            ) -> listenerManager.announce(
                PNStatus(
                    PNStatusCategory.PNConnectedCategory,
                    error = false,
                    operation = PNOperationType.PNSubscribeOperation
                )
            )
            transition(previousState, effect, Reconnecting::class, Receiving::class) -> listenerManager.announce(
                PNStatus(
                    PNStatusCategory.PNReconnectedCategory,
                    error = false,
                    operation = PNOperationType.PNSubscribeOperation
                )
            )

            state(effect, ReconnectingFailed::class) -> listenerManager.announce(
                PNStatus(
                    PNStatusCategory.PNReconnectionAttemptsExhausted,
                    error = true,
                    operation = PNOperationType.PNSubscribeOperation
                )

            )

        }

        return {}
    }

    private fun <T : SubscribeState> state(new: NewState, state: KClass<T>): Boolean {
        return new.name == state.simpleName!!
    }

    private fun <T : SubscribeState, U : SubscribeState> transition(
        prev: NewState?,
        new: NewState,
        from: KClass<T>,
        to: KClass<U>
    ): Boolean {
        return prev != null && prev.name == from.simpleName!! && new.name == to.simpleName!!
    }
}