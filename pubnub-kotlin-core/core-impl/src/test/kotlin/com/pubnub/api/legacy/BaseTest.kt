package com.pubnub.api.legacy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.pubnub.test.CommonUtils.defaultListenDuration
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.internal.BasePubNubImpl
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before

abstract class BaseTest {
    lateinit var wireMockServer: WireMockServer
    protected lateinit var pubnubBase: TestPubNub private set
    protected lateinit var pubnub: InternalPubNubClient private set
    protected lateinit var config: PNConfiguration private set

    @Before
    open fun beforeEach() {
        wireMockServer =
            WireMockServer(
                wireMockConfig()
                    .bindAddress("localhost")
                    .dynamicPort(),
            )
        wireMockServer.start()
        WireMock.configureFor("http", "localhost", wireMockServer.port())
        defaultListenDuration = 2

        onBefore()
    }

    @After
    open fun afterEach() {
        wireMockServer.stop()
        wireMockServer.findAllUnmatchedRequests().forEach {
            println("Unmatched ${it.url}")
        }
        assertTrue(wireMockServer.findAllUnmatchedRequests().isEmpty())
        onAfter()
    }

    open fun onBefore() {
        initConfiguration()
        initPubNub()
    }

    open fun onAfter() {
        pubnub.forceDestroy()
    }

    fun initConfiguration() {
        config = createConfiguration()
    }

    fun createConfiguration() =
        PNConfiguration(userId = UserId("myUUID")).apply {
            subscribeKey = "mySubscribeKey"
            publishKey = "myPublishKey"
            origin = wireMockServer.baseUrl().toHttpUrlOrNull()!!.run { "$host:$port" }
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }

    fun clearConfiguration() {
        config = PNConfiguration(userId = UserId(BasePubNubImpl.generateUUID()))
    }

    fun initPubNub(customPubNub: TestPubNub? = null) {
        if (::pubnub.isInitialized) {
            pubnub.destroy()
        }
        pubnubBase = customPubNub ?: TestPubNub(config)
        pubnub = pubnubBase.internalPubNubClient
    }
}
