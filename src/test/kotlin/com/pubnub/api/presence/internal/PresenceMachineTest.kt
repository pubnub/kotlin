package com.pubnub.api.presence.internal

import org.junit.Test

class PresenceMachineTest {
    @Test
    fun firstPresenceMachineTest() {
        val (machine, initialEffects) = presenceEventEngine()

        val events = listOf(
            Commands.SubscribeIssued(channels = listOf("ch1")),
            IAmHere.Succeed,
            Commands.SubscribeIssued(channels = listOf("ch2")),
            IAmHere.Succeed,
            HeartbeatIntervalOver
        )

        val effects = initialEffects + events.flatMap { machine.transition(it) }

        println(effects)
    }
}