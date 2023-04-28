package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.ManagedEffect
import java.time.Duration
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class DelayedManagedEffect(
    private val managedEffect: ManagedEffect,
    private val executorService: ScheduledExecutorService,
    private val delay: Duration
) : ManagedEffect {

    @Transient
    private var scheduled: ScheduledFuture<*>? = null

    @Transient
    private var cancelled = false

    @Synchronized
    override fun runEffect(completionBlock: () -> Unit) {
        if (cancelled) {
            return
        }

        scheduled = executorService.schedule({
            try {
                managedEffect.runEffect(completionBlock)
            } finally {
                completionBlock()
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS)
    }

    @Synchronized
    override fun cancel() {
        if (cancelled) {
            return
        }

        cancelled = true
        managedEffect.cancel()
        scheduled?.cancel(true)
    }
}
