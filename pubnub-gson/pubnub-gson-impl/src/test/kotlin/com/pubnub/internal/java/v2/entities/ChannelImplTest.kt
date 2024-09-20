package com.pubnub.internal.java.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.v2.entities.ChannelName
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ChannelImplTest {
    private lateinit var objectUnderTest: ChannelImpl

    private val pubnub: PubNubForJavaImpl = mockk(relaxed = true)

    @Test
    fun shouldCallSubscriptionWithEmptyOptionObjectWhenCallingSubscriptionWithoutOpitons() {
        objectUnderTest = spyk(ChannelImpl(pubnub, ChannelName("abc")))

        objectUnderTest.subscription()

        verify { objectUnderTest.subscription(EmptyOptions) }
    }
}
