package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import java.util.concurrent.ScheduledExecutorService

internal class PresenceEffectFactory(
    private val heartbeatProvider: HeartbeatProvider,
    private val leaveProvider: HeartbeatProvider,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
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
                val heartbeatRemoteAction = leaveProvider.getHeartbeatRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups
                )
                LeaveEffect(heartbeatRemoteAction)
            }
            is PresenceEffectInvocation.Wait -> {
                TODO()
            }

            PresenceEffectInvocation.CancelDelayedHeartbeat -> {
                TODO()
            }
            PresenceEffectInvocation.CancelWait -> {
                TODO()
            }
        }
    }
}
