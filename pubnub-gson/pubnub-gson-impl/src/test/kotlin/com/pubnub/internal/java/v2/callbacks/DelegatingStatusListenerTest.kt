package com.pubnub.internal.java.v2.callbacks

import com.pubnub.api.UserId
import com.pubnub.api.java.PubNub
import com.pubnub.api.java.v2.PNConfiguration
import com.pubnub.api.java.v2.callbacks.StatusListener
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DelegatingStatusListenerTest {
    @Test
    fun testEquals() {
        val pubnub = PubNub.create(PNConfiguration.builder(UserId("abc"), "demo").build())

        val statusListener = StatusListener { pubnub, pnStatus -> TODO("Not yet implemented") }
        val otherStatusListener = StatusListener { pubnub, pnStatus -> TODO("Not yet implemented") }
        val delegating1 = DelegatingStatusListener(statusListener, pubnub)
        val delegating2 = DelegatingStatusListener(statusListener, pubnub)
        val otherDelegating = DelegatingStatusListener(otherStatusListener, pubnub)

        Assertions.assertEquals(delegating1, delegating2)
        Assertions.assertEquals(delegating2, delegating1)
        Assertions.assertNotEquals(delegating1, otherDelegating)
        Assertions.assertNotEquals(delegating2, otherDelegating)
        Assertions.assertNotEquals(otherDelegating, delegating1)
        Assertions.assertNotEquals(otherDelegating, delegating2)
    }
}
