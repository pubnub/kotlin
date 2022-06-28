package com.pubnub.api.endpoints.access

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.managers.RetrofitManager
import com.pubnub.api.models.consumer.access_manager.sum.SpaceIdGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.server.access_manager.v3.GrantTokenData
import com.pubnub.api.models.server.access_manager.v3.GrantTokenResponse
import com.pubnub.api.services.AccessManagerService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response

internal class GrantTokenTest {
    private lateinit var pubnub: PubNub

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration = PNConfiguration(userId = UserId("myUserId")).apply {
            subscribeKey = "something"
            secretKey = "something"
        }
        pubnub = spyk(PubNub(configuration = pnConfiguration))
    }

    @MockK
    private lateinit var grantTokenEndpointMock: GrantToken

    @MockK
    private lateinit var call: Call<GrantTokenResponse>

    @MockK
    private lateinit var responseFromServer: Response<GrantTokenResponse>

    @MockK
    private lateinit var rawResponse: okhttp3.Response

    @Test
    fun can_beAnExample() {
        val expectedTTL = 1337
        val authorizedUserId = UserId("authorizedUserId")
        val expectedToken = "token_value"
        val grantTokenResult = PNGrantTokenResult(token = expectedToken)

        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val capturedBodies = mutableListOf<Any>()
        val call = mockk<Call<GrantTokenResponse>>()

        every { pubnub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.grantToken(any(), capture(capturedBodies), any()) } returns call
        every { call.execute() } returns Response.success(GrantTokenResponse(GrantTokenData(expectedToken)))

        val result = pubnub.grantToken(
            ttl = expectedTTL,
            authorizedUserId = authorizedUserId,
            spaces = listOf(SpaceIdGrant.name(spaceId = SpaceId("mySpaceId"), read = true, delete = true))
        ).sync()

        val capturedBody = capturedBodies[0]
        // some assertions on body
    }

    @Test
    fun can_createGrantTokenSimple() {
        val expectedTTL = 1337
        val authorizedUserId = UserId("authorizedUserId")
        val expectedToken = "token_value"
        val grantTokenResult = PNGrantTokenResult(token = expectedToken)
        every { pubnub.grantToken(ttl = any(), authorizedUserId = any(), spaceIds = any()) } returns grantTokenEndpointMock
        every { grantTokenEndpointMock.sync() } returns grantTokenResult

        val grantTokenEndpoint = pubnub.grantToken(
            ttl = expectedTTL,
            authorizedUserId = authorizedUserId,
            spaceIds = listOf(SpaceIdGrant.name(spaceId = SpaceId("mySpaceId"), read = true, delete = true))
        )
        val actualGrantTokenResult: PNGrantTokenResult? = grantTokenEndpoint.sync()
        val token = actualGrantTokenResult!!.token

        assertEquals(expectedToken, token)
    }

    @Test
    fun can_createGrantToken() {
        val expectedTTL = 1337
        val authorizedUserIdValue = "authorizedUserId"
        val expectedToken = "token_value"
        grantTokenEndpointMock = spyk(
            GrantToken(
                pubnub = pubnub,
                ttl = 1111,
                meta = null,
                authorizedUUID = authorizedUserIdValue,
                channels = listOf(
                    ChannelGrant.name(name = "channel1", read = true, delete = true),
                    ChannelGrant.pattern(pattern = "channel.*", write = true, manage = true)
                ),
                channelGroups = emptyList(),
                uuids = emptyList()
            )
        )

        every { pubnub.grantToken(ttl = any(), authorizedUserId = any(), spaceIds = any()) } returns grantTokenEndpointMock
        every { grantTokenEndpointMock.doWork(any()) } returns call
        every { call.execute() } returns responseFromServer
        every { responseFromServer.isSuccessful } returns true
        every { responseFromServer.body()?.data?.token } returns expectedToken
        every { responseFromServer.raw() } returns rawResponse
        every { rawResponse.receivedResponseAtMillis } returns 1111
        every { rawResponse.sentRequestAtMillis } returns 1110

        val grantTokenEndpoint = pubnub.grantToken(
            ttl = expectedTTL,
            authorizedUserId = UserId(authorizedUserIdValue),
            spaceIds = listOf(SpaceIdGrant.name(spaceId = SpaceId("mySpaceId"), read = true, delete = true))
        )
        val actualGrantTokenResult: PNGrantTokenResult? = grantTokenEndpoint.sync()
        val token = actualGrantTokenResult!!.token

        assertEquals(expectedToken, token)
    }
}
