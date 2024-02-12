package com.pubnub.api

import org.awaitility.Awaitility
import org.awaitility.Durations
import org.awaitility.pollinterval.FibonacciPollInterval
import org.hamcrest.core.IsEqual
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

fun <Input, Output> Endpoint<Input, Output>.await(function: (result: Result<Output>) -> Unit) {
    val success = AtomicBoolean()
    async { result ->
        function.invoke(result)
        success.set(true)
    }
    success.listen()
}

fun String.encodedParamString(param: String): String {
    return split("&")
        .first { it.startsWith(param) }
        .split("=")[1]
}

fun AtomicBoolean.listen(function: () -> Boolean): AtomicBoolean {
    Awaitility.await()
        .atMost(Durations.FIVE_SECONDS)
        .with()
        .until {
            function.invoke()
        }
    return this
}

fun <Input, Output> Endpoint<Input, Output>.asyncRetry(
    function: (result: Result<Output>) -> Unit
) {
    val hits = AtomicInteger(0)

    val block = {
        hits.incrementAndGet()
        val latch = CountDownLatch(1)
        val success = AtomicBoolean()
        queryParam += mapOf("key" to UUID.randomUUID().toString())
        async { result ->
            try {
                function.invoke(result)
                success.set(true)
            } catch (e: Throwable) {
                success.set(false)
            }
            latch.countDown()
        }
        latch.await(2L, TimeUnit.SECONDS)
        success.get()
    }

    Awaitility.await()
        .atMost(CommonUtils.DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
        .pollInterval(FibonacciPollInterval(TimeUnit.SECONDS))
        .until {
            block.invoke()
        }
}

fun <Input, Output> Endpoint<Input, Output>.retryForbidden(
    onFail: (exception: Throwable) -> Unit,
    function: (result: Result<Output>) -> Unit
) {
    val success = AtomicBoolean()

    // first run should return forbidden
    async { result ->
        result.onFailure {
            println(it)
//        if (status.error && status.statusCode == 403) { //TODO check what exception is thrown for 403 and how to detect it
//            onFail.invoke(status)
//            success.set(false)
//        }
        }
    }

    Awaitility.await()
        .atMost(10L, TimeUnit.SECONDS)
        .untilAtomic(success, IsEqual.equalTo(false))

    Thread.sleep(2_000L)

    // retry and invoke callback
    queryParam += mapOf("key" to UUID.randomUUID().toString())
    async { result ->
        try {
            function.invoke(result)
            success.set(true)
        } catch (e: Throwable) {
            success.set(false)
        }
    }

    Awaitility.await()
        .atMost(CommonUtils.DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
        .pollInterval(FibonacciPollInterval(TimeUnit.SECONDS))
        .untilAtomic(success, IsEqual.equalTo(true))
}

fun AtomicBoolean.listen(seconds: Int = CommonUtils.DEFAULT_LISTEN_DURATION) {
    CommonUtils.observe(this, seconds)
}

fun PubNub.subscribeToBlocking(vararg channels: String) {
    this.subscribe(
        channels = listOf(*channels),
        withPresence = true
    )
    Thread.sleep(2000)
}

fun PubNub.unsubscribeFromBlocking(vararg channels: String) {
    unsubscribe(
        channels = listOf(*channels)
    )
    Thread.sleep(2000)
}
