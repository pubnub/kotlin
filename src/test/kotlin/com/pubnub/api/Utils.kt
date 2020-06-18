package com.pubnub.api

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.api.models.consumer.PNStatus
import okhttp3.HttpUrl
import okhttp3.Request
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

var DEFAULT_LISTEN_DURATION = 5

private fun observe(success: AtomicBoolean, seconds: Int) {
    Awaitility.await()
        .atMost(seconds.toLong(), TimeUnit.SECONDS)
        .untilTrue(success)
}

fun AtomicBoolean.listen(seconds: Int = DEFAULT_LISTEN_DURATION) {
    observe(this, seconds)
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

fun assertPnException(expectedPubNubError: PubNubError, pnStatus: PNStatus) {
    Assertions.assertTrue(pnStatus.error)
    assertEquals(expectedPubNubError, pnStatus.exception!!.pubnubError)
}

fun assertPnException(expectedPubNubError: PubNubError, exception: Exception) {
    exception as PubNubException
    assertEquals(expectedPubNubError, exception.pubnubError)
}

fun PNStatus.param(param: String) = clientRequest!!.url().queryParameter(param)

fun PNStatus.encodedParam(param: String) = clientRequest!!.url().encodedQuery()!!.encodedParamString(param)

fun String.encodedParamString(param: String): String {
    return split("&")
        .first { it.startsWith(param) }
        .split("=")[1]
}

fun emptyJson() = WireMock.aResponse().withBody("{}")

fun failTest(message: String? = null) {
    Assertions.fail<String>(message)
}

private fun decomposeAndVerifySignature(
    configuration: PNConfiguration,
    url: String,
    method: String,
    body: String = ""
) {
    val httpUrl = HttpUrl.parse(url)
    println(httpUrl)

    val sortedQueryString = httpUrl!!.run {
        queryParameterNames()
            .filter { it != "signature" }
            .map { it to PubNubUtil.pamEncode(queryParameterValues(it).first()) }
            .toMap()
            .toSortedMap()
            .map { "${it.key}=${it.value}" }
            .joinToString("&")
    }

    var v2Signature = true

    val input =
        if (!(httpUrl.encodedPath().startsWith("/publish") && method.toLowerCase() == "post")) {
            """
                ${method.toUpperCase()}
                ${configuration.publishKey}
                ${httpUrl.encodedPath()}
                $sortedQueryString
                $body
            """.trimIndent()
        } else {
            v2Signature = false
            """
                ${configuration.subscribeKey}
                ${configuration.publishKey}
                ${httpUrl.encodedPath()}
                $sortedQueryString
                """.trimIndent()
        }

    val actualSignature = httpUrl.queryParameter("signature")
    val verifiedSignature = verifyViaPython(configuration.secretKey, input, v2Signature)

    val rebuiltSignature = PubNubUtil.signSHA256(configuration.secretKey, input).run {
        if (v2Signature) "v2.${this.trim('=')}"
        else this
    }

    println("originalTimestamp:\t${httpUrl.queryParameter("timestamp")}")
    println("signatureInput:\t$input")
    println("actualSignature:\t$actualSignature")
    println("rebuiltSignature:\t$rebuiltSignature")
    println("verifiedSignature:\t$verifiedSignature")

    assertEquals(actualSignature, rebuiltSignature)
    assertEquals(actualSignature, verifiedSignature)
}

fun decomposeAndVerifySignature(configuration: PNConfiguration, request: LoggedRequest) {
    decomposeAndVerifySignature(
        configuration = configuration,
        url = request.absoluteUrl,
        method = request.method.name,
        body = request.bodyAsString
    )
}

fun decomposeAndVerifySignature(configuration: PNConfiguration, request: Request) {
    decomposeAndVerifySignature(
        configuration = configuration,
        url = request.url().toString(),
        method = request.method(),
        body = PubNubUtil.requestBodyToString(request)!!
    )
}

private fun verifyViaPython(key: String, input: String, v2Signature: Boolean): String {
    val i = input.replace("\n", "###")
    val path = ClassLoader.getSystemClassLoader().getResource("hmacsha256.py")!!.path
    val command = "python $path $key $i $v2Signature"
    val process = Runtime.getRuntime().exec(command)
    return BufferedReader(InputStreamReader(process.inputStream)).readLine()
}

fun getSpecialCharsMap(): List<SpecialChar> {
    val resourceFileAsString = getResourceFileAsString("special_chars.json")
    return Gson().fromJson(resourceFileAsString, object : TypeToken<List<SpecialChar>>() {}.type)
}

fun getResourceFileAsString(fileName: String?): String? {
    val classLoader = ClassLoader.getSystemClassLoader()
    classLoader.getResourceAsStream(fileName).use { inputStream ->
        if (inputStream == null) return null
        InputStreamReader(inputStream).use { isr ->
            BufferedReader(isr).use { reader ->
                return reader.lines().collect(Collectors.joining(System.lineSeparator()))
            }
        }
    }
}

fun Any.stringify() = Gson().toJson(this)

class SpecialChar(
    val name: String,
    val regular: String,
    val encoded: String
)

fun retry(function: () -> Unit) {
    val success = AtomicBoolean()
    val block = {
        try {
            val executor = Executors.newSingleThreadExecutor()
            val submit = executor.submit(
                Callable {
                    try {
                        function.invoke()
                        true
                    } catch (t: Throwable) {
                        false
                    }
                })
            success.set(submit.get())
        } catch (t: Throwable) {
            success.set(false)
        }
    }

    Awaitility.await()
        .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
        .pollInterval(Durations.FIVE_HUNDRED_MILLISECONDS)
        .until {
            block.invoke()
            success.get()
        }
}

fun <Input, Output> Endpoint<Input, Output>.await(function: (result: Output?, status: PNStatus) -> Unit) {
    val success = AtomicBoolean()
    async { result, status ->
        function.invoke(result, status)
        success.set(true)
    }
    success.listen()
}

fun <Input, Output> Endpoint<Input, Output>.asyncRetry(
    function: (result: Output?, status: PNStatus) -> Unit
) {
    val hits = AtomicInteger(0)

    val block = {
        hits.incrementAndGet()
        val latch = CountDownLatch(1)
        val success = AtomicBoolean()
        queryParam = mapOf("key" to UUID.randomUUID().toString())
        async { result, status ->
            try {
                function.invoke(result, status)
                success.set(true)
            } catch (e: Throwable) {
                success.set(false)
            }
            latch.countDown()
        }
        latch.await()
        success.get()
    }

    Awaitility.await()
        .atMost(DEFAULT_LISTEN_DURATION!!.toLong(), TimeUnit.SECONDS)
        .pollInterval(Durations.FIVE_HUNDRED_MILLISECONDS)
        .until { block.invoke() }
}

fun randomValue(length: Int = 10): String {
    return UUID.randomUUID().toString()
        .replace("-", "")
        .take(length)
        .toUpperCase()
}

fun randomChannel(): String {
    return "ch_${System.currentTimeMillis()}_${randomValue()}"
}

fun randomNumeric(length: Int = 10): String {
    return generateSequence { (0..9).random() }.take(length).toList().shuffled().joinToString(separator = "")
}
