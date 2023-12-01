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
import org.hamcrest.CoreMatchers.hasItems
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
        val presenceData = createPresenceData(channels = setOf(CHANNEL_01, CHANNEL_02), channelGroups = setOf(CHANNEL_GROUPS_01, CHANNEL_GROUPS_02))
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
    fun `should create leaveAll event when leaving last channel`() {
        // given
        val queuedElements = mutableListOf<Pair<String, String>>()
        val presenceData = createPresenceData(channels = setOf(CHANNEL_01), channelGroups = setOf())
        val presence =
            Presence.create(
                listenerManager = listenerManager,
                eventEngineConf = QueueEventEngineConf(eventSinkSource = TestSinkSource(queuedElements)),
                presenceData = presenceData
            )

        // when
        presence.left(channels = setOf(CHANNEL_01))

        // then
        Assertions.assertEquals(listOf("event" to "LEFT_ALL"), queuedElements)
    }

    @Test
    fun `should create leaveAll event when leaving last channelGroup`() {
        // given
        val queuedElements = mutableListOf<Pair<String, String>>()
        val presenceData = createPresenceData(channels = setOf(), channelGroups = setOf(CHANNEL_GROUPS_01))
        val presence =
            Presence.create(
                listenerManager = listenerManager,
                eventEngineConf = QueueEventEngineConf(eventSinkSource = TestSinkSource(queuedElements)),
                presenceData = presenceData
            )

        // when
        presence.left(channelGroups = setOf(CHANNEL_GROUPS_01))

        // then
        Assertions.assertEquals(listOf("event" to "LEFT_ALL"), queuedElements)
    }

    @Test
    fun `should add channel and channelGroup to local storage when joining channel`() {
        // given
        val queuedElements = mutableListOf<Pair<String, String>>()
        val presenceData = createPresenceData(channels = setOf(CHANNEL_01), channelGroups = setOf(CHANNEL_GROUPS_01))
        val presence =
            Presence.create(
                listenerManager = listenerManager,
                eventEngineConf = QueueEventEngineConf(eventSinkSource = TestSinkSource(queuedElements)),
                presenceData = presenceData
            )

        // when
        presence.joined(channels = setOf(CHANNEL_02), channelGroups = setOf(CHANNEL_GROUPS_02))

        // then
        assertThat(presenceData.channels, hasItems(CHANNEL_01, CHANNEL_02))
        assertThat(presenceData.channelGroups, hasItems(CHANNEL_GROUPS_01, CHANNEL_GROUPS_02))
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

    private fun Presence.Companion.create(
        listenerManager: ListenerManager,
        heartbeatInterval: Duration = Duration.ofSeconds(3),
        enableEventEngine: Boolean = true,
        heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL,
        eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent> = QueueEventEngineConf(),
        presenceData: PresenceData = PresenceData()
    ) = create(
        heartbeatInterval = heartbeatInterval,
        retryPolicy = NoRetriesPolicy,
        enableEventEngine = enableEventEngine,
        eventEngineConf = eventEngineConf,
        leaveProvider = { _, _ -> successfulRemoteAction(true) },
        heartbeatProvider = { _, _ -> successfulRemoteAction(true) },
        heartbeatNotificationOptions = heartbeatNotificationOptions,
        listenerManager = listenerManager,
        suppressLeaveEvents = false,
        presenceData = presenceData
    )

    private fun createPresenceData(channels: Set<String>, channelGroups: Set<String>): PresenceData {
        val presenceData = PresenceData()
        presenceData.channels.addAll(channels)
        presenceData.channelGroups.addAll(channelGroups)
        return presenceData
    }
}
