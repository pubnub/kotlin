package com.pubnub.extension

import java.time.Duration
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture

fun ScheduledExecutorService.scheduleWithDelay(delay: Duration, action: () -> Unit): ScheduledFuture<*> = schedule(action, delay.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS)
