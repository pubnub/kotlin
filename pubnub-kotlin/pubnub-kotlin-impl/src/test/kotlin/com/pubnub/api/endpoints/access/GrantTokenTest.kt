package com.pubnub.api.endpoints.access

import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.TokenGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.access_manager.v3.UserGrant
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.access.GrantTokenEndpoint
import com.pubnub.internal.managers.RetrofitManager
import com.pubnub.internal.models.server.access_manager.v3.GrantTokenData
import com.pubnub.internal.models.server.access_manager.v3.GrantTokenRequestBody
import com.pubnub.internal.models.server.access_manager.v3.GrantTokenResponse
import com.pubnub.internal.services.AccessManagerService
import com.pubnub.internal.v2.PNConfigurationImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response

internal class GrantTokenTest {
    private lateinit var pubnub: PubNubImpl

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration =
            PNConfigurationImpl(
                userId = UserId("myUserId"),
                subscribeKey = "something",
                secretKey = "something",
            )
        pubnub = spyk(PubNubImpl(configuration = pnConfiguration))
    }

    @MockK
    private lateinit var grantTokenEndpointMock: GrantTokenEndpoint

    @Test
    fun can_createGrantTokenSimple() {
        val expectedTTL = 1337
        val authorizedUUID = "authorizedUserId"
        val expectedToken = "token_value"
        val grantTokenResult = PNGrantTokenResult(token = expectedToken)
        every {
            pubnub.grantToken(
                ttl = any(),
                meta = any(),
                authorizedUUID = any(),
                channels = any(),
                channelGroups = any(),
                uuids = any(),
            )
        } returns grantTokenEndpointMock
        every { grantTokenEndpointMock.sync() } returns grantTokenResult

        val grantTokenEndpoint =
            pubnub.grantToken(
                ttl = expectedTTL,
                authorizedUUID = authorizedUUID,
                channels = listOf(ChannelGrant.name(name = "mySpaceId", read = true, delete = true)),
            )
        val actualGrantTokenResult: PNGrantTokenResult? = grantTokenEndpoint.sync()
        val token = actualGrantTokenResult!!.token

        assertEquals(expectedToken, token)
    }

    @Test
    fun can_createGrantToken() {
        val expectedTTL = 1337
        val authorizedUUID = "authorizedUserId"
        val expectedToken = "token_value"
        val channelValue = "mySpaceId"

        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val capturedBodies = mutableListOf<Any>()
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), capture(capturedBodies), any()) } returns call
        every { call.execute() } returns Response.success(GrantTokenResponse(GrantTokenData(expectedToken)))

        val actualGrantTokenResult: PNGrantTokenResult? =
            pubnub.grantToken(
                ttl = expectedTTL,
                authorizedUUID = authorizedUUID,
                channels =
                    listOf(
                        ChannelGrant.name(
                            name = channelValue,
                            read = true,
                            delete = true,
                        ),
                    ),
            ).sync()

        val capturedBody = capturedBodies[0]

        assertEquals(expectedToken, actualGrantTokenResult!!.token)

        val ttl: Int = (capturedBody as GrantTokenRequestBody).ttl
        val permissions = capturedBody.permissions

        assertEquals(expectedTTL, ttl)
        assertTrue(permissions.resources.channels.containsKey(channelValue))
        assertEquals(authorizedUUID, permissions.uuid)
    }

    @Test
    fun flatGrantsListPartitionsIntoCorrectBuckets() {
        val expectedTTL = 42
        val authorizedUserId = "authorized-user"
        val expectedToken = "token_value"

        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val capturedBodies = mutableListOf<Any>()
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), capture(capturedBodies), any()) } returns call
        every { call.execute() } returns Response.success(GrantTokenResponse(GrantTokenData(expectedToken)))

        // when — one grant of each supported type in a single flat list
        pubnub.grantToken(
            ttl = expectedTTL,
            authorizedUserId = UserId(authorizedUserId),
            grants =
                listOf(
                    ChannelGrant.name("chan-A", read = true),
                    ChannelGroupGrant.id("grp-A", read = true),
                    UserGrant.id("user-A", get = true),
                    DataSyncGrant.entity("ent-A", get = true),
                ),
        ).sync()

        val permissions = (capturedBodies[0] as GrantTokenRequestBody).permissions
        val resources = permissions.resources

        // then — each grant lands in its own bucket and the authorized principal maps to permissions.uuid
        assertTrue(resources.channels.containsKey("chan-A"))
        assertTrue(resources.groups.containsKey("grp-A"))
        assertTrue(resources.users.containsKey("user-A"))
        assertTrue(resources.datasyncEntities.containsKey("ent-A"))
        assertFalse(resources.uuids.containsKey("user-A"))
        assertEquals(authorizedUserId, permissions.uuid)
    }

    @Test
    fun tokenGrantMarkerExcludesUuidGrant() {
        // The flat-list overload accepts only TokenGrant. UUIDGrant deliberately does not implement it, so it can't
        // be passed to grants(...) — the exclusion is a compile-time guard. Assert the marker wiring reflects that.
        val channel: PNGrant = ChannelGrant.name("c")
        val channelGroup: PNGrant = ChannelGroupGrant.id("g")
        val user: PNGrant = UserGrant.id("u")
        val dataSync: PNGrant = DataSyncGrant.entity("e")
        val uuid: PNGrant = UUIDGrant.id("legacy")

        assertTrue(channel is TokenGrant)
        assertTrue(channelGroup is TokenGrant)
        assertTrue(user is TokenGrant)
        assertTrue(dataSync is TokenGrant)
        assertFalse(uuid is TokenGrant)
    }
}
