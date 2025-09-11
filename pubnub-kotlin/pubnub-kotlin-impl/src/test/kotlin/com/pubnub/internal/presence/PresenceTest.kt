package com.pubnub.internal.presence

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.logging.LogConfig
import com.pubnub.contract.subscribe.eventEngine.state.TestSinkSource
import com.pubnub.internal.eventengine.EventEngineConf
import com.pubnub.internal.eventengine.QueueEventEngineConf
import com.pubnub.internal.managers.ListenerManager
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.internal.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.internal.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.successfulRemoteAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val CHANNEL_01 = "channel01"
private const val CHANNEL_GROUPS_01 = "channelGroups01"

internal class PresenceTest {
    private val listenerManager: ListenerManager = mockk()
    private val executorService: ScheduledExecutorService = mockk()
    private val logConfig: LogConfig = LogConfig(pnInstanceId = "testInstanceId", userId = "testUserId")

    companion object {
        @JvmStatic
        fun eventAndMethodProvider(): List<Arguments> {
            return listOf(
                arguments(
                    "JOINED",
                    { presence: Presence -> presence.joined(setOf(CHANNEL_01), setOf(CHANNEL_GROUPS_01)) },
                ),
                arguments("LEFT", { presence: Presence -> presence.left(setOf(CHANNEL_01), setOf(CHANNEL_GROUPS_01)) }),
                arguments("LEFT_ALL", { presence: Presence -> presence.leftAll() }),
                arguments("JOINED", { presence: Presence ->
                    presence.presence(
                        setOf(CHANNEL_01),
                        setOf(CHANNEL_GROUPS_01),
                        connected = true,
                    )
                }),
                arguments("LEFT", { presence: Presence ->
                    presence.presence(
                        setOf(CHANNEL_01),
                        setOf(CHANNEL_GROUPS_01),
                        connected = false,
                    )
                }),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("eventAndMethodProvider")
    fun `should pass correct event for each method call`(
        eventName: String,
        method: Presence.() -> Unit,
    ) {
        // given
        val queuedElements = mutableListOf<Pair<String, String>>()
        val presenceData = PresenceData()
        val presence =
            Presence.create(
                listenerManager = listenerManager,
                eventEngineConf = QueueEventEngineConf(eventSinkSource = TestSinkSource(queuedElements)),
                presenceData = presenceData,
            )

        // when
        presence.method()

        // then
        Assertions.assertEquals(listOf("event" to eventName), queuedElements)
    }

    @Test
    fun `create returns PresenceNoOp if heartbeat interval is 0`() {
        // when
        val presence = Presence.create(listenerManager = listenerManager, heartbeatInterval = 0.seconds)

        // then
        assertThat(presence, Matchers.isA(PresenceNoOp::class.java))
    }

    @Test
    fun `Leave events not created when suppressLeaveEvents is true and heartbeat interval is 0`() {
        // given
        val leaveProviderMock: LeaveProvider = mockk()
        val presence =
            Presence.create(
                listenerManager = listenerManager,
                heartbeatInterval = 0.seconds,
                suppressLeaveEvents = true,
                leaveProvider = leaveProviderMock,
            )

        // when
        presence.joined(setOf("abc"))
        presence.leftAll()

        // then
        verify(exactly = 0) { leaveProviderMock.getLeaveRemoteAction(any(), any()) }
    }

    @Test
    fun `Leave events created when suppressLeaveEvents is false and heartbeat interval is 0`() {
        // given
        val leaveProviderMock: LeaveProvider = mockk()
        every { leaveProviderMock.getLeaveRemoteAction(any(), any()) } returns successfulRemoteAction(true)

        val presence =
            Presence.create(
                listenerManager = listenerManager,
                heartbeatInterval = 0.seconds,
                suppressLeaveEvents = false,
                leaveProvider = leaveProviderMock,
            )

        // when
        presence.joined(setOf("abc"))
        presence.leftAll()

        // then
        verify { leaveProviderMock.getLeaveRemoteAction(any(), any()) }
    }

    @Test
    fun `Leave events created when suppressLeaveEvents is false and heartbeat interval is 0 and presence(false) called`() {
        // given
        val leaveProviderMock: LeaveProvider = mockk()
        every { leaveProviderMock.getLeaveRemoteAction(any(), any()) } returns successfulRemoteAction(true)

        val presence =
            Presence.create(
                listenerManager = listenerManager,
                heartbeatInterval = 0.seconds,
                suppressLeaveEvents = false,
                leaveProvider = leaveProviderMock,
            )

        // when
        presence.presence(channels = setOf("abc"), connected = false)

        // then
        assertInstanceOf(PresenceNoOp::class.java, presence)
        verify { leaveProviderMock.getLeaveRemoteAction(any(), any()) }
    }

    @Test
    fun `should call heartbeat when joining channels with heartbeat interval 0`() {
        // given
        val heartbeatProviderMock: HeartbeatProvider = mockk()
        every { heartbeatProviderMock.getHeartbeatRemoteAction(any(), any(), any()) } returns successfulRemoteAction(
            true
        )

        val presence =
            Presence.create(
                listenerManager = listenerManager,
                heartbeatInterval = 0.seconds,
                heartbeatProvider = heartbeatProviderMock,
            )

        // when
        presence.joined(setOf("abc"))

        // then
        verify { heartbeatProviderMock.getHeartbeatRemoteAction(setOf("abc"), emptySet(), any()) }
    }

    @Test
    fun `should not call heartbeat when joining empty channels with heartbeat interval 0`() {
        // given
        val heartbeatProviderMock: HeartbeatProvider = mockk()
        every { heartbeatProviderMock.getHeartbeatRemoteAction(any(), any(), any()) } returns successfulRemoteAction(
            true
        )

        val presence =
            Presence.create(
                listenerManager = listenerManager,
                heartbeatInterval = 0.seconds,
                heartbeatProvider = heartbeatProviderMock,
            )

        // when
        presence.joined(emptySet())

        // then
        verify(exactly = 0) { heartbeatProviderMock.getHeartbeatRemoteAction(any(), any(), any()) }
    }

    private fun Presence.Companion.create(
        listenerManager: ListenerManager,
        heartbeatInterval: Duration = 3.seconds,
        heartbeatNotificationOptions: PNHeartbeatNotificationOptions = PNHeartbeatNotificationOptions.ALL,
        eventEngineConf: EventEngineConf<PresenceEffectInvocation, PresenceEvent> = QueueEventEngineConf(),
        presenceData: PresenceData = PresenceData(),
        suppressLeaveEvents: Boolean = false,
        leaveProvider: LeaveProvider = LeaveProvider { _, _ -> successfulRemoteAction(true) },
        heartbeatProvider: HeartbeatProvider = HeartbeatProvider { _, _, _ -> successfulRemoteAction(true) },
        logConfig: LogConfig = LogConfig(pnInstanceId = "testInstanceId", userId = "testUserId"),
    ) = create(
        heartbeatProvider = heartbeatProvider,
        leaveProvider = leaveProvider,
        heartbeatInterval = heartbeatInterval,
        suppressLeaveEvents = suppressLeaveEvents,
        heartbeatNotificationOptions = heartbeatNotificationOptions,
        listenerManager = listenerManager,
        eventEngineConf = eventEngineConf,
        presenceData = presenceData,
        sendStateWithHeartbeat = true,
        executorService = executorService,
        logConfig = logConfig,
    )
}
