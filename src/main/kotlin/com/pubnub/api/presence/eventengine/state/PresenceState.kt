package com.pubnub.api.presence.eventengine.state

import com.pubnub.api.PubNubException
import com.pubnub.api.eventengine.State
import com.pubnub.api.eventengine.noTransition
import com.pubnub.api.eventengine.transitionTo
import com.pubnub.api.presence.eventengine.effect.PresenceEffectInvocation
import com.pubnub.api.presence.eventengine.event.PresenceEvent

sealed class PresenceState : State<PresenceEffectInvocation, PresenceEvent, PresenceState> {
    object HearbeatInactive : PresenceState() {
        override fun transition(event: PresenceEvent): Pair<PresenceState, Set<PresenceEffectInvocation>> {
            return when (event) {
                is PresenceEvent.Joined -> {
                    transitionTo(Heartbeating(event.channels, event.channelGroups))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class Heartbeating(
        val channels: Set<String>,
        val channelGroups: Set<String>
    ) : PresenceState() {
        override fun onEntry(): Set<PresenceEffectInvocation> = setOf(PresenceEffectInvocation.Heartbeat(channels, channelGroups))

        override fun transition(event: PresenceEvent): Pair<PresenceState, Set<PresenceEffectInvocation>> {
            return when (event) {
                is PresenceEvent.LeftAll -> {
                    transitionTo(HearbeatInactive, PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                is PresenceEvent.Disconnect -> {
                    transitionTo(HeartbeatStopped(channels, channelGroups), PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                is PresenceEvent.Joined -> {
                    transitionTo(Heartbeating(channels + event.channels, channelGroups + event.channelGroups))
                }
                is PresenceEvent.Left -> {
                    transitionTo(Heartbeating(channels - event.channels, channelGroups - event.channelGroups), PresenceEffectInvocation.Leave(event.channels, event.channelGroups))
                }
                is PresenceEvent.StateSet -> {
                    transitionTo(Heartbeating(event.channels, event.channelGroups))
                }
                is PresenceEvent.HeartbeatSuccess -> {
                    transitionTo(HeartbeatWaiting(channels, channelGroups))
                }
                is PresenceEvent.HeartbeatFailure -> {
                    transitionTo(HeartbeatReconnecting(channels, channelGroups, 0, event.reason))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HeartbeatReconnecting(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val attempts: Int,
        val reason: PubNubException?
    ) : PresenceState() {
        override fun onEntry(): Set<PresenceEffectInvocation> = setOf(PresenceEffectInvocation.DelayedHeartbeat())
        override fun onExit(): Set<PresenceEffectInvocation> = setOf(PresenceEffectInvocation.CancelDelayedHeartbeat)

        override fun transition(event: PresenceEvent): Pair<PresenceState, Set<PresenceEffectInvocation>> {
            return when (event) {
                is PresenceEvent.Joined -> {
                    transitionTo(Heartbeating(channels + event.channels, channelGroups + event.channelGroups))
                }
                is PresenceEvent.Left -> {
                    transitionTo(Heartbeating(channels - event.channels, channelGroups - event.channelGroups), PresenceEffectInvocation.Leave(event.channels, event.channelGroups))
                }
                is PresenceEvent.StateSet -> {
                    transitionTo(Heartbeating(event.channels, event.channelGroups))
                }
                is PresenceEvent.HeartbeatSuccess -> { // toDo shouldn't we introduce HeartbeatReconnectSuccess event?
                    transitionTo(HeartbeatWaiting(channels, channelGroups))
                }
                is PresenceEvent.HeartbeatFailure -> { // toDo shouldn't we introduce HeartbeatReconnectFailure event?
                    transitionTo(HeartbeatReconnecting(channels, channelGroups, attempts + 1, event.reason))
                }
                is PresenceEvent.HeartbeatGiveup -> {
                    transitionTo(HeartbeatFailed(channels, channelGroups, event.reason))
                }
                is PresenceEvent.Disconnect -> {
                    transitionTo(HeartbeatStopped(channels, channelGroups), PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                is PresenceEvent.LeftAll -> {
                    transitionTo(HearbeatInactive, PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HeartbeatStopped(
        val channels: Set<String>,
        val channelGroups: Set<String>
    ) : PresenceState() {
        override fun transition(event: PresenceEvent): Pair<PresenceState, Set<PresenceEffectInvocation>> {
            return when (event) {
                is PresenceEvent.LeftAll -> {
                    transitionTo(HearbeatInactive)
                }
                is PresenceEvent.Joined -> {
                    transitionTo(HeartbeatStopped(channels + event.channels, channelGroups + event.channelGroups))
                }
                is PresenceEvent.Left -> {
                    transitionTo(HeartbeatStopped(channels - event.channels, channelGroups - event.channelGroups))
                }
                is PresenceEvent.StateSet -> {
                    transitionTo(HeartbeatStopped(event.channels, event.channelGroups))
                }
                is PresenceEvent.Reconnect -> {
                    transitionTo(Heartbeating(channels, channelGroups))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HeartbeatFailed(
        val channels: Set<String>,
        val channelGroups: Set<String>,
        val reason: PubNubException?
    ) : PresenceState() {
        override fun transition(event: PresenceEvent): Pair<PresenceState, Set<PresenceEffectInvocation>> {
            return when (event) {
                is PresenceEvent.LeftAll -> {
                    transitionTo(HearbeatInactive, PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                is PresenceEvent.Joined -> {
                    transitionTo(Heartbeating(channels + event.channels, channelGroups + event.channelGroups))
                }
                is PresenceEvent.Left -> {
                    transitionTo(Heartbeating(channels - event.channels, channelGroups - event.channelGroups), PresenceEffectInvocation.Leave(event.channels, event.channelGroups))
                }
                is PresenceEvent.StateSet -> {
                    transitionTo(Heartbeating(event.channels, event.channelGroups))
                }
                is PresenceEvent.Reconnect -> {
                    transitionTo(Heartbeating(channels, channelGroups))
                }
                is PresenceEvent.Disconnect -> {
                    transitionTo(HeartbeatStopped(channels, channelGroups), PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }

    data class HeartbeatWaiting(
        val channels: Set<String>,
        val channelGroups: Set<String>
    ) : PresenceState() {
        override fun onEntry(): Set<PresenceEffectInvocation> = setOf(PresenceEffectInvocation.ScheduleNextHeartbeat())
        override fun onExit(): Set<PresenceEffectInvocation> = setOf(PresenceEffectInvocation.CancelScheduleNextHeartbeat)

        override fun transition(event: PresenceEvent): Pair<PresenceState, Set<PresenceEffectInvocation>> {
            return when (event) {
                is PresenceEvent.Joined -> {
                    transitionTo(Heartbeating(channels + event.channels, channelGroups + event.channelGroups))
                }
                is PresenceEvent.Left -> {
                    transitionTo(Heartbeating(channels - event.channels, channelGroups - event.channelGroups), PresenceEffectInvocation.Leave(event.channels, event.channelGroups))
                }
                is PresenceEvent.StateSet -> {
                    transitionTo(Heartbeating(event.channels, event.channelGroups))
                }
                is PresenceEvent.NextHeartbeat -> {
                    transitionTo(Heartbeating(channels, channelGroups))
                }
                is PresenceEvent.Disconnect -> {
                    transitionTo(HeartbeatStopped(channels, channelGroups), PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                is PresenceEvent.LeftAll -> {
                    transitionTo(HearbeatInactive, PresenceEffectInvocation.Leave(channels, channelGroups))
                }
                else -> {
                    noTransition()
                }
            }
        }
    }
}
