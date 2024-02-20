package com.pubnub.internal.subscribe.eventengine.state

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.eventengine.State
import com.pubnub.internal.eventengine.noTransition
import com.pubnub.internal.eventengine.transitionTo
import com.pubnub.internal.subscribe.eventengine.effect.SubscribeEffectInvocation
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor

internal sealed class SubscribeState : State<SubscribeEffectInvocation, SubscribeEvent, SubscribeState> {
    object Unsubscribed : SubscribeState() {
        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups))
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class Handshaking(
        channels: Set<String>,
        channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor? = null,
    ) : SubscribeState() {
        // toSet() is a must because we want to make sure that channels is immutable, and Handshaking constructor
        // doesn't prevent from providing "channels" that is mutable set.
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun onEntry() = setOf(SubscribeEffectInvocation.Handshake(channels, channelGroups))
        override fun onExit() = setOf(SubscribeEffectInvocation.CancelHandshake)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.HandshakeSuccess -> {
                    val cursor = subscriptionCursor?.copy(region = event.subscriptionCursor.region)
                        ?: event.subscriptionCursor
                    transitionTo(
                        state = Receiving(
                            channels,
                            channelGroups,
                            cursor
                        ),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.Connected,
                                currentTimetoken = cursor.timetoken,
                                channels = channels.toList(),
                                channelGroups = channelGroups.toList()
                            )
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                is SubscribeEvent.HandshakeFailure -> {
                    transitionTo(HandshakeReconnecting(channels, channelGroups, 0, event.reason, subscriptionCursor))
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, subscriptionCursor))
                }

                is SubscribeEvent.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason = null))
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class HandshakeReconnecting(
        channels: Set<String>,
        channelGroups: Set<String>,
        val attempts: Int,
        val reason: PubNubException,
        val subscriptionCursor: SubscriptionCursor? = null
    ) : SubscribeState() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun onEntry() =
            setOf(SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason))

        override fun onExit() = setOf(SubscribeEffectInvocation.CancelHandshakeReconnect)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.HandshakeReconnectFailure -> {
                    transitionTo(
                        HandshakeReconnecting(
                            this.channels, this.channelGroups, this.attempts + 1, event.reason, subscriptionCursor
                        )
                    )
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, subscriptionCursor))
                }

                is SubscribeEvent.Disconnect -> {
                    transitionTo(HandshakeStopped(channels, channelGroups, reason))
                }

                is SubscribeEvent.HandshakeReconnectGiveup -> {
                    transitionTo(
                        HandshakeFailed(channels, channelGroups, event.reason),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(PNStatusCategory.ConnectionError, reason)
                        )
                    )
                }

                is SubscribeEvent.HandshakeReconnectSuccess -> {
                    val cursor = subscriptionCursor?.copy(region = event.subscriptionCursor.region)
                        ?: event.subscriptionCursor
                    transitionTo(
                        state = Receiving(
                            channels,
                            channelGroups,
                            cursor

                        ),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.Connected,
                                currentTimetoken = cursor.timetoken,
                                channels = channels.toList(),
                                channelGroups = channelGroups.toList()
                            )
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class HandshakeStopped(
        channels: Set<String>,
        channelGroups: Set<String>,
        val reason: PubNubException?
    ) : SubscribeState() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups, event.subscriptionCursor))
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(
                        HandshakeStopped(
                            event.channels,
                            event.channelGroups,
                            reason = null
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(
                        HandshakeStopped(
                            event.channels,
                            event.channelGroups,
                            reason = null
                        )
                    )
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class HandshakeFailed(
        channels: Set<String>,
        channelGroups: Set<String>,
        val reason: PubNubException,
        val subscriptionCursor: SubscriptionCursor? = null
    ) : SubscribeState() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, subscriptionCursor))
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(
                        Handshaking(
                            event.channels, event.channelGroups, event.subscriptionCursor
                        )
                    )
                }

                is SubscribeEvent.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups, event.subscriptionCursor))
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class Receiving(
        channels: Set<String>,
        channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeState() {

        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun onEntry() = setOf(
            SubscribeEffectInvocation.ReceiveMessages(
                channels, channelGroups, subscriptionCursor
            )
        )

        override fun onExit() = setOf(SubscribeEffectInvocation.CancelReceiveMessages)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {

            return when (event) {
                is SubscribeEvent.ReceiveFailure -> {
                    transitionTo(
                        ReceiveReconnecting(
                            channels, channelGroups, subscriptionCursor, 0, event.reason
                        )
                    )
                }

                is SubscribeEvent.Disconnect -> {
                    transitionTo(
                        state = ReceiveStopped(channels, channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(PNStatusCategory.Disconnected)
                        )
                    )
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(
                        Receiving(event.channels, event.channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.SubscriptionChanged,
                                currentTimetoken = subscriptionCursor.timetoken,
                                channels = event.channels,
                                channelGroups = event.channelGroups
                            )
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(
                        Receiving(
                            event.channels,
                            event.channelGroups,
                            SubscriptionCursor(event.subscriptionCursor.timetoken, subscriptionCursor.region)
                        ),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.SubscriptionChanged,
                                currentTimetoken = event.subscriptionCursor.timetoken,
                                channels = event.channels,
                                channelGroups = event.channelGroups
                            )
                        )
                    )
                }

                is SubscribeEvent.ReceiveSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages)
                    )
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(
                        state = Unsubscribed,
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(PNStatusCategory.Disconnected)
                        )
                    )
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class ReceiveReconnecting(
        channels: Set<String>,
        channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor,
        val attempts: Int,
        val reason: PubNubException?
    ) : SubscribeState() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun onEntry() =
            setOf(
                SubscribeEffectInvocation.ReceiveReconnect(
                    channels,
                    channelGroups,
                    subscriptionCursor,
                    attempts,
                    reason
                )
            )

        override fun onExit() = setOf(SubscribeEffectInvocation.CancelReceiveReconnect)

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.ReceiveReconnectFailure -> {
                    transitionTo(
                        ReceiveReconnecting(
                            channels, channelGroups, subscriptionCursor, attempts + 1, event.reason
                        )
                    )
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(
                        Receiving(event.channels, event.channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.SubscriptionChanged,
                                currentTimetoken = subscriptionCursor.timetoken,
                                channels = event.channels,
                                channelGroups = event.channelGroups
                            )
                        )
                    )
                }

                is SubscribeEvent.Disconnect -> {
                    transitionTo(
                        ReceiveStopped(channels, channelGroups, subscriptionCursor),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(PNStatusCategory.Disconnected)
                        )
                    )
                }

                is SubscribeEvent.ReceiveReconnectGiveup -> {
                    transitionTo(
                        state = ReceiveFailed(channels, channelGroups, subscriptionCursor, event.reason),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.UnexpectedDisconnect,
                                event.reason
                            )
                        )
                    )
                }

                is SubscribeEvent.ReceiveReconnectSuccess -> {
                    transitionTo(
                        state = Receiving(channels, channelGroups, event.subscriptionCursor),
                        SubscribeEffectInvocation.EmitMessages(event.messages),
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(
                        Receiving(
                            event.channels,
                            event.channelGroups,
                            SubscriptionCursor(event.subscriptionCursor.timetoken, subscriptionCursor.region)
                        ),
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(
                                PNStatusCategory.SubscriptionChanged,
                                currentTimetoken = event.subscriptionCursor.timetoken,
                                channels = event.channels,
                                channelGroups = event.channelGroups
                            )
                        )
                    )
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(
                        state = Unsubscribed,
                        SubscribeEffectInvocation.EmitStatus(
                            PNStatus(PNStatusCategory.Disconnected)
                        )
                    )
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class ReceiveStopped(
        channels: Set<String>,
        channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor
    ) : SubscribeState() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups, event.subscriptionCursor ?: subscriptionCursor))
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(
                        ReceiveStopped(
                            event.channels,
                            event.channelGroups,
                            subscriptionCursor
                        )
                    )
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(
                        ReceiveStopped(
                            event.channels,
                            event.channelGroups,
                            event.subscriptionCursor
                        )
                    )
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }

    class ReceiveFailed(
        channels: Set<String>,
        channelGroups: Set<String>,
        val subscriptionCursor: SubscriptionCursor,
        val reason: PubNubException
    ) : SubscribeState() {
        val channels: Set<String> = channels.toSet()
        val channelGroups: Set<String> = channelGroups.toSet()

        override fun transition(event: SubscribeEvent): Pair<SubscribeState, Set<SubscribeEffectInvocation>> {
            return when (event) {
                is SubscribeEvent.Reconnect -> {
                    transitionTo(Handshaking(channels, channelGroups, event.subscriptionCursor ?: subscriptionCursor))
                }

                is SubscribeEvent.SubscriptionChanged -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, subscriptionCursor))
                }

                is SubscribeEvent.SubscriptionRestored -> {
                    transitionTo(Handshaking(event.channels, event.channelGroups, event.subscriptionCursor))
                }

                is SubscribeEvent.UnsubscribeAll -> {
                    transitionTo(Unsubscribed)
                }

                else -> {
                    noTransition()
                }
            }
        }
    }
}
