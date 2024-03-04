package com.pubnub.internal.v2.callbacks

import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.internal.callbacks.DelegatingStatusListener
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DelegatingStatusListenerTest {
    @Test
    fun testEquals() {
        val statusListener = StatusListener { pubnub, pnStatus -> TODO("Not yet implemented") }
        val otherStatusListener = StatusListener { pubnub, pnStatus -> TODO("Not yet implemented") }
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