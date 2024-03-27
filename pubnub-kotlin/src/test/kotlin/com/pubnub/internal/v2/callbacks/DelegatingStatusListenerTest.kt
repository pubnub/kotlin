package com.pubnub.internal.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.v2.callbacks.StatusListener
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DelegatingStatusListenerTest {
    @Test
    fun testEquals() {
        val statusListener =
            object : StatusListener {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {}
            }
        val otherStatusListener =
            object : StatusListener {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {}
            }
        val delegating1 = DelegatingStatusListener(statusListener)
        val delegating2 = DelegatingStatusListener(statusListener)
        val otherDelegating = DelegatingStatusListener(otherStatusListener)

        Assertions.assertEquals(delegating1, delegating2)
        Assertions.assertEquals(delegating2, delegating1)
        Assertions.assertNotEquals(delegating1, otherDelegating)
        Assertions.assertNotEquals(delegating2, otherDelegating)
        Assertions.assertNotEquals(otherDelegating, delegating1)
        Assertions.assertNotEquals(otherDelegating, delegating2)
    }
}
