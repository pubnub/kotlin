package com.pubnub.internal.java.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

class ChannelGroupImplTest {
    private lateinit var objectUnderTest: ChannelGroupImpl

    private val pubnub: PubNubForJavaImpl = mockk(relaxed = true)

    @Test
    fun shouldCallSubscriptionWithEmptyOptionObjectWhenCallingSubscriptionWithoutOptions() {
        objectUnderTest = spyk(ChannelGroupImpl(pubnub, ChannelGroupName("abc")))

        objectUnderTest.subscription()

        verify { objectUnderTest.subscription(EmptyOptions) }
    }
}
