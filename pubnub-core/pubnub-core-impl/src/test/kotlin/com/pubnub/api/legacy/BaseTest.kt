package com.pubnub.api.legacy

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.internal.BasePubNubImpl
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.TestPubNub
import com.pubnub.test.CommonUtils.defaultListenDuration
import com.pubnub.test.TestPNConfigurationImpl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before

abstract class BaseTest {
    lateinit var wireMockServer: WireMockServer
    protected val pubnubBase: TestPubNub by lazy {
        TestPubNub(config.build())
    }
    protected val pubnub: PubNubCore by lazy {
        pubnubBase.pubNubCore
    }
    protected lateinit var config: TestPNConfigurationImpl.Builder private set

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
        TestPNConfigurationImpl.Builder(userId = UserId("myUUID")).apply {
            subscribeKey = "mySubscribeKey"
            publishKey = "myPublishKey"
            origin = wireMockServer.baseUrl().toHttpUrlOrNull()!!.run { "$host:$port" }
            secure = false
            logVerbosity = PNLogVerbosity.BODY
        }

    fun clearConfiguration() {
        config = TestPNConfigurationImpl.Builder(userId = UserId(BasePubNubImpl.generateUUID()))
    }
}
