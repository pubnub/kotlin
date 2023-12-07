package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.presence.eventengine.data.PresenceData
import com.pubnub.api.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.api.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.RetryPolicy
import com.pubnub.api.subscribe.eventengine.effect.StatusConsumer
import java.time.Duration
import java.util.concurrent.ScheduledExecutorService

internal class PresenceEffectFactory(
    private val heartbeatProvider: HeartbeatProvider,
    private val leaveProvider: LeaveProvider,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService,
    private val heartbeatInterval: Duration,
    private val suppressLeaveEvents: Boolean,
    private val heartbeatNotificationOptions: PNHeartbeatNotificationOptions,
    private val statusConsumer: StatusConsumer,
    private val presenceData: PresenceData,
    private val sendStateWithHeartbeat: Boolean,
) : EffectFactory<PresenceEffectInvocation> {
    override fun create(effectInvocation: PresenceEffectInvocation): Effect? {
        return when (effectInvocation) {
            is PresenceEffectInvocation.Heartbeat -> {
                val heartbeatRemoteAction = heartbeatProvider.getHeartbeatRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    if (sendStateWithHeartbeat) {
                        presenceData.channelStates
                    } else {
                        null
                    }
                )
                HeartbeatEffect(heartbeatRemoteAction, presenceEventSink, heartbeatNotificationOptions, statusConsumer)
            }

            is PresenceEffectInvocation.DelayedHeartbeat -> {
                val heartbeatRemoteAction = heartbeatProvider.getHeartbeatRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    if (sendStateWithHeartbeat) {
                        presenceData.channelStates
                    } else {
                        null
                    }
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
                if (!suppressLeaveEvents) {
                    val leaveRemoteAction = leaveProvider.getLeaveRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups
                    )
                    LeaveEffect(leaveRemoteAction)
                } else {
                    null
                }
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
