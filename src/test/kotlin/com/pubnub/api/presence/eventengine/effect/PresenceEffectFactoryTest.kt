package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.ScheduledExecutorService

class PresenceEffectFactoryTest {
    private lateinit var presenceEffectFactory: PresenceEffectFactory

    private val heartbeatProvider = mockk<HeartbeatProvider>()
    private val leaveProvider = mockk<LeaveProvider>()
    private val presenceEventSink = mockk<Sink<PresenceEvent>>()
    private val policy = mockk<RetryPolicy>()
    private val executorService = mockk<ScheduledExecutorService>()
    private val heartbeatInterval = Duration.ofSeconds(10)
    private val channels = setOf("channel1")
    private val channelGroups = setOf("channelGroup1")
    private val heartbeatRemoteAction: RemoteAction<Boolean> = mockk()
    private val leaveRemoteAction: RemoteAction<Boolean> = mockk()
    private val suppressLeaveEvents = true
    private val attempts: Int = 1
    private val reason: PubNubException = mockk()
    private val heartbeatNotificationOptions: PNHeartbeatNotificationOptions = mockk()
    private val statusConsumer: StatusConsumer = mockk()

    @BeforeEach
    fun setUp() {
        presenceEffectFactory = PresenceEffectFactory(
            heartbeatProvider,
            leaveProvider,
            presenceEventSink,
            policy,
            executorService,
            heartbeatInterval,
            suppressLeaveEvents,
            heartbeatNotificationOptions,
            statusConsumer
        )
    }

    @Test
    fun `should return Heartbeat effect when getting Heartbeat invocation`() {
        // given
        val effectInvocation = PresenceEffectInvocation.Heartbeat(channels, channelGroups)
        every { heartbeatProvider.getHeartbeatRemoteAction(effectInvocation.channels, effectInvocation.channelGroups) } returns heartbeatRemoteAction

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
    fun `should return Leave effect when getting Leave invocation`() {
        // given
        val effectInvocation = PresenceEffectInvocation.Leave(channels, channelGroups)
        every { leaveProvider.getLeaveRemoteAction(effectInvocation.channels, effectInvocation.channelGroups) } returns leaveRemoteAction

        // when
        val effect: LeaveEffect = presenceEffectFactory.create(effectInvocation) as LeaveEffect

        // then
        assertThat(effect, IsInstanceOf.instanceOf(LeaveEffect::class.java))
        assertEquals(suppressLeaveEvents, effect.suppressLeaveEvents)
        assertEquals(leaveRemoteAction, effect.leaveRemoteAction)
    }

    @Test
    fun `should return DelayedHeartbeat effect when getting DelayedHeartbeat invocation`() {
        // given
        val effectInvocation = PresenceEffectInvocation.DelayedHeartbeat(channels, channelGroups, attempts, reason)
        every { heartbeatProvider.getHeartbeatRemoteAction(effectInvocation.channels, effectInvocation.channelGroups) } returns heartbeatRemoteAction

        // when
        val effect: DelayedHeartbeatEffect = presenceEffectFactory.create(effectInvocation) as DelayedHeartbeatEffect

        // then
        assertThat(effect, IsInstanceOf.instanceOf(DelayedHeartbeatEffect::class.java))
        assertEquals(heartbeatRemoteAction, effect.heartbeatRemoteAction)
        assertEquals(presenceEventSink, effect.presenceEventSink)
        assertEquals(policy, effect.policy)
        assertEquals(attempts, effect.delayedHeartbeatInvocation.attempts)
        assertEquals(reason, effect.delayedHeartbeatInvocation.reason)
    }
}
