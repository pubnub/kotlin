package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
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

    @BeforeEach
    fun setUp() {
        presenceEffectFactory = PresenceEffectFactory(
            heartbeatProvider,
            leaveProvider,
            presenceEventSink,
            policy,
            executorService,
            heartbeatInterval
        )
    }

    @Test
    fun `should return Heartbeat effect when getting Heartbeat invocation`() {
        // given
        val effectInvocation = PresenceEffectInvocation.Heartbeat(channels, channelGroups)
        every { heartbeatProvider.getHeartbeatRemoteAction(effectInvocation.channels, effectInvocation.channelGroups) } returns heartbeatRemoteAction

        // when
        val effect = presenceEffectFactory.create(PresenceEffectInvocation.Heartbeat(channels, channelGroups))

        // then
        assertThat(effect, IsInstanceOf.instanceOf(HeartbeatEffect::class.java))
    }

    // todo add missing tests
}
