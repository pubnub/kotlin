package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.test.CommonUtils.createInterceptor
import com.pubnub.test.Keys
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class BaseIntegrationTest {
    protected val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    val pubnub: PubNub by lazy { createPubNub() }
    val server: PubNub by lazy { createServer() }
    var clientConfig: PNConfiguration.Builder.() -> Unit = {}

    val serverConfig = PNConfiguration.builder(UserId("server-${UUID.randomUUID()}"), Keys.pamSubKey)

    private var mGuestClients = mutableListOf<PubNub>()

    @Before
    @BeforeEach
    fun before() {
        onPrePubnub()
        onBefore()
    }

    @After
    @AfterEach
    fun after() {
        onAfter()
        for (guestClient in mGuestClients) {
            destroyClient(guestClient)
        }
        mGuestClients.clear()
    }

    protected fun createPubNub(action: PNConfiguration.Builder.() -> Unit): PubNub {
        var pnConfiguration = provideStagingConfiguration(action)
        if (pnConfiguration == null) {
            pnConfiguration = getBasicPnConfiguration().apply(action).build()
        }
        val pubNub = PubNub.create(pnConfiguration)
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun createPubNub(): PubNub {
        var pnConfiguration = provideStagingConfiguration()
        if (pnConfiguration == null) {
            pnConfiguration = getBasicPnConfiguration().apply(clientConfig).build()
        }
        val pubNub = PubNub.create(pnConfiguration)
        registerGuestClient(pubNub)
        return pubNub
    }

//    protected fun createPubNub(config: PNConfiguration): PubNub {
//        val pubNub = PubNub.create(config)
//        registerGuestClient(pubNub)
//        return pubNub
//    }

    protected fun createServer(action: PNConfiguration.Builder.() -> Unit = {}): PubNub {
        val pubNub = PubNub.create(getServerPnConfiguration(action))
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun registerGuestClient(guestClient: PubNub) {
        mGuestClients.add(guestClient)
    }

    protected open fun getBasicPnConfiguration(): PNConfiguration.Builder {
        val clientConfig = PNConfiguration.builder(
            UserId("client-${UUID.randomUUID()}"),
            if (!needsServer()) {
                Keys.subKey
            } else {
                Keys.pamSubKey
            },
        )
        if (!needsServer()) {
            clientConfig.publishKey = Keys.pubKey
        } else {
            clientConfig.publishKey = Keys.pamPubKey
            clientConfig.authKey = provideAuthKey()!!
        }
        clientConfig.retryConfiguration = RetryConfiguration.None
        clientConfig.logVerbosity = PNLogVerbosity.NONE
//        clientConfig.httpLoggingInterceptor = createInterceptor(logger) // todo remove when all places get logging statements
        clientConfig.logVerbosity = PNLogVerbosity.BODY
        clientConfig.customLoggers = listOf(ObfuscatingLogger())

        return clientConfig
    }

    private fun getServerPnConfiguration(action: PNConfiguration.Builder.() -> Unit = {}): PNConfiguration {
        serverConfig.subscribeKey = Keys.pamSubKey
        serverConfig.publishKey = Keys.pamPubKey
        serverConfig.secretKey = Keys.pamSecKey
        serverConfig.logVerbosity = PNLogVerbosity.NONE
        serverConfig.httpLoggingInterceptor = createInterceptor(logger)
        serverConfig.action()

        return serverConfig.build()
    }

    private fun destroyClient(client: PubNub) {
        client.unsubscribeAll()
        client.forceDestroy()
    }

    private fun needsServer() = provideAuthKey() != null

    protected open fun onBefore() = noAction()

    protected open fun onAfter() = noAction()

    protected open fun onPrePubnub() = noAction()

    protected open fun provideAuthKey(): String? = null

    protected open fun provideStagingConfiguration(action: PNConfiguration.Builder.() -> Unit = {}): PNConfiguration? = null

    fun wait(seconds: Int = 3) {
        Thread.sleep((seconds * 1_000).toLong())
    }

    private fun noAction() {
        // No action
    }
}
