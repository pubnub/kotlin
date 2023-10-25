package com.pubnub.api.presence

import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueEventEngineConf
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.NoRetriesPolicy
import com.pubnub.api.subscribe.eventengine.effect.successfulRemoteAction
import com.pubnub.contract.subscribe.eventEngine.state.TestSinkSource
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

internal class PresenceTest {

    companion object {
        @JvmStatic
        fun eventAndMethodProvider(): List<Arguments> {
            return listOf(
                arguments(
                    "JOINED",
                    { presence: Presence -> presence.joined(setOf("Channel01"), setOf("ChannelGroup01")) }
                ),
                arguments("LEFT", { presence: Presence -> presence.left(setOf("Channel01"), setOf("ChannelGroup01")) }),
                arguments("LEFT_ALL", { presence: Presence -> presence.leftAll() }),
                arguments("JOINED", { presence: Presence ->
                    presence.presence(
                        setOf("Channel01"), setOf("ChannelGroup01"), connected = true
                    )
                }),
                arguments("LEFT", { presence: Presence ->
                    presence.presence(
                        setOf("Channel01"), setOf("ChannelGroup01"), connected = false
                    )
                })
            )
        }
    }

    @ParameterizedTest
    @MethodSource("eventAndMethodProvider")
    fun `should pass correct event for each method call`(eventName: String, method: Presence.() -> Unit) {
        // given
        val queuedElements = mutableListOf<Pair<String, String>>()
        val presence =
            Presence.create(eventEngineConf = QueueEventEngineConf(eventSinkSource = TestSinkSource(queuedElements)))

        // when
        presence.method()

        // then
        Assertions.assertEquals(listOf("event" to eventName), queuedElements)
    }

    @Test
    fun `create returns PresenceNoOp if event engine is disabled`() {
        // when
        val presence = Presence.create(enableEventEngine = false)

        // then
        assertThat(presence, Matchers.isA(PresenceNoOp::class.java))
    }

    @Test
    fun `create returns PresenceNoOp if heartbeat interval is 0`() {
        // when
        val presence = Presence.create(heartbeatInterval = Duration.ofSeconds(0))

        // then
        assertThat(presence, Matchers.isA(PresenceNoOp::class.java))
    }

    private fun Presence.Companion.create(
        heartbeatInterval: Duration = Duration.ofSeconds(3),
        enableEventEngine: Boolean = true,
        eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent> = QueueEventEngineConf()
    ) = create(
        heartbeatInterval = heartbeatInterval,
        retryPolicy = NoRetriesPolicy,
        enableEventEngine = enableEventEngine,
        eventEngineConf = eventEngineConf,
        leaveProvider = { _, _ -> successfulRemoteAction(true) },
        heartbeatProvider = { _, _ -> successfulRemoteAction(true) },
    )
}
