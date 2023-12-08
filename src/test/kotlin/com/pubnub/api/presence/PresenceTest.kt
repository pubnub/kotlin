package com.pubnub.api.presence

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.eventengine.EventEngineConf
import com.pubnub.api.eventengine.QueueEventEngineConf
import com.pubnub.api.managers.ListenerManager
import com.pubnub.api.presence.eventengine.data.PresenceData
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.NoRetriesPolicy
import com.pubnub.api.subscribe.eventengine.effect.successfulRemoteAction
import com.pubnub.contract.subscribe.eventEngine.state.TestSinkSource
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

private const val CHANNEL_01 = "channel01"
private const val CHANNEL_02 = "channel02"

private const val CHANNEL_GROUPS_01 = "channelGroups01"
private const val CHANNEL_GROUPS_02 = "channelGroups02"

internal class PresenceTest {
    private val listenerManager: ListenerManager = mockk()

    companion object {
        @JvmStatic
        fun eventAndMethodProvider(): List<Arguments> {
            return listOf(
                arguments(
                    "JOINED",
                    { presence: Presence -> presence.joined(setOf(CHANNEL_01), setOf(CHANNEL_GROUPS_01)) }
                ),
                arguments("LEFT", { presence: Presence -> presence.left(setOf(CHANNEL_01), setOf(CHANNEL_GROUPS_01)) }),
                arguments("LEFT_ALL", { presence: Presence -> presence.leftAll() }),
                arguments("JOINED", { presence: Presence ->
                    presence.presence(
                        setOf(CHANNEL_01), setOf(CHANNEL_GROUPS_01), connected = true
                    )
                }),
                arguments("LEFT", { presence: Presence ->
                    presence.presence(
                        setOf(CHANNEL_01), setOf(CHANNEL_GROUPS_01), connected = false
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
        val presenceData = PresenceData()
        val presence =
            Presence.create(
                listenerManager = listenerManager,
                eventEngineConf = QueueEventEngineConf(eventSinkSource = TestSinkSource(queuedElements)),
                presenceData = presenceData
            )

        // when
        presence.method()

        // then
        Assertions.assertEquals(listOf("event" to eventName), queuedElements)
    }

    @Test
    fun `create returns PresenceNoOp if event engine is disabled`() {
        // when
        val presence = Presence.create(listenerManager = listenerManager, enableEventEngine = false)

        // then
        assertThat(presence, Matchers.isA(PresenceNoOp::class.java))
    }

    @Test
    fun `create returns PresenceNoOp if heartbeat interval is 0`() {
        // when
        val presence = Presence.create(listenerManager = listenerManager, heartbeatInterval = Duration.ofSeconds(0))

        // then
        assertThat(presence, Matchers.isA(PresenceNoOp::class.java))
    }

    @Test
    fun `when left is called PresenceData state is cleared for relevant channels`() {
        // given
        val presenceData = PresenceData()
        val state = mapOf("aaa" to "bbb")
        presenceData.channelStates[CHANNEL_01] = state
        presenceData.channelStates[CHANNEL_02] = state
        val presence = Presence.create(listenerManager = listenerManager, enableEventEngine = true, presenceData = presenceData)

        // when
        presence.left(setOf(CHANNEL_02))

        // then
        assertThat(presenceData.channelStates, Matchers.`is`(mapOf(CHANNEL_01 to state)))
    }

    @Test
    fun `when leftAll is called PresenceData state is cleared for all channels`() {
        // given
        val presenceData = PresenceData()
        val state = mapOf("aaa" to "bbb")
        presenceData.channelStates[CHANNEL_01] = state
        presenceData.channelStates[CHANNEL_02] = state
        val presence = Presence.create(listenerManager = listenerManager, enableEventEngine = true, presenceData = presenceData)

        // when
        presence.leftAll()

        // then
        assertThat(presenceData.channelStates, Matchers.anEmptyMap())
    }

    private fun Presence.Companion.create(
        listenerManager: ListenerManager,
        heartbeatInterval: Duration = Duration.ofSeconds(3),
        enableEventEngine: Boolean = true,
        heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL,
        eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent> = QueueEventEngineConf(),
        presenceData: PresenceData = PresenceData()
    ) = create(
        heartbeatProvider = { _, _, _ -> successfulRemoteAction(true) },
        leaveProvider = { _, _ -> successfulRemoteAction(true) },
        heartbeatInterval = heartbeatInterval,
        enableEventEngine = enableEventEngine,
        retryPolicy = NoRetriesPolicy,
        suppressLeaveEvents = false,
        heartbeatNotificationOptions = heartbeatNotificationOptions,
        listenerManager = listenerManager,
        eventEngineConf = eventEngineConf,
        presenceData = presenceData,
        sendStateWithHeartbeat = true
    )
}
