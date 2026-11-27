@file:Suppress("ktlint")

package com.pubnub.docs.configuration

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.PNConfigurationOverride
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.assertNothingArrives
import com.pubnub.docs.support.Kit.assertTimetoken
import com.pubnub.docs.support.Kit.probe
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Redact
import com.pubnub.docs.support.Retry
import com.pubnub.docs.support.Stdout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Live tests of the Kotlin docs page configuration (docs/sdks/kotlin/api-reference/configuration.md). One test per snippet.
 * The text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions
 * below it. The samples build clients with placeholder keys. Where the sample's behavior only shows against the service
 * (a filter, a cipher key, a secret key, a publish key) a hidden line swaps in the live keys of a role.
 */
@ExtendWith(LiveExtension::class)
class ConfigurationDocsTest {
    private val OPEN = "open"

    private fun sender(clients: LiveClients) = clients.kotlinClient(OPEN, "docs-config-sender")

    // ---------------------------------------------------------------------------------------------------------------
    // Configuration
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun configurationMain() {
        val out = Stdout.captureForTest()

        com.pubnub.docs.configuration.samples.main()

        out.assertPrinted("PubNub Configuration Example")
        out.assertPrinted("1. Basic Configuration (Required Parameters)")
        out.assertPrinted("User ID: UserId(value=basic-user-id)")
        out.assertPrinted("User ID: UserId(value=advanced-user-id)")
        out.assertPrinted("User ID: UserId(value=encrypted-user-id)")
        // the basic and the advanced configuration have no cipher key, the third one has
        assertEquals(2, out.lines("Encryption: Disabled").size)
        assertEquals(1, out.lines("Encryption: Enabled").size)
        assertEquals(0, Redact.leakCount(out.text()), "the sample printed a key of the keysets")

        // the values the sample documents
        val basic = com.pubnub.docs.configuration.samples.createBasicConfig()
        assertEquals("basic-user-id", basic.userId.value)
        assertEquals("demo", basic.subscribeKey)
        assertEquals("demo", basic.publishKey)
        assertTrue(basic.secure)
        assertNull(basic.cryptoModule)
        val advanced = com.pubnub.docs.configuration.samples.createAdvancedConfig()
        assertEquals(10, advanced.connectTimeout)
        assertEquals(310, advanced.subscribeTimeout)
        assertEquals(10, advanced.nonSubscribeReadTimeout)
        assertTrue(advanced.secure)
        assertEquals(300, advanced.presenceTimeout)
        assertEquals(150, advanced.heartbeatInterval)
        assertFalse(advanced.suppressLeaveEvents)
        assertEquals(1, advanced.customLoggers?.size)
        assertEquals(PNHeartbeatNotificationOptions.ALL, advanced.heartbeatNotificationOptions)
        val retry = advanced.retryConfiguration as RetryConfiguration.Linear
        assertEquals("3s", retry.delayInSec.toString())
        assertEquals(5, retry.maxRetryNumber)
        val encrypted = com.pubnub.docs.configuration.samples.createEncryptedConfig()
        assertNotNull(encrypted.cryptoModule)
    }

    @Test
    fun configurationMethod(clients: LiveClients) {
        // snippet.configurationMethod
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            // config options
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        assertEquals("abc", pubnub.configuration.userId.value)
        assertEquals("subscribeKey", pubnub.configuration.subscribeKey)
        // the documented defaults of the table
        assertTrue(pubnub.configuration.secure)
        assertEquals(5, pubnub.configuration.connectTimeout)
        assertEquals(310, pubnub.configuration.subscribeTimeout)
        assertEquals(10, pubnub.configuration.nonSubscribeReadTimeout)
        assertEquals(300, pubnub.configuration.presenceTimeout)
        assertEquals(0, pubnub.configuration.heartbeatInterval)
        assertEquals(PNHeartbeatNotificationOptions.FAILURES, pubnub.configuration.heartbeatNotificationOptions)
        assertFalse(pubnub.configuration.suppressLeaveEvents)
        assertNull(pubnub.configuration.cryptoModule)
    }

    @Test
    fun valueOverride(clients: LiveClients, recorder: Recorder) {
        val client = sender(clients)
        val pubNub = recorder.wrap(client)
        Cleanup.record("history", "myChannel")
        // control: without the override the client publishes
        val control = Retry.retryTransient(label = "publish without override") { client.publish("myChannel", "control").sync() }
        assertTimetoken(control.timetoken)

        // the override carries a publish key that the service does not know, so only the override can make the call fail
        val error = assertThrows(PubNubException::class.java) { valueOverrideSample(pubNub) }

        val recorded = recorder.awaitFailure("publish")
        assertTrue(recorded === error || recorded.message == error.message, "the recorder saw another failure")
        assertEquals(400, error.statusCode)
        assertTrue(error.errorMessage.orEmpty().contains("Invalid Key") || error.message.orEmpty().contains("Invalid Key"), "the service did not name the key")
        // the client itself is unchanged: it still publishes with its own key
        assertTimetoken(Retry.retryTransient(label = "publish after override") { client.publish("myChannel", "after").sync() }.timetoken)
    }

    private fun valueOverrideSample(pubNub: PubNub) {
        // snippet.valueOverride
        // publish
        pubNub.publish("myChannel", "mymessage").overrideConfiguration {
            publishKey = "overridePublishKey"
        }.sync()
        // snippet.end
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Initialization
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun initializePubNub(clients: LiveClients) {
        val receiver = sender(clients)
        val message = probe("initialize")
        val arrived = armMessage(receiver, "myChannel", message)
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.initializePubNub
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        assertEquals("abc", pubnub.configuration.userId.value)
        // the client built by the sample publishes: it has a publish key and the subscribe key of the keyset
        val published = pubnub.publish("myChannel", message).sync()
        val received = arrived.await() as PNMessageResult
        assertEquals("abc", received.publisher)
        assertEquals(published.timetoken, received.timetoken)
        assertTimetoken(received.timetoken)
    }

    @Test
    fun initializationReadOnlyClient(clients: LiveClients) {
        Cleanup.record("history", "myChannel")

        // snippet.initializationReadOnlyClient
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey")
        // snippet.hide
        builder.subscribeKey = com.pubnub.docs.support.Env.subscribeKey("open")
        // snippet.show
        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        // no publish key: the client reads, it cannot publish
        assertEquals("", pubnub.configuration.publishKey)
        val error = assertThrows(PubNubException::class.java) { pubnub.publish("myChannel", "read only").sync() }
        assertEquals(PubNubError.PUBLISH_KEY_MISSING.message, error.errorMessage ?: error.pubnubError?.message)
        // reading works with the subscribe key alone
        assertTimetoken(pubnub.time().sync().timetoken)
        assertNotNull(pubnub.hereNow(channels = listOf("myChannel")).sync())
    }

    @Test
    fun initializeNonSecureClient(clients: LiveClients) {
        val receiver = sender(clients)
        val message = probe("non-secure")
        val arrived = armMessage(receiver, "myChannel", message)
        subscribeAndConnect(receiver, listOf("myChannel"))
        Cleanup.record("history", "myChannel")

        // snippet.initializeNonSecureClient
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            secure = false
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        assertFalse(pubnub.configuration.secure)
        // the service takes a publish over plain HTTP, a secure subscriber gets it
        val published = pubnub.publish("myChannel", message).sync()
        val received = arrived.await() as PNMessageResult
        assertEquals(published.timetoken, received.timetoken)
        assertEquals("abc", received.publisher)
        storedItem(receiver, "myChannel", published.timetoken)
    }

    @Test
    fun initializeWithAccessManager(clients: LiveClients) {
        // snippet.initializeWithAccessManager
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            secretKey = "SecretKey"
            secure = true
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "pam")
            // snippet.show
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        assertTrue(pubnub.configuration.secure)
        assertTrue(pubnub.configuration.secretKey.isNotEmpty(), "the client has no secret key")
        // the secret key signs the Access Manager calls, a client without it cannot grant
        val granted = pubnub.grantToken(
            ttl = 1,
            authorizedUUID = "docs-config-receiver",
            channels = listOf(ChannelGrant.name("docs-config-pam", read = true)),
        ).sync()
        Redact.register(granted.token)
        assertTrue(granted.token.isNotEmpty(), "the grant returned no token")
    }

    @Test
    fun initializeWithProxy(clients: LiveClients) {
        // control: the service is reachable without the proxy
        assertTimetoken(sender(clients).time().sync().timetoken)

        // snippet.initializeWithProxy
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            secretKey = "SecretKey"
            proxy = Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress("http://mydomain.com", 8080)
            )
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        assertEquals(Proxy.Type.HTTP, pubnub.configuration.proxy?.type())
        assertEquals(8080, (pubnub.configuration.proxy?.address() as InetSocketAddress).port)
        // every request of the client goes through the proxy, the address of the sample does not exist
        assertThrows(PubNubException::class.java) { pubnub.time().sync() }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // UserId
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun configurationSetUserId(clients: LiveClients) {
        // snippet.configurationSetUserId
        // Setting the userId during initialization
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey")
        val pubnub = PubNub.create(builder.build())

        // Setting the userId after initialization
        val pnConfigurationWithModifiedUserId: PNConfiguration = PNConfigurationOverride.from(pubnub.configuration).apply {
            userId = UserId("new-user-id")
        }.build()
        // snippet.end
        clients.track(pubnub)

        assertEquals("new-user-id", pnConfigurationWithModifiedUserId.userId.value)
        // the configuration of the client is immutable, only the copy has the new user id
        assertEquals("abc", pubnub.configuration.userId.value)
        assertEquals("subscribeKey", pnConfigurationWithModifiedUserId.subscribeKey)
    }

    @Test
    fun configurationGetUserId(clients: LiveClients) {
        // snippet.configurationGetUserId
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey")
        val pubnub = PubNub.create(builder.build())

        val userId: UserId = pubnub.configuration.userId
        // snippet.end
        clients.track(pubnub)

        assertEquals("abc", userId.value)
        assertEquals(UserId("abc"), userId)
    }

    @Test
    fun userIdMustNotBeBlank() {
        // The docs say a client without a userId cannot connect. The SDK enforces it when the UserId is created.
        val empty = assertThrows(PubNubException::class.java) { UserId("") }
        assertEquals(PubNubError.USERID_NULL_OR_EMPTY, empty.pubnubError)
        assertEquals("UserId can't have empty value", empty.errorMessage)
        val blank = assertThrows(PubNubException::class.java) { UserId("   ") }
        assertEquals(PubNubError.USERID_NULL_OR_EMPTY, blank.pubnubError)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Filter expression
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun configurationSetFileterExpression(clients: LiveClients) {
        val publisher = sender(clients)
        Cleanup.record("history", "docs-config-filter")

        // snippet.configurationSetFileterExpression
        val builder = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            publishKey = "publishKey"
            filterExpression = "such=wow"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }
        val pubnub = PubNub.create(builder.build())
        // snippet.end
        clients.track(pubnub)

        assertEquals("such=wow", pubnub.configuration.filterExpression)
        val wanted = probe("filter-wanted")
        val unwanted = probe("filter-unwanted")
        // control: the filter language of the service needs a comparison operator and a quoted value
        val control = clients.kotlinClient(OPEN, "docs-config-receiver") { filterExpression = "such=='wow'" }
        val controlWanted = armMessage(control, "docs-config-filter", wanted)
        val controlUnwanted = armMessage(control, "docs-config-filter", unwanted)
        subscribeAndConnect(control, listOf("docs-config-filter"))
        val arrivedWanted = armMessage(pubnub, "docs-config-filter", wanted)
        val arrivedUnwanted = armMessage(pubnub, "docs-config-filter", unwanted)
        subscribeAndConnect(pubnub, listOf("docs-config-filter"))
        publisher.publish("docs-config-filter", unwanted, meta = mapOf("such" to "sad")).sync()
        publisher.publish("docs-config-filter", wanted, meta = mapOf("such" to "wow")).sync()
        val received = controlWanted.await() as PNMessageResult
        assertEquals("""{"such":"wow"}""", received.userMetadata.toString())
        assertNothingArrives(controlUnwanted, "a message that the filter expression excludes arrived")
        // The expression of the docs sample ("such=wow") is not valid filter syntax: the service takes the subscription and
        // then delivers nothing to it, not even the message that matches (docs finding).
        assertNothingArrives(arrivedWanted, "the filter expression of the sample delivered a message", 1000)
        assertNothingArrives(arrivedUnwanted, "the filter expression of the sample delivered a message", 1000)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Encryption on the configuration
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun configurationWithCryptoModule(clients: LiveClients) {
        val plainReader = sender(clients)
        val message = probe("crypto")
        val seenByPlainReader = armMessage(plainReader, "docs-config-crypto", "")
        subscribeAndConnect(plainReader, listOf("docs-config-crypto"))
        val sameKeyReader = clients.kotlinClient(OPEN, "docs-config-receiver") {
            cryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ")
        }
        val seenBySameKey = armMessage(sameKeyReader, "docs-config-crypto", message)
        subscribeAndConnect(sameKeyReader, listOf("docs-config-crypto"))
        Cleanup.record("history", "docs-config-crypto")

        // snippet.configurationWithCryptoModule
        val builderWithAesCbcCryptoModule = PNConfiguration.builder(UserId("abc"), "subscribeKey").apply {
            // encrypts using 256-bit AES-CBC cipher (recommended)
            // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
            // use a high-entropy, randomly generated cipher key rather than a dictionary word
            cryptoModule = CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ")
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
        }

        // OR

        val builderWithLegacyCryptoModule = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            // encrypts with 128-bit cipher key entropy (legacy)
            // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
            cryptoModule = CryptoModule.createLegacyCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ")
        }
        val pubnub = PubNub.create(builderWithAesCbcCryptoModule.build())
        // snippet.end
        clients.track(pubnub)

        assertNotNull(pubnub.configuration.cryptoModule)
        assertNotNull(builderWithLegacyCryptoModule.build().cryptoModule)
        val published = pubnub.publish("docs-config-crypto", message).sync()
        // a client with the same cipher key reads the text
        val readable = seenBySameKey.await() as PNMessageResult
        assertEquals(message, readable.message.asString)
        assertEquals(published.timetoken, readable.timetoken)
        // a client without a crypto module gets the cipher text, not the message
        val raw = seenByPlainReader.await() as PNMessageResult
        assertEquals(published.timetoken, raw.timetoken)
        assertNotEquals(message, raw.message.toString().trim('"'))
        assertFalse(raw.message.toString().contains(message), "the message crossed the service in clear text")
    }
}
