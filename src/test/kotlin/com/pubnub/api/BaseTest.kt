package com.pubnub.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseTest {

    private val wireMockServer: WireMockServer = WireMockServer()

    protected var pubnub = PubNub(PNConfiguration().apply {
        subscribeKey = "mySubscribeKey"
        publishKey = "myPublishKey"
        uuid = "myUUID"
        origin = "localhost:8080"
        secure = false
        logVerbosity = PNLogVerbosity.BODY
    })

    @BeforeEach
    fun beforeEach() {
        onBefore()
        wireMockServer.start()
    }

    @AfterEach
    fun afterEach() {
        wireMockServer.stop()
        onAfter()
    }

    open fun onBefore() {

    }

    open fun onAfter() {

    }
}

interface IEndpointTest {
    fun provideTelemetryParameterName(): String
    fun providePnOperation(): PNOperationType
    fun provideKeyMatrix(): Int
    @Test
    fun testTelemetryParameter()
    fun provideWorkingCode(): Endpoint<*, *>
}

val SUB = 0x001
val PUB = 0x010
val AUTH = 0x100

private fun observe(success: AtomicBoolean) {
    Awaitility.await()
        .atMost(Durations.FIVE_SECONDS)
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
    Assertions.assertEquals(expectedPubNubError, pnStatus.exception!!.pubnubError)
}

fun assertPnException(expectedPubNubError: PubNubError, exception: Exception) {
    exception as PubNubException
    Assertions.assertEquals(expectedPubNubError, exception.pubnubError)
}

fun PNStatus.param(param: String) = clientRequest!!.url().queryParameter(param)

fun emptyJson() = WireMock.aResponse().withBody("{}")

fun failTest(message: String? = null) {
    Assertions.fail<String>(message)
}