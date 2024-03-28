package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.internal.PubNubImpl
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ChannelImplTest {
    private lateinit var objectUnderTest: ChannelImpl

    private val pubnub: PubNubImpl = mockk(relaxed = true)

    @Test
    fun shouldCallSubscriptionWithEmptyOptionObjectWhenCallingSubscriptionWithoutOpitons() {
        val channelName = "myChannelName"
        objectUnderTest = ChannelImpl(pubnub, channelName)

        objectUnderTest.subscription()

        verify { objectUnderTest.subscription(EmptyOptions) }
    }
}
