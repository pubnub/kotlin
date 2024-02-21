package com.pubnub.internal.extension

import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import kotlin.time.Duration

fun ScheduledExecutorService.scheduleWithDelay(delay: Duration, action: () -> Unit): ScheduledFuture<*> =
    schedule(action, delay.inWholeMilliseconds, java.util.concurrent.TimeUnit.MILLISECONDS)
