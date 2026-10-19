package com.pubnub.internal.java.endpoints.access

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.access.Grant
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

@Suppress("DEPRECATION")
class GrantImplTest {
    private val pubNubCore: PubNub = mockk()
    private val grantEndpoint: Grant = mockk()

    @Test
    fun categoryFlagsPassThroughToKotlin() {
        // given
        val objectUnderTest = GrantImpl(pubNubCore)
        objectUnderTest.authKeys(listOf("key1"))
            .getAllChannels(true)
            .getAllUUIDs(true)
        every {
            pubNubCore.grant(authKeys = listOf("key1"), getAllChannels = true, getAllUUIDs = true)
        } returns grantEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then
        verify { pubNubCore.grant(authKeys = listOf("key1"), getAllChannels = true, getAllUUIDs = true) }
    }

    @Test
    fun categoryFlagsDefaultToFalse() {
        // given — no category flag set
        val objectUnderTest = GrantImpl(pubNubCore)
        objectUnderTest.authKeys(listOf("key1")).read(true)
        every {
            pubNubCore.grant(read = true, authKeys = listOf("key1"), getAllChannels = false, getAllUUIDs = false)
        } returns grantEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then
        verify {
            pubNubCore.grant(read = true, authKeys = listOf("key1"), getAllChannels = false, getAllUUIDs = false)
        }
    }
}
