package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.createInterceptor
import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class BaseIntegrationTest {

    protected val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    val pubnub: PubNubImpl by lazy { createPubNub() }
    val server: PubNubImpl by lazy { createServer() }

    private var mGuestClients = mutableListOf<PubNubImpl>()

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

    protected fun createPubNub(): PubNubImpl {
        var pnConfiguration = provideStagingConfiguration()
        if (pnConfiguration == null) {
            pnConfiguration = getBasicPnConfiguration()
        }
        val pubNub = PubNubImpl(pnConfiguration)
        registerGuestClient(pubNub)
        return pubNub
    }

    protected fun createPubNub(config: PNConfiguration): PubNubImpl {
        val pubNub = PubNubImpl(config)
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun createServer(): PubNubImpl {
        val pubNub = PubNubImpl(getServerPnConfiguration())
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun registerGuestClient(guestClient: PubNubImpl) {
        mGuestClients.add(guestClient)
    }

    protected open fun getBasicPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration(userId = UserId(PubNubImpl.generateUUID()))
        if (!needsServer()) {
            pnConfiguration.subscribeKey = Keys.subKey
            pnConfiguration.publishKey = Keys.pubKey
        } else {
            pnConfiguration.subscribeKey = Keys.pamSubKey
            pnConfiguration.publishKey = Keys.pamPubKey
            pnConfiguration.authKey = provideAuthKey()!!
        }
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = createInterceptor(logger)

        pnConfiguration.userId = UserId("client-${UUID.randomUUID()}")
        return pnConfiguration
    }

    private fun getServerPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration(userId = UserId(PubNubImpl.generateUUID()))
        pnConfiguration.subscribeKey = Keys.pamSubKey
        pnConfiguration.publishKey = Keys.pamPubKey
        pnConfiguration.secretKey = Keys.pamSecKey
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = createInterceptor(logger)

        pnConfiguration.userId = UserId("server-${UUID.randomUUID()}")
        return pnConfiguration
    }

    private fun destroyClient(client: PubNubImpl) {
        client.unsubscribeAll()
        client.forceDestroy()
    }

    private fun needsServer() = provideAuthKey() != null

    protected open fun onBefore() = noAction()
    protected open fun onAfter() = noAction()
    protected open fun onPrePubnub() = noAction()

    protected open fun provideAuthKey(): String? = null

    protected open fun provideStagingConfiguration(): PNConfiguration? = null

    fun wait(seconds: Int = 3) {
        Thread.sleep((seconds * 1_000).toLong())
    }

    private fun noAction() {
        // No action
    }
}
