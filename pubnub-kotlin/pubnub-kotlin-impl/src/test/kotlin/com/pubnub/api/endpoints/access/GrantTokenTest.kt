package com.pubnub.api.endpoints.access

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.TokenGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
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
                    DataSyncGrant.user("user-A", get = true),
                    DataSyncGrant.channel("chan-B", get = true),
                    DataSyncGrant.subscribe("ent-A", "admin"),
                    DataSyncGrant.entity("ent-A", get = true),
                ),
        ).sync()

        val permissions = (capturedBodies[0] as GrantTokenRequestBody).permissions
        val resources = permissions.resources

        // then — each grant lands in its own bucket and the authorized principal maps to permissions.uuid
        assertTrue(resources.channels.containsKey("chan-A"))
        assertTrue(resources.groups.containsKey("grp-A"))
        assertTrue(resources.users.containsKey("user-A"))
        assertTrue(resources.channels.containsKey("chan-B"))
        assertEquals(1, resources.channels["__admin__ent-A"])
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
        val user: PNGrant = DataSyncGrant.user("u")
        val dataSync: PNGrant = DataSyncGrant.entity("e")
        val subscribe: PNGrant = DataSyncGrant.subscribe("e")
        val uuid: PNGrant = UUIDGrant.id("legacy")

        assertTrue(channel is TokenGrant)
        assertTrue(channelGroup is TokenGrant)
        assertTrue(user is TokenGrant)
        assertTrue(dataSync is TokenGrant)
        assertTrue(subscribe is ChannelGrant)
        assertFalse(uuid is TokenGrant)
    }

    @Test
    fun dataSyncUserGrantAloneSatisfiesAtLeastOneGrantCheck() {
        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val capturedBodies = mutableListOf<Any>()
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), capture(capturedBodies), any()) } returns call
        every { call.execute() } returns Response.success(GrantTokenResponse(GrantTokenData("token_value")))

        // when — the only grant is a DataSync user grant (routed to `users` inside the request body)
        val result = pubnub.grantToken(ttl = 60, grants = listOf(DataSyncGrant.user("user-A", get = true))).sync()

        // then — no "At least one grant required" error, and the grant reaches the users bucket
        assertEquals("token_value", result.token)
        assertEquals(32, (capturedBodies[0] as GrantTokenRequestBody).permissions.resources.users["user-A"])
    }

    @Test
    fun categoriesOnlyGrantSatisfiesAtLeastOneGrantCheck() {
        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val capturedBodies = mutableListOf<Any>()
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), capture(capturedBodies), any()) } returns call
        every { call.execute() } returns Response.success(GrantTokenResponse(GrantTokenData("token_value")))

        // when — no resource or pattern grants, only the category flags
        val result = pubnub.grantToken(ttl = 60, getAllChannels = true, getAllUUIDs = true).sync()

        // then — no "At least one grant required" error, and the categories reach the request body
        assertEquals("token_value", result.token)
        val categories = (capturedBodies[0] as GrantTokenRequestBody).permissions.categories!!
        assertEquals(32, categories.channels)
        assertEquals(32, categories.uuids)
    }

    @Test
    fun oldGrantTokenSignatureIsKeptForBinaryCompatibility() {
        // given — the pre-categories 6-param JVM signature, as called by code compiled against the previous release
        val oldGrantToken =
            PubNub::class.java.getMethod(
                "grantToken",
                Int::class.javaPrimitiveType,
                Any::class.java,
                String::class.java,
                List::class.java,
                List::class.java,
                List::class.java,
            )

        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val capturedBodies = mutableListOf<Any>()
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), capture(capturedBodies), any()) } returns call
        every { call.execute() } returns Response.success(GrantTokenResponse(GrantTokenData("token_value")))

        // when
        val grantToken =
            oldGrantToken.invoke(
                pubnub,
                60,
                null,
                "authorizedUserId",
                listOf(ChannelGrant.name("ch", read = true)),
                emptyList<ChannelGroupGrant>(),
                emptyList<UUIDGrant>(),
            ) as GrantToken
        val result = grantToken.sync()

        // then — it delegates to the new overload: arguments passed through, both flags off
        assertEquals("token_value", result.token)
        val body = capturedBodies[0] as GrantTokenRequestBody
        assertEquals(1, body.permissions.resources.channels["ch"])
        assertEquals("authorizedUserId", body.permissions.uuid)
        assertEquals(null, body.permissions.categories)
    }

    @Test
    fun oldGrantSignatureIsKeptForBinaryCompatibility() {
        // given — the pre-categories 12-param JVM signature of the legacy `grant`
        val oldGrant =
            PubNub::class.java.getMethod(
                "grant",
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                List::class.java,
                List::class.java,
                List::class.java,
                List::class.java,
            )

        // when
        val grant =
            oldGrant.invoke(
                pubnub,
                true, // read
                false, // write
                false, // manage
                false, // delete
                true, // get
                false, // update
                false, // join
                60, // ttl
                listOf("key"), // authKeys
                listOf("ch"), // channels
                emptyList<String>(), // channelGroups
                emptyList<String>(), // uuids
            ) as Grant

        // then — the arguments are passed through and both flags are off
        assertTrue(grant.read)
        assertTrue(grant.get)
        assertEquals(listOf("ch"), grant.channels)
        assertFalse(grant.getAllChannels)
        assertFalse(grant.getAllUUIDs)
    }

    @Test
    fun grantWithoutResourcesOrCategoriesIsRejected() {
        // when — nothing is granted at all
        val exception =
            assertThrows(PubNubException::class.java) {
                pubnub.grantToken(ttl = 60).sync()
            }

        // then
        assertEquals("At least one grant required", exception.errorMessage)
    }

    @Test
    fun failedGrantReportsDataSyncChannelIdsAsAffectedChannels() {
        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), any(), any()) } returns call
        every { call.execute() } returns Response.error(400, "{}".toResponseBody("application/json".toMediaType()))

        // when — the grant fails and the error body names no channels
        val exception =
            assertThrows(PubNubException::class.java) {
                pubnub.grantToken(ttl = 60, grants = listOf(DataSyncGrant.channel("x", get = true))).sync()
            }

        // then — the DataSync channel id is still reported as affected (it lands in the `channels` bucket)
        assertEquals(listOf("x"), exception.affectedChannels)
    }
}
