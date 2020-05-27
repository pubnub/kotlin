package com.pubnub.api.legacy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class BaseTest {

    protected val wireMockServer = WireMockServer(wireMockConfig())

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
        wireMockServer.start()
        onBefore()
    }

    @AfterEach
    fun afterEach() {
        wireMockServer.stop()
        // assertTrue(wireMockServer.findAllUnmatchedRequests().isEmpty())
        onAfter()
    }

    open fun onBefore() {

    }

    open fun onAfter() {

    }
}
