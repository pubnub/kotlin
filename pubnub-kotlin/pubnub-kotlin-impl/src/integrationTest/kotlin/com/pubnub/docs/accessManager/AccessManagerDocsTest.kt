@file:Suppress("ktlint", "DEPRECATION")

package com.pubnub.docs.accessManager

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.docs.support.AccessManagerKit
import com.pubnub.docs.support.AccessManagerKit.DATASYNC
import com.pubnub.docs.support.AccessManagerKit.PAM
import com.pubnub.docs.support.AccessManagerKit.REVOKE_TIMEOUT_MS
import com.pubnub.docs.support.AccessManagerKit.ScrubbedStdout
import com.pubnub.docs.support.AccessManagerKit.UNTOUCHED
import com.pubnub.docs.support.AccessManagerKit.UNTOUCHED_PRODUCT
import com.pubnub.docs.support.AccessManagerKit.assertEntityAllowed
import com.pubnub.docs.support.AccessManagerKit.assertEntityRefused
import com.pubnub.docs.support.AccessManagerKit.assertHistoryAllowed
import com.pubnub.docs.support.AccessManagerKit.assertHistoryRefused
import com.pubnub.docs.support.AccessManagerKit.assertPublishAllowed
import com.pubnub.docs.support.AccessManagerKit.assertPublishRefused
import com.pubnub.docs.support.AccessManagerKit.assertListChannelsAllowed
import com.pubnub.docs.support.AccessManagerKit.assertListChannelsRefused
import com.pubnub.docs.support.AccessManagerKit.assertListUuidsAllowed
import com.pubnub.docs.support.AccessManagerKit.assertListUuidsRefused
import com.pubnub.docs.support.AccessManagerKit.assertPublishRefusedEventually
import com.pubnub.docs.support.AccessManagerKit.authKeyClient
import com.pubnub.docs.support.AccessManagerKit.closeAuthKeyGrant
import com.pubnub.docs.support.AccessManagerKit.createProduct
import com.pubnub.docs.support.AccessManagerKit.granted
import com.pubnub.docs.support.AccessManagerKit.grantRoom
import com.pubnub.docs.support.AccessManagerKit.plainClient
import com.pubnub.docs.support.AccessManagerKit.tokenClient
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Redact
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs pages access-manager and access-manager-v2 (docs/sdks/kotlin/api-reference/access-manager.md
 * and access-manager-v2.md). One test per snippet. The text between a start marker and `snippet.end` is the published
 * sample, the arrangement is above it and the assertions below it.
 *
 * Grants need the secret key, so the samples run on server clients (`clients.server`). The proof that a token works uses
 * clients WITHOUT the secret key (`AccessManagerKit.tokenClient`): allowed on the granted resource, refused (403) on a fresh
 * name that no grant touches and without the token. Tokens are registered with `Redact` and never appear in a message.
 * The v2 grant is closed by a cleanup that is registered before the sample runs, so a failing test closes it too.
 */
@ExtendWith(LiveExtension::class)
class AccessManagerDocsTest {
    // The token that the parseToken and setToken samples show. It is not a live token: the setToken test swaps it for one.
    private val sampleToken =
        "qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo="

    // The token that the parseTokenDataSync sample shows.
    private val sampleDataSyncToken =
        "qGF2AmF0GmqXSuBjdHRsD2R1dWlkanVzZXItYWxpY2VjcmVzpWRjaGFuoXNjaGFubmVsLXN1bW1lci1zYWxlAWNncnCgZHV1aWSgY3VzcqFqdXNlci1hbGljZRhgcWRhdGFzeW5jOmVudGl0aWVzoXJwcm9kdWN0LXNuZWFrZXItNDIYcGNwYXSjZGNoYW6gY2dycKBkdXVpZKBkbWV0YaFucG4tcHJvamVjdGlvbnOhY3Jlc6F4JGRhdGFzeW5jOmVudGl0aWVzOnByb2R1Y3Qtc25lYWtlci00MmVhZG1pbmNzaWdYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    // ---------------------------------------------------------------------------------------------------------------
    // Access Manager v2
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun grantWithGetAllMetadataAccess(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val pubnub = recorder.wrap(server)
        val lister = authKeyClient(clients, PAM, "docs-am-lister", "my-auth-key")
        // The close of the grant runs after the test, also when an assertion below fails.
        Cleanup.cleanupAfterTest("close the grant of my-auth-key") { closeAuthKeyGrant(server, "my-auth-key") }

        // snippet.grantWithGetAllMetadataAccess
        // Allow the auth keys to list all channel and uuid metadata on the keyset.
        // The `get` permission is sent automatically, so no other permission can be set.
        pubnub.grant(
            authKeys = listOf("my-auth-key"),
            getAllChannels = true,
            getAllUUIDs = true
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val result = recorder.awaitOk("grant") as PNAccessManagerGrantResult
        assertEquals(setOf("channels", "uuids"), result.categories.keys)
        assertTrue(result.categories["channels"]!!["my-auth-key"]!!.getEnabled, "the auth key may list all channel metadata")
        assertTrue(result.categories["uuids"]!!["my-auth-key"]!!.getEnabled, "the auth key may list all uuid metadata")
        // The keyset disallows listing all metadata for requests without a signature: the secret key client may list,
        // a client with the auth key but without the secret key and a client without both are refused with 403.
        assertListChannelsAllowed(server)
        assertListUuidsAllowed(server)
        AccessManagerKit.assertListAllRefused(lister)
        AccessManagerKit.assertListAllRefused(plainClient(clients, PAM, "docs-am-lister"))
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Grant token
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun grantTokenMain(clients: LiveClients) {
        val server = clients.server(PAM)
        val observer = clients.server(PAM, "docs-observer")
        val delivered = armMessage(observer, "token-demo-channel", "Hello from a token-authorized client!")
        subscribeAndConnect(observer, listOf("token-demo-channel"))
        val out = ScrubbedStdout.captureForTest()

        com.pubnub.docs.accessManager.samples.main()

        // Step 1: the sample asked for a token and printed it.
        out.assertNotPrinted("Failed to generate token")
        out.assertPrinted("Token generated successfully")
        val token = out.printedToken()
        val parsed = server.parseToken(token)
        assertEquals("client-user", parsed.authorizedUUID)
        assertEquals(15L, parsed.ttl)
        assertEquals(setOf("read", "write"), granted(parsed.resources.channels["token-demo-channel"]))
        assertEquals(setOf("read"), granted(parsed.patterns.channels["^readonly-.*$"]))
        // Steps 2 to 4: the client with the token connected, published and received its own message.
        // Docs finding: the sample subscribes with receivePresenceEvents(), but the token grants no read on
        // token-demo-channel-pnpres, so the subscribe is refused and the client never connects or receives its message.
        out.assertPrinted("Error status: PNConnectionError")
        out.assertNotPrinted("Connected/Reconnected")
        out.assertNotPrinted("Error while publishing")
        out.assertPrinted("Message sent successfully, timetoken: ")
        out.assertNotPrinted("Received message: ")
        val message = delivered.await() as PNMessageResult
        assertEquals("client-user", message.publisher)
        assertEquals("Hello from a token-authorized client!", message.message.asJsonObject["content"].asString)
        // Step 5: the channel that the token does not name is refused, and nothing was stored there.
        out.assertPrinted("Expected error (we don't have access)")
        out.assertNotPrinted("This shouldn't succeed")
        out.assertPrinted("Example completed")
        Kit.assertNoStoredText(server, "restricted-channel", "Hello from a token-authorized client!")
    }

    @Test
    fun grantTokenDifferentAccessLevels(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val pubnub = recorder.wrap(server)

        // snippet.grantTokenDifferentAccessLevels
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.name(name = "channel-a", read = true),
                ChannelGrant.name(name = "channel-b", read = true, write = true),
                ChannelGrant.name(name = "channel-c", read = true, write = true),
                ChannelGrant.name(name = "channel-d", read = true, write = true)
            ),
            channelGroups = listOf(
                ChannelGroupGrant.id(id = "channel-group-b", read = true)
            ),
            uuids = listOf(
                UUIDGrant.id(id = "uuid-c", get = true),
                UUIDGrant.id(id = "uuid-d", get = true, update = true)
            )
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val token = (recorder.awaitOk("grantToken") as PNGrantTokenResult).token
        Redact.register(token)
        val parsed = server.parseToken(token)
        assertEquals("my-authorized-uuid", parsed.authorizedUUID)
        assertEquals(15L, parsed.ttl)
        assertEquals(setOf("read"), granted(parsed.resources.channels["channel-a"]))
        for (name in listOf("channel-b", "channel-c", "channel-d")) {
            assertEquals(setOf("read", "write"), granted(parsed.resources.channels[name]), "permissions of $name")
        }
        assertEquals(setOf("read"), granted(parsed.resources.channelGroups["channel-group-b"]))
        assertEquals(setOf("get"), granted(parsed.resources.uuids["uuid-c"]))
        assertEquals(setOf("get", "update"), granted(parsed.resources.uuids["uuid-d"]))
        assertTrue(parsed.patterns.channels.isEmpty(), "the token has no channel pattern")

        val holder = tokenClient(clients, PAM, "my-authorized-uuid", token)
        assertPublishAllowed(holder, "channel-b")
        assertHistoryAllowed(holder, "channel-a")
        assertPublishRefused(holder, "channel-a")
        assertPublishRefused(holder, UNTOUCHED)
        assertPublishRefused(plainClient(clients, PAM, "my-authorized-uuid"), "channel-b")
    }

    @Test
    fun grantTokenWithPatternBasedChannelAccess(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val pubnub = recorder.wrap(server)

        // snippet.grantTokenWithPatternBasedChannelAccess
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.pattern(pattern = "^channel-[A-Za-z0-9]*$", read = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val token = (recorder.awaitOk("grantToken") as PNGrantTokenResult).token
        Redact.register(token)
        val parsed = server.parseToken(token)
        assertEquals("my-authorized-uuid", parsed.authorizedUUID)
        assertEquals(setOf("read"), granted(parsed.patterns.channels["^channel-[A-Za-z0-9]*$"]))
        assertTrue(parsed.resources.channels.isEmpty(), "the token names no single channel")

        val holder = tokenClient(clients, PAM, "my-authorized-uuid", token)
        assertHistoryAllowed(holder, "channel-a")
        assertHistoryAllowed(holder, "channel-e")
        assertPublishRefused(holder, "channel-a")
        assertHistoryRefused(holder, UNTOUCHED)
        assertHistoryRefused(plainClient(clients, PAM, "my-authorized-uuid"), "channel-a")
    }

    @Test
    fun grantTokenWithPatternBasedChannelGroupAccess(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val pubnub = recorder.wrap(server)

        // snippet.grantTokenWithPatternBasedChannelGroupAccess
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.name(name = "channel-a", read = true),
                ChannelGrant.name(name = "channel-b", read = true, write = true),
                ChannelGrant.name(name = "channel-c", read = true, write = true),
                ChannelGrant.name(name = "channel-d", read = true, write = true),
                ChannelGrant.pattern(pattern = "^channel-[A-Za-z0-9]*$", read = true)
            ),
            channelGroups = listOf(
                ChannelGroupGrant.id(id = "channel-group-b", read = true)
            ),
            uuids = listOf(
                UUIDGrant.id(id = "uuid-c", get = true),
                UUIDGrant.id(id = "uuid-d", get = true, update = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val token = (recorder.awaitOk("grantToken") as PNGrantTokenResult).token
        Redact.register(token)
        val parsed = server.parseToken(token)
        assertEquals("my-authorized-uuid", parsed.authorizedUUID)
        assertEquals(setOf("read"), granted(parsed.resources.channels["channel-a"]))
        assertEquals(setOf("read", "write"), granted(parsed.resources.channels["channel-b"]))
        assertEquals(setOf("read"), granted(parsed.resources.channelGroups["channel-group-b"]))
        assertEquals(setOf("get", "update"), granted(parsed.resources.uuids["uuid-d"]))
        assertEquals(setOf("read"), granted(parsed.patterns.channels["^channel-[A-Za-z0-9]*$"]))

        val holder = tokenClient(clients, PAM, "my-authorized-uuid", token)
        // A named channel with write access, and a channel that only the pattern covers (read only).
        assertPublishAllowed(holder, "channel-b")
        assertHistoryAllowed(holder, "channel-e")
        assertPublishRefused(holder, "channel-e")
        assertPublishRefused(holder, UNTOUCHED)
        assertPublishRefused(plainClient(clients, PAM, "my-authorized-uuid"), "channel-b")
    }

    @Test
    fun grantTokenWithGetAllMetadataAccess(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val pubnub = recorder.wrap(server)

        // snippet.grantTokenWithGetAllMetadataAccess
        // Allow listing all channel and uuid metadata on the keyset (getAllChannelMetadata / getAllUUIDMetadata).
        // A `get` on a named channel or uuid doesn't imply this.
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            getAllChannels = true,
            getAllUUIDs = true
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end

        val token = (recorder.awaitOk("grantToken") as PNGrantTokenResult).token
        Redact.register(token)
        val parsed = server.parseToken(token)
        assertEquals("my-authorized-uuid", parsed.authorizedUUID)
        assertTrue(parsed.getAllChannels, "the token may list all channel metadata")
        assertTrue(parsed.getAllUUIDs, "the token may list all uuid metadata")

        val holder = tokenClient(clients, PAM, "my-authorized-uuid", token)
        // The keyset disallows listing all metadata for requests without a signature: the secret key client may list,
        // the token holder without the secret key and a client without a token are refused with 403.
        assertListChannelsAllowed(server)
        assertListUuidsAllowed(server)
        AccessManagerKit.assertListAllRefused(holder)
        AccessManagerKit.assertListAllRefused(plainClient(clients, PAM, "my-authorized-uuid"))
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Grant token with DataSync (the datasync keyset)
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun grantTokenFlatList(clients: LiveClients) {
        val out = ScrubbedStdout.captureForTest()

        // snippet.grantTokenFlatList
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                ChannelGrant.name(name = "channel-summer-sale", read = true, write = true),
                ChannelGrant.pattern(pattern = "^announcements-.*$", read = true)
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val token = out.printedToken()
        out.assertNotPrinted("Failed to grant token")
        val parsed = pubnub.parseToken(token)
        assertEquals("user-alice", parsed.authorizedUUID)
        assertEquals(15L, parsed.ttl)
        assertEquals(setOf("read", "write"), granted(parsed.resources.channels["channel-summer-sale"]))
        assertEquals(setOf("read"), granted(parsed.patterns.channels["^announcements-.*$"]))

        val holder = tokenClient(clients, DATASYNC, "user-alice", token)
        assertPublishAllowed(holder, "channel-summer-sale")
        assertPublishRefused(holder, UNTOUCHED)
        assertPublishRefused(plainClient(clients, DATASYNC, "user-alice"), "channel-summer-sale")
    }

    @Test
    fun grantTokenDataSync(clients: LiveClients) {
        val admin = clients.server(DATASYNC)
        createProduct(admin, "product-sneaker-42")
        createProduct(admin, UNTOUCHED_PRODUCT)
        val out = ScrubbedStdout.captureForTest()

        // snippet.grantTokenDataSync
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                DataSyncGrant.entity(name = "product-sneaker-42", get = true, update = true),
                DataSyncGrant.relationship(name = "rel-bob-owns-sneaker-42", get = true),
                DataSyncGrant.membership(name = "membership-alice-summer-sale", get = true, update = true),
                // User and channel grants share the regular users and channels buckets of the token
                DataSyncGrant.user(name = "user-alice", get = true, update = true),
                DataSyncGrant.channel(name = "channel-summer-sale", get = true),
                // Real-time events are delivered on the resource id, so grant read access to it
                DataSyncGrant.subscribe(id = "product-sneaker-42"),
                DataSyncGrant.subscribe(id = "user-alice")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val token = out.printedToken()
        out.assertNotPrinted("Failed to grant token")
        val parsed = pubnub.parseToken(token)
        assertEquals("user-alice", parsed.authorizedUUID)
        assertEquals(setOf("get", "update"), granted(parsed.resources.datasyncEntities["product-sneaker-42"]))
        assertEquals(setOf("get"), granted(parsed.resources.datasyncRelationships["rel-bob-owns-sneaker-42"]))
        assertEquals(setOf("get", "update"), granted(parsed.resources.datasyncMemberships["membership-alice-summer-sale"]))
        assertEquals(setOf("get", "update"), granted(parsed.resources.users["user-alice"]))
        assertEquals(setOf("get"), granted(parsed.resources.channels["channel-summer-sale"]))
        // The two subscribe grants are read grants on the ref channels of the product and of the user.
        val subscribed = parsed.resources.channels.filter { it.key != "channel-summer-sale" && granted(it.value) == setOf("read") }.keys
        assertTrue(subscribed.any { it.contains("product-sneaker-42") }, "the token has a read grant on the ref channel of the product")
        assertTrue(subscribed.any { it.contains("user-alice") }, "the token has a read grant on the ref channel of the user")

        val holder = tokenClient(clients, DATASYNC, "user-alice", token)
        assertEntityAllowed(holder, "product-sneaker-42")
        assertEntityRefused(holder, UNTOUCHED_PRODUCT)
        assertEntityRefused(plainClient(clients, DATASYNC, "user-alice"), "product-sneaker-42")
    }

    @Test
    fun grantTokenDataSyncPattern(clients: LiveClients) {
        val admin = clients.server(DATASYNC)
        createProduct(admin, "product-sneaker-42")
        createProduct(admin, UNTOUCHED_PRODUCT)
        val out = ScrubbedStdout.captureForTest()

        // snippet.grantTokenDataSyncPattern
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                DataSyncGrant.entityPattern(pattern = "^product-.*$", get = true, update = true),
                DataSyncGrant.subscribePattern(pattern = "^product-.*$")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val token = out.printedToken()
        out.assertNotPrinted("Failed to grant token")
        val parsed = pubnub.parseToken(token)
        assertEquals("user-alice", parsed.authorizedUUID)
        assertEquals(setOf("get", "update"), granted(parsed.patterns.datasyncEntities["^product-.*$"]))
        assertTrue(
            parsed.patterns.channels.any { it.key.contains("product-") && granted(it.value) == setOf("read") },
            "the token has a read pattern on the ref channels of the products",
        )

        val holder = tokenClient(clients, DATASYNC, "user-alice", token)
        assertEntityAllowed(holder, "product-sneaker-42")
        assertEntityRefused(holder, UNTOUCHED_PRODUCT)
    }

    @Test
    fun grantTokenDataSyncProjection(clients: LiveClients) {
        val admin = clients.server(DATASYNC)
        createProduct(admin, "product-sneaker-42")
        val out = ScrubbedStdout.captureForTest()

        // snippet.grantTokenDataSyncProjection
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
                // snippet.hide
                com.pubnub.docs.support.LiveKeys.apply(this, "datasync")
                // snippet.show
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                DataSyncGrant.entity(name = "product-sneaker-42", get = true, projection = "admin"),
                DataSyncGrant.subscribe(id = "product-sneaker-42", projection = "admin")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
        clients.track(pubnub)

        val token = out.printedToken()
        out.assertNotPrinted("Failed to grant token")
        val parsed = pubnub.parseToken(token)
        assertEquals("user-alice", parsed.authorizedUUID)
        assertEquals(setOf("get"), granted(parsed.resources.datasyncEntities["product-sneaker-42"]))
        assertEquals("admin", parsed.projections?.resources?.entities?.get("product-sneaker-42"))

        val holder = tokenClient(clients, DATASYNC, "user-alice", token)
        assertEntityAllowed(holder, "product-sneaker-42")
        assertEntityRefused(plainClient(clients, DATASYNC, "user-alice"), "product-sneaker-42")
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Revoke token
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun revokeToken(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val token = grantRoom(server, "docs-am-revokee", "docs-am-revoke-room")
        val holder = tokenClient(clients, PAM, "docs-am-revokee", token)
        assertPublishAllowed(holder, "docs-am-revoke-room")
        recorder.placeholder("p0thisAkFl043rhDdHRsCkNyZXisRGNoYW6hanNlY3JldAFDZ3Jwsample3KgQ3NwY6BDcGF0pERjaGFuoENnctokenV", token)
        val pubnub = recorder.wrap(server)

        // snippet.revokeToken
        pubnub.revokeToken("p0thisAkFl043rhDdHRsCkNyZXisRGNoYW6hanNlY3JldAFDZ3Jwsample3KgQ3NwY6BDcGF0pERjaGFuoENnctokenV")
        // snippet.end

        // The sample only builds the endpoint. Nothing runs until `async` or `sync` is called, so the sample sends no request
        // (docs finding: the Kotlin sample does not revoke anything). The token still works.
        assertTrue(recorder.records("revokeToken").isEmpty(), "the sample sent no revoke request")
        assertPublishAllowed(holder, "docs-am-revoke-room")

        // The call the page describes: revoke through the endpoint, then the token is refused within the time the docs allow.
        server.revokeToken(token).sync()
        assertPublishRefusedEventually(holder, "docs-am-revoke-room")
        for (attempt in 1..2) {
            // Enforcement reaches the nodes unevenly, so two more fresh clients must be refused too.
            assertPublishRefusedEventually(tokenClient(clients, PAM, "docs-am-revokee", token), "docs-am-revoke-room", 60_000)
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Parse token
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun parseToken(clients: LiveClients) {
        val pubnub = clients.server(PAM)

        // snippet.parseToken
        pubnub.parseToken(
            "qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo="
        )
        // snippet.end

        // The sample drops the result, so the same token is parsed again. It decodes locally, no request is sent.
        val parsed = pubnub.parseToken(sampleToken)
        assertEquals(2, parsed.version)
        assertEquals(1747117669L, parsed.timestamp)
        assertEquals(1337L, parsed.ttl)
        assertEquals("authorizedUser", parsed.authorizedUUID)
        assertEquals(setOf("delete"), granted(parsed.resources.channels["space01"]))
        assertEquals(setOf("get"), granted(parsed.resources.uuids["user01"]))
        assertEquals(setOf("read"), granted(parsed.patterns.channels["space.*"]))
        assertEquals(setOf("get"), granted(parsed.patterns.uuids["user.*"]))
        assertTrue(parsed.resources.channelGroups.isEmpty() && parsed.patterns.channelGroups.isEmpty(), "the token has no channel groups")
    }

    @Test
    fun parseTokenDataSync(clients: LiveClients) {
        val out = ScrubbedStdout.captureForTest()

        // snippet.parseTokenDataSync
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").build()
        )

        val token = pubnub.parseToken(
            "qGF2AmF0GmqXSuBjdHRsD2R1dWlkanVzZXItYWxpY2VjcmVzpWRjaGFuoXNjaGFubmVsLXN1bW1lci1zYWxlAWNncnCgZHV1aWSgY3VzcqFqdXNlci1hbGljZRhgcWRhdGFzeW5jOmVudGl0aWVzoXJwcm9kdWN0LXNuZWFrZXItNDIYcGNwYXSjZGNoYW6gY2dycKBkdXVpZKBkbWV0YaFucG4tcHJvamVjdGlvbnOhY3Jlc6F4JGRhdGFzeW5jOmVudGl0aWVzOnByb2R1Y3Qtc25lYWtlci00MmVhZG1pbmNzaWdYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        )

        val entityPermissions = token.resources.datasyncEntities["product-sneaker-42"]
        println("Can create entities: ${entityPermissions?.create}")
        println("Can update entities: ${entityPermissions?.update}")
        println("User permissions: ${token.resources.users["user-alice"]}")
        println("Projection: ${token.projections?.resources?.entities?.get("product-sneaker-42")}")
        // snippet.end
        clients.track(pubnub)

        // The token of the sample grants create and update on the entity, get and update on the user, and the admin projection.
        out.assertPrinted("Can create entities: true")
        out.assertPrinted("Can update entities: true")
        out.assertPrinted("User permissions: PNResourcePermissions(read=false, write=false, manage=false, delete=false, get=true, update=true, join=false, create=false)")
        out.assertPrinted("Projection: admin")
        assertEquals(setOf("get", "update", "create"), granted(token.resources.datasyncEntities["product-sneaker-42"]))
        assertEquals("user-alice", token.authorizedUUID)
    }

    @Test
    fun parseTokenWithGetAllMetadataAccess(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val pubnub = recorder.wrap(server)

        // snippet.parseTokenWithGetAllMetadataAccess
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            getAllChannels = true
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                val token = pubnub.parseToken(value.token)
                val canListAllChannels = token.getAllChannels // true
                val canListAllUuids = token.getAllUUIDs // false
            }
        }
        // snippet.end

        // The values of the sample stay inside its callback, so the token is parsed again here.
        val token = (recorder.awaitOk("grantToken") as PNGrantTokenResult).token
        Redact.register(token)
        val parsed = server.parseToken(token)
        assertTrue(parsed.getAllChannels, "the token may list all channel metadata")
        assertFalse(parsed.getAllUUIDs, "the token may not list all uuid metadata")

        val holder = tokenClient(clients, PAM, "my-authorized-uuid", token)
        // The keyset disallows listing all metadata for requests without a signature, so the token holder is refused
        // on both listings and the secret key client may list.
        assertListChannelsAllowed(server)
        assertListUuidsAllowed(server)
        AccessManagerKit.assertListAllRefused(holder)
        AccessManagerKit.assertListAllRefused(plainClient(clients, PAM, "my-authorized-uuid"))
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Set token
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun setToken(clients: LiveClients, recorder: Recorder) {
        val server = clients.server(PAM)
        val token = grantRoom(server, "docs-am-alice", "docs-am-room")
        val client = plainClient(clients, PAM, "docs-am-alice")
        // Without a token the keyset refuses the client.
        assertPublishRefused(client, "docs-am-room")
        recorder.placeholder(sampleToken, token)
        val pubnub = recorder.wrap(client)

        // snippet.setToken
        pubnub.setToken(
            "qEF2AkF0Gmgi5mVDdHRsGQU5Q3Jlc6VEY2hhbqFnc3BhY2UwMQhDZ3JwoENzcGOgQ3VzcqBEdXVpZKFmdXNlcjAxGCBDcGF0pURjaGFuoWdzcGFjZS4qAUNncnCgQ3NwY6BDdXNyoER1dWlkoWZ1c2VyLioYIERtZXRhoER1dWlkbmF1dGhvcml6ZWRVc2VyQ3NpZ1ggkOSK0vQY5LFE5IHctQ6rGokqHbRH8EopbQRGAbU7Zfo="
        )
        // snippet.end

        assertTrue(client.getToken() == token, "the client holds the token the sample set")
        assertPublishAllowed(client, "docs-am-room")
        // The token names one channel only.
        assertPublishRefused(client, UNTOUCHED)
    }
}
