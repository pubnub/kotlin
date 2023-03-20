package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Request
import okio.Timeout
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TEST_MESSAGE = "test message in the bottle"
private const val TEST_CHANNEL = "testChannel"
private const val TEST_SUBKEY = "subKey"
private const val TEST_PUBKEY = "pubKey"
private const val CIPHER_KEY = "enigma"
private const val PN_VERSION = "PubNub-Kotlin/version"

class PublishTest {
    private val pnConfigurationHardcodedIV = PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
        subscribeKey = TEST_SUBKEY
        publishKey = TEST_PUBKEY
        cipherKey = CIPHER_KEY
    }
    private val pnConfigurationRandomIV = PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
        subscribeKey = TEST_SUBKEY
        publishKey = TEST_PUBKEY
        cipherKey = CIPHER_KEY
        useRandomInitializationVector = true
    }

    private val configurationNeededForPublishWithHardcodedIVImpl = ConfigurationNeededForPublishImpl(pnConfigurationHardcodedIV)
    private val configurationNeededForPublishWithRandomIVImpl = ConfigurationNeededForPublishImpl(pnConfigurationRandomIV)
    private val configurationNeededForEndpointImpl = ConfigurationNeededForEndpointImpl(pnConfigurationHardcodedIV, PN_VERSION)

    private val publishServiceExternal = mockk<PublishServiceExternal>(relaxed = true)
    private val configurationNeededForPublishWithHardcodedIV = configurationNeededForPublishWithHardcodedIVImpl
    private val configurationNeededForPublishWithRandomIV = configurationNeededForPublishWithRandomIVImpl
    private val toJsonMapper = mockk<ToJsonMapper>(relaxed = true)
    private val sequenceManagerExternal = mockk<SequenceManagerExternal>(relaxed = true)
    private val telemetryManagerExternal = mockk<TelemetryManagerExternal>(relaxed = true)
    private val configurationNeededForEndpoint = configurationNeededForEndpointImpl
    private val pnInstanceIdProvider = mockk<PNInstanceIdProvider>(relaxed = true)
    private val tokenRetriever = mockk<TokenRetriever>(relaxed = true)
    private val jsonMapperManagerForEndpoint = mockk<JsonMapperManagerForEndpoint>(relaxed = true)

    private val encryptedMessageSlot = slot<String>()

    class FakeCall : Call<List<Any>> {
        override fun clone(): Call<List<Any>> = this

        override fun execute(): Response<List<Any>> = Response.success(listOf("test1", "test2", "10"))

        override fun enqueue(callback: Callback<List<Any>>) {
        }

        override fun isExecuted(): Boolean = false

        override fun cancel() {
        }

        override fun isCanceled(): Boolean = false

        override fun request(): Request = Request.Builder().build()

        override fun timeout(): Timeout = Timeout.NONE
    }

    init {
        every {
            publishServiceExternal.publish(
                any(),
                any(),
                any(),
                capture(encryptedMessageSlot),
                any()
            )
        } answers { FakeCall() }
    }

    @Test
    fun `publish with encryption respects random IV setting`() {
        val publishHardcodedIVUnderTest =
            Publish(
                channel = TEST_CHANNEL,
                message = TEST_MESSAGE,
                publishServiceExternal = publishServiceExternal,
                configurationNeededForPublish = configurationNeededForPublishWithHardcodedIV,
                toJsonMapper = toJsonMapper,
                sequenceManagerExternal = sequenceManagerExternal,
                telemetryManagerExternal = telemetryManagerExternal,
                configurationNeededForEndpoint = configurationNeededForEndpoint,
                pnInstanceIdProvider = pnInstanceIdProvider,
                tokenRetriever = tokenRetriever,
                jsonMapperManagerForEndpoint = jsonMapperManagerForEndpoint
            )
        publishHardcodedIVUnderTest.sync()

        val encryptedMessageHardcodedIV = encryptedMessageSlot.captured

        val publishRandomIVUnderTest =
            Publish(
                channel = TEST_CHANNEL,
                message = TEST_MESSAGE,
                publishServiceExternal = publishServiceExternal,
                configurationNeededForPublish = configurationNeededForPublishWithRandomIV,
                toJsonMapper = toJsonMapper,
                sequenceManagerExternal = sequenceManagerExternal,
                telemetryManagerExternal = telemetryManagerExternal,
                configurationNeededForEndpoint = configurationNeededForEndpoint,
                pnInstanceIdProvider = pnInstanceIdProvider,
                tokenRetriever = tokenRetriever,
                jsonMapperManagerForEndpoint = jsonMapperManagerForEndpoint
            )
        publishRandomIVUnderTest.sync()

        val encryptedMessageRandomIV = encryptedMessageSlot.captured

        assertThat(encryptedMessageRandomIV, not(`is`(encryptedMessageHardcodedIV)))
    }
}
