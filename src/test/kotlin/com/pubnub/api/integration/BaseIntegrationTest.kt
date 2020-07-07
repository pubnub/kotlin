package com.pubnub.api.integration

import com.google.gson.JsonObject
import com.pubnub.api.DEFAULT_LISTEN_DURATION
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.randomValue
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {

    protected val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

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
        DEFAULT_LISTEN_DURATION = 5
    }

    @BeforeEach
    private fun before() {
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
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = createInterceptor(logger)

        pnConfiguration.uuid = "client-${UUID.randomUUID()}"
        return pnConfiguration
    }

    private fun getServerPnConfiguration(): PNConfiguration {
        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = PAM_SUB_KEY
        pnConfiguration.publishKey = PAM_PUB_KEY
        pnConfiguration.secretKey = PAM_SEC_KEY
        pnConfiguration.logVerbosity = PNLogVerbosity.NONE
        pnConfiguration.httpLoggingInterceptor = createInterceptor(logger)

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

    fun wait(seconds: Int = 3) {
        Thread.sleep((seconds * 1_000).toLong())
    }
}

internal fun PubNub.subscribeToBlocking(vararg channels: String) {
    this.subscribe().apply {
        this.channels = listOf(*channels)
        withPresence = true
    }.execute()
    Thread.sleep(2000)
}

internal fun PubNub.unsubscribeFromBlocking(vararg channels: String) {
    unsubscribe().apply {
        this.channels = listOf(*channels)
    }.execute()
    Thread.sleep(2000)
}

fun publishMixed(pubnub: PubNub, count: Int, channel: String): List<PNPublishResult> {
    val list = mutableListOf<PNPublishResult>()
    repeat(count) {
        val sync = pubnub.publish().apply {
            this.channel = channel
            this.message = "${it}_msg"
            val meta = when {
                it % 2 == 0 -> generateMap()
                it % 3 == 0 -> randomValue(4)
                else -> null
            }
            meta?.let { this.meta = it }
        }.sync()
        list.add(sync!!)
    }
    return list
}

fun generateMap() = mapOf(
    "text" to randomValue(8),
    "uncd" to unicode(),
    "info" to randomValue(8)
)

fun generatePayload(): JsonObject {
    return JsonObject().apply {
        addProperty("text", randomValue())
        addProperty("uncd", unicode(8))
        addProperty("info", randomValue())
    }
}

fun generateMessage(pubnub: PubNub): JsonObject {
    return JsonObject().apply {
        addProperty("publisher", pubnub.configuration.uuid)
        addProperty("text", randomValue())
        addProperty("uncd", unicode(8))
    }
}

fun generatePayloadJSON(): JSONObject {
    return JSONObject().apply {
        put("text", randomValue())
        put("uncd", unicode(8))
        put("info", randomValue())
    }
}

fun unicode(length: Int = 5): String {
    val unicodeChars = "!?+-="
    return unicodeChars.toList().shuffled().take(length).joinToString(separator = "")
}

fun emoji(): String {
    return listOf(
        "😀",
        "😁",
        "😂",
        "🤣",
        "😃",
        "😄",
        "😅",
        "😆",
        "😉",
        "😊",
        "😋",
        "😎",
        "😍",
        "😘",
        "🥰",
        "😗",
        "😙",
        "😚",
        "☺️",
        "🙂",
        "🤗",
        "🤩",
        "🤔",
        "🤨",
        "😐",
        "😑",
        "😶",
        "🙄",
        "😏",
        "😣",
        "😥",
        "😮",
        "🤐",
        "😯",
        "😪",
        "😫",
        "😴",
        "😌",
        "😛",
        "😜",
        "😝",
        "🤤",
        "😒",
        "😓",
        "😔",
        "😕",
        "🙃",
        "🤑",
        "😲",
        "☹️",
        "🙁",
        "😖",
        "😞",
        "😟",
        "😤",
        "😢",
        "😭",
        "😦",
        "😧",
        "😨",
        "😩",
        "🤯",
        "😬",
        "😰",
        "😱",
        "🥵",
        "🥶",
        "😳",
        "🤪",
        "😵",
        "😡",
        "😠",
        "🤬",
        "😷",
        "🤒",
        "🤕",
        "🤢",
        "🤮",
        "🤧",
        "😇",
        "🤠",
        "🤡",
        "🥳",
        "🥴",
        "🥺",
        "🤥",
        "🤫",
        "🤭",
        "🧐",
        "🤓",
        "😈",
        "👿",
        "👹",
        "👺",
        "💀",
        "👻",
        "👽",
        "🤖",
        "💩",
        "😺",
        "😸",
        "😹",
        "😻",
        "😼",
        "😽",
        "🙀",
        "😿",
        "😾"
    ).shuffled().take(5).joinToString("")
}

private fun createInterceptor(logger: Logger) =
    HttpLoggingInterceptor {
        logger.debug(it)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
