package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Effect
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


data class TrackedEffect(
    val cancelFn: CancelFn? = null
)

class LongRunningEffectsTracker(
    private val effects: MutableMap<String, TrackedEffect> = Collections.synchronizedMap(mutableMapOf()),
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
) {

    fun track(effect: Effect, cancelFn: CancelFn) {
        println("Tracking ${effect.id}")
        synchronized(effects) {
            if (!effects.contains(effect.id)) {
                effects[effect.id] = TrackedEffect(cancelFn = cancelFn)
            } else {
                cancelFn()
            }
        }
    }

    fun cancel(effectId: String) {
        println("Canceled tracking of $effectId")
        synchronized(effects) {
            effects.getOrPut(effectId) { TrackedEffect() }.let {
                it.cancelFn?.invoke()
            }
            executorService.schedule({ effects.remove(effectId) }, 1, TimeUnit.SECONDS)
        }
    }

    fun stopTracking(effectId: String) {
        println("Stopping tracking of $effectId")

        synchronized(effects) {
            effects.getOrPut(effectId) { TrackedEffect() }
            executorService.schedule({ effects.remove(effectId) }, 1, TimeUnit.SECONDS)
        }
    }
}