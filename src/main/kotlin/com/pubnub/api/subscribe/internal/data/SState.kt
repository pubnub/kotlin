package com.pubnub.api.subscribe.internal.data

import com.pubnub.api.subscribe.internal.*
import com.pubnub.api.subscribe.internal.InitialEvent
import com.pubnub.api.subscribe.internal.NewState
import com.pubnub.api.subscribe.internal.data.SAction.SetChannels
import com.pubnub.api.subscribe.internal.data.SAction.SetCursor
import com.pubnub.api.subscribe.internal.data.SEffect.*
import com.pubnub.api.subscribe.internal.data.SState.*
import com.pubnub.api.subscribe.internal.data.SState.Handshaking
import com.pubnub.api.subscribe.internal.data.SState.HandshakingFailed
import com.pubnub.api.subscribe.internal.data.SState.Receiving
import com.pubnub.api.subscribe.internal.data.SState.Unsubscribed
import com.pubnub.api.subscribe.internal.data.STransition.*

data class Context(
    val channels: List<String>,
    val channelGroups: List<String>,
    val cursor: Cursor?
)

enum class SState {
    Unsubscribed,
    Handshaking,
    Receiving,
    ReceivingFailed,
    Reconnecting,
    ReconnectingFailed,
    HandshakingFailed
}

enum class SEffect {
    SendHandshake,
    SendReceiveMessages,
    EmitEvents,
    NewState,
}

enum class STransition {
    HandshakeFailure,
    HandshakeSuccess,
    SubscriptionChange,
    Reconnect,
    ReceiveFailure,
    ReceiveSuccess,
    InitialEvent
}

enum class SAction {
    SetCursor,
    SetChannels
}

val signature = mapOf   (
    Unsubscribed to St(
        transitions = mapOf(
            SubscriptionChange to Tr(
                target = Handshaking,
                actions = listOf(SetChannels)
            ),
            STransition.InitialEvent to Tr(
                target = Unsubscribed
            )
        )
    ),
    Handshaking to St(
        effects = listOf(SendHandshake),
        transitions = mapOf(
            HandshakeFailure to Tr(
                target = HandshakingFailed
            ),
            HandshakeSuccess to Tr(
                target = Receiving,
                actions = listOf(SetCursor)
            ),
            SubscriptionChange to Tr(
                target = Handshaking,
                actions = listOf(SetChannels)
            )
        )
    ),
    HandshakingFailed to St(
        transitions = mapOf(
            Reconnect to Tr(target = Handshaking),
            SubscriptionChange to Tr(
                target = Handshaking,
                actions = listOf(SetChannels)
            )
        )
    ),
    Receiving to St(
        effects = listOf(SendReceiveMessages),
        transitions = mapOf(
            ReceiveFailure to Tr(target = Receiving),
            ReceiveSuccess to Tr(
                target = Receiving,
                actions = listOf(SetCursor),
                effects = listOf(EmitEvents)
            ),
            SubscriptionChange to Tr(
                target = Receiving,
                actions = listOf(SetChannels)
            )
        )
    ),
    ReceivingFailed to St(
        transitions = mapOf(
            Reconnect to Tr(target = Receiving),
            SubscriptionChange to Tr(
                target = Handshaking,
                actions = listOf(SetChannels)
            )
        )
    )
)


typealias St = StateDescription
typealias Tr = TransitionDescription

data class TransitionDescription(
    val target: SState,
    val effects: List<SEffect> = listOf(),
    val actions: List<SAction> = listOf()
)

data class StateDescription(
    val effects: List<SEffect> = listOf(),
    val transitions: Map<STransition, TransitionDescription>
)

class Interpreter(
    private val signature: Map<SState, StateDescription>,
    private var context: Context = Context(channels = listOf(), channelGroups = listOf(), cursor = null),
    private val reducers: Reducers,
    initialState: SState
) {
    var currentState = initialState

    fun handle(event: SubscribeEvent): Pair<Context, List<SEffect>> {
        val stateDescription = signature[currentState]!!

        val transitionDescription = stateDescription.transitions[event.toTransition()]!!

        currentState = transitionDescription.target
        val nextStateDescription = signature[currentState]!!

        context = handleActions(transitionDescription.actions, event)
        return context to transitionDescription.effects + nextStateDescription.effects
    }

    private fun handleActions(actions: List<SAction>, event: SubscribeEvent): Context {
        return actions.foldRight(context) { it, ctx ->
            reducers[it]!!(ctx, event)
        }
    }
}

private fun SubscribeEvent.toTransition(): STransition {
    return when (this) {
        is Commands.SubscribeIssued -> SubscriptionChange
        Commands.UnsubscribeAllIssued -> TODO()
        is Commands.UnsubscribeIssued -> SubscriptionChange
        is HandshakeResult.HandshakeFailed -> HandshakeFailure
        is HandshakeResult.HandshakeSucceeded -> HandshakeSuccess
        InitialEvent -> STransition.InitialEvent
        is ReceivingResult.ReceivingFailed -> ReceiveFailure
        is ReceivingResult.ReceivingSucceeded -> ReceiveSuccess
    }
}

typealias Reducer = (Context, SubscribeEvent) -> Context
typealias Reducers = Map<SAction, Reducer>

fun Context.toStatus(): SubscriptionStatus {
    return SubscriptionStatus(channels = channels,
    groups = channelGroups,
    cursor = cursor)
}

fun Interpreter.fitInDataDriven(event: SubscribeEvent): List<SubscribeEffectInvocation> {
    val (context, uncompatibleEffects) = handle(event)
    return uncompatibleEffects.map {
        when (it) {
            SendHandshake -> SubscribeHttpEffectInvocation.HandshakeHttpCallEffectInvocation(subscriptionStatus = context.toStatus())
            SendReceiveMessages -> SubscribeHttpEffectInvocation.ReceiveMessagesHttpCallEffectInvocation(subscriptionStatus = context.toStatus())
            EmitEvents -> if (event is ReceivingResult.ReceivingSucceeded) {
                NewMessages(event.subscribeEnvelope.messages)
            } else {
                NewMessages(listOf())
            }
            SEffect.NewState -> when (currentState) {
                Unsubscribed -> NewState(com.pubnub.api.subscribe.internal.Unsubscribed::class.simpleName!!, status = context.toStatus())
                Handshaking -> NewState(com.pubnub.api.subscribe.internal.Handshaking::class.simpleName!!, status = context.toStatus())
                Receiving -> NewState(com.pubnub.api.subscribe.internal.Receiving::class.simpleName!!, status = context.toStatus())
                ReceivingFailed -> NewState(com.pubnub.api.subscribe.internal.ReconnectingFailed::class.simpleName!!, status = context.toStatus())
                HandshakingFailed -> NewState(com.pubnub.api.subscribe.internal.HandshakingFailed::class.simpleName!!, status = context.toStatus())
                else -> TODO()
            }
        }
    }
}

