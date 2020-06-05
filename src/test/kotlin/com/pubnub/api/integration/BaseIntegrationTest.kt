package com.pubnub.api.integration

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import java.util.*
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {

    companion object {
        const val TIMEOUT_MEDIUM = 5
        const val TIMEOUT_LOW = 2
        lateinit var PUB_KEY: String
        lateinit var SUB_KEY: String
        lateinit var PAM_PUB_KEY: String
        lateinit var PAM_SUB_KEY: String
        lateinit var PAM_SEC_KEY: String
    }

    lateinit var pubnub: PubNub
    lateinit var server: PubNub

    private var mGuestClients = mutableListOf<PubNub>()

    @BeforeAll
    fun extractProperties() {
        val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
        val properties = Properties()
        properties.load(inputStream)
        PUB_KEY = properties.getProperty("pub_key")
        SUB_KEY = properties.getProperty("sub_key")
        PAM_PUB_KEY = properties.getProperty("pam_pub_key")
        PAM_SUB_KEY = properties.getProperty("pam_sub_key")
        PAM_SEC_KEY = properties.getProperty("pam_sec_key")
    }

    @BeforeEach
    open fun before(): Unit {
        onPrePubnub()
        pubnub = createPubNub()
        if (needsServer()) {
            server = createServer()
        }
        onBefore()
    }

    @AfterEach
    open fun after() {
        onAfter()
        destroyClient(pubnub)
        for (guestClient in mGuestClients) {
            destroyClient(guestClient)
        }
    }

    protected fun createPubNub(): PubNub {
        var pnConfiguration = provideStagingConfiguration()
        if (pnConfiguration == null) {
            pnConfiguration = getBasicPnConfiguration()
        }
        val pubNub = PubNub(pnConfiguration)
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun createServer(): PubNub {
        val pubNub = PubNub(getServerPnConfiguration())
        registerGuestClient(pubNub)
        return pubNub
    }

    private fun registerGuestClient(guestClient: PubNub) {
        mGuestClients.add(guestClient)
    }

    protected open fun getBasicPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration()
        if (!needsServer()) {
            pnConfiguration.subscribeKey = SUB_KEY
            pnConfiguration.publishKey = PUB_KEY
        } else {
            pnConfiguration.subscribeKey = PAM_SUB_KEY
            pnConfiguration.publishKey = PAM_PUB_KEY
            pnConfiguration.authKey = provideAuthKey()!!
        }
        pnConfiguration.logVerbosity = PNLogVerbosity.BODY
        pnConfiguration.uuid = "client-${UUID.randomUUID()}"
        return pnConfiguration
    }

    private fun getServerPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = PAM_SUB_KEY
        pnConfiguration.publishKey = PAM_PUB_KEY
        pnConfiguration.secretKey = PAM_SEC_KEY
        pnConfiguration.logVerbosity = PNLogVerbosity.BODY
        pnConfiguration.uuid = "server-${UUID.randomUUID()}"
        return pnConfiguration
    }

    private fun destroyClient(client: PubNub) {
        client.unsubscribeAll()
        client.forceDestroy()
    }

    private fun needsServer() = provideAuthKey() != null

    protected open fun onBefore() {}
    protected open fun onAfter() {}
    protected open fun onPrePubnub() {}

    protected open fun provideAuthKey(): String? {
        return null
    }

    protected open fun provideStagingConfiguration(): PNConfiguration? {
        return null
    }

}

fun randomValue(length: Int = 10): String {
    return ((0..9).toList().map(Int::toString) + ('A'..'Z').toList().map(Char::toString))
        .shuffled()
        .toList()
        .take(length)
        .joinToString(separator = "")
}

fun randomNumeric(length: Int = 10): String {
    return generateSequence { (0..9).random() }.take(length).toList().shuffled().joinToString(separator = "")
}