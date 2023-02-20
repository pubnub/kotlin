package com.pubnub.api

import com.pubnub.api.models.consumer.PNStatus
import org.awaitility.Awaitility
import org.awaitility.Durations
import java.util.concurrent.atomic.AtomicBoolean

fun <Input, Output> Endpoint<Input, Output>.await(function: (result: Output?, status: PNStatus) -> Unit) {
    val success = AtomicBoolean()
    async { result, status ->
        function.invoke(result, status)
        success.set(true)
    }
    success.listen()
}

fun PNStatus.param(param: String) = clientRequest!!.url.queryParameter(param)

fun PNStatus.encodedParam(param: String) =
    clientRequest!!.url.encodedQuery!!.encodedParamString(param)

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

fun AtomicBoolean.listen(seconds: Int = CommonUtils.DEFAULT_LISTEN_DURATION) {
    CommonUtils.observe(this, seconds)
}
