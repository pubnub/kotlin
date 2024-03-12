package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.internal.PubNubImpl
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ChannelGroupImplTest {
    private lateinit var objectUnderTest: ChannelGroupImpl

    private val pubnub: PubNubImpl = mockk(relaxed = true)

    @Test
    fun shouldCallSubscriptionWithEmptyOptionObjectWhenCallingSubscriptionWithoutOptions() {
        val channelGroupName = "myChannelGroupName"
        objectUnderTest = ChannelGroupImpl(pubnub, channelGroupName)

        val subscription: Subscription = objectUnderTest.subscription()

        verify { objectUnderTest.subscription(EmptyOptions) }
    }
}
