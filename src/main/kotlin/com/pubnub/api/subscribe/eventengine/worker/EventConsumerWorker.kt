package com.pubnub.api.subscribe.eventengine.worker

import com.pubnub.api.subscribe.eventengine.effect.EffectInvocation
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.state.State
import com.pubnub.api.subscribe.eventengine.transition.transition
import java.util.concurrent.LinkedBlockingQueue

class EventConsumerWorker(
    private var currentState: State,
    private val eventQueue: LinkedBlockingQueue<Event>,
    private val effectQueue: LinkedBlockingQueue<EffectInvocation>,
    private val flowQueue: LinkedBlockingQueue<Any>,  //queue that is used for testing purpose to track changes in EventEngine maybe also in EffectDispatcher ?
    private val testMode: Boolean
) : Runnable {


    override fun run() {
        while (!Thread.interrupted()) {
            try {
                //what if eventQueue is empty?
                println("---EventEngineWorker--- queue size: " + eventQueue.size)
                val event: Event = eventQueue.take()
                addToFLowQueue(currentState)         //test
                addToFLowQueue(event)                //test
                performTransitionAndEmitEffects(event)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun addToFLowQueue(eventOrStateOrTransition: Any) {
        if (testMode) {
            flowQueue.add(eventOrStateOrTransition)
        }
    }

    private fun performTransitionAndEmitEffects(event: Event) {
        println("---EventEngineWorker performs transition as reaction for event: $event")
        val (stateAfterTransition, transitionEffects) = transition(currentState, event)

        currentState = stateAfterTransition
        addToFLowQueue(currentState)           //test

        //add all transitionEffects into queue
        transitionEffects.forEach { effectQueue.add(it) }
    }

}

////cos jak statyczna metoda
//internal fun transition(state: State, event: Event): Pair<State, List<EffectInvocation>> {
//    val transitionResult = when (state) {
//        is State.Unsubscribed -> {
////            state.transition(event)
//            (State.Unsubscribed).transition(event)  //<-- może lepiej tak, żeby przekierowywało do odpowiendiej implementacji metody transition  ?
//        }
//        is State.Handshaking -> {
//            state.transition(event)
////            (State.Handshaking).transition(event)   //<--dlaczego tu nie mogę tak?
//        }
//        is State.Receiving -> {
//            state.transition(event)
////            (State.Receiving).transition(event)    //<--dlaczego tu nie mogę tak?
//        }
//
//
//        else -> {
//            TODO()
//        }
//    }
//
//    val newState = transitionResult.first
//    val transitionEffects = transitionResult.second
//
//
//    val effectsOnCurrentStateExit = state.onExit()
//    val effectsOnNewStateEntry = newState.onEntry()
//    return newState to effectsOnCurrentStateExit + transitionEffects + effectsOnNewStateEntry
//}

//private fun handleEvent_TransitionFromUnsubscribedState(event: Event): Pair<State, List<Effect>> {
//    var stateAfterTransition: State = State.UNSUBSCRIBED
//    val transitionEffects = mutableListOf<Effect>()
//
//    when (event) {
//        is Event.Restore -> {
//            println("---State:UNSUBSCRIBED, Event: RESTORE, Transition to: RECONNECTING, Transition effects: ..")
//            stateAfterTransition = State.RECONNECTING
//            //transitionEffects.add() <--not transition effects here
//            //albo przy uzyciu TransitionContext
//            TransitionContext(stateAfterTransition, mutableListOf())
//
//        }
//        is Event.SubscriptionChange -> {
//            //na podstawie current stanu wyciągnij onExit effects
//            println("---State:UNSUBSCRIBED, Event: SUBSCRIPTION_CHANGE, Transition to: HANDSHAKING, Transition effects: ..")
//            stateAfterTransition = State.HANDSHAKING(event.channels, event.channelGroups)
//
//            //transitionEffects.add() <--not transition effects here
//        }
//        is Event.AuthorizationChanged -> {
//            println("---State:UNSUBSCRIBED, Event: AUTHORIZATION_CHANGED, Transition to: UNSUBSCRIBED, Transition effects: ..")
//            stateAfterTransition = State.UNSUBSCRIBED
//            //transitionEffects.add() <--not transition effects here
//        }
//        is Event.AllChannelUnsubscribe -> {
//            println("---State:UNSUBSCRIBED, Event: AUTHORIZATION_CHANGED, Transition to: UNSUBSCRIBED, Transition effects: ..")
//            stateAfterTransition = State.UNSUBSCRIBED
//            //transitionEffects.add() <--not transition effects here
//        }
//        is Event.Disconnected -> {
//            println("---State:UNSUBSCRIBED, Event: AUTHORIZATION_CHANGED, Transition to: PREPARING, Transition effects: ..")
//            stateAfterTransition = State.PREPARING
//            //transitionEffects.add() <--not transition effects here
//        }
//        is Event.Reconnect -> {
//            println("---State:UNSUBSCRIBED, Event: AUTHORIZATION_CHANGED, Transition to: UNSUBSCRIBED, Transition effects: ..")
//            stateAfterTransition = State.UNSUBSCRIBED
//            //transitionEffects.add() <--not transition effects here
//        }
//        else -> {
//            //ignore it
//            println("In UNSUBSCRIBED state ignoring event: $event")
//        }
//    }
//    return Pair(stateAfterTransition, transitionEffects)
//}

private fun handleTransitionFromHandshakingState(event: Event): Pair<State, List<EffectInvocation>> {
    var stateAfterTransition: State
    val effects: List<EffectInvocation> = ArrayList()

    //toDo implement

    stateAfterTransition = State.STOPPED //remove it once method implemented
    return Pair(stateAfterTransition, effects)
}

private fun handleTransitionFromHandshakeFailedState(event: Event): Pair<State, List<EffectInvocation>> {
    var stateAfterTransition: State
    val effects: List<EffectInvocation> = ArrayList()

    //toDo implement

    stateAfterTransition = State.STOPPED //remove it once method implemented
    return Pair(stateAfterTransition, effects)
}


private fun handleTransitionFromReconnectingState(event: Event): Pair<State, List<EffectInvocation>> {
    var stateAfterTransition: State
    val effects: List<EffectInvocation> = ArrayList()

    //toDo implement

    stateAfterTransition = State.STOPPED //remove it once method implemented
    return Pair(stateAfterTransition, effects)
}

private fun handleTransitionFromReconnectingFailedState(event: Event): Pair<State, List<EffectInvocation>> {
    var stateAfterTransition: State
    val effects: List<EffectInvocation> = ArrayList()

    //toDo implement

    stateAfterTransition = State.STOPPED //remove it once method implemented
    return Pair(stateAfterTransition, effects)
}

private fun handleTransitionFromDisconnectedState(event: Event): Pair<State, List<EffectInvocation>> {
    var stateAfterTransition: State
    val effects: List<EffectInvocation> = ArrayList()

    //toDo implement

    stateAfterTransition = State.STOPPED //remove it once method implemented
    return Pair(stateAfterTransition, effects)
}

private fun handleTransitionFromReceivingState(event: Event): Pair<State, List<EffectInvocation>> {
    var stateAfterTransition: State
    val effects: List<EffectInvocation> = ArrayList()

    //toDo implement

    stateAfterTransition = State.STOPPED //remove it once method implemented
    return Pair(stateAfterTransition, effects)
}

