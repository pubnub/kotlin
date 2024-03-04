package com.pubnub.internal.v2.callbacks

import com.pubnub.api.v2.callbacks.EventListener
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DelegatingEventListenerTest {
    @Test
    fun testEquals() {
        val eventListener = object : EventListener {}
        val otherEventListener = object : EventListener {}
        val delegating1 = DelegatingEventListener(eventListener)
        val delegating2 = DelegatingEventListener(eventListener)
        val otherDelegating = DelegatingEventListener(otherEventListener)

        Assertions.assertEquals(delegating1, delegating2)
        Assertions.assertEquals(delegating2, delegating1)
        Assertions.assertNotEquals(delegating1, otherDelegating)
        Assertions.assertNotEquals(delegating2, otherDelegating)
        Assertions.assertNotEquals(otherDelegating, delegating1)
        Assertions.assertNotEquals(otherDelegating, delegating2)
    }
}
