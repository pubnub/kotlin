package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.v2.PNConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.concurrent.Callable

/**
 * Helpers of the access-manager group (Access Manager v3 tokens, the v2 grant and the DataSync grants). Nothing here is part
 * of a sample. Tokens and secret keys never appear in a message: every token is registered with [Redact], and the helpers
 * that print or assert never put one in text.
 *
 * Roles: [PAM] holds the secret key of the Access Manager keyset, [DATASYNC] the secret key of the DataSync keyset. A
 * client made by [plainClient], [tokenClient] or [authKeyClient] has NO secret key, so the keyset enforces its permissions.
 */
object AccessManagerKit {
    const val PAM = "pam"
    const val DATASYNC = "datasync"

    /** A channel that no grant of the tests touches, for the negative checks. */
    const val UNTOUCHED = "docs-am-untouched"

    /** A DataSync entity that no grant of the tests touches. */
    const val UNTOUCHED_PRODUCT = "docs-am-untouched-product"

    /** The documented refusal of Access Manager. */
    const val FORBIDDEN = 403

    /** The time the docs allow a revoke to take (they say up to a minute), with room for a slow node. */
    const val REVOKE_TIMEOUT_MS = 120_000L

    /**
     * The hidden line of a "Main" sample for a client that must NOT carry the secret key: sets the subscribe and publish key of
     * [role] only, so the keyset enforces the token of that client.
     */
    @JvmStatic
    fun applyClientKeys(
        builder: PNConfiguration.Builder,
        role: String,
    ) {
        builder.subscribeKey = Env.subscribeKey(role)
        builder.publishKey = Env.publishKey(role)
    }

    /** A client of [role] for [user] without the secret key and without a token. */
    @JvmStatic
    fun plainClient(
        clients: LiveClients,
        role: String,
        user: String,
    ): PubNub = clients.kotlinClient(role, user) { secretKey = "" }

    /** A client of [role] for [user] without the secret key that holds [token]. */
    @JvmStatic
    fun tokenClient(
        clients: LiveClients,
        role: String,
        user: String,
        token: String,
    ): PubNub {
        Redact.register(token)
        return plainClient(clients, role, user).also { it.setToken(token) }
    }

    /** A client of [role] for [user] without the secret key that authenticates with the Access Manager v2 [authKey]. */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun authKeyClient(
        clients: LiveClients,
        role: String,
        user: String,
        authKey: String,
    ): PubNub =
        clients.kotlinClient(role, user) {
            secretKey = ""
            this.authKey = authKey
        }

    /** Grants a token for [user] with read and write on [channel] through [server] (a client with the secret key). */
    @JvmStatic
    fun grantRoom(
        server: PubNub,
        user: String,
        channel: String,
    ): String =
        Retry.retryTransient(label = "grant a token for $user") {
            server.grantToken(
                ttl = 15,
                authorizedUUID = user,
                channels = listOf(ChannelGrant.name(channel, read = true, write = true)),
            ).sync().token
        }.also { Redact.register(it) }

    /** The names of the permissions that are set in [permissions] (empty for null), so a test compares exact sets. */
    @JvmStatic
    fun granted(permissions: PNToken.PNResourcePermissions?): Set<String> {
        if (permissions == null) {
            return emptySet()
        }
        return listOf(
            "read" to permissions.read,
            "write" to permissions.write,
            "manage" to permissions.manage,
            "delete" to permissions.delete,
            "get" to permissions.get,
            "update" to permissions.update,
            "join" to permissions.join,
            "create" to permissions.create,
        ).filter { it.second }.map { it.first }.toSet()
    }

    // ---- assertions ---------------------------------------------------------------------------------------------

    /** [client] can publish to [channel] (nothing is stored). */
    @JvmStatic
    fun assertPublishAllowed(
        client: PubNub,
        channel: String,
    ) {
        val result =
            Retry.retryTransient(label = "publish to $channel") {
                client.publish(channel, "docs-am-check", shouldStore = false).sync()
            }
        Kit.assertTimetoken(result.timetoken)
    }

    /** [client] is refused (403) when it publishes to [channel]. */
    @JvmStatic
    fun assertPublishRefused(
        client: PubNub,
        channel: String,
    ) = assertRefused("publish to $channel") { client.publish(channel, "docs-am-check", shouldStore = false).sync() }

    /** [client] can read the history of [channel]. */
    @JvmStatic
    fun assertHistoryAllowed(
        client: PubNub,
        channel: String,
    ) {
        Retry.retryTransient(label = "history of $channel") { client.fetchMessages(listOf(channel)).sync() }
    }

    /** [client] is refused (403) when it reads the history of [channel]. */
    @JvmStatic
    fun assertHistoryRefused(
        client: PubNub,
        channel: String,
    ) = assertRefused("history of $channel") { client.fetchMessages(listOf(channel)).sync() }

    /** [client] can list the metadata of all channels (the `getAllChannels` permission). */
    @JvmStatic
    fun assertListChannelsAllowed(client: PubNub) {
        Retry.retryTransient(label = "list all channel metadata") { client.getAllChannelMetadata(limit = 1).sync() }
    }

    /** [client] can list the metadata of all users (the `getAllUUIDs` permission). */
    @JvmStatic
    fun assertListUuidsAllowed(client: PubNub) {
        Retry.retryTransient(label = "list all uuid metadata") { client.getAllUUIDMetadata(limit = 1).sync() }
    }

    /** [client] is refused (403) when it lists the metadata of all channels. */
    @JvmStatic
    fun assertListChannelsRefused(client: PubNub) =
        assertRefused("list all channel metadata") {
            client.getAllChannelMetadata(limit = 1).sync()
        }

    /** [client] is refused (403) when it lists the metadata of all users. */
    @JvmStatic
    fun assertListUuidsRefused(client: PubNub) =
        assertRefused("list all uuid metadata") {
            client.getAllUUIDMetadata(limit = 1).sync()
        }

    /**
     * [client] is refused (403) when it lists the metadata of all channels and of all users. The keyset disallows both
     * listings without permission. Retried for [timeoutMs] in case the setting has not reached every node.
     */
    @JvmStatic
    @JvmOverloads
    fun assertListAllRefused(
        client: PubNub,
        timeoutMs: Long = 30_000L,
    ) {
        assertRefusedEventually("list all channel metadata", timeoutMs) { client.getAllChannelMetadata(limit = 1).sync() }
        assertRefusedEventually("list all uuid metadata", timeoutMs) { client.getAllUUIDMetadata(limit = 1).sync() }
    }

    /** [client] is refused (403) when it lists the metadata of all users, within [timeoutMs]. */
    @JvmStatic
    @JvmOverloads
    fun assertListUuidsRefusedEventually(
        client: PubNub,
        timeoutMs: Long = 30_000L,
    ) = assertRefusedEventually("list all uuid metadata", timeoutMs) { client.getAllUUIDMetadata(limit = 1).sync() }

    /** Waits until an Access Manager v2 grant has reached the nodes:[client] lists the metadata of all channels and users. */
    @JvmStatic
    fun awaitListAllowed(client: PubNub) {
        Wait.eventually("the auth key lists the channel and the uuid metadata") {
            try {
                client.getAllChannelMetadata(limit = 1).sync()
                client.getAllUUIDMetadata(limit = 1).sync()
            } catch (error: PubNubException) {
                throw AssertionError("the auth key is not allowed yet, status ${error.statusCode}")
            }
        }
    }

    /** Closes the Access Manager v2 grant of [authKey] (every permission off, ttl 1). A downgrade can take a while to apply. */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun closeAuthKeyGrant(
        server: PubNub,
        authKey: String,
    ) {
        server.grant(
            read = false,
            write = false,
            manage = false,
            delete = false,
            get = false,
            update = false,
            join = false,
            ttl = 1,
            authKeys = listOf(authKey),
            getAllChannels = false,
            getAllUUIDs = false,
        ).sync()
    }

    /** [call] fails with HTTP 403, the documented answer of Access Manager. Any other outcome fails the test. */
    @JvmStatic
    fun assertRefused(
        what: String,
        call: Callable<*>,
    ) {
        try {
            call.call()
        } catch (error: PubNubException) {
            assertEquals(FORBIDDEN, error.statusCode, "$what must be refused with 403, got ${error.statusCode}")
            return
        }
        throw AssertionError("$what was allowed, it must be refused with 403")
    }

    /**
     * [call] is refused (403) within [timeoutMs]. For a revoke: the service caches the not-revoked state, so a fresh
     * check can still be allowed for a while. Never use it for a sample call.
     */
    @JvmStatic
    @JvmOverloads
    fun assertRefusedEventually(
        what: String,
        timeoutMs: Long = REVOKE_TIMEOUT_MS,
        call: Callable<*>,
    ) {
        Wait.eventually(what, timeoutMs = timeoutMs, intervalMs = 2_000) { assertRefused(what, call) }
    }

    /** [client] is refused (403) when it publishes to [channel], within [timeoutMs]. For the clients of a revoked token. */
    @JvmStatic
    @JvmOverloads
    fun assertPublishRefusedEventually(
        client: PubNub,
        channel: String,
        timeoutMs: Long = REVOKE_TIMEOUT_MS,
    ) = assertRefusedEventually("publish to $channel with a revoked token", timeoutMs) {
        client.publish(channel, "docs-am-check", shouldStore = false).sync()
    }

    /** For a Java client: removes the secret key from the Java builder (`PNConfiguration.Builder`, typed [Any] here). */
    @JvmStatic
    fun withoutSecret(builder: Any) {
        Class.forName("com.pubnub.api.java.v2.PNConfiguration\$Builder").getMethod("secretKey", String::class.java).invoke(builder, "")
    }

    // ---- DataSync -----------------------------------------------------------------------------------------------

    /** Creates the DataSync entity [id] of the class `product` (removed after the test). [server] holds the secret key. */
    @JvmStatic
    fun createProduct(
        server: PubNub,
        id: String,
    ) {
        try {
            server.dataSync.removeEntity(id).sync()
        } catch (error: PubNubException) {
            assertEquals(404, error.statusCode, "an old entity could not be removed")
        }
        Retry.retryTransient(label = "create product $id") {
            server.dataSync.createEntity(
                className = "product",
                classVersion = 1,
                entityId = id,
                status = "active",
                payload = mapOf("name" to "Sneaker", "price" to 99),
            ).sync()
        }
        Cleanup.cleanupAfterTest("remove product $id") { server.dataSync.removeEntity(id).sync() }
    }

    /** [client] can read the DataSync entity [id]. */
    @JvmStatic
    fun assertEntityAllowed(
        client: PubNub,
        id: String,
    ) {
        val entity = Retry.retryTransient(label = "get entity $id") { client.dataSync.getEntity(id).sync() }
        assertEquals(id, entity.data.id)
    }

    /** [client] is refused (403) when it reads the DataSync entity [id]. */
    @JvmStatic
    fun assertEntityRefused(
        client: PubNub,
        id: String,
    ) = assertRefused("get entity $id") { client.dataSync.getEntity(id).sync() }

    // ---- what a sample prints -----------------------------------------------------------------------------------

    /**
     * Captures what a sample prints, like [Stdout], but the text that reaches the console and the test report is redacted
     * line by line, so a token that a sample prints never lands in a log. The raw text stays in memory for the assertions.
     * Start it with [captureForTest].
     */
    class ScrubbedStdout private constructor(private val original: PrintStream) : AutoCloseable {
        private val raw = ByteArrayOutputStream()
        private val line = ByteArrayOutputStream()
        private var closed = false

        private val sink =
            object : OutputStream() {
                override fun write(b: Int) {
                    synchronized(raw) {
                        raw.write(b)
                        if (b == '\n'.code) {
                            emit()
                        } else {
                            line.write(b)
                        }
                    }
                }
            }

        init {
            System.setOut(PrintStream(sink, true, "UTF-8"))
        }

        // The caller holds the lock of raw.
        private fun emit() {
            original.println(Redact.scrub(line.toString("UTF-8")))
            line.reset()
        }

        /** Everything printed since the capture started. Never print it without [Redact.scrub]. */
        fun text(): String = synchronized(raw) { raw.toString("UTF-8") }

        /** The printed lines that contain [part]. */
        fun lines(part: String): List<String> = text().lines().filter { it.contains(part) }

        /** Fails (with the redacted output) unless a line with [part] was printed. */
        fun assertPrinted(part: String) {
            if (lines(part).isEmpty()) {
                throw AssertionError(Redact.scrub("nothing printed that contains '$part'. Printed so far: ${text().take(1500)}"))
            }
        }

        /** Fails (with the redacted output) when a line with [part] was printed. */
        fun assertNotPrinted(part: String) {
            if (lines(part).isNotEmpty()) {
                throw AssertionError(Redact.scrub("a line with '$part' was printed: ${text().take(1500)}"))
            }
        }

        /**
         * The token a sample printed after [prefix] (for example `Token: `). Waits for the print, because a sample answers
         * from a callback. Registers the token with [Redact].
         */
        fun printedToken(prefix: String = "Token: "): String {
            val token =
                Wait.eventually("the sample printed a line that starts with '$prefix'") {
                    val found = lines(prefix).firstOrNull { it.trimStart().startsWith(prefix.trim()) }
                    val printed = Redact.scrub(text().take(800))
                    assertTrue(found != null, "no line that starts with '$prefix' was printed. Printed so far: $printed")
                    found!!.substringAfter(prefix).trim()
                }
            assertTrue(token.length > 20, "the printed token is too short to be a token")
            Redact.register(token)
            return token
        }

        @Synchronized
        override fun close() {
            if (!closed) {
                closed = true
                synchronized(raw) {
                    if (line.size() > 0) {
                        emit()
                    }
                }
                System.out.flush()
                System.setOut(original)
            }
        }

        companion object {
            /** Starts a capture and registers its end with the cleanups of the test. */
            @JvmStatic
            fun captureForTest(): ScrubbedStdout =
                ScrubbedStdout(System.out).also { stream -> Cleanup.cleanupAfterTest("restore System.out") { stream.close() } }
        }
    }
}
