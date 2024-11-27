package com.pubnub.internal.managers

import com.google.gson.JsonPrimitive
import com.pubnub.api.PubNub
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.callbacks.EventListener
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ListenerManagerTest : BaseTest() {
    @Test
    fun `exception in listener is isolated from other listeners`() {
        // given
        val listenerManager = ListenerManager(pubnub)
        val exception = Exception("Crash!")
        var received = mutableListOf<Any>()

        listenerManager.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                received += exception
                throw exception
            }
        })
        listenerManager.addListener(object : EventListener {
            override fun message(pubnub: PubNub, result: PNMessageResult) {
                received += true
            }
        })

        // when
        listenerManager.announce(PNMessageResult(BasePubSubResult("a", null, null, null, null), JsonPrimitive("a")))

        // then
        assertEquals(listOf(exception, true), received)
    }
}
