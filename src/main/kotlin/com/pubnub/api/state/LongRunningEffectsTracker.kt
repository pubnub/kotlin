package com.pubnub.api.state

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class LongRunningEffectsTracker(
    private val effects: MutableMap<String, CancelFn> = Collections.synchronizedMap(mutableMapOf()),
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
) {

    fun track(effect: Effect, cancelFnProvider: () -> CancelFn) {
        println("Tracking ${effect.id}")
        synchronized(effects) {
            if (!effects.contains(effect.id)) {
                effects[effect.id] = cancelFnProvider()
            } else {
                //do nothing. Already tracked
            }
        }
    }

    fun cancel(effectId: String) {
        println("Canceled tracking of $effectId")
        synchronized(effects) {
            effects.getOrPut(effectId) { {} }.invoke()
            executorService.schedule({ effects.remove(effectId) }, 1, TimeUnit.SECONDS)
        }
    }

    fun stopTracking(effectId: String) {
        println("Stopping tracking of $effectId")

        synchronized(effects) {
            effects.getOrPut(effectId) { {} }
            executorService.schedule({ effects.remove(effectId) }, 1, TimeUnit.SECONDS)
        }
    }
}