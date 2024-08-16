package com.pubnub.api.legacy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.PubNubImpl
import com.pubnub.test.CommonUtils.defaultListenDuration
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before

abstract class BaseTest {
    lateinit var wireMockServer: WireMockServer
    protected val pubnub: PubNubImpl by lazy {
        PubNubImpl(config.build())
    }
    protected lateinit var config: PNConfiguration.Builder private set

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
    }

    open fun onAfter() {
        pubnub.forceDestroy()
    }

    fun initConfiguration() {
        config = createConfiguration()
    }

    fun createConfiguration() =
        PNConfiguration.builder(userId = UserId("myUUID"), "") {
            subscribeKey = "mySubscribeKey"
            publishKey = "myPublishKey"
            origin = wireMockServer.baseUrl().toHttpUrlOrNull()!!.run { "$host:$port" }
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }

    fun clearConfiguration() {
        config = PNConfiguration.builder(userId = UserId(PubNubImpl.generateUUID()), "")
    }
}
