package com.pubnub.api.endpoints.access

import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.access.RevokeTokenEndpoint
import com.pubnub.internal.managers.RetrofitManager
import com.pubnub.internal.models.server.access_manager.v3.RevokeTokenData
import com.pubnub.internal.models.server.access_manager.v3.RevokeTokenResponse
import com.pubnub.internal.services.AccessManagerService
import com.pubnub.internal.v2.PNConfigurationImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.Call
import retrofit2.Response

class RevokeTokenTest {
    private lateinit var pubNub: PubNubImpl

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration =
            PNConfigurationImpl(
                userId = UserId("myUserId"),
                subscribeKey = "something",
                secretKey = "secretKey"
            )
        pubNub = spyk(PubNubImpl(configuration = pnConfiguration))
    }

    @MockK
    private lateinit var revokeTokenEndpointMock: RevokeTokenEndpoint

    @Test
    fun shouldThrowExceptionWhenSecretKeyNotProvided() {
        val pnConfiguration =
            PNConfigurationImpl(
                userId = UserId("myUserId"),
                subscribeKey = "something",
            )
        pubNub = spyk(PubNubImpl(configuration = pnConfiguration))

        val token = "test-token"
        val exception = assertThrows<PubNubException> {
            pubNub.revokeToken(token).sync()
        }

        assertEquals("Secret Key not configured", exception.errorMessage)
    }

    @Test
    fun shouldThrowExceptionWhenTokenIsBlank() {
        val blankToken = ""
        val exception = assertThrows<PubNubException> {
            pubNub.revokeToken(blankToken).sync()
        }

        assertEquals("Token missing", exception.errorMessage)
    }

    @Test
    fun can_callRevokeToken() {
        val expectedToken = "token_value"
        val expectedSubscribeKey = "something"

        val retrofitManager = mockk<RetrofitManager>(relaxed = true)
        val accessManagerService = mockk<AccessManagerService>(relaxed = true)
        val call = mockk<Call<RevokeTokenResponse>>()

        every { pubNub.retrofitManager } returns retrofitManager
        every { retrofitManager.accessManagerService } returns accessManagerService
        every { accessManagerService.revokeToken(any(), any(), any()) } returns call
        every { call.execute() } returns Response.success(
            RevokeTokenResponse(
                200,
                RevokeTokenData("message", "token"),
                "service"
            )
        )

        // Act
        pubNub.revokeToken(expectedToken).sync()

        // Assert: verify the call with expected arguments
        verify {
            accessManagerService.revokeToken(
                expectedSubscribeKey,
                any(),
                any()
            )
        }
    }
}
