package com.pubnub.api

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.suite.SpecialChar
import okhttp3.HttpUrl
import okhttp3.Request
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Collectors


private fun observe(success: AtomicBoolean) {
    Awaitility.await()
        .atMost(Durations.TWO_SECONDS)
        .with()
        .until(success::get)
}

fun AtomicBoolean.listen(): AtomicBoolean {
    this.set(false)
    observe(this)
    return this
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

fun PNStatus.printQueryParams() {
    this.clientRequest!!.url().queryParameterNames().map {
        print("$it ${this.clientRequest?.url()?.queryParameterValues(it)?.first()} ")
    }
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