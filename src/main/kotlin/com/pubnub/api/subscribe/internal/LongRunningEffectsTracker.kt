package com.pubnub.api.subscribe.internal

import com.pubnub.api.state.Effect

class LongRunningEffectsTracker() {

    fun track(effect: Effect, cancelFn: CancelFn) {
        println("Tracking ${effect.id}")
    }

    fun cancel(effectId: String) {
        println("Canceled tracking of $effectId")
    }

    fun stopTracking(effectId: String) {
        println("Stopped tracking of $effectId")
    }
}