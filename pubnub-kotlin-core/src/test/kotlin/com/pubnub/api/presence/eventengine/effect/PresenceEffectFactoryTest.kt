package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.eventengine.Sink
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration.Companion.seconds

class PresenceEffectFactoryTest {
    private lateinit var presenceEffectFactory: PresenceEffectFactory

    private val heartbeatProvider = mockk<HeartbeatProvider>()
    private val leaveProvider = mockk<LeaveProvider>()
    private val presenceEventSink = mockk<Sink<PresenceEvent>>()
    private val retryConfiguration = RetryConfiguration.None
    private val executorService = mockk<ScheduledExecutorService>()
    private val heartbeatInterval = 10.seconds
    private val channels = setOf("channel1")
    private val channelGroups = setOf("channelGroup1")
    private val heartbeatRemoteAction: RemoteAction<Boolean> = mockk()
    private val leaveRemoteAction: RemoteAction<Boolean> = mockk()
    private val suppressLeaveEvents = true
    private val attempts: Int = 1
    private val reason: PubNubException = mockk()
    private val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = mockk()
    private val statusConsumer: StatusConsumer = mockk()
    private val presenceData = PresenceData()

    @BeforeEach
    fun setUp() {
        presenceData.channelStates.clear()
        presenceEffectFactory = PresenceEffectFactory(
            heartbeatProvider,
            leaveProvider,
            presenceEventSink,
            retryConfiguration,
            executorService,
            heartbeatInterval,
            suppressLeaveEvents,
            heartbeatNotificationOptions,
            statusConsumer,
            presenceData,
            true
        )
    }

    @Test
    fun `should return Heartbeat effect when getting Heartbeat invocation`() {
        // given
        val effectInvocation = PresenceEffectInvocation.Heartbeat(channels, channelGroups)
        every {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns heartbeatRemoteAction

        // when
        val effect: HeartbeatEffect = presenceEffectFactory.create(effectInvocation) as HeartbeatEffect

        // then
        assertThat(effect, IsInstanceOf.instanceOf(HeartbeatEffect::class.java))
        assertEquals(heartbeatRemoteAction, effect.heartbeatRemoteAction)
        assertEquals(presenceEventSink, effect.presenceEventSink)
        assertEquals(heartbeatNotificationOptions, effect.heartbeatNotificationOptions)
        assertEquals(statusConsumer, effect.statusConsumer)
    }

    @Test
    fun `should include State from PresenceData into Heartbeat effect when getting Heartbeat invocation`() {
        // given
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")
        presenceData.channelStates["nonSubscribedChannel"] = mapOf("aaa" to "bbb")

        val effectInvocation = PresenceEffectInvocation.Heartbeat(channels, channelGroups)
        every {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns heartbeatRemoteAction

        // when
        presenceEffectFactory.create(effectInvocation)

        // then
        verify {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                mapOf(
                    channels.first() to mapOf(
                        "aaa" to "bbb"
                    )
                )
            )
        }
    }

    @Test
    fun `should include State from PresenceData into delayed Heartbeat effect when getting delayed Heartbeat invocation`() {
        // given
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")
        presenceData.channelStates["nonSubscribedChannel"] = mapOf("aaa" to "bbb")

        val effectInvocation = PresenceEffectInvocation.DelayedHeartbeat(channels, channelGroups, attempts, reason)
        every {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns heartbeatRemoteAction

        // when
        presenceEffectFactory.create(effectInvocation)

        // then
        verify {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                mapOf(
                    channels.first() to mapOf(
                        "aaa" to "bbb"
                    )
                )
            )
        }
    }

    @Test
    fun `should not include State from PresenceData into Heartbeat when sending state is disabled`() {
        // given
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")
        val effectInvocation = PresenceEffectInvocation.Heartbeat(channels, channelGroups)
        every {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns heartbeatRemoteAction

        // when
        PresenceEffectFactory(
            heartbeatProvider,
            leaveProvider,
            presenceEventSink,
            retryConfiguration,
            executorService,
            heartbeatInterval,
            suppressLeaveEvents,
            heartbeatNotificationOptions,
            statusConsumer,
            presenceData,
            false
        ).create(effectInvocation)

        // then
        verify {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                null
            )
        }
    }

    @Test
    fun `should not include State from PresenceData into delayed Heartbeat effect when sending state is disabled`() {
        // given
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")

        val effectInvocation = PresenceEffectInvocation.DelayedHeartbeat(channels, channelGroups, attempts, reason)
        every {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns heartbeatRemoteAction

        // when
        PresenceEffectFactory(
            heartbeatProvider,
            leaveProvider,
            presenceEventSink,
            retryConfiguration,
            executorService,
            heartbeatInterval,
            suppressLeaveEvents,
            heartbeatNotificationOptions,
            statusConsumer,
            presenceData,
            false
        ).create(effectInvocation)

        // then
        verify {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                null
            )
        }
    }

    @Test
    fun `should return null when getting Leave invocation while suppressLeaveEvents is true`() {
        // given
        val effectInvocation = PresenceEffectInvocation.Leave(channels, channelGroups)
        every {
            leaveProvider.getLeaveRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups
            )
        } returns leaveRemoteAction

        // when
        val effect = presenceEffectFactory.create(effectInvocation)

        // then
        assertNull(effect)
    }

    @Test
    fun `should return Leave effect when getting Leave invocation while suppressLeaveEvents is false`() {
        // given
        presenceEffectFactory = PresenceEffectFactory(
            heartbeatProvider,
            leaveProvider,
            presenceEventSink,
            retryConfiguration,
            executorService,
            heartbeatInterval,
            suppressLeaveEvents = false,
            heartbeatNotificationOptions,
            statusConsumer,
            PresenceData(),
            true
        )
        val effectInvocation = PresenceEffectInvocation.Leave(channels, channelGroups)
        every {
            leaveProvider.getLeaveRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups
            )
        } returns leaveRemoteAction

        // when
        val effect: LeaveEffect = presenceEffectFactory.create(effectInvocation) as LeaveEffect

        // then
        assertThat(effect, IsInstanceOf.instanceOf(LeaveEffect::class.java))
        assertEquals(leaveRemoteAction, effect.leaveRemoteAction)
    }

    @Test
    fun `should return DelayedHeartbeat effect when getting DelayedHeartbeat invocation`() {
        // given
        val effectInvocation = PresenceEffectInvocation.DelayedHeartbeat(channels, channelGroups, attempts, reason)
        every {
            heartbeatProvider.getHeartbeatRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns heartbeatRemoteAction

        // when
        val effect: DelayedHeartbeatEffect = presenceEffectFactory.create(effectInvocation) as DelayedHeartbeatEffect

        // then
        assertThat(effect, IsInstanceOf.instanceOf(DelayedHeartbeatEffect::class.java))
        assertEquals(heartbeatRemoteAction, effect.heartbeatRemoteAction)
        assertEquals(presenceEventSink, effect.presenceEventSink)
        assertEquals(retryConfiguration, effect.retryConfiguration)
        assertEquals(attempts, effect.attempts)
        assertEquals(reason, effect.reason)
    }
}
