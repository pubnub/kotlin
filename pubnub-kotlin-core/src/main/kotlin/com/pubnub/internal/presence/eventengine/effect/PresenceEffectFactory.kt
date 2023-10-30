package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.enums.PNHeartbeatNotificationOptions
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.eventengine.Effect
import com.pubnub.internal.eventengine.EffectFactory
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.presence.eventengine.data.PresenceData
import com.pubnub.internal.presence.eventengine.effect.effectprovider.HeartbeatProvider
import com.pubnub.internal.presence.eventengine.effect.effectprovider.LeaveProvider
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.StatusConsumer
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration

internal class PresenceEffectFactory(
    private val heartbeatProvider: HeartbeatProvider,
    private val leaveProvider: LeaveProvider,
    private val presenceEventSink: Sink<PresenceEvent>,
    private val retryConfiguration: RetryConfiguration,
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
                        presenceData.channelStates.filter { it.key in effectInvocation.channels }
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
                        presenceData.channelStates.filter { it.key in effectInvocation.channels }
                    } else {
                        null
                    }
                )
                DelayedHeartbeatEffect(
                    heartbeatRemoteAction,
                    presenceEventSink,
                    retryConfiguration,
                    executorService,
                    effectInvocation.attempts,
                    effectInvocation.reason
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
                WaitEffect(heartbeatInterval, presenceEventSink, executorService)
            }

            PresenceEffectInvocation.CancelDelayedHeartbeat,
            PresenceEffectInvocation.CancelWait
            -> null
        }
    }
}
