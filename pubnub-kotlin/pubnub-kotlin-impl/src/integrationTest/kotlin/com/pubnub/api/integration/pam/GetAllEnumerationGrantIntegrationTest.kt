package com.pubnub.api.integration.pam

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.test.CommonUtils
import com.pubnub.test.Keys
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.*

/**
 * Enumeration (`getAllChannelMetadata` / `getAllUUIDMetadata`) enforcement through the `getAllChannels` /
 * `getAllUUIDs` category grants.
 *
 * Requires the PAM keyset (`PAM_PUB_KEY` / `PAM_SUB_KEY` / `PAM_SEC_KEY`) to have
 * `pam_objects_enumeration_getall_mode = 2` (Enforce).
 */
@Suppress("DEPRECATION")
class GetAllEnumerationGrantIntegrationTest : BaseIntegrationTest() {
    private val channelId = "enum-channel-" + CommonUtils.randomChannel()
    private val channelId02 = "enum-channel02-" + CommonUtils.randomChannel()
    private val uuidId = "enum-uuid-" + CommonUtils.randomChannel()
    private val uuidId02 = "enum-uuid02-" + CommonUtils.randomChannel()
    private val channelFilter = "id == \"$channelId\""
    private val uuidFilter = "id == \"$uuidId\""
    private val tokens = mutableListOf<String>()

    override fun onBefore() {
        server.setChannelMetadata(channel = channelId, name = "enumeration test channel").sync()
        server.setChannelMetadata(channel = channelId02, name = "enumeration test channel02").sync()
        server.setUUIDMetadata(uuid = uuidId, name = "enumeration test uuid").sync()
        server.setUUIDMetadata(uuid = uuidId02, name = "enumeration test uuid02").sync()
    }

    override fun onAfter() {
        runCatching { server.removeChannelMetadata(channelId).sync() }
        runCatching { server.removeChannelMetadata(channelId02).sync() }
        runCatching { server.removeUUIDMetadata(uuidId).sync() }
        runCatching { server.removeUUIDMetadata(uuidId02).sync() }
        // Best effort: revoke may be disabled on the keyset, which must not fail the test.
        tokens.forEach { token -> runCatching { server.revokeToken(token).sync() } }
    }

    @Test
    fun grantToken_flagsRoundTripThroughParseToken() {
        val token = grantToken(getAllChannels = true, getAllUUIDs = true)

        val parsed = server.parseToken(token)

        assertTrue(parsed.getAllChannels)
        assertTrue(parsed.getAllUUIDs)
    }

    @Test
    fun tokenWithoutCategories_cannotListMetadata() {
        val client =
            tokenClient(
                grantToken(
                    getAllChannels = false,
                    getAllUUIDs = false,
                    channelGrant = ChannelGrant.name(channelId, get = true),
                    uuidGrant = UUIDGrant.id(uuidId, get = true),
                ),
            )

        assertForbidden { client.getAllChannelMetadata(filter = channelFilter).sync() }
        assertForbidden { client.getAllUUIDMetadata(filter = uuidFilter).sync() }
    }

    @Test
    fun tokenWithBothCategories_canListMetadata() {
        val client = tokenClient(grantToken(getAllChannels = true, getAllUUIDs = true))

        val fetchedChannelId = client.getAllChannelMetadata().sync().data.map { it.id }.find { it == channelId }
        assertTrue(listOf(channelId).contains(fetchedChannelId))
        val fetchedUuidId = client.getAllUUIDMetadata().sync().data.map { it.id }.find { it == uuidId }
        assertTrue(listOf(uuidId).contains(fetchedUuidId))
    }

    @Test
    fun tokenWithGetAllChannelsOnly_canListOnlyChannels() {
        val client = tokenClient(grantToken(getAllChannels = true, getAllUUIDs = false))

        assertListsChannel(client)
        assertForbidden { client.getAllUUIDMetadata(filter = uuidFilter).sync() }
    }

    @Test
    fun tokenWithGetAllUUIDsOnly_canListOnlyUuids() {
        val client = tokenClient(grantToken(getAllChannels = false, getAllUUIDs = true))

        assertForbidden { client.getAllChannelMetadata(filter = channelFilter).sync() }
        assertListsUuid(client)
    }

    @Test
    fun serverWithSecretKey_canListMetadata() {
        assertListsChannel(server)
        assertListsUuid(server)
    }

    @Test
    fun clientWithoutTokenOrSecretKey_cannotListMetadata() {
        val client = createAuthorizedClient()

        assertForbidden { client.getAllChannelMetadata(filter = channelFilter).sync() }
        assertForbidden { client.getAllUUIDMetadata(filter = uuidFilter).sync() }
    }

    @Test
    fun legacyGrant_returnsCategories() {
        val authKey = "enum-auth-" + UUID.randomUUID()

        val result = server.grant(authKeys = listOf(authKey), getAllChannels = true, getAllUUIDs = true).sync()

        assertEquals(setOf("channels", "uuids"), result.categories.keys)
        assertTrue(result.categories["channels"]!![authKey]!!.getEnabled)
        assertTrue(result.categories["uuids"]!![authKey]!!.getEnabled)
    }

    @Test
    fun legacyGrant_authKeyWithCategories_canListMetadata() {
        val authKey = "enum-auth-" + UUID.randomUUID()
        server.grant(authKeys = listOf(authKey), getAllChannels = true, getAllUUIDs = true).sync()

        val client = authKeyClient(authKey)

        assertListsChannel(client)
        assertListsUuid(client)
    }

    @Test
    fun legacyGrant_authKeyWithoutCategories_cannotListMetadata() {
        val client = authKeyClient("enum-auth-" + UUID.randomUUID())

        assertForbidden { client.getAllChannelMetadata(filter = channelFilter).sync() }
        assertForbidden { client.getAllUUIDMetadata(filter = uuidFilter).sync() }
    }

    // A named-resource `get` is included so the token is valid even with both flags off.
    private fun grantToken(
        getAllChannels: Boolean,
        getAllUUIDs: Boolean,
        channelGrant: ChannelGrant? = null,
        uuidGrant: UUIDGrant? = null
    ): String =
        server.grantToken(
            ttl = 60,
            channels = listOfNotNull(channelGrant),
            uuids = listOfNotNull(uuidGrant),
            getAllChannels = getAllChannels,
            getAllUUIDs = getAllUUIDs,
        ).sync().token.also {
            tokens.add(it)
        }

    private fun tokenClient(token: String): PubNub = createAuthorizedClient().apply { setToken(token) }

    private fun authKeyClient(authKey: String): PubNub =
        createPubNub {
            userId = UserId("enum-client-${UUID.randomUUID()}")
            subscribeKey = Keys.pamSubKey
            publishKey = Keys.pamPubKey
            this.authKey = authKey
        }

    private fun assertListsChannel(client: PubNub) {
        val ids = client.getAllChannelMetadata(filter = channelFilter).sync().data.map { it.id }
        assertEquals(listOf(channelId), ids)
    }

    private fun assertListsUuid(client: PubNub) {
        val ids = client.getAllUUIDMetadata(filter = uuidFilter).sync().data.map { it.id }
        assertEquals(listOf(uuidId), ids)
    }

    private fun assertForbidden(call: () -> Unit) {
        try {
            call()
            fail("Expected HTTP 403")
        } catch (e: PubNubException) {
            // The SDK doesn't expose the server error code (2013 / 2014), only the status.
            assertEquals(403, e.statusCode)
        }
    }
}
