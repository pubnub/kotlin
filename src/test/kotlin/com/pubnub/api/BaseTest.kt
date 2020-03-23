package com.pubnub.api

import com.github.tomakehurst.wiremock.WireMockServer
import com.pubnub.api.models.consumer.PNStatus
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseTest {

    private val wireMockServer = WireMockServer()

    protected val pubnub = PubNub(PNConfiguration().apply {
        subscribeKey = "mySubscribeKey"
        publishKey = "myPublishKey"
        uuid = "myUUID"
        origin = "localhost:8080"
        secure = false
    })

    @BeforeEach
    fun beforeEach() {
        wireMockServer.start()
    }

    @AfterEach
    fun afterEach() {
        wireMockServer.stop()
    }

}

private fun observe(success: AtomicBoolean) {
    Awaitility.await()
        .atMost(Durations.FIVE_SECONDS)
        .with()
        .until(success::get)
}

fun AtomicBoolean.listen(): AtomicBoolean {
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
    val map = this.clientRequest?.url()?.queryParameterNames()
        ?.map {
            print("$it ${this.clientRequest?.url()?.queryParameterValues(it)?.first()} ")
        }
}

fun assertPnException(pnStatus: PNStatus, pubNubError: PubNubError) {
    Assertions.assertTrue(pnStatus.error)
    Assertions.assertEquals(pubNubError, pnStatus.exception!!.pubnubError)
}

