package com.pubnub.api.presence.internal

import org.junit.Test

class PresenceMachineTest {
    @Test
    fun firstPresenceMachineTest() {
        val machine = presenceMachine()

        val events = listOf(
            Commands.SubscribeIssued(channels = listOf("ch1")),
            IAmHere.Succeed,
            Commands.SubscribeIssued(channels = listOf("ch2")),
            IAmHere.Succeed,
            HeartbeatIntervalOver
        )

        val effects = events.map { machine(it) }

        println(effects)
    }
}