package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import java.time.Duration
import java.util.concurrent.ScheduledExecutorService

internal class PresenceEffectFactory(
    private val heartbeatProvider: HeartbeatProvider,
    private val leaveProvider: LeaveProvider,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
    private val heartbeatInterval: Duration,
) : EffectFactory<PresenceEffectInvocation> {
    override fun create(effectInvocation: PresenceEffectInvocation): Effect? {
        return when (effectInvocation) {
            is PresenceEffectInvocation.Heartbeat -> {
                val heartbeatRemoteAction = heartbeatProvider.getHeartbeatRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups
                )
                HeartbeatEffect(heartbeatRemoteAction, presenceEventSink)
            }

            is PresenceEffectInvocation.DelayedHeartbeat -> {
                val heartbeatRemoteAction = heartbeatProvider.getHeartbeatRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups
                )
                DelayedHeartbeatEffect(
                    heartbeatRemoteAction,
                    presenceEventSink,
                    policy,
                    executorService,
                    effectInvocation
                )
            }
            is PresenceEffectInvocation.Leave -> {
                val leaveRemoteAction = leaveProvider.getLeaveRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups
                )
                LeaveEffect(leaveRemoteAction)
            }
            is PresenceEffectInvocation.Wait -> {
                WaitEffect(heartbeatInterval, presenceEventSink)
            }

            PresenceEffectInvocation.CancelDelayedHeartbeat,
            PresenceEffectInvocation.CancelWait
            -> null
        }
    }
}
