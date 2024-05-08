package com.pubnub.internal.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DelegatingSubscribeCallbackTest {
    @Test
    fun testEquals() {
        val statusListener =
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {}
            }
        val otherStatusListener =
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {}
            }

        val delegating1 = DelegatingSubscribeCallback(statusListener)
        val delegating2 = DelegatingSubscribeCallback(statusListener)
        val otherDelegating = DelegatingSubscribeCallback(otherStatusListener)

        Assertions.assertEquals(delegating1, delegating2)
        Assertions.assertEquals(delegating2, delegating1)
        Assertions.assertNotEquals(delegating1, otherDelegating)
        Assertions.assertNotEquals(delegating2, otherDelegating)
        Assertions.assertNotEquals(otherDelegating, delegating1)
        Assertions.assertNotEquals(otherDelegating, delegating2)
    }
}
